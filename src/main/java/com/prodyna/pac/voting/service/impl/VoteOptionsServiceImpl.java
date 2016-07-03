package com.prodyna.pac.voting.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prodyna.pac.voting.domain.Vote;
import com.prodyna.pac.voting.domain.VoteOption;
import com.prodyna.pac.voting.repository.VoteOptionsRepository;
import com.prodyna.pac.voting.service.VoteOptionsService;

/**
 * Service Implementation for managing VoteOptions.
 */
@Service("votingOptionsService")
@Transactional
public class VoteOptionsServiceImpl implements VoteOptionsService
{
    private final Logger log = LoggerFactory.getLogger(VoteOptionsServiceImpl.class);

    @Inject
    private VoteOptionsRepository voteOptionsRepository;

    /**
     * Save a voteOptions.
     *
     * @param voteOptions
     *            the entity to save
     * @return the persisted entity
     */
    @Override
    public VoteOption save(final VoteOption voteOption)
    {
        this.log.debug("Request to save VoteOptions : {}", voteOption);
        final VoteOption result = this.voteOptionsRepository.save(voteOption);
        return result;
    }

    /**
     * Get all the voteOptions.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<VoteOption> findAll()
    {
        this.log.debug("Request to get all VoteOptions");
        final List<VoteOption> result = this.voteOptionsRepository.findAll();
        return result;
    }

    /**
     * Get one voteOptions by id.
     *
     * @param id
     *            the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public VoteOption findOne(final Long id)
    {
        this.log.debug("Request to get VoteOptions : {}", id);
        final VoteOption voteOptions = this.voteOptionsRepository.findOne(id);
        return voteOptions;
    }

    /**
     * Delete the voteOptions by id.
     *
     * @param id
     *            the id of the entity
     */
    @Override
    public void delete(final Long id)
    {
        this.log.debug("Request to delete VoteOptions : {}", id);
        final VoteOption voteOptions = this.voteOptionsRepository.findOne(id);
        if (voteOptions != null)
        {
            this.voteOptionsRepository.delete(id);
            this.log.debug("User deleted: {}", voteOptions);
        }
    }

    /**
     * Get all voteOptions relates to give {@link Vote}
     *
     * @param vote
     *            the parent {@link Vote}
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<VoteOption> findAllByVote(final Vote vote)
    {
        this.log.debug("Request to get all VoteOptions relates to vote: {}", vote);
        final List<VoteOption> voteOptionsByVote = this.voteOptionsRepository.findAllByVote(vote);
        return voteOptionsByVote;
    }
}
