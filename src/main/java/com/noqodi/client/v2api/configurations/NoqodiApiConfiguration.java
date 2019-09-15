package com.noqodi.client.v2api.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@PropertySource(value = "classpath:application.yml", ignoreResourceNotFound = false)
@ConfigurationProperties(prefix = "noqodi.v2.api")
@Data
public class NoqodiApiConfiguration {

    private String baseUrl;
    private Endpoints endpoints;
    private Credentials credentials;
    private Map<String, String> testData;

    @Data
    public static class Credentials {
        private String clientId;
        private String clientSecret;
        private List<User> users;
    }

    @Data
    public static class User {
        private String username;
        private String password;
        private Role role;
    }

    public static enum Role {ROLE_ADMIN, ROLE_USER }

    @Data
    public static class Endpoints {
        private OauthUris oauthUris;
        private PaymentUris paymentUris;
        private BalancesUris balancesUris;
        private RefundsUris refundsUris;
        private PayoutsUris payoutsUris;
    }

    @Data
    public static class OauthUris {
        private String passwordUri;
        private String clientCredentialsUri;
    }

    @Data
    public static class PaymentUris {
        private String preAuthUri;
        private String voidAuthUri;
        private String captureUri;
        private String completeUri;
    }

    @Data
    public static class BalancesUris {
        private String merchantUri;
        private String merchantBeneficiariesUri;
        private String customerWalletUri;
    }

    @Data
    public static class RefundsUris {
        private String refundsUri;
        private String completeRefundsUri;
    }

    @Data
    public static class PayoutsUris {
        private String payoutsUri;
    }
}
