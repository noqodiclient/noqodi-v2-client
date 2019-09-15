package com.noqodi.client.v2api.domain.commands.api.token;

import com.noqodi.client.v2api.domain.constants.AuthorizationType;
import com.noqodi.client.v2api.domain.constants.Constants;
import com.noqodi.client.v2api.domain.factory.ObjectFactory;
import com.noqodi.client.v2api.domain.helpers.Helper;
import com.noqodi.client.v2api.domain.helpers.RestTemplateParameters;
import com.noqodi.client.v2api.domain.validation.DefaultValidationManager;
import com.noqodi.client.v2api.domain.validation.NoqodiValidationException;
import com.noqodi.client.v2api.domain.cache.AccessTokenCacheManager;
import com.noqodi.client.v2api.domain.commands.base.AbstractRemoteOperationCommand;
import com.noqodi.client.v2api.domain.helpers.ResponseHelper;
import com.noqodi.client.v2api.domain.models.base.RemoteOperationModel;
import com.noqodi.client.v2api.domain.models.auth.HttpAuthorization;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import com.noqodi.client.v2api.domain.models.token.AbstractAccessTokenRequestModel;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.token.AccessTokenResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;

import javax.validation.ConstraintViolation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import static com.noqodi.client.v2api.domain.helpers.ResponseHelper.*;
import static com.noqodi.client.v2api.domain.helpers.RestTemplateHelper.executeHttpRequest;
import static com.noqodi.client.v2api.domain.models.mappers.AccessTokenResponseModelMapper.toAccessTokenResponseModel;

public abstract class AbstractGetAccessTokenCommand<T extends AbstractAccessTokenRequestModel> extends AbstractRemoteOperationCommand<T, AccessTokenResponseModel> {

    @Autowired
    private ObjectFactory objectFactory;

    @Autowired
    private AccessTokenCacheManager abstractCacheManager;

    @Autowired
    private DefaultValidationManager validationManager;

    @Override
    public RemoteOperationModel<T, AccessTokenResponseModel> handle(T requestModel) {
        MultiValueMap<String, String> formData = getRequestData(requestModel);
        HttpHeaders requestHeaders = getRequestHeaders(requestModel);

        RestTemplateParameters<MultiValueMap<String, String>, String> restTemplateParameters =
                RestTemplateParameters
                        .<MultiValueMap<String, String>, String>builder()
                        .restTemplate(objectFactory.getRestTemplate())
                        .requestUrl(requestModel.getRequestUrl())
                        .httpEntity(Helper.createRequestEntityWithHeaderAndFormData(formData, requestHeaders))
                        .responseType(String.class)
                        .httpMethod(HttpMethod.POST)
                        .build();

        ResponseEntity<String> responseEntity = executeHttpRequest(restTemplateParameters);
        HashMap accessTokenResponse = objectFactory.getGson().fromJson(responseEntity.getBody(), HashMap.class);
        AccessTokenResponseModel responseModel = toAccessTokenResponseModel(accessTokenResponse, getSuccessStatusInfo());
        return this.buildResponse(requestModel, responseModel, responseEntity);
    }

    @Override
    public RemoteOperationModel<T, AccessTokenResponseModel> buildResponse(T requestModel, AccessTokenResponseModel responseModel, ResponseEntity responseEntity) {
        StatusInfo statusInfo = responseModel != null ? responseModel.getStatusInfo() : null;
        return RemoteOperationModel
                .<T, AccessTokenResponseModel>builder()
                .httpMethod(HttpMethod.POST)
                .httpStatus(responseEntity.getStatusCode())
                .requestUrl(requestModel.getRequestUrl())
                .request(requestModel)
                .response(responseModel)
                .statusInfo(statusInfo)
                .build();
    }

    @Override
    public void validate(AbstractAccessTokenRequestModel requestModel) throws NoqodiValidationException {
        Set<ConstraintViolation<AbstractRequestModel>> constraintViolations =
                validationManager
                        .getValidator()
                        .validate(requestModel);

        if (!constraintViolations.isEmpty())
            throw new NoqodiValidationException(Constants.VALIDATION_ERROR_MESSAGE, constraintViolations);
    }

    private HttpHeaders getRequestHeaders(AbstractAccessTokenRequestModel requestModel) {
        HttpAuthorization authorization = HttpAuthorization.builder()
                .type(AuthorizationType.BASIC)
                .username(requestModel.getClientId())
                .password(requestModel.getClientSecret())
                .build();
        return Helper.createHttpHeaders(authorization, MediaType.APPLICATION_FORM_URLENCODED, Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    @Override
    public void preHandle(T requestModel) {

    }

    @Override
    public void postHandle(RemoteOperationModel<T, AccessTokenResponseModel> remoteOperationModel) {
        if (!ResponseHelper.isSuccess(remoteOperationModel)) return;
        abstractCacheManager.saveAccessTokenToCache(remoteOperationModel.getResponse());
    }

    @Override
    public RemoteOperationModel<T, AccessTokenResponseModel> handleError(T requestModel, AccessTokenResponseModel responseModel, Exception exception) {
        return this.buildResponse(
                requestModel,
                sampleAccessTokenResponseGenericError(exception),
                tryGetResponseEntity(exception, responseModel, AccessTokenResponseModel.class)
        );
    }

    protected abstract MultiValueMap<String, String> getRequestData(T requestModel);

}
