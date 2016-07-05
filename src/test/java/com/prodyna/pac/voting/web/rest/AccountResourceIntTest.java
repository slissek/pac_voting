package com.prodyna.pac.voting.web.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.prodyna.pac.voting.VotingApplication;
import com.prodyna.pac.voting.domain.Authority;
import com.prodyna.pac.voting.domain.User;
import com.prodyna.pac.voting.security.AuthoritiesConstants;
import com.prodyna.pac.voting.service.UserService;

/**
 * Test class for the AccountResource REST controller.
 *
 * @see AccountResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VotingApplication.class)
@WebAppConfiguration
@IntegrationTest
public class AccountResourceIntTest
{
    private static final String USER_NAME = "test";
    private static final String FIRST_NAME = "mighty";
    private static final String LAST_NAME = "admin";

    @Inject
    private UserService userService;

    @Mock
    private UserService mockUserService;

    private MockMvc restMvc;
    private MockMvc restMockMvc;

    @PostConstruct
    public void setup()
    {
        MockitoAnnotations.initMocks(this);

        final AccountResource accountResource = new AccountResource();
        ReflectionTestUtils.setField(accountResource, "userService", this.userService);

        final AccountResource accountMockResource = new AccountResource();
        ReflectionTestUtils.setField(accountMockResource, "userService", this.mockUserService);

        this.restMvc = MockMvcBuilders.standaloneSetup(accountResource).build();
        this.restMockMvc = MockMvcBuilders.standaloneSetup(accountMockResource).build();
    }

    @Test
    public void testNonAuthenticatedUser() throws Exception
    {
        this.restMvc.perform(get("/api/authenticate")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(""));
    }

    @Test
    public void testAuthenticatedUser() throws Exception
    {
        this.restMvc.perform(get("/api/authenticate")
                .with(request -> {
                    request.setRemoteUser(USER_NAME);
                    return request;
                })
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(USER_NAME));
    }

    @Test
    public void testGetExistingAccount() throws Exception
    {
        final Set<Authority> authorities = new HashSet<>();
        final Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.ADMIN);
        authorities.add(authority);

        final User user = new User();
        user.setUserName(USER_NAME);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setAuthorities(authorities);
        when(this.mockUserService.getUserWithAuthorities()).thenReturn(user);

        this.restMockMvc.perform(get("/api/account")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.userName").value(USER_NAME))
        .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
        .andExpect(jsonPath("$.lastName").value(LAST_NAME))
        .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN));
    }

    @Test
    public void testGetUnknownAccount() throws Exception
    {
        when(this.mockUserService.getUserWithAuthorities()).thenReturn(null);

        this.restMockMvc.perform(get("/api/account")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError());
    }
}
