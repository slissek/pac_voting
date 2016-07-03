package com.prodyna.pac.voting.web.rest;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
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
import com.prodyna.pac.voting.domain.VoteOption;
import com.prodyna.pac.voting.repository.VoteOptionsRepository;
import com.prodyna.pac.voting.service.VoteOptionsService;

/**
 * Test class for the VoteOptionsResource REST controller.
 *
 * @see VoteOptionsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VotingApplication.class)
@WebAppConfiguration
@IntegrationTest
public class VoteOptionsResourceIntTest
{

    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";

    @Inject
    private VoteOptionsRepository voteOptionsRepository;

    @Inject
    private VoteOptionsService voteOptionsService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVoteOptionsMockMvc;

    private VoteOption voteOptions;

    @PostConstruct
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        final VoteOptionsResource voteOptionsResource = new VoteOptionsResource();
        ReflectionTestUtils.setField(voteOptionsResource, "voteOptionsService", this.voteOptionsService);
        this.restVoteOptionsMockMvc = MockMvcBuilders.standaloneSetup(voteOptionsResource)
                .setCustomArgumentResolvers(this.pageableArgumentResolver).setMessageConverters(this.jacksonMessageConverter).build();
    }

    @Before
    public void initTest()
    {
        //        this.voteOptions = new VoteOptions(VoteOptionsResourceIntTest.DEFAULT_TEXT);
    }

    @Ignore("TODO")
    @Test
    @Transactional
    public void createVoteOptions() throws Exception
    {
        final int databaseSizeBeforeCreate = this.voteOptionsRepository.findAll().size();

        // Create the VoteOptions

        this.restVoteOptionsMockMvc
        .perform(MockMvcRequestBuilders.post("/api/vote-options").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.voteOptions)))
        .andExpect(MockMvcResultMatchers.status().isCreated());

        // Validate the VoteOptions in the database
        final List<VoteOption> voteOptions = this.voteOptionsRepository.findAll();
        Assertions.assertThat(voteOptions).hasSize(databaseSizeBeforeCreate + 1);
        final VoteOption testVoteOptions = voteOptions.get(voteOptions.size() - 1);
        Assertions.assertThat(testVoteOptions.getText()).isEqualTo(VoteOptionsResourceIntTest.DEFAULT_TEXT);

    }

    @Ignore("TODO")
    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception
    {
        final int databaseSizeBeforeTest = this.voteOptionsRepository.findAll().size();
        // set the field null
        this.voteOptions.setText(null);

        // Create the VoteOptions, which fails.

        this.restVoteOptionsMockMvc
        .perform(MockMvcRequestBuilders.post("/api/vote-options").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.voteOptions)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

        final List<VoteOption> voteOptions = this.voteOptionsRepository.findAll();
        Assertions.assertThat(voteOptions).hasSize(databaseSizeBeforeTest);
    }

    @Ignore("TODO")
    @Test
    @Transactional
    public void getAllVoteOptions() throws Exception
    {
        // Initialize the database
        this.voteOptionsRepository.saveAndFlush(this.voteOptions);

        // Get all the voteOptions
        this.restVoteOptionsMockMvc.perform(MockMvcRequestBuilders.get("/api/vote-options?sort=id,desc"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").value(Matchers.hasItem(this.voteOptions.getId().intValue())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].text")
                .value(Matchers.hasItem(VoteOptionsResourceIntTest.DEFAULT_TEXT.toString())));
    }

    @Ignore("TODO")
    @Test
    @Transactional
    public void getVoteOptions() throws Exception
    {
        // Initialize the database
        this.voteOptionsRepository.saveAndFlush(this.voteOptions);

        // Get the voteOptions
        this.restVoteOptionsMockMvc.perform(MockMvcRequestBuilders.get("/api/vote-options/{id}", this.voteOptions.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(this.voteOptions.getId().intValue()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(VoteOptionsResourceIntTest.DEFAULT_TEXT.toString()));
    }

    @Ignore("TODO")
    @Test
    @Transactional
    public void getNonExistingVoteOptions() throws Exception
    {
        // Get the voteOptions
        this.restVoteOptionsMockMvc.perform(MockMvcRequestBuilders.get("/api/vote-options/{id}", Long.MAX_VALUE))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Ignore("TODO")
    @Test
    @Transactional
    public void updateVoteOptions() throws Exception
    {
        // Initialize the database
        //        this.voteOptionsService.save(this.voteOptions);

        final int databaseSizeBeforeUpdate = this.voteOptionsRepository.findAll().size();

        // Update the voteOptions
        //        final VoteOptions updatedVoteOptions = new VoteOptions(VoteOptionsResourceIntTest.UPDATED_TEXT);
        //        updatedVoteOptions.setId(this.voteOptions.getId());

        //        this.restVoteOptionsMockMvc.perform(MockMvcRequestBuilders.put("/api/vote-options").contentType(TestUtil.APPLICATION_JSON_UTF8)
        //                .content(TestUtil.convertObjectToJsonBytes(updatedVoteOptions))).andExpect(MockMvcResultMatchers.status().isOk());

        // Validate the VoteOptions in the database
        final List<VoteOption> voteOptions = this.voteOptionsRepository.findAll();
        Assertions.assertThat(voteOptions).hasSize(databaseSizeBeforeUpdate);
        final VoteOption testVoteOptions = voteOptions.get(voteOptions.size() - 1);
        Assertions.assertThat(testVoteOptions.getText()).isEqualTo(VoteOptionsResourceIntTest.UPDATED_TEXT);

    }

    @Ignore("TODO")
    @Test
    @Transactional
    public void deleteVoteOptions() throws Exception
    {
        // Initialize the database
        //        this.voteOptionsService.save(this.voteOptions);

        final int databaseSizeBeforeDelete = this.voteOptionsRepository.findAll().size();

        // Get the voteOptions
        this.restVoteOptionsMockMvc.perform(
                MockMvcRequestBuilders.delete("/api/vote-options/{id}", this.voteOptions.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(MockMvcResultMatchers.status().isOk());

        // Validate the database is empty
        final List<VoteOption> voteOptions = this.voteOptionsRepository.findAll();
        Assertions.assertThat(voteOptions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Ignore("TODO")
    @Test
    @Transactional
    public void searchVoteOptions() throws Exception
    {
        // Initialize the database
        //        this.voteOptionsService.save(this.voteOptions);

        // Search the voteOptions
        this.restVoteOptionsMockMvc.perform(MockMvcRequestBuilders.get("/api/_search/vote-options?query=id:" + this.voteOptions.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").value(Matchers.hasItem(this.voteOptions.getId().intValue())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.[*].text")
                .value(Matchers.hasItem(VoteOptionsResourceIntTest.DEFAULT_TEXT.toString())));
    }
}
