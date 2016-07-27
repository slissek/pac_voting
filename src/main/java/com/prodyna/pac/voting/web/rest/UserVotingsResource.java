package com.prodyna.pac.voting.web.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
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
import com.prodyna.pac.voting.domain.UserVotings;
import com.prodyna.pac.voting.service.UserVotingsService;
import com.prodyna.pac.voting.web.rest.converter.UserVotingsConverter;
import com.prodyna.pac.voting.web.rest.dto.UserVotingsDTO;
import com.prodyna.pac.voting.web.rest.util.HeaderUtil;

/**
 * REST controller for managing UserVotings.
 */
@RestController
@RequestMapping("/api")
public class UserVotingsResource
{

    private final Logger log = LoggerFactory.getLogger(UserVotingsResource.class);

    @Inject
    private UserVotingsService userVotingsService;

    /**
     * POST /user-votings : Create a new userVotings.
     *
     * @param userVotings
     *            the userVotings to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userVotings, or with status 400 (Bad Request) if the
     *         userVotings has already an ID
     * @throws URISyntaxException
     *             if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-votings", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserVotingsDTO> createUserVotings(@Valid @RequestBody final UserVotingsDTO userVotingsDTO)
            throws URISyntaxException
    {
        this.log.debug("REST request to save UserVotings : {}", userVotingsDTO);
        if (userVotingsDTO.getIdentifier() != null)
        {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("userVotings", "idexists", "A new userVotings cannot already have an ID"))
                    .body(null);
        }
        // Check if user already voted
        final List<UserVotings> findByUserIdAndVoteId = this.userVotingsService.findByUserIdAndVoteId(userVotingsDTO.getUserId(),
                userVotingsDTO.getVoteId());
        if ((findByUserIdAndVoteId != null) && (!findByUserIdAndVoteId.isEmpty()))
        {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("userVotings", "useralreadyvoted", "The user already voted this vote"))
                    .body(null);
        }

        final UserVotings userVotings = UserVotingsConverter.toEntity(userVotingsDTO);
        final UserVotingsDTO result = UserVotingsConverter.toDto(this.userVotingsService.save(userVotings));
        result.add(UserVotingsResource.createLinktToSelf(result.getIdentifier()));
        return ResponseEntity.created(new URI("/api/user-votings/" + result.getIdentifier()))
                .headers(HeaderUtil.createEntityCreationAlert("userVotings", result.getIdentifier().toString()))
                .body(result);
    }

    /**
     * PUT /user-votings : Updates an existing userVotings.
     *
     * @param userVotings
     *            the userVotings to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userVotings, or with status 400 (Bad Request) if the
     *         userVotings is not valid, or with status 500 (Internal Server Error) if the userVotings couldnt be updated
     * @throws URISyntaxException
     *             if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-votings", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserVotingsDTO> updateUserVotings(@Valid @RequestBody final UserVotingsDTO userVotingsDTO)
            throws URISyntaxException
    {
        this.log.debug("REST request to update UserVotings : {}", userVotingsDTO);
        if (userVotingsDTO.getIdentifier() == null)
        {
            return this.createUserVotings(userVotingsDTO);
        }
        // Check if user already voted
        final List<UserVotings> findByUserIdAndVoteId = this.userVotingsService.findByUserIdAndVoteId(userVotingsDTO.getUserId(),
                userVotingsDTO.getVoteId());
        if ((findByUserIdAndVoteId != null) && (!findByUserIdAndVoteId.isEmpty()))
        {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("userVotings", "useralreadyvoted", "The user already voted this vote"))
                    .body(null);
        }
        final UserVotings userVotings = UserVotingsConverter.toEntity(userVotingsDTO);
        final UserVotingsDTO result = UserVotingsConverter.toDto(this.userVotingsService.save(userVotings));
        result.add(UserVotingsResource.createLinktToSelf(result.getIdentifier()));
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("userVotings", userVotings.getId().toString()))
                .body(result);
    }

    /**
     * GET /user-votings : get all the userVotings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userVotings in body
     */
    @RequestMapping(value = "/user-votings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserVotingsDTO> getAllUserVotings()
    {
        this.log.debug("REST request to get all UserVotings");
        final List<UserVotingsDTO> userVotings = UserVotingsConverter
                .toDtoList(this.userVotingsService.findAll(new Sort(Sort.Direction.ASC, "id")));
        userVotings.stream().forEach(userVoting -> userVoting.add(UserVotingsResource.createLinktToSelf(userVoting.getIdentifier())));
        return userVotings;
    }

    /**
     * GET /user-votings/:id : get the "id" userVotings.
     *
     * @param id
     *            the id of the userVotings to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userVotings, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/user-votings/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserVotingsDTO> getUserVotings(@PathVariable final Long id)
    {
        this.log.debug("REST request to get UserVotings : {}", id);
        final UserVotings userVotings = this.userVotingsService.findOne(id);
        if (userVotings != null)
        {
            final UserVotingsDTO userVotingsDTO = UserVotingsConverter.toDto(userVotings);
            userVotingsDTO.add(UserVotingsResource.createLinktToSelf(userVotingsDTO.getIdentifier()));
            return new ResponseEntity<>(userVotingsDTO, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * DELETE /user-votings/:id : delete the "id" userVotings.
     *
     * @param id
     *            the id of the userVotings to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/user-votings/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserVotings(@PathVariable final Long id)
    {
        this.log.debug("REST request to delete UserVotings : {}", id);
        this.userVotingsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userVotings", id.toString())).build();
    }

    public static Link createLinktToSelf(final Long id)
    {
        return linkTo(methodOn(UserVotingsResource.class).getUserVotings(id)).withSelfRel();
    }
}
