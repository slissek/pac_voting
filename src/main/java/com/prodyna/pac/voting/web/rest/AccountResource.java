package com.prodyna.pac.voting.web.rest;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.prodyna.pac.voting.domain.PersistentToken;
import com.prodyna.pac.voting.domain.User;
import com.prodyna.pac.voting.repository.PersistentTokenRepository;
import com.prodyna.pac.voting.security.SecurityUtils;
import com.prodyna.pac.voting.service.UserService;
import com.prodyna.pac.voting.web.rest.dto.ManagedUserDTO;
import com.prodyna.pac.voting.web.rest.util.HeaderUtil;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource
{

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Inject
    private UserService userService;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    /**
     * GET /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request
     *            the HTTP request
     * @return the login if the user is authenticated
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String isAuthenticated(final HttpServletRequest request)
    {
        this.log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET /account : get the current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the current user in body, or status 500 (Internal Server Error) if the user
     *         couldn't be returned
     */
    @RequestMapping(value = "/account", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagedUserDTO> getAccount()
    {
        return Optional.ofNullable(this.userService.getUserWithAuthorities())
                .map(user -> new ResponseEntity<>(new ManagedUserDTO(user), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST /account : update the current user information.
     *
     * @param userDTO
     *            the current user information
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) or 500 (Internal Server Error) if the user couldn't be
     *         updated
     */
    @RequestMapping(value = "/account", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> saveAccount(@Valid @RequestBody final ManagedUserDTO userDTO)
    {
        final User existingUser = this.userService.getUserByUserName(userDTO.getUserName());
        if (existingUser != null)
        {
            this.userService.updateUserInformation(userDTO.getFirstName(), userDTO.getLastName());
            return new ResponseEntity<String>(HttpStatus.OK);
        }
        else
        {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("user-management", "usernameexists", "Username already in use")).body(null);
        }
    }

    /**
     * GET /account/sessions : get the current open sessions.
     *
     * @return the ResponseEntity with status 200 (OK) and the current open sessions in body, or status 500 (Internal Server Error) if the
     *         current open sessions couldn't be retrieved
     */
    @RequestMapping(value = "/account/sessions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PersistentToken>> getCurrentSessions()
    {
        final User user = this.userService.getUserByUserName (SecurityUtils.getCurrentUserName());
        if (user != null)
        {
            return new ResponseEntity<>(this.persistentTokenRepository.findByUser(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
