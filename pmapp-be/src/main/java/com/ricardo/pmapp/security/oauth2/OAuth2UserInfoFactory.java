package com.ricardo.pmapp.security.oauth2;

import com.ricardo.pmapp.exceptions.OAuth2AuthenticationProcessingException;
import com.ricardo.pmapp.security.oauth2.models.AuthProvider;
import com.ricardo.pmapp.security.oauth2.models.FacebookOAuth2UserInfo;
import com.ricardo.pmapp.security.oauth2.models.GithubOAuth2UserInfo;
import com.ricardo.pmapp.security.oauth2.models.GoogleOAuth2UserInfo;
import com.ricardo.pmapp.security.oauth2.models.OAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.facebook.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.github.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException(
                    "Sorry! Login with " + registrationId + " is not supported yet."
            );
        }
    }
}
