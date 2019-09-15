package com.noqodi.client.v2api.domain.commands.api.token;

import com.noqodi.client.v2api.configurations.NoqodiApiConfiguration;
import com.noqodi.client.v2api.domain.models.token.CCGAccessTokenRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class GetAccessTokenCCGCommandImpl extends AbstractGetAccessTokenCommand<CCGAccessTokenRequestModel> {

    @Autowired
    protected NoqodiApiConfiguration configuration;

    @Override
    protected MultiValueMap<String, String> getRequestData(CCGAccessTokenRequestModel requestModel) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
        data.add("grant_type", requestModel.getGrantType().name().toLowerCase());
        return data;
    }
}
