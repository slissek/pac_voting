package com.prodyna.pac.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prodyna.pac.voting.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
