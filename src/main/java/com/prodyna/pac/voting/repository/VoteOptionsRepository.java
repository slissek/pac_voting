package com.prodyna.pac.voting.repository;

import java.util.List;

import javax.persistence.Entity;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prodyna.pac.voting.domain.Vote;
import com.prodyna.pac.voting.domain.VoteOptions;

/**
 * Spring Data JPA repository for the VoteOptions entity.
 */
public interface VoteOptionsRepository extends JpaRepository<VoteOptions, Long>
{
    /**
     * Get all VoteOptions
     *
     * @return all found {@link Vote} {@link Entity}
     */
    List<VoteOptions> findAllByVote(Vote vote);
}
