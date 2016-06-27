/**
 *
 */
package com.prodyna.pac.voting.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.csrf.CsrfFilter;

import com.prodyna.pac.voting.security.AjaxAuthenticationFailureHandler;
import com.prodyna.pac.voting.security.AjaxAuthenticationSuccessHandler;
import com.prodyna.pac.voting.security.AjaxLogoutSuccessHandler;
import com.prodyna.pac.voting.security.CustomAccessDeniedHandler;
import com.prodyna.pac.voting.security.Http401UnauthorizedEntryPoint;
import com.prodyna.pac.voting.web.filter.CsrfCookieGeneratorFilter;

/**
 * @author <a href="mailto:sven.lissek@prodyna.com">Sven Lissek</a>, <a href="http://www.prodyna.com">PRODYNA AG</a>
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Inject
    ApplicationProperties applicationProerties;

    @Inject
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Inject
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Inject
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Inject
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private RememberMeServices rememberMeServices;

    @Inject
    private SessionRegistry sessionRegistry;

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Inject
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder());
    }

    // @formatter:off
    @Override
    protected void configure(final HttpSecurity http) throws Exception
    {
        http
            .sessionManagement()
            .maximumSessions(32) // maximum number of concurrent sessions for one user
            .sessionRegistry(this.sessionRegistry)
            .and().and()
            .csrf()
        .and()
            .addFilterAfter(new CsrfCookieGeneratorFilter(), CsrfFilter.class)
            .exceptionHandling()
            .accessDeniedHandler(new CustomAccessDeniedHandler())
            .authenticationEntryPoint(this.authenticationEntryPoint)
        .and()
            .rememberMe()
            .rememberMeServices(this.rememberMeServices)
            .rememberMeParameter("remember-me")
            .key(this.applicationProerties.getSecurity().getRememberMe().getKey())
        .and()
            .formLogin()
            .loginProcessingUrl("/api/authentication")
            .successHandler(this.ajaxAuthenticationSuccessHandler)
            .failureHandler(this.ajaxAuthenticationFailureHandler)
            .usernameParameter("username")
            .passwordParameter("password")
            .permitAll()
        .and()
            .logout()
            .logoutUrl("/api/logout")
            .logoutSuccessHandler(this.ajaxLogoutSuccessHandler)
            .deleteCookies("JSESSIONID", "CSRF-TOKEN", "hazelcast.sessionId")
            .permitAll()
        .and()
            .headers()
            .frameOptions()
            .disable()
        .and()
            .authorizeRequests()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/**").authenticated();
    }
    // @formatter:on
}
