package com.ricardo.pmapp.configurations;

import com.google.gson.Gson;
import com.ricardo.pmapp.security.jwttoken.TokenAuthenticationFilter;
import com.ricardo.pmapp.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeansConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // This is the password Encoder that will be used internally by spring. A random salt will be stored, and the hash
        // will be saved, then with the matches method a password will be checked re-calculating the hash with the password +
        // the stored salt made in encoding phase
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        // By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
        // the authorization request. But, since our service is stateless, we can't save it in
        // the session. We'll save the request in a Base64 encoded cookie instead.
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }
}
