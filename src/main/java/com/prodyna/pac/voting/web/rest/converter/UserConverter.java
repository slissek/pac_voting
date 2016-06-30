package com.prodyna.pac.voting.web.rest.converter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.prodyna.pac.voting.domain.Authority;
import com.prodyna.pac.voting.domain.User;
import com.prodyna.pac.voting.repository.AuthorityRepository;
import com.prodyna.pac.voting.web.rest.dto.ManagedUserDTO;

public class UserConverter
{
    public static User toEntity(final ManagedUserDTO userDTO, final AuthorityRepository authorityRepository)
    {
        final User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        user.setUserName(userDTO.getUserName());

        if (userDTO.getAuthorities() != null)
        {
            final Set<Authority> authorities = new HashSet<Authority>(userDTO.getAuthorities().size());
            userDTO.getAuthorities().stream().forEach(authority -> authorities.add(authorityRepository.findOne(authority)));
            user.setAuthorities(authorities);
        }

        return user;
    }

    public static ManagedUserDTO toDto(final User user)
    {
        return new ManagedUserDTO(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getPassword(),
                user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
    }
}
