package com.prodyna.pac.voting.service;

import java.util.List;

import com.prodyna.pac.voting.domain.Vote;
import com.prodyna.pac.voting.domain.VoteOptions;

/**
 * Service Interface for managing VoteOptions.
 */
public interface VoteOptionsService
{

    /**
     * Save a voteOptions.
     *
     * @param voteOptions
     *            the entity to save
     * @return the persisted entity
     */
    VoteOptions save(VoteOptions voteOptions);

    /**
     * Get all the voteOptions.
     *
     * @return the list of entities
     */
    List<VoteOptions> findAll();

    /**
     * Get all the voteOptions.
     *
     * @param vote
     *            the vote the options relates to
     * @return the list of entities
     */
    List<VoteOptions> findAllByVote(Vote vote);

    /**
     * Get the "id" voteOptions.
     *
     * @param id
     *            the id of the entity
     * @return the entity
     */
    <Optional>VoteOptions findOne(Long id);

    /**
     * Delete the "id" voteOptions.
     *
     * @param id
     *            the id of the entity
     */
    void delete(Long id);

}
