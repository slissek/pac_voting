package com.prodyna.pac.voting.security;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prodyna.pac.voting.domain.User;
import com.prodyna.pac.voting.repository.UserRepository;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Inject
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String userName)
    {
        this.log.debug("Authenticating {}", userName);

        final String lowercaseLogin = userName.toLowerCase(Locale.ENGLISH);
        final User userFromDatabase = this.userRepository.findOneByUserName(lowercaseLogin);

        if (userFromDatabase != null)
        {
            final List<GrantedAuthority> grantedAuthorities = userFromDatabase.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(lowercaseLogin, userFromDatabase.getPassword(),
                    grantedAuthorities);
        }
        else
        {
            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " + "database");
        }
    }
}
