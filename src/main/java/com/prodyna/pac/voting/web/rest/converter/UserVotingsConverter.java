package com.prodyna.pac.voting.web.rest.converter;

import java.util.ArrayList;
import java.util.List;

import com.prodyna.pac.voting.domain.UserVotings;
import com.prodyna.pac.voting.web.rest.dto.UserVotingsDTO;

public class UserVotingsConverter
{
    public static UserVotings toEntity(final UserVotingsDTO userVotingDTO)
    {
        final UserVotings userVotings = new UserVotings();
        userVotings.setId(userVotingDTO.getIdentifier());
        userVotings.setUserId(userVotingDTO.getUserId());
        userVotings.setVoteId(userVotingDTO.getVoteId());
        userVotings.setVoteOptionsId(userVotingDTO.getVoteOptionsId());
        return userVotings;
    }

    public static UserVotingsDTO toDto(final UserVotings userVoting)
    {
        final UserVotingsDTO userVotingsDTO = new UserVotingsDTO();
        userVotingsDTO.setIdentifier(userVoting.getId());
        userVotingsDTO.setUserId(userVoting.getUserId());
        userVotingsDTO.setVoteId(userVoting.getVoteId());
        userVotingsDTO.setVoteOptionsId(userVoting.getVoteOptionsId());
        return userVotingsDTO;
    }

    public static List<UserVotingsDTO> toDtoList(final List<UserVotings> userVotings)
    {
        final List<UserVotingsDTO> result = new ArrayList<>(userVotings.size());
        userVotings.stream().forEach(userVoting -> result.add(UserVotingsConverter.toDto(userVoting)));
        return result;
    }
}
