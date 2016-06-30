package com.prodyna.pac.voting.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prodyna.pac.voting.domain.Vote;
import com.prodyna.pac.voting.repository.VoteRepository;
import com.prodyna.pac.voting.service.VoteService;
import com.prodyna.pac.voting.web.rest.converter.VoteConverter;
import com.prodyna.pac.voting.web.rest.dto.VoteDTO;

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

    /**
     * Save a vote.
     *
     * @param voteDTO
     *            the entity to save
     * @return the persisted entity
     */
    @Override
    public Vote save(final VoteDTO voteDTO)
    {
        final Vote vote = VoteConverter.toEntity(voteDTO);
        final Vote result = this.voteRepository.save(vote);

        this.log.debug("Saved Vote : {}", voteDTO);

        return result;
    }

    /**
     * Get all the votes.
     *
     * @param pageable
     *            the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Vote> getAll()
    {
        this.log.debug("Request to get all Votes");

        final List<Vote> result = this.voteRepository.findAll();
        return result;
    }

    /**
     * Get one vote by id.
     *
     * @param id
     *            the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Vote findOne(final Long id)
    {
        this.log.debug("Request to get Vote : {}", id);
        final Vote vote = this.voteRepository.findOne(id);
        return vote;
    }

    /**
     * Delete the vote by id.
     *
     * @param id
     *            the id of the entity
     */
    @Override
    public void delete(final Long id)
    {
        this.log.debug("Request to delete Vote : {}", id);
        final Vote vote = this.voteRepository.findOne(id);
        if (vote != null)
        {
            this.voteRepository.delete(id);
            this.log.debug("Vote deleted: {}", vote);
        }
    }
}
