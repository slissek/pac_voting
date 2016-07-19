package com.prodyna.pac.voting.web.rest.converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.prodyna.pac.voting.domain.Authority;
import com.prodyna.pac.voting.domain.User;
import com.prodyna.pac.voting.repository.AuthorityRepository;
import com.prodyna.pac.voting.web.rest.dto.ManagedUserDTO;

public class UserConverter
{
    public static User toEntity(final ManagedUserDTO userDTO, final AuthorityRepository authorityRepository)
    {
        final User user = new User();
        user.setId(userDTO.getIdentifier());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        user.setUserName(userDTO.getUserName());

        if (userDTO.getAuthorities() != null)
        {
            final Set<Authority> authorities = new HashSet<>(userDTO.getAuthorities().size());
            userDTO.getAuthorities().stream().forEach(authority -> authorities.add(authorityRepository.findOne(authority)));
            user.setAuthorities(authorities);
        }

        return user;
    }

    public static ManagedUserDTO toDto(final User user)
    {
        final Set<String> authorities = new HashSet<>(user.getAuthorities().size());
        user.getAuthorities().stream().forEach(authority -> authorities.add(authority.getName()));

        final ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setIdentifier(user.getId());
        managedUserDTO.setUserName(user.getUserName());
        managedUserDTO.setFirstName(user.getFirstName());
        managedUserDTO.setLastName(user.getLastName());
        managedUserDTO.setPassword(user.getPassword());
        managedUserDTO.setAuthorities(authorities);

        return managedUserDTO;
    }

    public static List<ManagedUserDTO> toDtoList(final List<User> users)
    {
        final List<ManagedUserDTO> result = new ArrayList<>(users.size());
        users.stream().forEach(user -> result.add(UserConverter.toDto(user)));
        return result;
    }
}
