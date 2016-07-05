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

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.prodyna.pac.voting.VotingApplication;
import com.prodyna.pac.voting.domain.Authority;
import com.prodyna.pac.voting.domain.User;
import com.prodyna.pac.voting.repository.AuthorityRepository;
import com.prodyna.pac.voting.security.AuthoritiesConstants;
import com.prodyna.pac.voting.service.UserService;
import com.prodyna.pac.voting.web.rest.dto.ManagedUserDTO;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VotingApplication.class)
@WebAppConfiguration
@IntegrationTest
public class UserResourceIntTest
{
    private static final Long DEFAULT_USER_ID = 1L;
    private static final String DEFAULT_USER_NAME = "pjane";
    private static final String UPDATED_USER_NAME = "tlisbon";
    private static final String DEFAULT_FIRST_NAME = "Patrick";
    private static final String UPDATED_FIRST_NAME = "Teresa";
    private static final String DEFAULT_LAST_NAME = "Jane";
    private static final String UPDATED_LAST_NAME = "Lisbon";
    private static final String DEFAULT_PASSWORD = "AAAAA";
    private static final String UPDATED_PASSWORD = "BBBBB";

    @Inject
    private UserService userService;

    @Inject
    private AuthorityRepository authorityRepository;

    private MockMvc restUserMockMvc;

    private ManagedUserDTO userDTO;
    private User user;

    @PostConstruct
    public void setup()
    {
        final UserResource userResource = new UserResource();
        ReflectionTestUtils.setField(userResource, "userService", this.userService);
        ReflectionTestUtils.setField(userResource, "authorityRepository", this.authorityRepository);

        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
    }

    @Before
    public void initTest()
    {
        final ManagedUserDTO managedUserDTO = new ManagedUserDTO();
        managedUserDTO.setId(null);
        managedUserDTO.setUserName(DEFAULT_USER_NAME);
        managedUserDTO.setFirstName(DEFAULT_FIRST_NAME);
        managedUserDTO.setLastName(DEFAULT_LAST_NAME);
        managedUserDTO.setPassword(DEFAULT_PASSWORD);
        managedUserDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));
        this.userDTO = managedUserDTO;

        final User user = new User();
        user.setId(null);
        user.setUserName(DEFAULT_USER_NAME);
        user.setFirstName(DEFAULT_FIRST_NAME);
        user.setLastName(DEFAULT_LAST_NAME);
        user.setPassword(DEFAULT_PASSWORD);

        final Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);

        user.setAuthorities(Collections.singleton(authority));
        this.user = user;
    }

    @Test
    @Transactional
    public void createUser() throws Exception
    {
        final int databaseSizeBeforeCreate = this.userService.getAll().size();

        // Create the User
        final byte[] jsonBytes = TestUtil.convertObjectToJsonBytes(this.userDTO);
        this.restUserMockMvc.perform(post("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8).content(jsonBytes))
        .andExpect(status().isCreated());

        // Validate the User in the database
        final List<User> users = this.userService.getAll();
        assertThat(users).hasSize(databaseSizeBeforeCreate + 1);

        final User testUser = users.get(users.size() - 1);
        assertThat(testUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testUser.getPassword()).isNotEmpty();
        assertThat(testUser.getAuthorities().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void checkUserNameIsRequired() throws Exception
    {
        final int databaseSizeBeforeTest = this.userService.getAll().size();
        // set the field null
        this.userDTO.setUserName(null);

        // Create the User, which fails.
        this.restUserMockMvc.perform(post("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(this.userDTO))).andExpect(status().isBadRequest());

        final List<User> users = this.userService.getAll();
        assertThat(users).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUser() throws Exception
    {
        // Initialize the database
        this.userService.save(this.user);

        // Get all the users
        this.restUserMockMvc.perform(get("/api/users")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.[*].id").value(hasItem(DEFAULT_USER_ID.intValue())))
        .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
        .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
        .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())));
    }

    @Test
    @Transactional
    public void getUser() throws Exception
    {
        // Initialize the database
        this.userService.save(this.user);

        // Get the users
        this.restUserMockMvc.perform(get("/api/users/{id}", this.user.getId())).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(this.user.getId().intValue()))
        .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
        .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
        .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUser() throws Exception
    {
        // Get the users
        this.restUserMockMvc.perform(get("/api/users/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUser() throws Exception
    {
        // Initialize the database
        this.userService.save(this.user);

        final int databaseSizeBeforeUpdate = this.userService.getAll().size();

        // Update the users
        final ManagedUserDTO updatedUser = new ManagedUserDTO();
        updatedUser.setFirstName(UPDATED_FIRST_NAME);
        updatedUser.setLastName(UPDATED_LAST_NAME);
        updatedUser.setUserName(UPDATED_USER_NAME);
        updatedUser.setPassword(UPDATED_PASSWORD);
        updatedUser.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));
        updatedUser.setId(this.user.getId());

        this.restUserMockMvc.perform(put("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUser))).andExpect(status().isOk());

        // Validate the User in the database
        final List<User> users = this.userService.getAll();
        assertThat(users).hasSize(databaseSizeBeforeUpdate);

        final User testUser = users.get(users.size() - 1);
        assertThat(testUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testUser.getPassword()).isNotEmpty();
        assertThat(testUser.getAuthorities().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void deleteUser() throws Exception
    {
        // Initialize the database
        this.userService.save(this.user);

        final int databaseSizeBeforeDelete = this.userService.getAll().size();

        // Get the users
        this.restUserMockMvc
        .perform(delete("/api/users/{id}", this.user.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk());

        // Validate the database is empty
        final List<User> users = this.userService.getAll();
        assertThat(users).hasSize(databaseSizeBeforeDelete - 1);
    }
}
