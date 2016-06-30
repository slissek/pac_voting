package com.prodyna.pac.voting.web.rest.converter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.prodyna.pac.voting.domain.Vote;
import com.prodyna.pac.voting.domain.VoteOption;
import com.prodyna.pac.voting.web.rest.dto.VoteDTO;

public class VoteConverter
{
    public static Vote toEntity(final VoteDTO voteDTO)
    {
        final Vote vote = new Vote();
        vote.setId(voteDTO.getId());
        vote.setTopic(voteDTO.getTopic());
        vote.setCreated(LocalDate.now());

        if (voteDTO.getOptions() != null)
        {
            final Set<VoteOption> voteOption = new HashSet<VoteOption>(voteDTO.getOptions().size());
            voteDTO.getOptions().stream().forEach(voteOptionDTO -> {
                voteOption.add(new VoteOption(vote, voteOptionDTO.getText()));
            });

        }
        return vote;
    }

    public static VoteDTO toDto(final Vote vote)
    {
        return new VoteDTO(vote.getId(), vote.getCreator().getId(), vote.getTopic(), vote.getVoteOptions());
    }
}
