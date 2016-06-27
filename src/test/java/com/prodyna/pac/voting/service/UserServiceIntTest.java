package com.prodyna.pac.voting.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.prodyna.pac.voting.VotingApplication;
import com.prodyna.pac.voting.repository.UserRepository;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VotingApplication.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class UserServiceIntTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

}
