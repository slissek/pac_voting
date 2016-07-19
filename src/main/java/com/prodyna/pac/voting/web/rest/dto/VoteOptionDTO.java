package com.prodyna.pac.voting.web.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.hateoas.ResourceSupport;

public class VoteOptionDTO extends ResourceSupport
{
    private Long identifier;

    private Long voteId;

    @NotNull
    @Size(max = 100)
    private String text;

    private int percent;

    private boolean userChoice = false;

    public VoteOptionDTO()
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
     * @return the percent
     */
    public int getPercent()
    {
        return this.percent;
    }

    /**
     * @param percent
     *            the percent to set
     */
    public void setPercent(final int percent)
    {
        this.percent = percent;
    }

    /**
     * @return the userChoice
     */
    public boolean isUserChoice()
    {
        return this.userChoice;
    }

    /**
     * @param userChoice
     *            the userChoice to set
     */
    public void setUserChoice(final boolean userChoice)
    {
        this.userChoice = userChoice;
    }
}
