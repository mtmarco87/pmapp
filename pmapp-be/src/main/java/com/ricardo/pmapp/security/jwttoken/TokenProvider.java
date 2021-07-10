package com.ricardo.pmapp.security.jwttoken;

import com.google.gson.Gson;
import com.ricardo.pmapp.configurations.AppConfig;
import com.ricardo.pmapp.security.models.UserPrincipal;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

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

        UserToken userToken = new UserToken(userPrincipal.getUsername(), userPrincipal.getEmail());
        String json = this.gson.toJson(userToken);

        return Jwts
                .builder()
                .setSubject(json)
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

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appConfig.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
