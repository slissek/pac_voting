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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity class for vote options.
 *
 * @author <a href="mailto:sven.lissek@prodyna.com">Sven Lissek</a>, <a href="http://www.prodyna.com">PRODYNA AG</a>
 */
@Entity
@Table(name = "vote_options")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
// @Document(indexName = "voteoptions")
public class VoteOption implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "text", nullable = false)
    private String text;

    @JsonIgnore
    @ManyToOne
    private Vote vote;

    public VoteOption()
    {
    }

    /**
     * VoteOptions constructor
     *
     * @param voteId
     *            the {@link Vote} id the option relates to
     * @param text
     *            the text value of this option
     */
    public VoteOption(final Vote vote, final String text)
    {
        super();
        this.vote = vote;
        this.text = text;
    }

    @Override
    public String toString()
    {
        return String.format("VoteOptions [id=%s, text=%s, vote=%s]", this.id, this.text, this.vote);
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
        final VoteOption voteOptions = (VoteOption) o;
        if ((voteOptions.id == null) || (this.id == null))
        {
            return false;
        }
        return Objects.equals(this.id, voteOptions.id);
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
     * @return the text
     */
    public String getText()
    {
        return this.text;
    }

    /**
     * @param text
     *            the text to set
     */
    public void setText(final String text)
    {
        this.text = text;
    }

    /**
     * @return the vote
     */
    public Vote getVote()
    {
        return this.vote;
    }

    /**
     *
     * @param vote
     *            the vote to set
     */
    public void setVote(final Vote vote)
    {
        this.vote = vote;
    }
}
