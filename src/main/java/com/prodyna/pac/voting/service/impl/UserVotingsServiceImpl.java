package com.prodyna.pac.voting.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prodyna.pac.voting.domain.UserVotings;
import com.prodyna.pac.voting.repository.UserVotingsRepository;
import com.prodyna.pac.voting.service.UserVotingsService;

/**
 * Service Implementation for managing UserVotings.
 */
@Service("userVotingService")
@Transactional
public class UserVotingsServiceImpl implements UserVotingsService
{
    private final Logger log = LoggerFactory.getLogger(UserVotingsServiceImpl.class);

    @Inject
    private UserVotingsRepository userVotingsRepository;

    /**
     * Save a userVotings.
     *
     * @param userVotings
     *            the entity to save
     * @return the persisted entity
     */
    @Override
    public UserVotings save(final UserVotings userVotings)
    {
        this.log.debug("Request to save UserVotings : {}", userVotings);
        final UserVotings result = this.userVotingsRepository.save(userVotings);
        return result;
    }

    /**
     * Get all the userVotings.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserVotings> findAll()
    {
        this.log.debug("Request to get all UserVotings");
        final List<UserVotings> result = this.userVotingsRepository.findAll();
        return result;
    }

    /**
     * Get one userVotings by id.
     *
     * @param id
     *            the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public UserVotings findOne(final Long id)
    {
        this.log.debug("Request to get UserVotings : {}", id);
        final UserVotings userVotings = this.userVotingsRepository.findOne(id);
        return userVotings;
    }

    /**
     * Delete the userVotings by id.
     *
     * @param id
     *            the id of the entity
     */
    @Override
    public void delete(final Long id)
    {
        this.log.debug("Request to delete UserVotings : {}", id);
        final UserVotings userVoting = this.userVotingsRepository.findOne(id);
        if (userVoting != null)
        {
            this.userVotingsRepository.delete(id);
            this.log.debug("User voting deleted: {}", userVoting);
        }
    }

    @Override
    public List<UserVotings> findByUserId(final Long userId)
    {
        this.log.debug("Request to get UserVotings by userID: {}", userId);
        final List<UserVotings> userVotings = this.userVotingsRepository.findByUserId(userId);
        return userVotings;
    }

    @Override
    public List<UserVotings> findByVoteId(final Long voteId)
    {
        this.log.debug("Request to get UserVotings by voteID: {}", voteId);
        final List<UserVotings> userVotings = this.userVotingsRepository.findByVoteId(voteId);
        return userVotings;
    }

    @Override
    public List<UserVotings> findByVoteOptionsId(final Long voteOptionsId)
    {
        this.log.debug("Request to get UserVotings by voteOptionsID: {}", voteOptionsId);
        final List<UserVotings> userVotings = this.userVotingsRepository.findByVoteOptionsId(voteOptionsId);
        return userVotings;
    }

    @Override
    public List<UserVotings> findByUserIdAndVoteId(final Long userId, final Long voteId)
    {
        this.log.debug("Request to get UserVotings by userId and voteID: {}", userId, voteId);
        final List<UserVotings> userVotings = this.userVotingsRepository.findByUserIdAndVoteId(userId, voteId);
        return userVotings;
    }

    @Override
    public Long getCountForVoteId(final Long voteId)
    {
        this.log.debug("Request to get count by voteID: {}", voteId);
        final Long countByVoteId = this.userVotingsRepository.countByVoteId(voteId);
        return countByVoteId;
    }

    @Override
    public Long getCountForVoteIdAndOption(final Long voteId, final Long voteOptionsId)
    {
        this.log.debug("Request to get count by voteID and voteOptionsId: {}", voteId, voteOptionsId);
        final Long countByVoteId = this.userVotingsRepository.countByVoteIdAndVoteOptionsId(voteId, voteOptionsId);
        return countByVoteId;
    }
}
