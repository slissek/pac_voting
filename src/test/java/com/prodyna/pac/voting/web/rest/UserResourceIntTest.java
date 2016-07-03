package com.prodyna.pac.voting.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.prodyna.pac.voting.VotingApplication;
import com.prodyna.pac.voting.domain.User;
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
public class UserResourceIntTest {
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
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Inject
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	private MockMvc restUserMockMvc;

	private ManagedUserDTO userDTO;

	@PostConstruct
	public void setup() {
		final UserResource userResource = new UserResource();
		ReflectionTestUtils.setField(userResource, "userService", this.userService);

		this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
	}

	@Before
	public void initTest() {
		this.userDTO = new ManagedUserDTO(null, DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME, DEFAULT_USER_NAME,
				DEFAULT_PASSWORD, Collections.emptySet());
	}

	@Ignore("TODO")
	@Test
	@Transactional
	public void createUser() throws Exception {
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
		assertThat(testUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
	}

	@Ignore("TODO")
	@Test
	@Transactional
	public void checkUserNameIsRequired() throws Exception {
//		final int databaseSizeBeforeTest = this.usersRepository.findAll().size();
//		// set the field null
//		this.user.setUserName(null);
//
//		// Create the User, which fails.
//
//		this.restUserMockMvc.perform(post("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
//				.content(TestUtil.convertObjectToJsonBytes(this.user))).andExpect(status().isBadRequest());
//
//		final List<User> users = this.usersRepository.findAll();
//		assertThat(users).hasSize(databaseSizeBeforeTest);
	}

	@Test
	@Transactional
	public void getAllUser() throws Exception {
		// Initialize the database
//		this.usersRepository.saveAndFlush(this.user);
//
//		// Get all the users
//		this.restUserMockMvc.perform(get("/api/users?sort=id,desc")).andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.[*].id").value(hasItem(this.user.getId().intValue())))
//				.andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
//				.andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
//				.andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
//				.andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())));
	}

	@Ignore("TODO")
	@Test
	@Transactional
	public void getUser() throws Exception {
		// Initialize the database
//		this.usersRepository.saveAndFlush(this.user);
//
//		// Get the users
//		this.restUserMockMvc.perform(get("/api/users/{id}", this.user.getId())).andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.id").value(this.user.getId().intValue()))
//				.andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
//				.andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
//				.andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
//				.andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()));
	}

	@Ignore("TODO")
	@Test
	@Transactional
	public void getNonExistingUser() throws Exception {
		// Get the users
//		this.restUserMockMvc.perform(get("/api/users/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
	}

	@Ignore("TODO")
	@Test
	@Transactional
	public void updateUser() throws Exception {
		// Initialize the database
//		this.usersService.save(this.user);
//
//		final int databaseSizeBeforeUpdate = this.usersRepository.findAll().size();
//
//		// Update the users
//		final User updatedUser = new User(UPDATED_FIRST_NAME, UPDATED_LAST_NAME, UPDATED_USER_NAME, UPDATED_PASSWORD);
//		updatedUser.setId(this.user.getId());
//
//		this.restUserMockMvc.perform(put("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
//				.content(TestUtil.convertObjectToJsonBytes(updatedUser))).andExpect(status().isOk());
//
//		// Validate the User in the database
//		final List<User> users = this.usersRepository.findAll();
//		assertThat(users).hasSize(databaseSizeBeforeUpdate);
//		final User testUser = users.get(users.size() - 1);
//		assertThat(testUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
//		assertThat(testUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
//		assertThat(testUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
//		assertThat(testUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
	}

	@Ignore("TODO")
	@Test
	@Transactional
	public void deleteUser() throws Exception {
		// Initialize the database
//		this.usersService.save(this.user);
//
//		final int databaseSizeBeforeDelete = this.usersRepository.findAll().size();
//
//		// Get the users
//		this.restUserMockMvc
//				.perform(delete("/api/users/{id}", this.user.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
//				.andExpect(status().isOk());
//
//		// Validate the database is empty
//		final List<User> users = this.usersRepository.findAll();
//		assertThat(users).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Ignore("TODO")
	@Test
	@Transactional
	public void searchUser() throws Exception {
		// Initialize the database
//		this.usersService.save(this.user);
//
//		// Search the users
//		this.restUserMockMvc.perform(get("/api/_search/users?query=id:" + this.user.getId())).andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.[*].id").value(hasItem(this.user.getId().intValue())))
//				.andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
//				.andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
//				.andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
//				.andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())));
	}
}
