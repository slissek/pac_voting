
package com.prodyna.pac.voting.web.rest.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.hateoas.ResourceSupport;

public class VoteDTO extends ResourceSupport
{
    private Long identifier;

    @NotNull
    private Long userId;

    @NotNull
    @Size(min = 1, max = 200)
    private String topic;

    private boolean userVoted;

    private boolean canEdit;

    private List<VoteOptionDTO> voteOptions;

    public VoteDTO()
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
     * @return the topic
     */
    public String getTopic()
    {
        return this.topic;
    }

    /**
     * @param topic
     *            the topic to set
     */
    public void setTopic(final String topic)
    {
        this.topic = topic;
    }

    /**
     * @return the userVoted
     */
    public boolean isUserVoted()
    {
        return this.userVoted;
    }

    /**
     * @param userVoted
     *            the userVoted to set
     */
    public void setUserVoted(final boolean userVoted)
    {
        this.userVoted = userVoted;
    }

    /**
     * @return the canEdit
     */
    public boolean isCanEdit()
    {
        return this.canEdit;
    }

    /**
     * @param canEdit
     *            the canEdit to set
     */
    public void setCanEdit(final boolean canEdit)
    {
        this.canEdit = canEdit;
    }

    /**
     * @return the voteOptions
     */
    public List<VoteOptionDTO> getVoteOptions()
    {
        return this.voteOptions;
    }

    /**
     * @param voteOptions
     *            the voteOptions to set
     */
    public void setVoteOptions(final List<VoteOptionDTO> voteOptions)
    {
        this.voteOptions = voteOptions;
    }
}
