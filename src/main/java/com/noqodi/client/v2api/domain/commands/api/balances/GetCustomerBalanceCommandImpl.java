package com.noqodi.client.v2api.domain.commands.api.balances;

import com.noqodi.client.v2api.domain.commands.api.AbstractApiCommand;
import com.noqodi.client.v2api.domain.commands.api.token.GetAccessTokenCCGCommandImpl;
import com.noqodi.client.v2api.domain.constants.Constants;
import com.noqodi.client.v2api.domain.helpers.Helper;
import com.noqodi.client.v2api.domain.helpers.RequestHelper;
import com.noqodi.client.v2api.domain.helpers.RestTemplateParameters;
import com.noqodi.client.v2api.domain.validation.DefaultValidationManager;
import com.noqodi.client.v2api.domain.validation.NoqodiValidationException;
import com.noqodi.client.v2api.domain.models.balances.CustomerBalanceRequestModel;
import com.noqodi.client.v2api.domain.models.balances.CustomerBalanceResponseModel;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.base.RemoteOperationModel;
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
public class GetCustomerBalanceCommandImpl extends AbstractApiCommand<CustomerBalanceRequestModel, CustomerBalanceResponseModel> {

    @Autowired
    private DefaultValidationManager validationManager;

    @Autowired
    public GetCustomerBalanceCommandImpl(GetAccessTokenCCGCommandImpl accessTokenBaseCommand) {
        super(accessTokenBaseCommand);
    }

    @Override
    public RemoteOperationModel<CustomerBalanceRequestModel, CustomerBalanceResponseModel> handle(CustomerBalanceRequestModel requestModel, AccessTokenResponseModel accessTokenResponseModel) {
        String accessToken = Helper.extractAccessToken(accessTokenResponseModel);
        HttpHeaders requestHeaders = Helper.getRequestHeaders(accessToken);
        RestTemplateParameters<MultiValueMap<String, String>, CustomerBalanceResponseModel> restTemplateParameters =
                RestTemplateParameters
                        .<MultiValueMap<String, String>, CustomerBalanceResponseModel>builder()
                        .restTemplate(objectFactory.getRestTemplate())
                        .requestUrl(getRequestUrl(requestModel))
                        .httpEntity(Helper.createHeaderOnlyRequestEntity(requestHeaders))
                        .responseType(CustomerBalanceResponseModel.class)
                        .httpMethod(HttpMethod.GET)
                        .build();
        ResponseEntity<CustomerBalanceResponseModel> responseEntity = executeHttpRequest(restTemplateParameters);
        return this.buildResponse(requestModel, responseEntity.getBody(), responseEntity);
    }

    @Override
    public RemoteOperationModel<CustomerBalanceRequestModel, CustomerBalanceResponseModel> buildResponse(CustomerBalanceRequestModel requestModel, CustomerBalanceResponseModel responseModel, ResponseEntity responseEntity) {
        StatusInfo statusInfo = responseModel != null ? responseModel.getStatusInfo() : null;
        return RemoteOperationModel
                .<CustomerBalanceRequestModel, CustomerBalanceResponseModel>builder()
                .httpMethod(HttpMethod.GET)
                .httpStatus(responseEntity.getStatusCode())
                .requestUrl(getRequestUrl(requestModel))
                .request(requestModel)
                .response(responseModel)
                .statusInfo(statusInfo)
                .build();
    }

    private String getRequestUrl(CustomerBalanceRequestModel requestModel) {
        return Helper.buildUrl(
                configuration.getBaseUrl(),
                null,
                configuration.getEndpoints().getBalancesUris().getMerchantUri(),
                "wallet",
                requestModel.getWalletId()
        );
    }

    @Override
    public void validate(CustomerBalanceRequestModel requestModel) throws NoqodiValidationException {
        Set<ConstraintViolation<AbstractRequestModel>> constraintViolations =
                validationManager
                        .getValidator()
                        .validate(requestModel);

        if (!constraintViolations.isEmpty())
            throw new NoqodiValidationException(Constants.VALIDATION_ERROR_MESSAGE, constraintViolations);
    }

    @Override
    public void preHandle(CustomerBalanceRequestModel requestModel) {

    }

    @Override
    public void postHandle(RemoteOperationModel<CustomerBalanceRequestModel, CustomerBalanceResponseModel> remoteOperationModel) {

    }

    @Override
    public AbstractAccessTokenRequestModel createAccessTokenRequestModel() {
        return buildSampleAccessTokenPGRequest(configuration);
    }

    @Override
    public RemoteOperationModel<CustomerBalanceRequestModel, CustomerBalanceResponseModel> buildTokenErrorResponse(CustomerBalanceRequestModel requestModel, RemoteOperationModel<AbstractAccessTokenRequestModel, AccessTokenResponseModel> tokenOperationModel) {
        return this.buildResponse(
                requestModel,
                customerBalanceErrorOrDefault(null, null, sampleStatusInfoForTokenFailure()),
                new ResponseEntity(tokenOperationModel.getHttpStatus())
        );
    }

    @Override
    public RemoteOperationModel<CustomerBalanceRequestModel, CustomerBalanceResponseModel> handleError(CustomerBalanceRequestModel requestModel, CustomerBalanceResponseModel responseModel, Exception exception) {
        return this.buildResponse(
                requestModel,
                customerBalanceErrorOrDefault(responseModel, exception, sampleStatusInfoGenericError()),
                tryGetResponseEntity(exception, responseModel, CustomerBalanceResponseModel.class)
        );
    }
}
