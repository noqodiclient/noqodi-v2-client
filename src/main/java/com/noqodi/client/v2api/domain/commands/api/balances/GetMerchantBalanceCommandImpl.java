package com.noqodi.client.v2api.domain.commands.api.balances;

import com.noqodi.client.v2api.domain.commands.api.AbstractApiCommand;
import com.noqodi.client.v2api.domain.commands.api.token.GetAccessTokenCCGCommandImpl;
import com.noqodi.client.v2api.domain.constants.Constants;
import com.noqodi.client.v2api.domain.helpers.Helper;
import com.noqodi.client.v2api.domain.helpers.RequestHelper;
import com.noqodi.client.v2api.domain.helpers.RestTemplateParameters;
import com.noqodi.client.v2api.domain.validation.DefaultValidationManager;
import com.noqodi.client.v2api.domain.validation.NoqodiValidationException;
import com.noqodi.client.v2api.domain.models.base.RemoteOperationModel;
import com.noqodi.client.v2api.domain.models.balances.MerchantBalanceRequestModel;
import com.noqodi.client.v2api.domain.models.balances.MerchantBalanceResponseModel;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import com.noqodi.client.v2api.domain.models.token.AbstractAccessTokenRequestModel;
import com.noqodi.client.v2api.domain.models.token.AccessTokenResponseModel;
import com.noqodi.client.v2api.domain.models.token.PGAccessTokenRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static com.noqodi.client.v2api.domain.helpers.RequestHelper.buildSampleAccessTokenPGRequest;
import static com.noqodi.client.v2api.domain.helpers.ResponseHelper.*;
import static com.noqodi.client.v2api.domain.helpers.RestTemplateHelper.executeHttpRequest;

@Service
public class GetMerchantBalanceCommandImpl extends AbstractApiCommand<MerchantBalanceRequestModel, MerchantBalanceResponseModel> {

    @Autowired
    private DefaultValidationManager validationManager;

    @Autowired
    public GetMerchantBalanceCommandImpl(GetAccessTokenCCGCommandImpl accessTokenBaseCommand) {
        super(accessTokenBaseCommand);
    }

    @Override
    public RemoteOperationModel<MerchantBalanceRequestModel, MerchantBalanceResponseModel> handle(MerchantBalanceRequestModel requestModel, AccessTokenResponseModel accessTokenResponseModel) {

        String accessToken = Helper.extractAccessToken(accessTokenResponseModel);
        HttpHeaders requestHeaders = Helper.getRequestHeaders(accessToken);
        RestTemplateParameters<MultiValueMap<String, String>, MerchantBalanceResponseModel> restTemplateParameters =
                RestTemplateParameters
                        .<MultiValueMap<String, String>, MerchantBalanceResponseModel>builder()
                        .restTemplate(objectFactory.getRestTemplate())
                        .requestUrl(getRequestUrl(requestModel))
                        .httpEntity(Helper.createHeaderOnlyRequestEntity(requestHeaders))
                        .responseType(MerchantBalanceResponseModel.class)
                        .httpMethod(HttpMethod.GET)
                        .build();
        ResponseEntity<MerchantBalanceResponseModel> responseEntity = executeHttpRequest(restTemplateParameters);
        return this.buildResponse(requestModel, responseEntity.getBody(), responseEntity);
    }

    @Override
    public RemoteOperationModel<MerchantBalanceRequestModel, MerchantBalanceResponseModel> buildResponse(MerchantBalanceRequestModel requestModel, MerchantBalanceResponseModel responseModel, ResponseEntity responseEntity) {
        StatusInfo statusInfo = responseModel != null ? responseModel.getStatusInfo() : null;
        return RemoteOperationModel
                .<MerchantBalanceRequestModel, MerchantBalanceResponseModel>builder()
                .httpMethod(HttpMethod.GET)
                .httpStatus(responseEntity.getStatusCode())
                .requestUrl(getRequestUrl(requestModel))
                .request(requestModel)
                .response(responseModel)
                .statusInfo(statusInfo)
                .build();
    }

    private String getRequestUrl(MerchantBalanceRequestModel requestModel) {
        return Helper.buildUrl(
                configuration.getBaseUrl(),
                null,
                configuration.getEndpoints().getBalancesUris().getMerchantUri(),
                "merchant",
                requestModel.getMerchantCode()
        );
    }

    @Override
    public void validate(MerchantBalanceRequestModel requestModel) throws NoqodiValidationException {
        Set<ConstraintViolation<AbstractRequestModel>> constraintViolations =
                validationManager
                        .getValidator()
                        .validate(requestModel);

        if (!constraintViolations.isEmpty())
            throw new NoqodiValidationException(Constants.VALIDATION_ERROR_MESSAGE, constraintViolations);
    }

    @Override
    public void preHandle(MerchantBalanceRequestModel requestModel) {

    }

    @Override
    public void postHandle(RemoteOperationModel<MerchantBalanceRequestModel, MerchantBalanceResponseModel> remoteOperationModel) {

    }

    @Override
    public AbstractAccessTokenRequestModel createAccessTokenRequestModel() {
        return buildSampleAccessTokenPGRequest(configuration);
    }

    @Override
    public RemoteOperationModel<MerchantBalanceRequestModel, MerchantBalanceResponseModel> buildTokenErrorResponse(MerchantBalanceRequestModel requestModel, RemoteOperationModel<AbstractAccessTokenRequestModel, AccessTokenResponseModel> tokenOperationModel) {
        return this.buildResponse(
                requestModel,
                merchantBalanceErrorOrDefault(null, null, sampleStatusInfoForTokenFailure()),
                new ResponseEntity(tokenOperationModel.getHttpStatus())
        );
    }

    @Override
    public RemoteOperationModel<MerchantBalanceRequestModel, MerchantBalanceResponseModel> handleError(MerchantBalanceRequestModel requestModel, MerchantBalanceResponseModel responseModel, Exception exception) {
        return this.buildResponse(
                requestModel,
                merchantBalanceErrorOrDefault(responseModel, exception, sampleStatusInfoGenericError()),
                tryGetResponseEntity(exception, responseModel, MerchantBalanceResponseModel.class)
        );
    }
}
