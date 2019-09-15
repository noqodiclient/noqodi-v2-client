package com.noqodi.client.v2api.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@PropertySource(value = "classpath:application.yml", ignoreResourceNotFound = false)
@ConfigurationProperties(prefix = "demo-ewb-app")
@Data
public class WebAppConfiguration {

    private String appBaseUrl;
    private String noqodiPaymentPage;
    private String noqodiPaymentPageOrigin;
    private String callbackUrl;
    private String noqodiReadyData;
    private String apiBasePath;
    private Credentials credentials;

    @Data
    public static class Credentials {

        private String username;
        private String password;
    }
}
