/**
 *
 */
package com.prodyna.pac.voting.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entity class for votes
 *
 * @author <a href="mailto:sven.lissek@prodyna.com">Sven Lissek</a>, <a href="http://www.prodyna.com">PRODYNA AG</a>
 */
@Entity
@Table(name = "vote")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
// @Document(indexName = "vote")
public class Vote implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User creator;

    @NotNull
    @Column(name = "created", nullable = false)
    private LocalDate created;

    @NotNull
    @Column(name = "topic", nullable = false)
    private String topic;

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<VoteOption> voteOptions = new HashSet<VoteOption>();

    public Vote()
    {
    }

    /**
     * Constructor
     *
     * @param creator
     *            the userId of the creator
     * @param created
     *            the {@link LocalDate} the vote was created at
     * @param voteTopic
     *            the {@link String} value contains the vote topic
     */
    public Vote(final User creator, final LocalDate created, final String voteTopic)
    {
        super();
        this.creator = creator;
        this.created = created;
        this.topic = voteTopic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return String.format("Vote [id=%s, creator=%s, created=%s, topic=%s, voteOptions=%s]", this.id, this.creator, this.created, this.topic, this.voteOptions);
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
        final Vote vote = (Vote) o;
        if ((vote.id == null) || (this.id == null))
        {
            return false;
        }
        return Objects.equals(this.id, vote.id);
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
     * @return the creator
     */
    public User getCreator()
    {
        return this.creator;
    }

    /**
     * @param creator
     *            the creator to set
     */
    public void setCreator(final User creator)
    {
        this.creator = creator;
    }

    /**
     * @return the created
     */
    public LocalDate getCreated()
    {
        return this.created;
    }

    /**
     * @param created
     *            the created to set
     */
    public void setCreated(final LocalDate created)
    {
        this.created = created;
    }

    /**
     * @return the topic
     */
    public String getTopic()
    {
        return this.topic;
    }

    /**
     * @param topic the topic to set
     */
    public void setTopic(final String topic)
    {
        this.topic = topic;
    }

    /**
     * @return the voteOptions
     */
    public Set<VoteOption> getVoteOptions()
    {
        return this.voteOptions;
    }

    /**
     * @param voteOptions the voteOptions to set
     */
    public void setVoteOptions(final Set<VoteOption> voteOptions)
    {
        this.voteOptions = voteOptions;
    }


}
