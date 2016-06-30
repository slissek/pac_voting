package com.prodyna.pac.voting.repository;

import java.util.List;

import javax.persistence.Entity;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prodyna.pac.voting.domain.Vote;
import com.prodyna.pac.voting.domain.VoteOption;

/**
 * Spring Data JPA repository for the VoteOptions entity.
 */
public interface VoteOptionsRepository extends JpaRepository<VoteOption, Long>
{
    /**
     * Get all VoteOptions
     *
     * @return all found {@link Vote} {@link Entity}
     */
    List<VoteOption> findAllByVote(Vote vote);
}
