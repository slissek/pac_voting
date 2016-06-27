package com.prodyna.pac.voting.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.prodyna.pac.voting.domain.VoteOptions;
import com.prodyna.pac.voting.service.VoteOptionsService;
import com.prodyna.pac.voting.web.rest.util.HeaderUtil;

/**
 * REST controller for managing VoteOptions.
 */
@RestController
@RequestMapping("/api")
public class VoteOptionsResource {

    private final Logger log = LoggerFactory.getLogger(VoteOptionsResource.class);

    @Inject
    private VoteOptionsService voteOptionsService;

    /**
     * POST  /vote-options : Create a new voteOptions.
     *
     * @param voteOptions the voteOptions to create
     * @return the ResponseEntity with status 201 (Created) and with body the new voteOptions, or with status 400 (Bad Request) if the voteOptions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/vote-options",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //    @Timed
    public ResponseEntity<VoteOptions> createVoteOptions(@Valid @RequestBody final VoteOptions voteOptions) throws URISyntaxException {
        this.log.debug("REST request to save VoteOptions : {}", voteOptions);
        if (voteOptions.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("voteOptions", "idexists", "A new voteOptions cannot already have an ID")).body(null);
        }
        final VoteOptions result = this.voteOptionsService.save(voteOptions);
        return ResponseEntity.created(new URI("/api/vote-options/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("voteOptions", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /vote-options : Updates an existing voteOptions.
     *
     * @param voteOptions the voteOptions to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated voteOptions,
     * or with status 400 (Bad Request) if the voteOptions is not valid,
     * or with status 500 (Internal Server Error) if the voteOptions couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/vote-options",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //    @Timed
    public ResponseEntity<VoteOptions> updateVoteOptions(@Valid @RequestBody final VoteOptions voteOptions) throws URISyntaxException {
        this.log.debug("REST request to update VoteOptions : {}", voteOptions);
        if (voteOptions.getId() == null) {
            return this.createVoteOptions(voteOptions);
        }
        final VoteOptions result = this.voteOptionsService.save(voteOptions);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("voteOptions", voteOptions.getId().toString()))
                .body(result);
    }

    /**
     * GET  /vote-options : get all the voteOptions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of voteOptions in body
     */
    @RequestMapping(value = "/vote-options",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //    @Timed
    public List<VoteOptions> getAllVoteOptions() {
        this.log.debug("REST request to get all VoteOptions");
        return this.voteOptionsService.findAll();
    }

    /**
     * GET  /vote-options/:id : get the "id" voteOptions.
     *
     * @param id the id of the voteOptions to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the voteOptions, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/vote-options/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //    @Timed
    public ResponseEntity<VoteOptions> getVoteOptions(@PathVariable final Long id) {
        this.log.debug("REST request to get VoteOptions : {}", id);
        final VoteOptions voteOptions = this.voteOptionsService.findOne(id);
        return Optional.ofNullable(voteOptions)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /vote-options/:id : delete the "id" voteOptions.
     *
     * @param id the id of the voteOptions to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/vote-options/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //    @Timed
    public ResponseEntity<Void> deleteVoteOptions(@PathVariable final Long id) {
        this.log.debug("REST request to delete VoteOptions : {}", id);
        this.voteOptionsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("voteOptions", id.toString())).build();
    }
}
