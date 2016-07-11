package com.prodyna.pac.voting.service;

import java.util.List;

import com.prodyna.pac.voting.domain.UserVotings;

/**
 * Service Interface for managing UserVotings.
 */
public interface UserVotingsService
{

    /**
     * Save a userVotings.
     *
     * @param userVotings
     *            the entity to save
     * @return the persisted entity
     */
    UserVotings save(UserVotings userVotings);

    /**
     * Get all the userVotings.
     *
     * @return the list of entities
     */
    List<UserVotings> findAll();

    /**
     * Get the "id" userVotings.
     *
     * @param id
     *            the id of the entity
     * @return the entity
     */
    UserVotings findOne(Long id);

    /**
     * Get all the userVotings for given UserId
     *
     * @param userId
     *            the userId to use
     * @return the list of entities
     */
    List<UserVotings> findByUserId(Long userId);

    /**
     * Get all the userVotings for given voteId
     *
     * @param voteId
     *            the voteId to use
     * @return the list of entities
     */
    List<UserVotings> findByVoteId(Long voteId);

    /**
     * Get all the userVotings for given voteOptionsId
     *
     * @param voteOptionsId
     *            the voteOptionsId to use
     * @return the list of entities
     */
    List<UserVotings> findByVoteOptionsId(Long voteOptionsId);

    /**
     * Get all the userVotings for given userId and voteId
     *
     * @param userId
     *            the userId to use
     *
     * @param voteId
     *            the voteId to use
     * @return the list of entities
     */
    List<UserVotings> findByUserIdAndVoteId(Long userId, Long voteId);

    /**
     * Get the count of all votes for given voteId
     *
     * @param voteId
     *            the voteId to use
     * @return the count
     */
    Long getCountForVoteId(Long voteId);

    /**
     * Get the count of all votes for given voteId and voteOptionId
     *
     * @param voteId
     *            the voteId to use
     * @param voteOptionId
     *            the voteOptionId
     * @return the count
     */
    Long getCountForVoteIdAndOption(Long voteId, Long voteOptionId);

    /**
     * Delete the "id" userVotings.
     *
     * @param id
     *            the id of the entity
     */
    void delete(Long id);

}
