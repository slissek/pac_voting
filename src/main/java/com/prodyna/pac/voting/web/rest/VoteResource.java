package com.prodyna.pac.voting.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.prodyna.pac.voting.domain.Vote;
import com.prodyna.pac.voting.service.VoteService;
import com.prodyna.pac.voting.web.rest.util.HeaderUtil;
import com.prodyna.pac.voting.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Vote.
 */
@RestController
@RequestMapping("/api")
public class VoteResource {

    private final Logger log = LoggerFactory.getLogger(VoteResource.class);

    @Inject
    private VoteService voteService;

    /**
     * POST  /votes : Create a new vote.
     *
     * @param vote the vote to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vote, or with status 400 (Bad Request) if the vote has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/votes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //    @Timed
    public ResponseEntity<Vote> createVote(@Valid @RequestBody final Vote vote) throws URISyntaxException {
        this.log.debug("REST request to save Vote : {}", vote);
        if (vote.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("vote", "idexists", "A new vote cannot already have an ID")).body(null);
        }
        final Vote result = this.voteService.save(vote);
        return ResponseEntity.created(new URI("/api/votes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("vote", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /votes : Updates an existing vote.
     *
     * @param vote the vote to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vote,
     * or with status 400 (Bad Request) if the vote is not valid,
     * or with status 500 (Internal Server Error) if the vote couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/votes",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //    @Timed
    public ResponseEntity<Vote> updateVote(@Valid @RequestBody final Vote vote) throws URISyntaxException {
        this.log.debug("REST request to update Vote : {}", vote);
        if (vote.getId() == null) {
            return this.createVote(vote);
        }
        final Vote result = this.voteService.save(vote);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("vote", vote.getId().toString()))
                .body(result);
    }

    /**
     * GET  /votes : get all the votes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of votes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/votes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //    @Timed
    public ResponseEntity<List<Vote>> getAllVotes(final Pageable pageable)
            throws URISyntaxException {
        this.log.debug("REST request to get a page of Votes");
        final Page<Vote> page = this.voteService.findAll(pageable);
        final HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/votes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /votes/:id : get the "id" vote.
     *
     * @param id the id of the vote to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vote, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/votes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //    @Timed
    public ResponseEntity<Vote> getVote(@PathVariable final Long id) {
        this.log.debug("REST request to get Vote : {}", id);
        final Vote vote = this.voteService.findOne(id);
        return Optional.ofNullable(vote)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /votes/:id : delete the "id" vote.
     *
     * @param id the id of the vote to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/votes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //    @Timed
    public ResponseEntity<Void> deleteVote(@PathVariable final Long id) {
        this.log.debug("REST request to delete Vote : {}", id);
        this.voteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("vote", id.toString())).build();
    }
}
