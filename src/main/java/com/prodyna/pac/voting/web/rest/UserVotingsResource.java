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

import com.prodyna.pac.voting.domain.UserVotings;
import com.prodyna.pac.voting.service.UserVotingsService;
import com.prodyna.pac.voting.web.rest.util.HeaderUtil;

/**
 * REST controller for managing UserVotings.
 */
@RestController
@RequestMapping("/api")
public class UserVotingsResource {

    private final Logger log = LoggerFactory.getLogger(UserVotingsResource.class);

    @Inject
    private UserVotingsService userVotingsService;

    /**
     * POST  /user-votings : Create a new userVotings.
     *
     * @param userVotings the userVotings to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userVotings, or with status 400 (Bad Request) if the userVotings has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-votings",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //    @Timed
    public ResponseEntity<UserVotings> createUserVotings(@Valid @RequestBody final UserVotings userVotings) throws URISyntaxException {
        this.log.debug("REST request to save UserVotings : {}", userVotings);
        if (userVotings.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userVotings", "idexists", "A new userVotings cannot already have an ID")).body(null);
        }
        final UserVotings result = this.userVotingsService.save(userVotings);
        return ResponseEntity.created(new URI("/api/user-votings/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("userVotings", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /user-votings : Updates an existing userVotings.
     *
     * @param userVotings the userVotings to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userVotings,
     * or with status 400 (Bad Request) if the userVotings is not valid,
     * or with status 500 (Internal Server Error) if the userVotings couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-votings",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //    @Timed
    public ResponseEntity<UserVotings> updateUserVotings(@Valid @RequestBody final UserVotings userVotings) throws URISyntaxException {
        this.log.debug("REST request to update UserVotings : {}", userVotings);
        if (userVotings.getId() == null) {
            return this.createUserVotings(userVotings);
        }
        final UserVotings result = this.userVotingsService.save(userVotings);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("userVotings", userVotings.getId().toString()))
                .body(result);
    }

    /**
     * GET  /user-votings : get all the userVotings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userVotings in body
     */
    @RequestMapping(value = "/user-votings",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //    @Timed
    public List<UserVotings> getAllUserVotings() {
        this.log.debug("REST request to get all UserVotings");
        return this.userVotingsService.findAll();
    }

    /**
     * GET  /user-votings/:id : get the "id" userVotings.
     *
     * @param id the id of the userVotings to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userVotings, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/user-votings/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //    @Timed
    public ResponseEntity<UserVotings> getUserVotings(@PathVariable final Long id) {
        this.log.debug("REST request to get UserVotings : {}", id);
        final UserVotings userVotings = this.userVotingsService.findOne(id);
        return Optional.ofNullable(userVotings)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-votings/:id : delete the "id" userVotings.
     *
     * @param id the id of the userVotings to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/user-votings/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    //    @Timed
    public ResponseEntity<Void> deleteUserVotings(@PathVariable final Long id) {
        this.log.debug("REST request to delete UserVotings : {}", id);
        this.userVotingsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userVotings", id.toString())).build();
    }
}
