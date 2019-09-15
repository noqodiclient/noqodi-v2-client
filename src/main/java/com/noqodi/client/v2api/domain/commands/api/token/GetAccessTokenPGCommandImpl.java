package com.noqodi.client.v2api.domain.commands.api.token;

import com.noqodi.client.v2api.configurations.NoqodiApiConfiguration;
import com.noqodi.client.v2api.domain.models.token.PGAccessTokenRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@Service
public class GetAccessTokenPGCommandImpl extends AbstractGetAccessTokenCommand<PGAccessTokenRequestModel> {

    @Autowired
    protected NoqodiApiConfiguration configuration;

    protected MultiValueMap<String, String> getRequestData(PGAccessTokenRequestModel requestModel) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();
        data.add("grant_type", requestModel.getGrantType().name().toLowerCase());
        data.add("username", requestModel.getUsername());
        data.add("password", requestModel.getPassword());
        return data;
    }
}
