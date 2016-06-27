package com.prodyna.pac.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prodyna.pac.voting.domain.Vote;

/**
 * Spring Data JPA repository for the Vote entity.
 */
public interface VoteRepository extends JpaRepository<Vote,Long> {

}
