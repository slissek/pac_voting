package com.prodyna.pac.voting.service;

import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.prodyna.pac.voting.VotingApplication;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VotingApplication.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class UserServiceIntTest {

}
