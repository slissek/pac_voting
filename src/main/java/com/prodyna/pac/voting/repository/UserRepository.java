/**
 *
 */
package com.prodyna.pac.voting.repository;

import javax.persistence.Entity;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prodyna.pac.voting.domain.User;

/**
 * The repository interface for the {@link User} {@link Entity}
 *
 * @author <a href="mailto:sven.lissek@prodyna.com">Sven Lissek</a>, <a href="http://www.prodyna.com">PRODYNA AG</a>
 */
public interface UserRepository extends JpaRepository<User, Long>
{
    User findOneById(Long id);

    User findOneByUserName(String username);
}
