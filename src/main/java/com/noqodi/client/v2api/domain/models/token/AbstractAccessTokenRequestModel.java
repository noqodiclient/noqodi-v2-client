package com.noqodi.client.v2api.domain.models.token;

import com.noqodi.client.v2api.domain.constants.OAuthGrantType;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;

public abstract class AbstractAccessTokenRequestModel extends AbstractRequestModel {

    public abstract OAuthGrantType getGrantType();
    public abstract String getClientId();
    public abstract String getClientSecret();
    public abstract String getRequestUrl();
}
