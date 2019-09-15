package com.noqodi.client.v2api.domain.models.token;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.noqodi.client.v2api.domain.models.base.AbstractResponseModel;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(builderClassName = "AccessTokenResponseModelBuilder", toBuilder = true)
@JsonDeserialize(builder = AccessTokenResponseModel.AccessTokenResponseModelBuilder.class)
public class AccessTokenResponseModel extends AbstractResponseModel {

    private String refreshTokenExpiresIn;
    private String apiProductList;
    private List<String> apiProductListJson;
    private String organizationName;
    private String developerEmail;
    private String tokenType;
    private String issuedAt;
    private String clientId;
    private String accessToken;
    private String applicationName;
    private String scope;
    private String expiresIn;
    private String refreshCount;
    private String status;
    private StatusInfo statusInfo;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AccessTokenResponseModelBuilder {}
}

