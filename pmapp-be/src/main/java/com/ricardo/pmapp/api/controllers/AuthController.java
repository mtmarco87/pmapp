package com.ricardo.pmapp.api.controllers;

import com.ricardo.pmapp.api.models.dtos.LoginResponseDto;
import com.ricardo.pmapp.api.models.dtos.LoginRequestDto;
import com.ricardo.pmapp.exceptions.ExceptionMessages;
import com.ricardo.pmapp.exceptions.LoginException;
import com.ricardo.pmapp.security.jwttoken.TokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    private final HttpServletRequest httpServletRequest;

    public AuthController(AuthenticationManager authenticationManager, TokenProvider tokenProvider,
                          HttpServletRequest httpServletRequest) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * Login: Standard Email/Password login
     *
     * @param loginRequestDto containing user email and password
     * @return containing the Authentication token
     * @throws LoginException in case of unauthorized request or failure
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) throws LoginException {
        Authentication authentication;
        try {
            authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
                    );
        } catch (Exception e) {
            throw new LoginException(String.format(ExceptionMessages.LOGIN_EXCEPTION, e.getMessage()));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    /**
     * Logout
     *
     * @throws ServletException in case of unauthorized request or failure
     */
    @PostMapping("/logout")
    public void logout() throws ServletException {
        this.httpServletRequest.logout();
    }

}
