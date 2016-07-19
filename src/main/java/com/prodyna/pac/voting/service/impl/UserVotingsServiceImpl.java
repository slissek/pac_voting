package com.prodyna.pac.voting.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
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
        return this.userVotingsRepository.save(userVotings);
    }

    /**
     * Get all the userVotings.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserVotings> findAll(final Sort sort)
    {
        this.log.debug("Request to get all UserVotings");
        return this.userVotingsRepository.findAll(sort);
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
        return this.userVotingsRepository.findOne(id);
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
    @Transactional(readOnly = true)
    public List<UserVotings> findByUserId(final Long userId)
    {
        this.log.debug("Request to get UserVotings by userID: {}", userId);
        return this.userVotingsRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserVotings> findByVoteId(final Long voteId)
    {
        this.log.debug("Request to get UserVotings by voteID: {}", voteId);
        return this.userVotingsRepository.findByVoteId(voteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserVotings> findByVoteOptionsId(final Long voteOptionsId)
    {
        this.log.debug("Request to get UserVotings by voteOptionsID: {}", voteOptionsId);
        return this.userVotingsRepository.findByVoteOptionsId(voteOptionsId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserVotings> findByUserIdAndVoteId(final Long userId, final Long voteId)
    {
        this.log.debug("Request to get UserVotings by userId and voteID: {}", userId, voteId);
        return this.userVotingsRepository.findByUserIdAndVoteId(userId, voteId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForVoteId(final Long voteId)
    {
        this.log.debug("Request to get count by voteID: {}", voteId);
        return this.userVotingsRepository.countByVoteId(voteId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCountForVoteIdAndOption(final Long voteId, final Long voteOptionsId)
    {
        this.log.debug("Request to get count by voteID and voteOptionsId: {}", voteId, voteOptionsId);
        return this.userVotingsRepository.countByVoteIdAndVoteOptionsId(voteId, voteOptionsId);
    }
}
