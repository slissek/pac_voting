package com.prodyna.pac.voting.web.rest.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.prodyna.pac.voting.domain.UserVotings;
import com.prodyna.pac.voting.domain.Vote;
import com.prodyna.pac.voting.domain.VoteOption;
import com.prodyna.pac.voting.service.UserVotingsService;
import com.prodyna.pac.voting.web.rest.dto.VoteOptionDTO;

public class VoteOptionConverter
{
    public static VoteOption toEntity(final VoteOptionDTO voteOptionDTO, final Vote vote)
    {
        final VoteOption voteOption = new VoteOption();
        voteOption.setId(voteOptionDTO.getId());
        voteOption.setText(voteOptionDTO.getText());
        voteOption.setVote(vote);

        return voteOption;
    }

    public static Set<VoteOption> toEntitySet(final List<VoteOptionDTO> voteOptionDTOs, final Vote vote)
    {
        final Set<VoteOption> voteOptions = new HashSet<VoteOption>(voteOptionDTOs.size());
        voteOptionDTOs.stream().forEach(voteOptionDTO -> voteOptions.add(VoteOptionConverter.toEntity(voteOptionDTO, vote)));
        return voteOptions;
    }

    public static VoteOptionDTO toDto(final Vote vote, final VoteOption voteOption, final UserVotingsService userVotingsService,
            final List<UserVotings> userVotings)
    {
        boolean userChoice = false;
        if ((userVotings != null) && (userVotings.size() == 1))
        {
            final Long userVoteOptionsId = userVotings.get(0).getVoteOptionsId();
            userChoice = voteOption.getId().equals(userVoteOptionsId);
        }

        final Long countForVoteId = userVotingsService.getCountForVoteId(vote.getId());
        final Long countForVoteIdAndOption = userVotingsService.getCountForVoteIdAndOption(vote.getId(), voteOption.getId());
        Long percent = 0L;

        if ((countForVoteId != null) && (countForVoteIdAndOption != null))
        {
            if (countForVoteId != 0)
            {
                percent =  (long) (((double) countForVoteIdAndOption / (double) countForVoteId) * 100);
            }
        }

        final VoteOptionDTO voteOptionDTO = new VoteOptionDTO();
        voteOptionDTO.setId(voteOption.getId());
        voteOptionDTO.setText(voteOption.getText());
        voteOptionDTO.setVoteId(vote.getId());
        voteOptionDTO.setPercent(percent);
        voteOptionDTO.setUserChoice(userChoice);

        return voteOptionDTO;
    }

    public static List<VoteOptionDTO> toDtoList(final Vote vote, final Collection<VoteOption> voteOptions,
            final UserVotingsService userVotingsService, final List<UserVotings> userVotings)
    {
        final List<VoteOptionDTO> voteOptionDTOs = new ArrayList<VoteOptionDTO>(voteOptions.size());
        voteOptions.stream()
        .forEach(voteOption -> voteOptionDTOs.add(VoteOptionConverter.toDto(vote, voteOption, userVotingsService, userVotings)));
        return voteOptionDTOs;
    }

}
