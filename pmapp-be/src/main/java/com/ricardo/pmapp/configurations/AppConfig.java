package com.ricardo.pmapp.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
@Getter
public class AppConfig {

    private final Auth auth = new Auth();

    private final OAuth2 oauth2 = new OAuth2();

    private final Cors cors = new Cors();

    private final InitScript initScript = new InitScript();

    @Getter
    @Setter
    public static class Auth {

        private String tokenSecret;
        private long tokenExpirationMsec;
    }

    @Getter
    @Setter
    public static final class OAuth2 {

        private List<String> authorizedRedirectUris = new ArrayList<>();
    }

    @Getter
    @Setter
    public static final class Cors {

        private List<String> allowedCorsOrigins = new ArrayList<>();
    }

    @Getter
    @Setter
    public static final class InitScript {

        private boolean enabled;
    }
}
