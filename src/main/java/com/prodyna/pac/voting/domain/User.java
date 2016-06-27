/**
 *
 */
package com.prodyna.pac.voting.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity class for user
 *
 * @author <a href="mailto:sven.lissek@prodyna.com">Sven Lissek</a>, <a href="http://www.prodyna.com">PRODYNA AG</a>
 */
@Entity
@Table(name = "users")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "username", length = 50, unique = true, nullable = false)
    private String userName;

    @Size(max = 50)
    @Column(name = "firstname", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "lastname", length = 50)
    private String lastName;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60)
    private String password;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_authority", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
                    @JoinColumn(name = "authority_name", referencedColumnName = "name")
    })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Authority> authorities = new HashSet<>();

    @Override
    public String toString()
    {
        return String.format("User [id=%d, userName='%s', firstName='%s', lastName='%s']", this.id, this.userName, this.firstName,
                this.lastName);
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if ((o == null) || (this.getClass() != o.getClass()))
        {
            return false;
        }

        final User user = (User) o;
        if (!this.userName.equals(user.userName))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return this.userName.hashCode();
    }

    /**
     * @return the id
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(final Long id)
    {
        this.id = id;
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
     * @return
     */
    public Set<Authority> getAuthorities()
    {
        return this.authorities;
    }

    /**
     * @param authorities
     */
    public void setAuthorities(final Set<Authority> authorities)
    {
        this.authorities = authorities;
    }
}
