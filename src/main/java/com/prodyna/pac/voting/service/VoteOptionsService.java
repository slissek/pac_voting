package com.prodyna.pac.voting.service;

import java.util.List;

import com.prodyna.pac.voting.domain.Vote;
import com.prodyna.pac.voting.domain.VoteOption;

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
    VoteOption save(VoteOption voteOptions);

    /**
     * Get all the voteOptions.
     *
     * @return the list of entities
     */
    List<VoteOption> findAll();

    /**
     * Get all the voteOptions.
     *
     * @param vote
     *            the vote the options relates to
     * @return the list of entities
     */
    List<VoteOption> findAllByVote(Vote vote);

    /**
     * Get the "id" voteOptions.
     *
     * @param id
     *            the id of the entity
     * @return the entity
     */
    <Optional>VoteOption findOne(Long id);

    /**
     * Delete the "id" voteOptions.
     *
     * @param id
     *            the id of the entity
     */
    void delete(Long id);

}
