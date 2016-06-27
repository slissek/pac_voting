package com.prodyna.pac.voting.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "authority")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Authority implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Size(min = 0, max = 50)
    @Column(length = 50)
    private String name;

    public String getName()
    {
        return this.name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return String.format("Authority [name='%s']", this.name);
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

        final Authority authority = (Authority) o;

        if (this.name != null ? !this.name.equals(authority.name) : authority.name != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return this.name != null ? this.name.hashCode() : 0;
    }

}
