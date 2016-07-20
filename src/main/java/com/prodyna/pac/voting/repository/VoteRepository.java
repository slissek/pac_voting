package com.prodyna.pac.voting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prodyna.pac.voting.domain.Vote;

/**
 * Spring Data JPA repository for the Vote entity.
 */
public interface VoteRepository extends JpaRepository<Vote, Long>
{
    List<Vote> findByCreatorId(Long id);

    Long countByCreatorId(Long id);
}
