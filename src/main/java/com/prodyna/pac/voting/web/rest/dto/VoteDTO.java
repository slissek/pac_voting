package com.prodyna.pac.voting.web.rest.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.prodyna.pac.voting.domain.VoteOption;
import com.prodyna.pac.voting.web.rest.converter.VoteOptionConverter;

public class VoteDTO
{
    private final Long id;

    @NotNull
    private final Long userId;

    @NotNull
    @Size(min = 1, max = 200)
    private final String topic;

    private List<VoteOptionDTO> options;

    public VoteDTO(final Long id, final Long userId, final String topic, final Set<VoteOption> voteOptions){
        this.id = id;
        this.userId = userId;
        this.topic = topic;
        final Set<VoteOptionDTO> options = new HashSet<VoteOptionDTO>(voteOptions.size());
        for (final VoteOption voteOption : voteOptions)
        {
            options.add(VoteOptionConverter.toDto(voteOption));
        }
    }

    /**
     * @return the id
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * @return the userId
     */
    public Long getUserId()
    {
        return this.userId;
    }

    /**
     * @return the topic
     */
    public String getTopic()
    {
        return this.topic;
    }

    /**
     * @return the options
     */
    public List<VoteOptionDTO> getOptions()
    {
        return this.options;
    }
}
