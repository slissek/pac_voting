package com.prodyna.pac.voting.web.rest;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
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
import com.prodyna.pac.voting.service.UserService;
import com.prodyna.pac.voting.service.UserVotingsService;
import com.prodyna.pac.voting.service.VoteService;
import com.prodyna.pac.voting.web.rest.converter.VoteConverter;
import com.prodyna.pac.voting.web.rest.dto.VoteDTO;

/**
 * Test class for the VoteResource REST controller.
 *
 * @see VoteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VotingApplication.class)
@WebAppConfiguration
@IntegrationTest
public class VoteResourceIntTest
{
    private static final Long DEFAULT_CREATOR_ID = 1L;
    private static final Long UPDATED_CREATOR_ID = 2L;
    private static final boolean DEFAULT_USER_VOTED = false;
    private static final boolean UPDATED_USER_VOTED = true;
    private static final LocalDate DEFAULT_CREATED = LocalDate.now();
    private static final LocalDate UPDATED_CREATED = LocalDate.now();
    private static final String DEFAULT_TOPIC = "AAAAA";
    private static final String UPDATED_TOPIC = "BBBBB";

    @Inject
    private VoteService voteService;

    @Inject
    private UserService userService;

    @Inject
    private UserVotingsService userVotingsService;

    private MockMvc restVoteMockMvc;

    private VoteDTO voteDTO;
    private Vote vote;

    @PostConstruct
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        final VoteResource voteResource = new VoteResource();

        ReflectionTestUtils.setField(voteResource, "voteService", this.voteService);
        ReflectionTestUtils.setField(voteResource, "userService", this.userService);
        ReflectionTestUtils.setField(voteResource, "userVotingsService", this.userVotingsService);

        this.restVoteMockMvc = MockMvcBuilders.standaloneSetup(voteResource).build();
    }

    @Before
    public void initTest()
    {

        final VoteDTO temp = new VoteDTO();
        temp.setTopic(DEFAULT_TOPIC);
        temp.setUserId(DEFAULT_CREATOR_ID);
        temp.setUserVoted(DEFAULT_USER_VOTED);
        this.voteDTO = temp;

        this.vote = VoteConverter.toEntity(temp, this.userService);
    }

    @Test
    @Transactional
    public void createVote() throws Exception
    {
        final int databaseSizeBeforeCreate = this.voteService.getAll().size();

        // Create the Vote

        this.restVoteMockMvc.perform(MockMvcRequestBuilders.post("/api/votes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.voteDTO)))
        .andExpect(MockMvcResultMatchers.status().isCreated());

        // Validate the Vote in the database
        final List<Vote> votes = this.voteService.getAll();
        Assertions.assertThat(votes).hasSize(databaseSizeBeforeCreate + 1);
        final Vote testVote = votes.get(votes.size() - 1);
        Assertions.assertThat(testVote.getCreated()).isEqualTo(VoteResourceIntTest.DEFAULT_CREATED);
        Assertions.assertThat(testVote.getTopic()).isEqualTo(VoteResourceIntTest.DEFAULT_TOPIC);

    }

    @Test
    @Transactional
    public void checkTopicIsRequired() throws Exception
    {
        final int databaseSizeBeforeTest = this.voteService.getAll().size();
        // set the field null
        this.voteDTO.setTopic(null);

        // Create the Vote, which fails.

        this.restVoteMockMvc.perform(MockMvcRequestBuilders.post("/api/votes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.voteDTO)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

        final List<Vote> votes = this.voteService.getAll();
        Assertions.assertThat(votes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVotes() throws Exception
    {
        // Initialize the database
        this.voteService.save(this.vote);

        // Get all the votes
        this.restVoteMockMvc.perform(MockMvcRequestBuilders.get("/api/votes"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").value(Matchers.hasItem(this.vote.getId().intValue())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].topic")
                .value(Matchers.hasItem(VoteResourceIntTest.DEFAULT_TOPIC.toString())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].userVoted")
                .value(Matchers.hasItem(VoteResourceIntTest.DEFAULT_USER_VOTED)));
    }

    @Test
    @Transactional
    public void getVote() throws Exception
    {
        // Initialize the database
        this.voteService.save(this.vote);

        // Get the vote
        this.restVoteMockMvc.perform(MockMvcRequestBuilders.get("/api/votes/{id}", this.vote.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(this.vote.getId().intValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.topic").value(VoteResourceIntTest.DEFAULT_TOPIC.toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.userVoted")
                .value(VoteResourceIntTest.DEFAULT_USER_VOTED));
    }

    @Test
    @Transactional
    public void getNonExistingVote() throws Exception
    {
        // Get the vote
        this.restVoteMockMvc.perform(MockMvcRequestBuilders.get("/api/votes/{id}", Long.MAX_VALUE))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVote() throws Exception
    {
        // Initialize the database
        this.voteService.save(this.vote);

        final int databaseSizeBeforeUpdate = this.voteService.getAll().size();

        // Update the vote
        final VoteDTO updatedVote = new VoteDTO();
        updatedVote.setId(this.vote.getId());
        updatedVote.setTopic(UPDATED_TOPIC);
        updatedVote.setUserId(UPDATED_CREATOR_ID);
        updatedVote.setUserVoted(UPDATED_USER_VOTED);

        this.restVoteMockMvc.perform(MockMvcRequestBuilders.put("/api/votes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedVote)))
        .andExpect(MockMvcResultMatchers.status().isOk());

        // Validate the Vote in the database
        final List<Vote> votes = this.voteService.getAll();
        Assertions.assertThat(votes).hasSize(databaseSizeBeforeUpdate);
        final Vote testVote = votes.get(votes.size() - 1);
        Assertions.assertThat(testVote.getCreated()).isEqualTo(VoteResourceIntTest.UPDATED_CREATED);
        Assertions.assertThat(testVote.getTopic()).isEqualTo(VoteResourceIntTest.UPDATED_TOPIC);

    }

    @Test
    @Transactional
    public void deleteVote() throws Exception
    {
        // Initialize the database
        this.voteService.save(this.vote);

        final int databaseSizeBeforeDelete = this.voteService.getAll().size();

        // Get the vote
        this.restVoteMockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/{id}", this.vote.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(MockMvcResultMatchers.status().isOk());

        // Validate the database is empty
        final List<Vote> votes = this.voteService.getAll();
        Assertions.assertThat(votes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
