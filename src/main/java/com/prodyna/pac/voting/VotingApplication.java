package com.prodyna.pac.voting;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import com.prodyna.pac.voting.config.ApplicationProperties;
import com.prodyna.pac.voting.config.Constants;

@SpringBootApplication
@EnableConfigurationProperties({ ApplicationProperties.class })
@EnableAutoConfiguration(exclude = { HazelcastAutoConfiguration.class })
public class VotingApplication
{
    private static final Logger log = LoggerFactory.getLogger(VotingApplication.class);

    /**
     * Main method, used to run the application.
     *
     * @param args
     *            the command line arguments
     * @throws UnknownHostException
     *             if the local host name could not be resolved into an address
     */
    public static void main(final String[] args) throws UnknownHostException
    {
        final SpringApplication app = new SpringApplication(VotingApplication.class);
        addDefaultProfile(app);
        final Environment env = app.run(args).getEnvironment();

        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\thttp://127.0.0.1:{}\n\t" +
                "External: \thttp://{}:{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
    }

    /**
     * set a default to use when no profile is configured.
     */
    protected static void addDefaultProfile(final SpringApplication app)
    {
        final Map<String, Object> defProperties = new HashMap<>();
        /*
         * The default profile to use when no other profiles are defined This cannot be set in the `application.yml` file. See
         * https://github.com/spring-projects/spring-boot/issues/1219
         */
        defProperties.put("spring.profiles.default", Constants.SPRING_PROFILE_DEVELOPMENT);
        app.setDefaultProperties(defProperties);
    }
}
