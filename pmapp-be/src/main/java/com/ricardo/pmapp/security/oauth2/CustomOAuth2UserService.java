package com.ricardo.pmapp.security.oauth2;

import com.ricardo.pmapp.exceptions.OAuth2AuthenticationProcessingException;
import com.ricardo.pmapp.exceptions.UserCreationException;
import com.ricardo.pmapp.exceptions.UserNotFoundException;
import com.ricardo.pmapp.persistence.models.entities.User;
import com.ricardo.pmapp.security.models.UserPrincipal;
import com.ricardo.pmapp.security.oauth2.models.OAuth2UserInfo;
import com.ricardo.pmapp.services.UserServiceI;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * FINALIZES AN OAUTH2 AUTH FLOW
 * <p>
 * The CustomOAuth2UserService extends Spring Security’s DefaultOAuth2UserService and implements its loadUser()
 * method. This method is called after an access token is obtained from the OAuth2 provider.
 * <p>
 * In this method, we first fetch the user’s details from the OAuth2 provider. If a user with
 * the same email already exists in our database then we update his details, otherwise, we register a new user.
 */

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserServiceI userService;

    public CustomOAuth2UserService(UserServiceI userService) {
        this.userService = userService;
    }

    /*
     * This method is called after an access token is obtained from the OAuth2 provider.
     * */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) throws UserCreationException {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oAuth2UserRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes()
        );
        if (!StringUtils.hasLength(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        User user;
        try {
            user = updateExistingUser(userService.getByEmail(oAuth2UserInfo.getEmail()), oAuth2UserInfo);
        } catch (UserNotFoundException ex) {
            user = registerNewUser(oAuth2UserInfo);
        }

        // The return value of this function will set the Current Logged User in the SecurityContext .
        // The Current user could be used with the @CurrentUser annotation that it is a wrapper of
        // @AuthenticationPrincipal that is set by the return of the loadUser
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserInfo oAuth2UserInfo) throws UserCreationException {
        User user = new User();

        setFirstAndLastName(user, oAuth2UserInfo);
        user.setUsername(oAuth2UserInfo.getEmail());
        user.setEmail(oAuth2UserInfo.getEmail());
        return userService.create(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) throws UserNotFoundException {
        setFirstAndLastName(existingUser, oAuth2UserInfo);
        return userService.update(existingUser);
    }

    private void setFirstAndLastName(User user, OAuth2UserInfo oAuth2UserInfo) {
        String oAuth2Name = oAuth2UserInfo.getName();
        if (oAuth2Name != null) {
            int count = 0;
            String surname = "";
            for (String s : oAuth2Name.split(" ")) {
                if (count == 0) {
                    user.setName(s);
                } else {
                    surname = surname.concat(s + " ");
                }
                count++;
            }
            surname = surname.trim();
            user.setSurname(surname);
        }
    }
}
