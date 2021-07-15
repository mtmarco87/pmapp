package com.ricardo.pmapp.security.jwttoken;

import com.google.gson.Gson;
import com.ricardo.pmapp.configurations.AppConfig;
import com.ricardo.pmapp.security.models.UserPrincipal;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private final AppConfig appConfig;

    private final Gson gson;

    public TokenProvider(AppConfig appConfig, Gson gson) {
        this.appConfig = appConfig;
        this.gson = gson;
    }

    public String createToken(Authentication authentication) {
        // Creates JWT token from an Authenticated User
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appConfig.getAuth().getTokenExpirationMsec());

        UserToken userToken = new UserToken(userPrincipal);
        String json = this.gson.toJson(userToken);

        return Jwts
                .builder()
                .setSubject(json)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appConfig.getAuth().getTokenSecret())
                .compact();
    }

    public String refreshToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appConfig.getAuth().getRefreshTokenExpirationMsec());

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appConfig.getAuth().getTokenSecret())
                .compact();
    }

    public UserToken getUserFromToken(String token) {
        // Decodes UserInToken from a JWT Token
        Claims claims = Jwts
                .parser()
                .setSigningKey(appConfig.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        String json = claims.getSubject();
        return this.gson.fromJson(json, UserToken.class);
    }

    public boolean validateToken(String authToken, HttpServletRequest request, HttpServletResponse response) {
        try {
            Jwts.parser().setSigningKey(appConfig.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
            String requestURL = request.getRequestURL().toString();
            // allow for Refresh Token creation if following conditions are true.
            if (requestURL.contains("refreshtoken")) {
                allowForRefreshToken(ex, request);
            } else if (!requestURL.contains("login") && !requestURL.contains("logout")) {
                response.addHeader("Auth-Status", "token-expired");
            }
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {

        // create a UsernamePasswordAuthenticationToken with null values.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // Set the claims so that in controller we will be using it to create
        // new JWT
        request.setAttribute("claims", ex.getClaims());
    }
}
