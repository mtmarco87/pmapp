package com.ricardo.pmapp.security.jwttoken;

import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.security.models.UserPrincipal;
import com.ricardo.pmapp.services.UserServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ricardo.pmapp.exceptions.ExceptionMessages.INVALID_JWT_USER;

/**
 * This class is used to read JWT authentication token from the request, verify it, and set Spring Security’s
 * SecurityContext if the token is valid -
 */

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserServiceI userService;

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt, request, response)) {
                UserToken userFromToken = tokenProvider.getUserFromToken(jwt);
                User userFromDb = userService.getByUsername(userFromToken.getUsername());

                // If the user that we read from the DB doesn't exist or is different from the one
                // found in the token, the authentication cannot be executed
                if (userFromDb == null ||
                        !userFromDb.getEmail().equals(userFromToken.getEmail())) {
                    throw new Exception(INVALID_JWT_USER);
                }

                // If the User from the token is OK we can create UserDetails.
                // UserDetails is a class on Spring side that will contain some basic
                // info about the CurrentUser that we'll store in the authentication object
                // and then we'll set in the SecurityContextHolder
                UserPrincipal userPrincipal = UserPrincipal.create(userFromDb);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userPrincipal,
                        null,
                        userPrincipal.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
