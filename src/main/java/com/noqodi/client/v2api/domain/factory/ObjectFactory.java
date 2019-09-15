package com.noqodi.client.v2api.domain.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.noqodi.client.v2api.configurations.NoqodiApiConfiguration;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
@Getter
public class ObjectFactory {

    private static ObjectFactory instance;

    @Autowired
    private NoqodiApiConfiguration configuration;
    private final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    @PostConstruct
    public void initInstance() {
        instance = this;
    }

    public static ObjectFactory getInstance() {
        return instance;
    }
}
