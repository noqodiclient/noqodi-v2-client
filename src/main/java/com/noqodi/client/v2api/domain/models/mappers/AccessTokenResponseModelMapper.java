package com.noqodi.client.v2api.domain.models.mappers;

import com.google.gson.internal.LinkedTreeMap;
import com.noqodi.client.v2api.domain.factory.ObjectFactory;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import com.noqodi.client.v2api.domain.models.token.AccessTokenResponseModel;

import java.util.ArrayList;
import java.util.Map;

import static com.noqodi.client.v2api.domain.helpers.ResponseHelper.getFailureStatusInfo;

public class AccessTokenResponseModelMapper {

    public static AccessTokenResponseModel toAccessTokenResponseModel(Map<String, Object> accessTokenResponse, StatusInfo statusInfo) {
        if (accessTokenResponse == null || accessTokenResponse.isEmpty()) {
            return AccessTokenResponseModel
                    .builder()
                    .statusInfo(statusInfo == null ? getFailureStatusInfo("SOMETHING_WENT_WRONG", "SOMETHING_WENT_WRONG") : statusInfo)
                    .build();
        } else {
            String json = ObjectFactory.getInstance().getGson().toJson((LinkedTreeMap) accessTokenResponse.get("statusInfo"));
            StatusInfo tokenResponseStatusInfo = ObjectFactory.getInstance().getGson().fromJson(json, StatusInfo.class);
            return AccessTokenResponseModel
                    .builder()
                    .refreshTokenExpiresIn((String) accessTokenResponse.get("refresh_token_expires_in"))
                    .apiProductList((String) accessTokenResponse.get("api_product_list"))
                    .apiProductListJson((ArrayList) accessTokenResponse.get("api_product_list_json"))
                    .organizationName((String) accessTokenResponse.get("organization_name"))
                    .developerEmail((String) accessTokenResponse.get("developer.email"))
                    .tokenType((String) accessTokenResponse.get("token_type"))
                    .issuedAt((String) accessTokenResponse.get("issued_at"))
                    .clientId((String) accessTokenResponse.get("client_id"))
                    .accessToken((String) accessTokenResponse.get("access_token"))
                    .applicationName((String) accessTokenResponse.get("application_name"))
                    .scope((String) accessTokenResponse.get("scope"))
                    .expiresIn((String) accessTokenResponse.get("expires_in"))
                    .refreshCount((String) accessTokenResponse.get("refresh_count"))
                    .status((String) accessTokenResponse.get("status"))
                    .statusInfo(tokenResponseStatusInfo)
                    .build();
        }
    }
}

