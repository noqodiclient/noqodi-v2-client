package com.noqodi.client.v2api.domain.models.token;

import com.noqodi.client.v2api.domain.constants.OAuthGrantType;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder(builderMethodName = "PrivateBuilder")
@Getter
public class CCGAccessTokenRequestModel extends AbstractAccessTokenRequestModel {

    @NotNull(message = "grantType should not be null")
    private OAuthGrantType grantType;
    @NotEmpty(message = "clientId should not be empty")
    private String clientId;
    @NotEmpty(message = "clientSecret should not be empty")
    private String clientSecret;
    @NotEmpty(message = "requestUrl should not be empty")
    private String requestUrl;

    public static CCGAccessTokenRequestModelBuilder builder(OAuthGrantType grantType) {
        return PrivateBuilder().grantType(grantType);
    }
}
