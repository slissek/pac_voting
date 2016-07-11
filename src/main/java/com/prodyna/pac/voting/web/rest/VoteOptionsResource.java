package com.prodyna.pac.voting.web.rest;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.prodyna.pac.voting.exceptions.PermissionsDeniedException;
import com.prodyna.pac.voting.service.VoteOptionsService;
import com.prodyna.pac.voting.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Vote options.
 */
@RestController
@RequestMapping("/api")
public class VoteOptionsResource
{

    private final Logger log = LoggerFactory.getLogger(VoteResource.class);

    @Inject
    private VoteOptionsService voteOptionsService;

    /**
     * DELETE /voteOptions/:id : delete the "id" vote option.
     *
     * @param id
     *            the id of the vote to delete
     * @return the ResponseEntity with status 200 (OK), or with status 403 (Forbidden) if the user has no privileges to delete the vote option.
     */
    @RequestMapping(value = "/voteOptions/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVote(@PathVariable final Long id)
    {
        this.log.debug("REST request to delete Vote : {}", id);
        try
        {
            this.voteOptionsService.delete(id);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("voteOptions", id.toString())).build();
        }
        catch (final PermissionsDeniedException ex)
        {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
