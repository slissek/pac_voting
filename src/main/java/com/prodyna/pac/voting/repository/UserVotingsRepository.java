package com.prodyna.pac.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prodyna.pac.voting.domain.UserVotings;

/**
 * Spring Data JPA repository for the UserVotings entity.
 */
public interface UserVotingsRepository extends JpaRepository<UserVotings,Long> {

}
