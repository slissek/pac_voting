package com.prodyna.pac.voting.web.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.prodyna.pac.voting.domain.User;
import com.prodyna.pac.voting.domain.Vote;
import com.prodyna.pac.voting.exceptions.PermissionsDeniedException;
import com.prodyna.pac.voting.security.SecurityUtils;
import com.prodyna.pac.voting.service.UserService;
import com.prodyna.pac.voting.service.UserVotingsService;
import com.prodyna.pac.voting.service.VoteService;
import com.prodyna.pac.voting.web.rest.converter.UserVotingsConverter;
import com.prodyna.pac.voting.web.rest.converter.VoteConverter;
import com.prodyna.pac.voting.web.rest.converter.VoteOptionConverter;
import com.prodyna.pac.voting.web.rest.dto.UserVotingsDTO;
import com.prodyna.pac.voting.web.rest.dto.VoteDTO;
import com.prodyna.pac.voting.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Vote.
 */
@RestController
@RequestMapping("/api")
public class VoteResource
{

    private final Logger log = LoggerFactory.getLogger(VoteResource.class);

    @Inject
    private VoteService voteService;

    @Inject
    private UserService userService;

    @Inject
    private UserVotingsService userVotingsService;

    /**
     * POST /votes : Create a new vote.
     *
     * @param voteDTO
     *            the vote to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vote, with status 400 (Bad Request) if the vote has
     *         already an ID, or with status 403 (Forbidden) if the user has no privileges to edit the vote.
     * @throws URISyntaxException
     *             if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/votes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VoteDTO> createVote(@Valid @RequestBody final VoteDTO voteDTO) throws URISyntaxException
    {
        this.log.debug("REST request to save Vote : {}", voteDTO);

        if (voteDTO.getIdentifier() != null)
        {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("vote", "idexists", "A new vote cannot already have an ID")).body(null);
        }
        try
        {
            final Vote vote = VoteConverter.toEntity(voteDTO, this.userService);
            final VoteDTO result = VoteConverter.toDto(this.getCurrentUserId(), this.voteService.save(vote), this.userVotingsService);
            result.add(VoteResource.createLinkToSelf(result.getIdentifier()));
            return ResponseEntity.created(new URI("/api/votes/" + result.getIdentifier()))
                    .headers(HeaderUtil.createEntityCreationAlert("vote", result.getIdentifier().toString()))
                    .body(result);
        }
        catch (final PermissionsDeniedException ex)
        {
            this.log.debug(
                    "User: " + SecurityUtils.getCurrentUserName() + " has no permissions to create the vote: " + ex.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * PUT /votes : Updates an existing vote.
     *
     * @param voteDTO
     *            the vote to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vote, or with status 400 (Bad Request) if the vote is not
     *         valid, or with status 403 (Forbidden) if the user has no privileges to edit the vote, or with status 500 (Internal Server
     *         Error) if the vote couldnt be updated
     * @throws URISyntaxException
     *             if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/votes", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VoteDTO> updateVote(@Valid @RequestBody final VoteDTO voteDTO) throws URISyntaxException
    {
        this.log.debug("REST request to update Vote : {}", voteDTO);

        if (voteDTO.getIdentifier() == null)
        {
            return this.createVote(voteDTO);
        }

        try
        {
            final Vote vote = this.voteService.findOne(voteDTO.getIdentifier());
            if (vote != null)
            {
                vote.setCreator(this.userService.getUserById(voteDTO.getUserId()));
                vote.setTopic(voteDTO.getTopic());
                vote.setVoteOptions(VoteOptionConverter.toEntitySet(voteDTO.getVoteOptions(), vote));

                final VoteDTO result = VoteConverter.toDto(this.getCurrentUserId(), this.voteService.save(vote), this.userVotingsService);
                result.add(VoteResource.createLinkToSelf(result.getIdentifier()));
                return ResponseEntity.ok()
                        .headers(HeaderUtil.createEntityUpdateAlert("vote", voteDTO.getIdentifier().toString()))
                        .body(result);
            }
            else
            {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch (final PermissionsDeniedException ex)
        {
            this.log.debug("User: " + SecurityUtils.getCurrentUserName() + " has no permissions to update the vote");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * GET /votes : get all the votes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of votes in body
     */
    @RequestMapping(value = "/votes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<VoteDTO>> getAllVotes()
    {
        this.log.debug("REST request to get all Votes");

        final List<VoteDTO> voteList = new ArrayList<>();
        this.voteService.getAll(new Sort(Sort.Direction.ASC, "id")).stream()
        .forEach(vote -> {
            final VoteDTO voteDTO = VoteConverter.toDto(this.getCurrentUserId(), vote, this.userVotingsService);
            voteDTO.add(VoteResource.createLinkToSelf(voteDTO.getIdentifier()));
            voteList.add(voteDTO);
        });
        return new ResponseEntity<>(voteList, HttpStatus.OK);
    }

    /**
     * GET /votes/:id : get the "id" vote.
     *
     * @param id
     *            the id of the vote to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vote, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/votes/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VoteDTO> getVote(@PathVariable final Long id)
    {
        this.log.debug("REST request to get Vote : {}", id);

        final Vote vote = this.voteService.findOne(id);
        if (vote != null)
        {
            final VoteDTO voteDTO = VoteConverter.toDto(this.getCurrentUserId(), vote, this.userVotingsService);
            voteDTO.add(VoteResource.createLinkToSelf(voteDTO.getIdentifier()));
            return new ResponseEntity<>(voteDTO, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * DELETE /votes/:id : delete the "id" vote.
     *
     * @param id
     *            the id of the vote to delete
     * @return the ResponseEntity with status 200 (OK), or with status 403 (Forbidden) if the user has no privileges to delete the vote.
     */
    @RequestMapping(value = "/votes/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVote(@PathVariable final Long id)
    {
        this.log.debug("REST request to delete Vote : {}", id);
        try
        {
            this.voteService.delete(id);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("vote", id.toString())).build();
        }
        catch (final PermissionsDeniedException ex)
        {
            this.log.debug(
                    "User: " + SecurityUtils.getCurrentUserName() + " has no permissions to create the vote: " + ex.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * GET /votes : get all votings for votes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of votes in body
     */
    @RequestMapping(value = "/votes/{id}/votings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserVotingsDTO>> getAllVotingsForVotes(@PathVariable final Long id)
    {
        this.log.debug("REST request to get all Votes");
        final List<UserVotingsDTO> dtoList = UserVotingsConverter.toDtoList(this.userVotingsService.findByVoteId(id));
        dtoList.stream()
        .forEach(userVotingDTO -> userVotingDTO.add(UserVotingsResource.createLinktToSelf(userVotingDTO.getIdentifier())));
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    /**
     * GET /votes/users/{id} : get all the votes created by given user.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of votes in body
     */
    @RequestMapping(value = "/votes/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<VoteDTO>> getAllVotesForCreator(@PathVariable final Long id)
    {
        this.log.debug("REST request to get all Votes created by user: " + id);

        if (id != null)
        {
            final List<VoteDTO> voteList = new ArrayList<>();
            this.voteService.getVotesByCreator(id).stream()
            .forEach(vote -> {
                final VoteDTO voteDTO = VoteConverter.toDto(this.getCurrentUserId(), vote, this.userVotingsService);
                voteDTO.add(VoteResource.createLinkToSelf(voteDTO.getIdentifier()));
                voteList.add(voteDTO);
            });
            return new ResponseEntity<>(voteList, HttpStatus.OK);
        }
        else
        {
            this.log.debug("The user with id: " + id + " was not found.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public static Link createLinkToSelf(final Long id)
    {
        return linkTo(methodOn(VoteResource.class).getVote(id)).withSelfRel();
    }

    public static Link createLinkToVotingsForVote(final Long id)
    {
        return linkTo(methodOn(VoteResource.class).getAllVotingsForVotes(id)).withSelfRel();
    }

    private Long getCurrentUserId()
    {
        final User user = this.userService.getUserByUserName(SecurityUtils.getCurrentUserName());
        return user != null ? user.getId() : null;
    }
}
