package com.prodyna.pac.voting.web.rest.converter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.prodyna.pac.voting.domain.UserVotings;
import com.prodyna.pac.voting.domain.Vote;
import com.prodyna.pac.voting.domain.VoteOption;
import com.prodyna.pac.voting.security.AuthoritiesConstants;
import com.prodyna.pac.voting.security.SecurityUtils;
import com.prodyna.pac.voting.service.UserService;
import com.prodyna.pac.voting.service.UserVotingsService;
import com.prodyna.pac.voting.web.rest.dto.VoteDTO;
import com.prodyna.pac.voting.web.rest.dto.VoteOptionDTO;

public class VoteConverter
{
    public static Vote toEntity(final VoteDTO voteDTO, final UserService userService)
    {
        final Vote vote = new Vote();
        vote.setId(voteDTO.getId());
        vote.setTopic(voteDTO.getTopic());
        vote.setCreated(LocalDate.now());
        vote.setCreator(userService.getUserById(voteDTO.getUserId()));

        if (voteDTO.getVoteOptions() != null)
        {
            final Set<VoteOption> voteOptions = new HashSet<VoteOption>(voteDTO.getVoteOptions().size());
            voteDTO.getVoteOptions().stream().forEach(voteOptionDTO -> {
                voteOptions.add(new VoteOption(vote, voteOptionDTO.getText()));
            });
            vote.setVoteOptions(voteOptions);
        }
        return vote;
    }

    public static VoteDTO toDto(final Long currentUserId, final Vote vote, final UserVotingsService userVotingsService)
    {
        boolean userVoted = false;
        final Long voteId = vote.getId();
        final List<UserVotings> userVotings = new ArrayList<UserVotings>();

        if ((voteId != null) && (currentUserId != null))
        {
            userVotings.addAll(userVotingsService.findByUserIdAndVoteId(currentUserId, voteId));
            if ((userVotings != null) && (userVotings.size() == 1))
            {
                userVoted = true;
            }
        }

        final boolean canEdit = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) || ((vote.getCreator() != null) && (vote.getCreator().getId() == currentUserId));

        final VoteDTO voteDTO = new VoteDTO();
        voteDTO.setId(voteId);
        voteDTO.setUserId(vote.getCreator() != null ? vote.getCreator().getId() : null);
        voteDTO.setTopic(vote.getTopic());
        voteDTO.setUserVoted(userVoted);
        voteDTO.setCanEdit(canEdit);

        final Set<VoteOption> voteOptions = vote.getVoteOptions();
        final List<VoteOptionDTO> options = new ArrayList<VoteOptionDTO>(voteOptions.size());
        for (final VoteOption voteOption : voteOptions)
        {
            options.add(VoteOptionConverter.toDto(vote, voteOption, userVotingsService, userVotings));
        }
        voteDTO.setVoteOptions(options);
        return voteDTO;
    }
}
