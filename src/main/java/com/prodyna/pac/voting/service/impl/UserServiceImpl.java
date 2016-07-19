package com.prodyna.pac.voting.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prodyna.pac.voting.domain.User;
import com.prodyna.pac.voting.exceptions.PermissionsDeniedException;
import com.prodyna.pac.voting.repository.UserRepository;
import com.prodyna.pac.voting.security.AuthoritiesConstants;
import com.prodyna.pac.voting.security.SecurityUtils;
import com.prodyna.pac.voting.service.UserService;

/**
 * Service Implementation for managing User.
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService
{
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Inject
    private UserRepository userRepository;

    @Override
    public User save(final User user) throws PermissionsDeniedException
    {
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN))
        {
            final String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
            user.setPassword(encryptedPassword);

            final User result = this.userRepository.save(user);

            this.log.debug("Saved User: {}", result);

            return result;
        }
        else
        {
            throw new PermissionsDeniedException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll(final Sort sort)
    {
        this.log.debug("Request to get all User");
        return this.userRepository.findAll(sort);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(final Long id)
    {
        this.log.debug("Request to get User : {}", id);
        return this.userRepository.findOneById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUserName(final String username)
    {
        this.log.debug("Request to get User by username: {}", username);
        return this.userRepository.findOneByUserName(username);
    }

    @Override
    public void delete(final Long id) throws PermissionsDeniedException
    {
        this.log.debug("Request to delete User : {}", id);
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN))
        {
            final User user = this.userRepository.findOne(id);
            if (user != null)
            {
                this.userRepository.delete(id);
                this.log.debug("User deleted: {}", user);
            }
        }
        else
        {
            throw new PermissionsDeniedException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserWithAuthorities(final Long id)
    {
        final User user = this.userRepository.findOne(id);
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserWithAuthorities()
    {
        final User user = this.userRepository.findOneByUserName(SecurityUtils.getCurrentUserName());
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserWithAuthoritiesByUserName(final String userName)
    {
        return this.userRepository.findOneByUserName(userName);
    }

    @Override
    public User updateUserInformation(final String firstName, final String lastName)
    {
        final User user = this.userRepository.findOneByUserName(SecurityUtils.getCurrentUserName());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return this.userRepository.save(user);
    }
}