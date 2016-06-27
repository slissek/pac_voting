package com.prodyna.pac.voting.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.prodyna.pac.voting.VotingApplication;
import com.prodyna.pac.voting.domain.UserVotings;
import com.prodyna.pac.voting.repository.UserVotingsRepository;
import com.prodyna.pac.voting.service.UserVotingsService;


/**
 * Test class for the UserVotingsResource REST controller.
 *
 * @see UserVotingsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VotingApplication.class)
@WebAppConfiguration
@IntegrationTest
public class UserVotingsResourceIntTest {


    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_VOTE_ID = 1L;
    private static final Long UPDATED_VOTE_ID = 2L;

    private static final Long DEFAULT_VOTE_OPTIONS_ID = 1L;
    private static final Long UPDATED_VOTE_OPTIONS_ID = 2L;

    @Inject
    private UserVotingsRepository userVotingsRepository;

    @Inject
    private UserVotingsService userVotingsService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserVotingsMockMvc;

    private UserVotings userVotings;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserVotingsResource userVotingsResource = new UserVotingsResource();
        ReflectionTestUtils.setField(userVotingsResource, "userVotingsService", this.userVotingsService);
        this.restUserVotingsMockMvc = MockMvcBuilders.standaloneSetup(userVotingsResource)
                .setCustomArgumentResolvers(this.pageableArgumentResolver)
                .setMessageConverters(this.jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        this.userVotings = new UserVotings(DEFAULT_USER_ID, DEFAULT_VOTE_ID, DEFAULT_VOTE_OPTIONS_ID);
    }

    @Test
    @Transactional
    public void createUserVotings() throws Exception {
        final int databaseSizeBeforeCreate = this.userVotingsRepository.findAll().size();

        // Create the UserVotings

        this.restUserVotingsMockMvc.perform(post("/api/user-votings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.userVotings)))
        .andExpect(status().isCreated());

        // Validate the UserVotings in the database
        final List<UserVotings> userVotings = this.userVotingsRepository.findAll();
        assertThat(userVotings).hasSize(databaseSizeBeforeCreate + 1);
        final UserVotings testUserVotings = userVotings.get(userVotings.size() - 1);
        assertThat(testUserVotings.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserVotings.getVoteId()).isEqualTo(DEFAULT_VOTE_ID);
        assertThat(testUserVotings.getVoteOptionsId()).isEqualTo(DEFAULT_VOTE_OPTIONS_ID);

    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        final int databaseSizeBeforeTest = this.userVotingsRepository.findAll().size();
        // set the field null
        this.userVotings.setUserId(null);

        // Create the UserVotings, which fails.

        this.restUserVotingsMockMvc.perform(post("/api/user-votings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.userVotings)))
        .andExpect(status().isBadRequest());

        final List<UserVotings> userVotings = this.userVotingsRepository.findAll();
        assertThat(userVotings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVoteIdIsRequired() throws Exception {
        final int databaseSizeBeforeTest = this.userVotingsRepository.findAll().size();
        // set the field null
        this.userVotings.setVoteId(null);

        // Create the UserVotings, which fails.

        this.restUserVotingsMockMvc.perform(post("/api/user-votings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.userVotings)))
        .andExpect(status().isBadRequest());

        final List<UserVotings> userVotings = this.userVotingsRepository.findAll();
        assertThat(userVotings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVoteOptionsIdIsRequired() throws Exception {
        final int databaseSizeBeforeTest = this.userVotingsRepository.findAll().size();
        // set the field null
        this.userVotings.setVoteOptionsId(null);

        // Create the UserVotings, which fails.

        this.restUserVotingsMockMvc.perform(post("/api/user-votings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.userVotings)))
        .andExpect(status().isBadRequest());

        final List<UserVotings> userVotings = this.userVotingsRepository.findAll();
        assertThat(userVotings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserVotings() throws Exception {
        // Initialize the database
        this.userVotingsRepository.saveAndFlush(this.userVotings);

        // Get all the userVotings
        this.restUserVotingsMockMvc.perform(get("/api/user-votings?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.[*].id").value(hasItem(this.userVotings.getId().intValue())))
        .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
        .andExpect(jsonPath("$.[*].voteId").value(hasItem(DEFAULT_VOTE_ID.intValue())))
        .andExpect(jsonPath("$.[*].voteOptionsId").value(hasItem(DEFAULT_VOTE_OPTIONS_ID.intValue())));
    }

    @Test
    @Transactional
    public void getUserVotings() throws Exception {
        // Initialize the database
        this.userVotingsRepository.saveAndFlush(this.userVotings);

        // Get the userVotings
        this.restUserVotingsMockMvc.perform(get("/api/user-votings/{id}", this.userVotings.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(this.userVotings.getId().intValue()))
        .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
        .andExpect(jsonPath("$.voteId").value(DEFAULT_VOTE_ID.intValue()))
        .andExpect(jsonPath("$.voteOptionsId").value(DEFAULT_VOTE_OPTIONS_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserVotings() throws Exception {
        // Get the userVotings
        this.restUserVotingsMockMvc.perform(get("/api/user-votings/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserVotings() throws Exception {
        // Initialize the database
        this.userVotingsService.save(this.userVotings);

        final int databaseSizeBeforeUpdate = this.userVotingsRepository.findAll().size();

        // Update the userVotings
        final UserVotings updatedUserVotings = new UserVotings(UPDATED_USER_ID, UPDATED_VOTE_ID, UPDATED_VOTE_OPTIONS_ID);
        updatedUserVotings.setId(this.userVotings.getId());

        this.restUserVotingsMockMvc.perform(put("/api/user-votings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUserVotings)))
        .andExpect(status().isOk());

        // Validate the UserVotings in the database
        final List<UserVotings> userVotings = this.userVotingsRepository.findAll();
        assertThat(userVotings).hasSize(databaseSizeBeforeUpdate);
        final UserVotings testUserVotings = userVotings.get(userVotings.size() - 1);
        assertThat(testUserVotings.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserVotings.getVoteId()).isEqualTo(UPDATED_VOTE_ID);
        assertThat(testUserVotings.getVoteOptionsId()).isEqualTo(UPDATED_VOTE_OPTIONS_ID);

    }

    @Test
    @Transactional
    public void deleteUserVotings() throws Exception {
        // Initialize the database
        this.userVotingsService.save(this.userVotings);

        final int databaseSizeBeforeDelete = this.userVotingsRepository.findAll().size();

        // Get the userVotings
        this.restUserVotingsMockMvc.perform(delete("/api/user-votings/{id}", this.userVotings.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

        // Validate the database is empty
        final List<UserVotings> userVotings = this.userVotingsRepository.findAll();
        assertThat(userVotings).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUserVotings() throws Exception {
        // Initialize the database
        this.userVotingsService.save(this.userVotings);

        // Search the userVotings
        this.restUserVotingsMockMvc.perform(get("/api/_search/user-votings?query=id:" + this.userVotings.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.[*].id").value(hasItem(this.userVotings.getId().intValue())))
        .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
        .andExpect(jsonPath("$.[*].voteId").value(hasItem(DEFAULT_VOTE_ID.intValue())))
        .andExpect(jsonPath("$.[*].voteOptionsId").value(hasItem(DEFAULT_VOTE_OPTIONS_ID.intValue())));
    }
}
