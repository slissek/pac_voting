package com.prodyna.pac.voting.web.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class VoteOptionDTO
{
    private final Long id;

    @NotNull
    @Size(max = 100)
    private final String text;

    private float percent;

    private boolean userChoice = false;

    public VoteOptionDTO(final Long id, final String text, final float percent)
    {
        this.id = id;
        this.text = text;
        this.percent = percent;
    }

    /**
     * @return the id
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * @return the text
     */
    public String getText()
    {
        return this.text;
    }

    /**
     * @return the percent
     */
    public float getPercent()
    {
        return this.percent;
    }

    /**
     * @param percent the percent to set
     */
    public void setPercent(final float percent)
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
     * @param userChoice the userChoice to set
     */
    public void setUserChoice(final boolean userChoice)
    {
        this.userChoice = userChoice;
    }

}
