package com.prodyna.pac.voting.web.rest;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.prodyna.pac.voting.VotingApplication;
import com.prodyna.pac.voting.domain.Vote;
import com.prodyna.pac.voting.domain.VoteOption;
import com.prodyna.pac.voting.service.UserService;
import com.prodyna.pac.voting.service.VoteOptionsService;
import com.prodyna.pac.voting.service.VoteService;
import com.prodyna.pac.voting.web.rest.converter.VoteConverter;
import com.prodyna.pac.voting.web.rest.converter.VoteOptionConverter;
import com.prodyna.pac.voting.web.rest.dto.VoteDTO;
import com.prodyna.pac.voting.web.rest.dto.VoteOptionDTO;

/**
 * Test class for the VoteResource REST controller.
 *
 * @see VoteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VotingApplication.class)
@WebAppConfiguration
@IntegrationTest
public class VoteOptionsResourceTest
{
    private static final String DEFAULT_TEXT = "AAAAA";

    private static final Long DEFAULT_CREATOR_ID = 1L;
    private static final boolean DEFAULT_USER_VOTED = false;
    private static final String DEFAULT_TOPIC = "AAAAA";

    @Inject
    private VoteOptionsService voteOptionsService;

    @Inject
    private VoteService voteService;

    @Inject
    private UserService userService;

    private MockMvc restVoteMockMvc;

    private VoteOptionDTO voteOptionDTO;
    private VoteOption voteOption;
    private Vote vote;

    @PostConstruct
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        final VoteOptionsResource voteOptionsResource = new VoteOptionsResource();

        ReflectionTestUtils.setField(voteOptionsResource, "voteOptionsService", this.voteOptionsService);
        this.restVoteMockMvc = MockMvcBuilders.standaloneSetup(voteOptionsResource).build();

        final Authentication authentication = Mockito.mock(Authentication.class);
        final UserDetails principal = Mockito.mock(UserDetails.class);

        Mockito.when(principal.getAuthorities()).thenReturn(TestUtil.getAdminAuthorities());
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getPrincipal()).thenReturn(principal);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Before
    public void initTest()
    {
        final VoteDTO voteDTO = new VoteDTO();
        voteDTO.setTopic(DEFAULT_TOPIC);
        voteDTO.setUserId(DEFAULT_CREATOR_ID);
        voteDTO.setUserVoted(DEFAULT_USER_VOTED);

        this.vote = VoteConverter.toEntity(voteDTO, this.userService);

        this.voteOptionDTO = new VoteOptionDTO();
        this.voteOptionDTO.setText(DEFAULT_TEXT);

        this.voteOption = VoteOptionConverter.toEntity(this.voteOptionDTO, this.vote);
    }

    @After
    public void tearDown()
    {
        SecurityContextHolder.clearContext();
    }

    @Test
    @Transactional
    public void deleteVote() throws Exception
    {
        // Initialize the database
        this.voteService.save(this.vote);
        this.voteOptionsService.save(this.voteOption);

        final int databaseSizeBeforeDelete = this.voteOptionsService.findAll().size();

        // Get the vote
        this.restVoteMockMvc.perform(MockMvcRequestBuilders.delete("/api/voteOptions/{id}", this.voteOption.getId().intValue())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(MockMvcResultMatchers.status().isOk());

        // Validate the database is empty
        final List<VoteOption> votes = this.voteOptionsService.findAll();
        Assertions.assertThat(votes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
