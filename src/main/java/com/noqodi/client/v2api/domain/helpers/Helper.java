package com.noqodi.client.v2api.domain.helpers;

import com.noqodi.client.v2api.domain.constants.AuthorizationType;
import com.noqodi.client.v2api.domain.factory.ObjectFactory;
import com.noqodi.client.v2api.domain.models.auth.HttpAuthorization;
import com.noqodi.client.v2api.domain.models.token.AccessTokenResponseModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

public class Helper {

    private final static ObjectFactory factory = ObjectFactory.getInstance();

    public static String buildUrl(String baseUrl, MultiValueMap<String, String> queryParams, String... paths) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path((paths != null && paths.length > 0) ? String.join("/", paths) : "")
                .queryParams(queryParams)
                .toUriString();
    }

    public static HttpEntity<MultiValueMap<String, String>> createHeaderOnlyRequestEntity(MultiValueMap<String, String> httpHeaders) {
        return new HttpEntity<>(httpHeaders);
    }

    public static <T> HttpEntity<T> createHeaderAndPayloadRequestEntity(T payload, MultiValueMap<String, String> httpHeaders) {
        return new HttpEntity<>(payload, httpHeaders);
    }

    public static HttpEntity<MultiValueMap<String, String>> createRequestEntityWithHeaderAndFormData(MultiValueMap<String, String> formData, HttpHeaders httpHeaders) {
        return new HttpEntity<>(formData, httpHeaders);
    }

    public static String extractAccessToken(AccessTokenResponseModel accessTokenResponseModel) {
        return accessTokenResponseModel.getAccessToken();
    }

    public static boolean isApprovedAccessToken(AccessTokenResponseModel response) {
        return !StringUtils.isEmpty(response.getStatus()) && response.getStatus().equalsIgnoreCase("approved");
    }

    public static int convertSecondsToMinutesOrDefault(int expirySeconds, int defaultMinutes) {
        if (expirySeconds < 1) return defaultMinutes;
        return expirySeconds / 60;
    }

    public static int getDesiredValueOrDefault(int desiredValue, int defaultValue) {
        return (desiredValue >= 120) ? desiredValue : defaultValue;
    }

    public static HttpHeaders createHttpHeaders(HttpAuthorization authorization, MediaType contentType, List<MediaType> acceptableMediaTypes) {
        HttpHeaders headers = new HttpHeaders();
        if (!StringUtils.isEmpty(contentType)) headers.setContentType(contentType);
        if (acceptableMediaTypes != null && !acceptableMediaTypes.isEmpty()) headers.setAccept(acceptableMediaTypes);
        if (authorization != null) {
            if (authorization.getType() == AuthorizationType.BASIC) {
                String username = authorization.getUsername();
                String password = authorization.getPassword();
                if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password))
                    headers.setBasicAuth(username, password);
            } else {
                String accessToken = authorization.getAccessToken();
                if (!StringUtils.isEmpty(accessToken)) headers.setBearerAuth(accessToken);
            }
        }
        return headers;
    }

    public static HttpHeaders getRequestHeaders(String accessToken) {
        HttpAuthorization authorization = HttpAuthorization
                .builder()
                .type(AuthorizationType.BEARER)
                .accessToken(accessToken)
                .build();
        return createHttpHeaders(authorization, null, Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    public static <T> String extractCommandPayload(T commandPayload, String abstractClassName) {
        if (commandPayload == null)
            return abstractClassName + " => executing this method with NULL object will fail validation; so watch out for that :|";
        return commandPayload.getClass().getName() + " => " + factory.getGson().toJson(commandPayload);
    }

}
