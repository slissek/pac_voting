package com.prodyna.pac.voting.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
@Component
public class AjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
            final Authentication authentication)
                    throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
