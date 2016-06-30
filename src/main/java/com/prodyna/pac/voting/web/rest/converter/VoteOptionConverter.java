package com.prodyna.pac.voting.web.rest.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.prodyna.pac.voting.domain.VoteOption;
import com.prodyna.pac.voting.web.rest.dto.VoteOptionDTO;

public class VoteOptionConverter
{
    public static VoteOption toEntity(final VoteOptionDTO voteOptionDTO)
    {
        final VoteOption voteOption = new VoteOption();
        voteOption.setId(voteOptionDTO.getId());
        voteOption.setText(voteOptionDTO.getText());

        return voteOption;
    }

    public static Set<VoteOption> toEntitySet(final List<VoteOptionDTO> voteOptionDTOs)
    {
        final Set<VoteOption> voteOptions = new HashSet<VoteOption>(voteOptionDTOs.size());
        voteOptionDTOs.stream().forEach(voteOptionDTO -> voteOptions.add(VoteOptionConverter.toEntity(voteOptionDTO)));
        return voteOptions;
    }

    public static VoteOptionDTO toDto(final VoteOption voteOption)
    {
        return new VoteOptionDTO(voteOption.getId(), voteOption.getText(), 0.0f);
    }

    public static List<VoteOptionDTO> toDtoList(final Collection<VoteOption> voteOptions)
    {
        final List<VoteOptionDTO> voteOptionDTOs = new ArrayList<VoteOptionDTO>(voteOptions.size());
        voteOptions.stream().forEach(voteOption -> voteOptionDTOs.add(VoteOptionConverter.toDto(voteOption)));
        return voteOptionDTOs;
    }
}
