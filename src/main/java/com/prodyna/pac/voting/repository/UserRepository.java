/**
 *
 */
package com.prodyna.pac.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prodyna.pac.voting.domain.User;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long>
{
    User findOneById(Long id);

    User findOneByUserName(String username);
}
