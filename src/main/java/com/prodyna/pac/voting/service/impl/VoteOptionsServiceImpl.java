package com.prodyna.pac.voting.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prodyna.pac.voting.domain.UserVotings;
import com.prodyna.pac.voting.domain.Vote;
import com.prodyna.pac.voting.domain.VoteOption;
import com.prodyna.pac.voting.exceptions.PermissionsDeniedException;
import com.prodyna.pac.voting.repository.VoteOptionsRepository;
import com.prodyna.pac.voting.security.AuthoritiesConstants;
import com.prodyna.pac.voting.security.SecurityUtils;
import com.prodyna.pac.voting.service.UserVotingsService;
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

    @Inject
    private UserVotingsService userVotingService;

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
        return this.voteOptionsRepository.save(voteOption);
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
        return this.voteOptionsRepository.findAll();
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
        return this.voteOptionsRepository.findOne(id);
    }

    /**
     * Delete the voteOptions by id.
     *
     * @param id
     *            the id of the entity
     */
    @Override
    public void delete(final Long id) throws PermissionsDeniedException
    {
        this.log.debug("Request to delete VoteOptions : {}", id);
        final VoteOption voteOption = this.voteOptionsRepository.getOne(id);
        if (voteOption != null)
        {
            final boolean hasPermission = voteOption.getVote().getCreator().getUserName()
                    .equalsIgnoreCase(SecurityUtils.getCurrentUserName())
                    || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
            if (hasPermission)
            {
                final VoteOption voteOptions = this.voteOptionsRepository.findOne(id);
                if (voteOptions != null)
                {
                    this.voteOptionsRepository.delete(id);
                    this.log.debug("VoteOption deleted: {}", voteOptions);

                    final List<UserVotings> userVotingsByOptionsId = this.userVotingService.findByVoteOptionsId(id);
                    for (final UserVotings userVotings : userVotingsByOptionsId)
                    {
                        final Long userVotingId = userVotings.getId();
                        this.log.debug("Request to delete UserVoting : {}", userVotingId);
                        this.userVotingService.delete(userVotingId);
                    }
                }
            }
            else
            {
                this.log.debug("The current user has no priviliges to delete the voteOption with id: {}", id);
                throw new PermissionsDeniedException();
            }
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
        return this.voteOptionsRepository.findAllByVote(vote);
    }
}
