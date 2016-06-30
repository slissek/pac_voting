/**
 *
 */
package com.prodyna.pac.voting.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entity class for user votings.
 *
 * @author <a href="mailto:sven.lissek@prodyna.com">Sven Lissek</a>, <a href="http://www.prodyna.com">PRODYNA AG</a>
 */
@Entity
@Table(name = "user_votings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
// @Document(indexName = "uservotings")
public class UserVotings implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "vote_id", nullable = false)
    private Long voteId;

    @NotNull
    @Column(name = "vote_options_id", nullable = false)
    private Long voteOptionsId;

    protected UserVotings()
    {
    }

    /**
     * UserVotes constructor
     *
     * @param userId
     *            the id of the {@link User} voted
     * @param voteId
     *            the id of the {@link Vote}
     * @param voteOptionsId
     *            the id of the {@link VoteOption}
     */
    public UserVotings(final long userId, final long voteId, final long voteOptionsId)
    {
        super();
        this.userId = userId;
        this.voteId = voteId;
        this.voteOptionsId = voteOptionsId;
    }

    @Override
    public String toString()
    {
        return String.format("UserVotings [id=%s, userId=%s, voteId=%s, voteOptionsId=%s]", this.id, this.userId, this.voteId,
                this.voteOptionsId);
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }
        final UserVotings userVotings = (UserVotings) o;
        if (userVotings.id == null || this.id == null)
        {
            return false;
        }
        return Objects.equals(this.id, userVotings.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.id);
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
     * @return the userId
     */
    public Long getUserId()
    {
        return this.userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(final Long userId)
    {
        this.userId = userId;
    }

    /**
     * @return the voteId
     */
    public Long getVoteId()
    {
        return this.voteId;
    }

    /**
     * @param voteId
     *            the voteId to set
     */
    public void setVoteId(final Long voteId)
    {
        this.voteId = voteId;
    }

    /**
     * @return the voteOptionsId
     */
    public Long getVoteOptionsId()
    {
        return this.voteOptionsId;
    }

    /**
     * @param voteOptionsId
     *            the voteOptionsId to set
     */
    public void setVoteOptionsId(final Long voteOptionsId)
    {
        this.voteOptionsId = voteOptionsId;
    }

}
