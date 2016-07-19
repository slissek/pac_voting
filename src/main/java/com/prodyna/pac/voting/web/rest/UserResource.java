package com.prodyna.pac.voting.web.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.prodyna.pac.voting.domain.Authority;
import com.prodyna.pac.voting.domain.User;
import com.prodyna.pac.voting.exceptions.PermissionsDeniedException;
import com.prodyna.pac.voting.repository.AuthorityRepository;
import com.prodyna.pac.voting.security.AuthoritiesConstants;
import com.prodyna.pac.voting.security.SecurityUtils;
import com.prodyna.pac.voting.service.UserService;
import com.prodyna.pac.voting.service.UserVotingsService;
import com.prodyna.pac.voting.web.rest.converter.UserConverter;
import com.prodyna.pac.voting.web.rest.converter.UserVotingsConverter;
import com.prodyna.pac.voting.web.rest.dto.ManagedUserDTO;
import com.prodyna.pac.voting.web.rest.dto.UserVotingsDTO;
import com.prodyna.pac.voting.web.rest.util.HeaderUtil;

/**
 * REST controller for managing User.
 */
@RestController
@RequestMapping("/api")
public class UserResource
{
    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserService userService;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private UserVotingsService userVotingsService;

    /**
     * POST /users : Create a new user.
     *
     * @param userDTO
     *            the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the user has
     *         already an ID, or with status 403 (Forbidden) if the user is not an admin.
     * @throws URISyntaxException
     *             if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<ManagedUserDTO> createUser(@RequestBody final ManagedUserDTO userDTO) throws URISyntaxException
    {
        this.log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getIdentifier() != null)
        {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("userManagement", "idexists", "A new user cannot already have an ID"))
                    .body(null);
        }
        else if (userDTO.getUserName() == null)
        {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("userManagement", "usernamemissing", "Username is mandatory")).body(null);
        }
        else if (this.userService.getUserByUserName(userDTO.getUserName()) != null)
        {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("userManagement", "usernameexists", "Username already in use")).body(null);
        }
        else
        {
            try
            {
                final User user = UserConverter.toEntity(userDTO, this.authorityRepository);
                final ManagedUserDTO newUser = UserConverter.toDto(this.userService.save(user));
                this.hateoasLinkBuilder(newUser);
                return ResponseEntity.created(new URI("/api/users/" + newUser.getIdentifier()))
                        .headers(HeaderUtil.createEntityCreationAlert("userManagement", newUser.getIdentifier().toString())).body(newUser);
            }
            catch (final PermissionsDeniedException ex)
            {
                this.log.debug("User: " + SecurityUtils.getCurrentUserName() + " has no permissions to create a user");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
    }

    /**
     * PUT /users : Updates an existing user.
     *
     * @param userDTO
     *            the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user, or with status 400 (Bad Request) if the user is not
     *         valid, or with status 500 (Internal Server Error) if the user couldnt be updated
     * @throws URISyntaxException
     *             if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/users", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<ManagedUserDTO> updateUser(@Valid @RequestBody final ManagedUserDTO userDTO) throws URISyntaxException
    {
        this.log.debug("REST request to update User : {}", userDTO);

        if (userDTO.getIdentifier() == null)
        {
            return this.createUser(userDTO);
        }

        final User existingUser = this.userService.getUserById(userDTO.getIdentifier());
        if (existingUser != null)
        {
            existingUser.setUserName(userDTO.getUserName());
            existingUser.setFirstName(userDTO.getFirstName());
            existingUser.setLastName(userDTO.getLastName());

            final Set<Authority> authorities = new HashSet<Authority>();
            userDTO.getAuthorities().stream().forEach(authority -> authorities.add(this.authorityRepository.findOne(authority)));
            existingUser.setAuthorities(authorities);

            try
            {
                final ManagedUserDTO result = UserConverter.toDto(this.userService.save(existingUser));
                this.hateoasLinkBuilder(result);

                return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("userManagement.updated", userDTO.getUserName()))
                        .body(result);
            }
            catch (final PermissionsDeniedException ex)
            {
                this.log.debug("User: " + SecurityUtils.getCurrentUserName() + " has no permissions to update a user");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GET /users : get all the user.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of user in body
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ManagedUserDTO>> getAllUser()
    {
        this.log.debug("REST request to get all User");

        final List<ManagedUserDTO> userDTOs = UserConverter.toDtoList(this.userService.getAll(new Sort(Sort.Direction.ASC, "id")));
        userDTOs.stream().forEach(userDTO -> this.hateoasLinkBuilder(userDTO));
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    /**
     * GET /users/:id : get the "id" user.
     *
     * @param id
     *            the id of the user to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the user, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagedUserDTO> getUser(@PathVariable final Long id)
    {
        this.log.debug("REST request to get User : {}", id);

        final User userById = this.userService.getUserById(id);

        if (userById != null)
        {
            final ManagedUserDTO userDTO = UserConverter.toDto(userById);
            this.hateoasLinkBuilder(userDTO);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * DELETE /users/:id : delete the "id" user.
     *
     * @param id
     *            the id of the user to delete
     * @return the ResponseEntity with status 200 (OK), or with status 403 (Forbidden) if the user is not an admin.
     */
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteUser(@PathVariable final Long id)
    {
        this.log.debug("REST request to delete User : {}", id);

        try
        {
            this.userService.delete(id);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userManagement", id.toString())).build();
        }
        catch (final PermissionsDeniedException ex)
        {
            this.log.debug("User: " + SecurityUtils.getCurrentUserName() + " has no permissions to delete the user");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * GET /users/:id/votes : get all votes for the given user.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of the votes of the user in body
     */
    @RequestMapping(value = "/users/{id}/votes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserVotingsDTO>> getAllVotesForUser(@PathVariable final Long id)
    {
        this.log.debug("REST request to get all User");

        final List<UserVotingsDTO> dtoList = UserVotingsConverter.toDtoList(this.userVotingsService.findByUserId(id));
        dtoList.stream()
        .forEach(userVotingDTO -> userVotingDTO.add(UserVotingsResource.createLinktToSelf(userVotingDTO.getIdentifier())));
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    /**
     * Create link to self
     *
     * @param id
     *            the id of the user
     * @return the link to the method
     */
    public static Link createLinkToSelf(final Long id)
    {
        return linkTo(methodOn(UserResource.class).getUser(id)).withSelfRel();
    }

    /**
     * Create link to user votes
     *
     * @param id
     *            the id of the user
     * @return the link to the method
     */
    public static Link createLinkToUserVotes(final Long id)
    {
        return linkTo(methodOn(UserResource.class).getAllVotesForUser(id)).withRel("votes");
    }

    private void hateoasLinkBuilder(final ManagedUserDTO userDTO)
    {
        userDTO.add(UserResource.createLinkToSelf(userDTO.getIdentifier()));
        userDTO.add(UserResource.createLinkToUserVotes(userDTO.getIdentifier()));
    }
}
