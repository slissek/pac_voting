package com.prodyna.pac.voting.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
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
import com.prodyna.pac.voting.service.UserService;
import com.prodyna.pac.voting.web.rest.converter.UserConverter;
import com.prodyna.pac.voting.web.rest.dto.ManagedUserDTO;
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

        if (userDTO.getId() != null)
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
                return ResponseEntity.created(new URI("/api/users/" + newUser.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert("userManagement", newUser.getId().toString())).body(newUser);
            }
            catch (final PermissionsDeniedException ex)
            {
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
    @Transactional
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<ManagedUserDTO> updateUser(@Valid @RequestBody final ManagedUserDTO userDTO) throws URISyntaxException
    {
        this.log.debug("REST request to update User : {}", userDTO);

        final User existingUser = this.userService.getUserByUserName(userDTO.getUserName().toLowerCase());
        if ((existingUser != null) && (!existingUser.getId().equals(userDTO.getId())))
        {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("userManagement", "usernameexists", "Username already in use")).body(null);
        }

        final User userById = this.userService.getUserById(userDTO.getId());
        if (userById != null)
        {
            userById.setUserName(userDTO.getUserName());
            userById.setFirstName(userDTO.getFirstName());
            userById.setLastName(userDTO.getLastName());

            final Set<Authority> authorities = new HashSet<Authority>();

            userDTO.getAuthorities().stream().forEach(authority -> authorities.add(this.authorityRepository.findOne(authority)));
            userById.setAuthorities(authorities);

            return ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.updated", userDTO.getUserName()))
                    .body(UserConverter.toDto(this.userService.getUserById(userDTO.getId())));
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
    @Transactional(readOnly = true)
    public ResponseEntity<List<ManagedUserDTO>> getAllUser() throws URISyntaxException
    {
        this.log.debug("REST request to get all User");

        final List<ManagedUserDTO> userDTOs = UserConverter.toDtoList(this.userService.getAll());
        return new ResponseEntity<List<ManagedUserDTO>>(userDTOs, HttpStatus.OK);
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
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
