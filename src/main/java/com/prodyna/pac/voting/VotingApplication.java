package com.prodyna.pac.voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.prodyna.pac.voting.config.ApplicationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ ApplicationProperties.class })
@EnableAutoConfiguration(exclude = { HazelcastAutoConfiguration.class })
public class VotingApplication
{
    public static void main(final String[] args)
    {
        SpringApplication.run(VotingApplication.class, args);
    }
}
