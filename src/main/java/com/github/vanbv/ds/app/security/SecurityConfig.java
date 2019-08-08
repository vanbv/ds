package com.github.vanbv.ds.app.security;

import com.github.vanbv.ds.backend.service.UserService;
import com.github.vanbv.ds.ui.views.error.RouteNotFoundError;
import com.github.vanbv.ds.ui.views.login.LoginView;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_URL = "/" + LoginView.ROUTE;
    private static final String LOGIN_FAILURE_URL = LOGIN_URL + "?" + LoginView.ERROR_PARAM;
    private static final String ACCESS_DENIED_URL = "/" + RouteNotFoundError.ROUTE;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .requestCache().requestCache(
                        new HttpSessionRequestCache() {

                            @Override
                            public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
                                if (!SecurityUtils.isFrameworkInternalRequest(request)) {
                                    super.saveRequest(request, response);
                                }
                            }
                        })
                .and().authorizeRequests()
                .antMatchers(LOGIN_URL).anonymous()
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage(LOGIN_URL)
                .loginProcessingUrl(LOGIN_URL)
                .failureUrl(LOGIN_FAILURE_URL)
                .and().logout().logoutSuccessUrl(LOGIN_URL)
                .and().exceptionHandling().accessDeniedPage(ACCESS_DENIED_URL);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline-page.html",
                "/icons/**",
                "/images/**",
                "/frontend/**",
                "/webjars/**",
                "/h2-console/**",
                "/frontend-es5/**", "/frontend-es6/**").and();
    }
}
