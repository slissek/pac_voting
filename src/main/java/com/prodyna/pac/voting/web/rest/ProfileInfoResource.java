package com.prodyna.pac.voting.web.rest;

import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProfileInfoResource
{
    @Inject
    Environment env;

    @RequestMapping("/profile-info")
    public ProfileInfoResponse getActiveProfiles()
    {
        return new ProfileInfoResponse(this.env.getActiveProfiles());
    }

    class ProfileInfoResponse
    {
        public String[] activeProfiles;

        ProfileInfoResponse(final String[] activeProfiles)
        {
            this.activeProfiles = activeProfiles;
        }
    }
}