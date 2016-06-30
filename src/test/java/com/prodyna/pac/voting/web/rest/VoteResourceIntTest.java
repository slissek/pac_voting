package com.prodyna.pac.voting.web.rest;

import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
import com.prodyna.pac.voting.repository.VoteRepository;
import com.prodyna.pac.voting.service.VoteService;


/**
 * Test class for the VoteResource REST controller.
 *
 * @see VoteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VotingApplication.class)
@WebAppConfiguration
@IntegrationTest
public class VoteResourceIntTest {

    private static final Long DEFAULT_CREATOR_ID = 1L;
    private static final Long UPDATED_CREATOR_ID = 2L;
    private static final LocalDate DEFAULT_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_TOPIC = "AAAAA";
    private static final String UPDATED_TOPIC = "BBBBB";

    @Inject
    private VoteRepository voteRepository;

    @Inject
    private VoteService voteService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVoteMockMvc;

    private Vote vote;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VoteResource voteResource = new VoteResource();
        ReflectionTestUtils.setField(voteResource, "voteService", this.voteService);
        this.restVoteMockMvc = MockMvcBuilders.standaloneSetup(voteResource)
                .setCustomArgumentResolvers(this.pageableArgumentResolver)
                .setMessageConverters(this.jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        //        this.vote = new Vote(VoteResourceIntTest.DEFAULT_CREATOR_ID, VoteResourceIntTest.DEFAULT_CREATED, VoteResourceIntTest.DEFAULT_TOPIC);
    }

    @Test
    @Transactional
    public void createVote() throws Exception {
        final int databaseSizeBeforeCreate = this.voteRepository.findAll().size();

        // Create the Vote

        this.restVoteMockMvc.perform(MockMvcRequestBuilders.post("/api/votes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.vote)))
        .andExpect(MockMvcResultMatchers.status().isCreated());

        // Validate the Vote in the database
        final List<Vote> votes = this.voteRepository.findAll();
        Assertions.assertThat(votes).hasSize(databaseSizeBeforeCreate + 1);
        final Vote testVote = votes.get(votes.size() - 1);
        Assertions.assertThat(testVote.getCreated()).isEqualTo(VoteResourceIntTest.DEFAULT_CREATED);
        Assertions.assertThat(testVote.getTopic()).isEqualTo(VoteResourceIntTest.DEFAULT_TOPIC);

    }

    @Test
    @Transactional
    public void checkCreatedIsRequired() throws Exception {
        final int databaseSizeBeforeTest = this.voteRepository.findAll().size();
        // set the field null
        this.vote.setCreated(null);

        // Create the Vote, which fails.

        this.restVoteMockMvc.perform(MockMvcRequestBuilders.post("/api/votes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.vote)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

        final List<Vote> votes = this.voteRepository.findAll();
        Assertions.assertThat(votes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTopicIsRequired() throws Exception {
        final int databaseSizeBeforeTest = this.voteRepository.findAll().size();
        // set the field null
        this.vote.setTopic(null);

        // Create the Vote, which fails.

        this.restVoteMockMvc.perform(MockMvcRequestBuilders.post("/api/votes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.vote)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

        final List<Vote> votes = this.voteRepository.findAll();
        Assertions.assertThat(votes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVotes() throws Exception {
        // Initialize the database
        this.voteRepository.saveAndFlush(this.vote);

        // Get all the votes
        this.restVoteMockMvc.perform(MockMvcRequestBuilders.get("/api/votes?sort=id,desc"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").value(Matchers.hasItem(this.vote.getId().intValue())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].created").value(Matchers.hasItem(VoteResourceIntTest.DEFAULT_CREATED.toString())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].topic").value(Matchers.hasItem(VoteResourceIntTest.DEFAULT_TOPIC.toString())));
    }

    @Test
    @Transactional
    public void getVote() throws Exception {
        // Initialize the database
        this.voteRepository.saveAndFlush(this.vote);

        // Get the vote
        this.restVoteMockMvc.perform(MockMvcRequestBuilders.get("/api/votes/{id}", this.vote.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(this.vote.getId().intValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.created").value(VoteResourceIntTest.DEFAULT_CREATED.toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.topic").value(VoteResourceIntTest.DEFAULT_TOPIC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVote() throws Exception {
        // Get the vote
        this.restVoteMockMvc.perform(MockMvcRequestBuilders.get("/api/votes/{id}", Long.MAX_VALUE))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVote() throws Exception {
        // Initialize the database
        //        this.voteService.save(this.vote);

        final int databaseSizeBeforeUpdate = this.voteRepository.findAll().size();

        // Update the vote
        //        final Vote updatedVote = new Vote(VoteResourceIntTest.UPDATED_CREATOR_ID, VoteResourceIntTest.UPDATED_CREATED, VoteResourceIntTest.UPDATED_TOPIC);
        //        updatedVote.setId(this.vote.getId());

        //        this.restVoteMockMvc.perform(MockMvcRequestBuilders.put("/api/votes")
        //                .contentType(TestUtil.APPLICATION_JSON_UTF8)
        //                .content(TestUtil.convertObjectToJsonBytes(updatedVote)))
        //        .andExpect(MockMvcResultMatchers.status().isOk());

        // Validate the Vote in the database
        final List<Vote> votes = this.voteRepository.findAll();
        Assertions.assertThat(votes).hasSize(databaseSizeBeforeUpdate);
        final Vote testVote = votes.get(votes.size() - 1);
        Assertions.assertThat(testVote.getCreated()).isEqualTo(VoteResourceIntTest.UPDATED_CREATED);
        Assertions.assertThat(testVote.getTopic()).isEqualTo(VoteResourceIntTest.UPDATED_TOPIC);

    }

    @Test
    @Transactional
    public void deleteVote() throws Exception {
        // Initialize the database
        //        this.voteService.save(this.vote);

        final int databaseSizeBeforeDelete = this.voteRepository.findAll().size();

        // Get the vote
        this.restVoteMockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/{id}", this.vote.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(MockMvcResultMatchers.status().isOk());

        // Validate the database is empty
        final List<Vote> votes = this.voteRepository.findAll();
        Assertions.assertThat(votes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVote() throws Exception {
        // Initialize the database
        //        this.voteService.save(this.vote);

        // Search the vote
        this.restVoteMockMvc.perform(MockMvcRequestBuilders.get("/api/_search/votes?query=id:" + this.vote.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").value(Matchers.hasItem(this.vote.getId().intValue())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].created").value(Matchers.hasItem(VoteResourceIntTest.DEFAULT_CREATED.toString())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].topic").value(Matchers.hasItem(VoteResourceIntTest.DEFAULT_TOPIC.toString())));
    }
}
