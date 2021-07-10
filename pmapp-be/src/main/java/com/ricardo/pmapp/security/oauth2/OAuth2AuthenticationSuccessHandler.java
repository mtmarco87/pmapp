package com.ricardo.pmapp.security.oauth2;

import com.ricardo.pmapp.configurations.AppConfig;
import com.ricardo.pmapp.exceptions.BadRequestException;
import com.ricardo.pmapp.security.jwttoken.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

/**
 * On successful authentication, Spring security invokes the onAuthenticationSuccess() method of the
 * OAuth2AuthenticationSuccessHandler configured in SecurityConfig.
 * <p>
 * In this method, we perform some validations, create a JWT authentication token, and redirect the user
 * to the redirect_uri specified by the client with the JWT token added in the query string -
 */

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final AppConfig appConfig;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    public OAuth2AuthenticationSuccessHandler(TokenProvider tokenProvider, AppConfig appConfig,
                                              HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.tokenProvider = tokenProvider;
        this.appConfig = appConfig;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrlAndCreateToken(request, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String determineTargetUrlAndCreateToken(HttpServletRequest request, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils
                .getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException(
                    "Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication"
            );
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        String token = tokenProvider.createToken(authentication);

        return UriComponentsBuilder.fromUriString(targetUrl).queryParam("token", token).build().toUriString();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        final URI redirectUri;

        try {
            redirectUri = URI.create(uri);
        } catch (Exception ex) {
            throw new BadRequestException("Malformed uri: " + uri);
        }

        return appConfig
                .getOauth2()
                .getAuthorizedRedirectUris()
                .stream()
                .anyMatch(
                        authorizedRedirectUriStr -> {
                            // Only validate host and port. Let the clients use different paths if they want to
                            URI authorizedUri;
                            try {
                                authorizedUri = URI.create(authorizedRedirectUriStr);
                            } catch (Exception ex) {
                                throw new BadRequestException("Malformed uri: " + authorizedRedirectUriStr);
                            }

                            return (authorizedUri.getHost() != null && redirectUri.getHost() != null)
                                    ? (authorizedUri.getHost().equalsIgnoreCase(redirectUri.getHost()) &&
                                    authorizedUri.getPort() == redirectUri.getPort())
                                    : (uri.equalsIgnoreCase(authorizedRedirectUriStr));
                        }
                );
    }
}
