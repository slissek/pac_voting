package com.prodyna.pac.voting.web.rest.dto;

import javax.validation.constraints.NotNull;

public class UserVotingsDTO
{
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long voteId;

    @NotNull
    private Long voteOptionsId;

    public UserVotingsDTO()
    {
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
