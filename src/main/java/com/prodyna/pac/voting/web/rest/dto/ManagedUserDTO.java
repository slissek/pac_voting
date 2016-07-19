package com.prodyna.pac.voting.web.rest.dto;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.hateoas.ResourceSupport;

/**
 * A DTO extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserDTO extends ResourceSupport
{
    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 100;

    private Long identifier;

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

    /**
     * @return the identifier
     */
    public Long getIdentifier()
    {
        return this.identifier;
    }

    /**
     * @param identifier
     *            the id to set
     */
    public void setIdentifier(final Long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * @return the userName
     */
    public String getUserName()
    {
        return this.userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(final String userName)
    {
        this.userName = userName;
    }

    /**
     * @return the firstName
     */
    public String getFirstName()
    {
        return this.firstName;
    }

    /**
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(final String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName()
    {
        return this.lastName;
    }

    /**
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(final String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * @return the password
     */
    public String getPassword()
    {
        return this.password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(final String password)
    {
        this.password = password;
    }

    /**
     * @return the authorities
     */
    public Set<String> getAuthorities()
    {
        return this.authorities;
    }

    /**
     * @param authorities
     *            the authorities to set
     */
    public void setAuthorities(final Set<String> authorities)
    {
        this.authorities = authorities;
    }
}
