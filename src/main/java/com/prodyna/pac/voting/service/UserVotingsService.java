package com.prodyna.pac.voting.service;

import java.util.List;

import com.prodyna.pac.voting.domain.UserVotings;

/**
 * Service Interface for managing UserVotings.
 */
public interface UserVotingsService {

    /**
     * Save a userVotings.
     *
     * @param userVotings the entity to save
     * @return the persisted entity
     */
    UserVotings save(UserVotings userVotings);

    /**
     *  Get all the userVotings.
     *
     *  @return the list of entities
     */
    List<UserVotings> findAll();

    /**
     *  Get the "id" userVotings.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    <Optional>UserVotings findOne(Long id);

    /**
     *  Delete the "id" userVotings.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

}
