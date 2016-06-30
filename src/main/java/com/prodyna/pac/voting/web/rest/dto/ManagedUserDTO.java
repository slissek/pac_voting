package com.prodyna.pac.voting.web.rest.dto;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserDTO
{
    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 100;

    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String userName;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    private Set<String> authorities;

    public ManagedUserDTO()
    {
    }

    public ManagedUserDTO(final Long userId, final String userName, final String firstName, final String lastName, final String password,
            final Set<String> authorities)
    {
        this.id = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * @return the id
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * @return the userName
     */
    public String getUserName()
    {
        return this.userName;
    }

    /**
     * @return the firstName
     */
    public String getFirstName()
    {
        return this.firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName()
    {
        return this.lastName;
    }

    /**
     * @return the password
     */
    public String getPassword()
    {
        return this.password;
    }

    /**
     * @return the authorities
     */
    public Set<String> getAuthorities()
    {
        return this.authorities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "UserDTO [userName=" + this.userName + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ", authorities="
                + this.authorities + "]";
    }

}
