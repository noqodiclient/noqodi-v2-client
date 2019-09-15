package com.noqodi.client.v2api.configurations;

import com.noqodi.client.v2api.security.AppPreAuthenticatedProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AppPreAuthenticatedProcessingFilter appPreAuthenticatedProcessingFilter;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilter(appPreAuthenticatedProcessingFilter)
                .authorizeRequests()
                .antMatchers("/", "/web/**", "/resources/**", "/api/token").permitAll()
                .antMatchers("/api/**").authenticated();
    }

}