package com.noqodi.client.v2api.domain.models.base;

import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Builder
@Getter
public class RemoteOperationModel<T extends AbstractRequestModel, R extends AbstractResponseModel> {
    private String requestUrl;
    private HttpMethod httpMethod;
    private HttpStatus httpStatus;
    private StatusInfo statusInfo;
    private T request;
    private R response;
}
