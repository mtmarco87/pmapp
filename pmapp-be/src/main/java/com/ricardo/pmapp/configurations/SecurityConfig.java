package com.ricardo.pmapp.configurations;

import com.ricardo.pmapp.security.auth.CustomUserDetailsService;
import com.ricardo.pmapp.security.auth.RestAuthenticationEntryPoint;
import com.ricardo.pmapp.security.jwttoken.TokenAuthenticationFilter;
import com.ricardo.pmapp.security.oauth2.CustomOAuth2UserService;
import com.ricardo.pmapp.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.ricardo.pmapp.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.ricardo.pmapp.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * WebSecurityConfigurer is the default configuration and it is implemented by WebSecurityConfigurerAdapter
 * with the default configs.
 * We can override for a customization of those rules
 */

@Configuration
/* Enable the web security */
@EnableWebSecurity
/*
 * securedEnabled: you can secure controller or service method @Secured("ROLE_ADMIN")
 * jsr250Enabled: you can secure with @RolesAllowed("ADMIN")
 * prePostEnabled: @PreAuthorize("isAnonymous()") or @PreAuthorize("hasRole('ROLE_USER')")
 * */
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    private final CustomOAuth2UserService customOAuth2UserService;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    private final HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          PasswordEncoder passwordEncoder,
                          TokenAuthenticationFilter tokenAuthenticationFilter,
                          CustomOAuth2UserService customOAuth2UserService,
                          OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler,
                          OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler,
                          HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
        this.cookieAuthorizationRequestRepository = cookieAuthorizationRequestRepository;
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * AuthenticationManagerBuilder is used to create an AuthenticationManager instance which is
     * the main Spring Security interface for authenticating a user.
     *
     * @param authenticationManagerBuilder AuthenticationManagerBuilder instance
     * @throws Exception generic exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     * HttpSecurity instance is used to configure Spring Security behavior
     *
     * @param http HttpSecurity instance
     * @throws Exception generic exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // csrf attack consist in one attack that via client browser wants user click on a link, or hidden
                // form, to send request to the server side but since we use auth via JWT, the browser does not
                // have cookie so there is no issue, we can disable it
                .csrf()
                .disable()
                // disable the basic login provided by spring
                .formLogin()
                .disable()
                .httpBasic()
                .disable()
                // handle auth exceptions in ExceptionHandlerController
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                // no auth endpoints
                .authorizeRequests()
                .antMatchers(
                        "/auth/**",
                        "/oauth2/**",
                        "/swagger-ui.html",
                        "/swagger-ui.html#",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/webjars/**"
                )
                .permitAll()
                // any other endpoint needs auth
                .anyRequest()
                .authenticated()
                .and()
                // oauth2 auth configuration
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository)
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                // 1) Will be invoked the method loadUser of CustomOAuth2UserService after the third party login
                //    Here we save the UserPrincipal that will be available with the @CurrentUser annotation
                .userService(customOAuth2UserService)
                .and()
                // 2) Then if everything is ok the method onAuthenticationSuccess of oAuth2AuthenticationSuccessHandler
                //    will be invoked
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        // Add our custom JWT Token based authentication filter
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
