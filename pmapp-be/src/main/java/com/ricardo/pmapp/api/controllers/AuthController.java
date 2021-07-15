package com.ricardo.pmapp.api.controllers;

import com.ricardo.pmapp.api.converters.UserConverter;
import com.ricardo.pmapp.api.models.dtos.LoginResponseDto;
import com.ricardo.pmapp.api.models.dtos.LoginRequestDto;
import com.ricardo.pmapp.api.models.dtos.UserDto;
import com.ricardo.pmapp.exceptions.ExceptionMessages;
import com.ricardo.pmapp.exceptions.LoginException;
import com.ricardo.pmapp.exceptions.UserNotFoundException;
import com.ricardo.pmapp.security.jwttoken.TokenProvider;
import com.ricardo.pmapp.security.jwttoken.UserToken;
import com.ricardo.pmapp.security.models.UserPrincipal;
import com.ricardo.pmapp.services.UserService;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    private final HttpServletRequest httpServletRequest;

    private final UserConverter userConverter;

    public AuthController(AuthenticationManager authenticationManager, TokenProvider tokenProvider,
                          HttpServletRequest httpServletRequest, UserConverter userConverter) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.httpServletRequest = httpServletRequest;
        this.userConverter = userConverter;
    }

    /**
     * Username/Password login returning a JWT token for API access
     *
     * @param loginRequestDto containing username and password
     * @return containing the JWT authentication token
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
        UserDto userDto = userConverter.ToDto((UserPrincipal) authentication.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDto(token, userDto));
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

    /**
     * Refresh Token
     *
     * @return containing the refreshed JWT authentication token
     */
    @PostMapping(value = "/refreshtoken")
    public ResponseEntity<LoginResponseDto> refreshtoken(HttpServletRequest request) {
        // From the HttpRequest get the claims
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

        // Refresh the token
        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
        String token = tokenProvider.refreshToken(expectedMap, expectedMap.get("sub").toString());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }
}
