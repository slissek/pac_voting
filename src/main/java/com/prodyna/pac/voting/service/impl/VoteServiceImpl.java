package com.prodyna.pac.voting.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prodyna.pac.voting.domain.UserVotings;
import com.prodyna.pac.voting.domain.Vote;
import com.prodyna.pac.voting.exceptions.PermissionsDeniedException;
import com.prodyna.pac.voting.repository.VoteRepository;
import com.prodyna.pac.voting.security.AuthoritiesConstants;
import com.prodyna.pac.voting.security.SecurityUtils;
import com.prodyna.pac.voting.service.UserVotingsService;
import com.prodyna.pac.voting.service.VoteService;

/**
 * Service Implementation for managing Vote.
 */
@Service("voteService")
@Transactional
public class VoteServiceImpl implements VoteService
{

    private final Logger log = LoggerFactory.getLogger(VoteServiceImpl.class);

    @Inject
    private VoteRepository voteRepository;

    @Inject
    private UserVotingsService userVotingsService;

    @Override
    public Vote save(final Vote vote) throws PermissionsDeniedException
    {
        final boolean hasPermission = (vote.getId() == null)
                || (vote.getCreator().getUserName().equalsIgnoreCase(SecurityUtils.getCurrentUserName())
                        || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN));
        if (hasPermission)
        {
            final Vote result = this.voteRepository.save(vote);
            this.log.debug("Saved Vote : {}", vote);
            return result;
        }
        else
        {
            this.log.debug("The current user has no priviliges to edit the vote: {}", vote);
            throw new PermissionsDeniedException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vote> getAll(final Sort sort)
    {
        this.log.debug("Request to get all Votes");
        return this.voteRepository.findAll(sort);
    }

    @Override
    @Transactional(readOnly = true)
    public Vote findOne(final Long id)
    {
        this.log.debug("Request to get Vote : {}", id);
        return this.voteRepository.findOne(id);
    }

    @Override
    public void delete(final Long id) throws PermissionsDeniedException
    {
        this.log.debug("Request to delete Vote : {}", id);
        final Vote vote = this.voteRepository.findOne(id);
        if (vote != null)
        {
            final boolean hasPermission = vote.getCreator().getUserName().equalsIgnoreCase(SecurityUtils.getCurrentUserName())
                    || SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
            if (hasPermission)
            {
                this.voteRepository.delete(id);
                this.log.debug("Vote deleted: {}", vote);

                final List<UserVotings> userVotingsByVoteId = this.userVotingsService.findByVoteId(id);
                for (final UserVotings userVotings : userVotingsByVoteId)
                {
                    final Long userVotingId = userVotings.getId();
                    this.log.debug("Request to delete UserVoting : {}", userVotingId);
                    this.userVotingsService.delete(userVotingId);
                }
            }
            else
            {
                this.log.debug("The current user has no priviliges to delete the vote: {}", vote);
                throw new PermissionsDeniedException();
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vote> getVotesByCreator(final Long userId)
    {
        this.log.debug("Request to get all Votes created by user: " + userId);
        return this.voteRepository.findByCreatorId(userId);
    }

    @Override
    public Long getVoteCountByCreator(final Long userId)
    {
        this.log.debug("Request to get count of votes created by user: " + userId);
        return this.voteRepository.countByCreatorId(userId);
    }
}
