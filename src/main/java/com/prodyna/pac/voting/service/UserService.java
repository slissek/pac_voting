package com.prodyna.pac.voting.service;

import java.util.List;

import com.prodyna.pac.voting.domain.User;

/**
 * Service Interface for managing Users.
 */
public interface UserService
{
    /**
     * Save a users.
     *
     * @param userDTO
     *            the entity to save
     * @return the persisted entity
     */
    User save(User user);

    /**
     * Get all the users.
     *
     * @return the list of entities
     */
    List<User> getAll();

    /**
     * Get the "id" user.
     *
     * @param id
     *            the id of the entity
     * @return the entity
     */
    User getUserById(Long id);

    /**
     * Get the "username" user.
     *
     * @param username
     *            the username of the entity
     * @return the entity
     */
    User getUserByUserName(String username);

    /**
     * Delete the "id" users.
     *
     * @param id
     *            the id of the entity
     */
    void delete(Long id);

    /**
     * Get the user with authorities
     *
     * @return the entity
     */
    User getUserWithAuthorities();

    /**
     * Get the "id" user with authorities.
     *
     * @param id
     *            the id of the entity
     * @return the entity
     */
    User getUserWithAuthorities(final Long id);

    /**
     * Get the "userName" user with authorities.
     *
     * @param userName
     *            the username of the entity
     * @return the entity
     */
    User getUserWithAuthoritiesByUserName(final String userName);

    /**
     * Update the user information.
     *
     * @param firstName
     *            the fist name of the entity
     * @param lastName
     *            the last name of the entity
     * @return the entity
     */
    User updateUserInformation(final String firstName, final String lastName);
}
