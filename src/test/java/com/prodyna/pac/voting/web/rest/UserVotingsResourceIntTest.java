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
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.prodyna.pac.voting.VotingApplication;
import com.prodyna.pac.voting.domain.UserVotings;
import com.prodyna.pac.voting.service.UserVotingsService;
import com.prodyna.pac.voting.web.rest.converter.UserVotingsConverter;
import com.prodyna.pac.voting.web.rest.dto.UserVotingsDTO;

/**
 * Test class for the UserVotingsResource REST controller.
 *
 * @see UserVotingsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VotingApplication.class)
@WebAppConfiguration
@IntegrationTest
public class UserVotingsResourceIntTest
{

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_VOTE_ID = 1L;
    private static final Long UPDATED_VOTE_ID = 2L;

    private static final Long DEFAULT_VOTE_OPTIONS_ID = 1L;
    private static final Long UPDATED_VOTE_OPTIONS_ID = 2L;

    @Inject
    private UserVotingsService userVotingsService;

    private MockMvc restUserVotingsMockMvc;

    private UserVotingsDTO userVotingsDTO;
    private UserVotings userVotings;
    private Sort sorting;

    @PostConstruct
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        final UserVotingsResource userVotingsResource = new UserVotingsResource();

        ReflectionTestUtils.setField(userVotingsResource, "userVotingsService", this.userVotingsService);

        this.restUserVotingsMockMvc = MockMvcBuilders.standaloneSetup(userVotingsResource).build();
        this.sorting = new Sort(Sort.Direction.ASC, "id");
    }

    @Before
    public void initTest()
    {
        this.userVotingsDTO = new UserVotingsDTO();
        this.userVotingsDTO.setUserId(DEFAULT_USER_ID);
        this.userVotingsDTO.setVoteId(DEFAULT_VOTE_ID);
        this.userVotingsDTO.setVoteOptionsId(DEFAULT_VOTE_OPTIONS_ID);

        this.userVotings = UserVotingsConverter.toEntity(this.userVotingsDTO);
    }

    @Test
    @Transactional
    public void createUserVotings() throws Exception
    {
        final int databaseSizeBeforeCreate = this.userVotingsService.findAll(this.sorting).size();

        // Create the UserVotings

        this.restUserVotingsMockMvc.perform(post("/api/user-votings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.userVotingsDTO)))
        .andExpect(status().isCreated());

        // Validate the UserVotings in the database
        final List<UserVotings> userVotings = this.userVotingsService.findAll(this.sorting);
        assertThat(userVotings).hasSize(databaseSizeBeforeCreate + 1);
        final UserVotings testUserVotings = userVotings.get(userVotings.size() - 1);
        assertThat(testUserVotings.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserVotings.getVoteId()).isEqualTo(DEFAULT_VOTE_ID);
        assertThat(testUserVotings.getVoteOptionsId()).isEqualTo(DEFAULT_VOTE_OPTIONS_ID);

    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception
    {
        final int databaseSizeBeforeTest = this.userVotingsService.findAll(this.sorting).size();
        // set the field null
        this.userVotingsDTO.setUserId(null);

        // Create the UserVotings, which fails.

        this.restUserVotingsMockMvc.perform(post("/api/user-votings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.userVotingsDTO)))
        .andExpect(status().isBadRequest());

        final List<UserVotings> userVotings = this.userVotingsService.findAll(this.sorting);
        assertThat(userVotings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVoteIdIsRequired() throws Exception
    {
        final int databaseSizeBeforeTest = this.userVotingsService.findAll(this.sorting).size();
        // set the field null
        this.userVotingsDTO.setVoteId(null);

        // Create the UserVotings, which fails.

        this.restUserVotingsMockMvc.perform(post("/api/user-votings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.userVotingsDTO)))
        .andExpect(status().isBadRequest());

        final List<UserVotings> userVotings = this.userVotingsService.findAll(this.sorting);
        assertThat(userVotings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVoteOptionsIdIsRequired() throws Exception
    {
        final int databaseSizeBeforeTest = this.userVotingsService.findAll(this.sorting).size();
        // set the field null
        this.userVotingsDTO.setVoteOptionsId(null);

        // Create the UserVotings, which fails.

        this.restUserVotingsMockMvc.perform(post("/api/user-votings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.userVotingsDTO)))
        .andExpect(status().isBadRequest());

        final List<UserVotings> userVotings = this.userVotingsService.findAll(this.sorting);
        assertThat(userVotings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserVotings() throws Exception
    {
        // Initialize the database
        this.userVotingsService.save(this.userVotings);

        // Get all the userVotings
        this.restUserVotingsMockMvc.perform(get("/api/user-votings"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.[*].identifier").value(hasItem(this.userVotings.getId().intValue())))
        .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
        .andExpect(jsonPath("$.[*].voteId").value(hasItem(DEFAULT_VOTE_ID.intValue())))
        .andExpect(jsonPath("$.[*].voteOptionsId").value(hasItem(DEFAULT_VOTE_OPTIONS_ID.intValue())));
    }

    @Test
    @Transactional
    public void getUserVotings() throws Exception
    {
        // Initialize the database
        this.userVotingsService.save(this.userVotings);

        // Get the userVotings
        this.restUserVotingsMockMvc.perform(get("/api/user-votings/{id}", this.userVotings.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.identifier").value(this.userVotings.getId().intValue()))
        .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
        .andExpect(jsonPath("$.voteId").value(DEFAULT_VOTE_ID.intValue()))
        .andExpect(jsonPath("$.voteOptionsId").value(DEFAULT_VOTE_OPTIONS_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserVotings() throws Exception
    {
        // Get the userVotings
        this.restUserVotingsMockMvc.perform(get("/api/user-votings/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserVotings() throws Exception
    {
        // Initialize the database
        this.userVotingsService.save(this.userVotings);

        final int databaseSizeBeforeUpdate = this.userVotingsService.findAll(this.sorting).size();

        // Update the userVotings
        final UserVotingsDTO updatedUserVotings = new UserVotingsDTO();
        updatedUserVotings.setUserId(UPDATED_USER_ID);
        updatedUserVotings.setVoteId(UPDATED_VOTE_ID);
        updatedUserVotings.setVoteOptionsId(UPDATED_VOTE_OPTIONS_ID);
        updatedUserVotings.setIdentifier(this.userVotings.getId());

        this.restUserVotingsMockMvc.perform(put("/api/user-votings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUserVotings)))
        .andExpect(status().isOk());

        // Validate the UserVotings in the database
        final List<UserVotings> userVotings = this.userVotingsService.findAll(this.sorting);
        assertThat(userVotings).hasSize(databaseSizeBeforeUpdate);
        final UserVotings testUserVotings = userVotings.get(userVotings.size() - 1);
        assertThat(testUserVotings.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserVotings.getVoteId()).isEqualTo(UPDATED_VOTE_ID);
        assertThat(testUserVotings.getVoteOptionsId()).isEqualTo(UPDATED_VOTE_OPTIONS_ID);

    }

    @Test
    @Transactional
    public void deleteUserVotings() throws Exception
    {
        // Initialize the database
        this.userVotingsService.save(this.userVotings);

        final int databaseSizeBeforeDelete = this.userVotingsService.findAll(this.sorting).size();

        // Get the userVotings
        this.restUserVotingsMockMvc.perform(delete("/api/user-votings/{id}", this.userVotings.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

        // Validate the database is empty
        final List<UserVotings> userVotings = this.userVotingsService.findAll(this.sorting);
        assertThat(userVotings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
