package com.noqodi.client.v2api.domain.commands.api.balances;

import com.noqodi.client.v2api.domain.commands.api.AbstractApiCommand;
import com.noqodi.client.v2api.domain.commands.api.token.GetAccessTokenCCGCommandImpl;
import com.noqodi.client.v2api.domain.constants.Constants;
import com.noqodi.client.v2api.domain.helpers.Helper;
import com.noqodi.client.v2api.domain.helpers.RequestHelper;
import com.noqodi.client.v2api.domain.helpers.RestTemplateParameters;
import com.noqodi.client.v2api.domain.validation.DefaultValidationManager;
import com.noqodi.client.v2api.domain.validation.NoqodiValidationException;
import com.noqodi.client.v2api.domain.models.balances.MerchantBalanceBeneficiariesResponseModel;
import com.noqodi.client.v2api.domain.models.balances.MerchantBeneficiariesBalanceRequestModel;
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
import java.util.*;

import static com.noqodi.client.v2api.domain.helpers.RequestHelper.buildSampleAccessTokenPGRequest;
import static com.noqodi.client.v2api.domain.helpers.ResponseHelper.*;
import static com.noqodi.client.v2api.domain.helpers.RestTemplateHelper.executeHttpRequest;

@Service
public class GetMerchantBeniesBalanceCommandImpl extends AbstractApiCommand<MerchantBeneficiariesBalanceRequestModel, MerchantBalanceBeneficiariesResponseModel> {

    @Autowired
    private DefaultValidationManager validationManager;

    @Autowired
    public GetMerchantBeniesBalanceCommandImpl(GetAccessTokenCCGCommandImpl accessTokenBaseCommand) {
        super(accessTokenBaseCommand);
    }

    @Override
    public RemoteOperationModel<MerchantBeneficiariesBalanceRequestModel, MerchantBalanceBeneficiariesResponseModel> handle(MerchantBeneficiariesBalanceRequestModel requestModel, AccessTokenResponseModel accessTokenResponseModel) {

        String accessToken = Helper.extractAccessToken(accessTokenResponseModel);
        HttpHeaders requestHeaders = Helper.getRequestHeaders(accessToken);
        RestTemplateParameters<MultiValueMap<String, String>, MerchantBalanceBeneficiariesResponseModel> restTemplateParameters =
                RestTemplateParameters
                        .<MultiValueMap<String, String>, MerchantBalanceBeneficiariesResponseModel>builder()
                        .restTemplate(objectFactory.getRestTemplate())
                        .requestUrl(getRequestUrl(requestModel))
                        .httpEntity(Helper.createHeaderOnlyRequestEntity(requestHeaders))
                        .responseType(MerchantBalanceBeneficiariesResponseModel.class)
                        .httpMethod(HttpMethod.GET)
                        .build();
        ResponseEntity<MerchantBalanceBeneficiariesResponseModel> responseEntity = executeHttpRequest(restTemplateParameters);
        return this.buildResponse(requestModel, responseEntity.getBody(), responseEntity);
    }

    @Override
    public RemoteOperationModel<MerchantBeneficiariesBalanceRequestModel, MerchantBalanceBeneficiariesResponseModel> buildResponse(MerchantBeneficiariesBalanceRequestModel requestModel, MerchantBalanceBeneficiariesResponseModel responseModel, ResponseEntity responseEntity) {
        StatusInfo statusInfo = responseModel != null ? responseModel.getStatusInfo() : null;
        return RemoteOperationModel
                .<MerchantBeneficiariesBalanceRequestModel, MerchantBalanceBeneficiariesResponseModel>builder()
                .httpMethod(HttpMethod.GET)
                .httpStatus(responseEntity.getStatusCode())
                .requestUrl(getRequestUrl(requestModel))
                .request(requestModel)
                .response(responseModel)
                .statusInfo(statusInfo)
                .build();
    }

    private String getRequestUrl(MerchantBeneficiariesBalanceRequestModel requestModel) {
        return Helper.buildUrl(
                configuration.getBaseUrl(),
                null,
                configuration.getEndpoints().getBalancesUris().getMerchantBeneficiariesUri(),
                "merchant",
                requestModel.getMerchantCode(),
                "beneficiaries",
                requestModel.getBeneficiaries()
        );
    }

    @Override
    public void validate(MerchantBeneficiariesBalanceRequestModel requestModel) throws NoqodiValidationException {
        // implement your validation logic here
        Set<ConstraintViolation<AbstractRequestModel>> constraintViolations =
                validationManager
                        .getValidator()
                        .validate(requestModel);

        if (!constraintViolations.isEmpty())
            throw new NoqodiValidationException(Constants.VALIDATION_ERROR_MESSAGE, constraintViolations);
    }

    @Override
    public void preHandle(MerchantBeneficiariesBalanceRequestModel requestModel) {

    }

    @Override
    public void postHandle(RemoteOperationModel<MerchantBeneficiariesBalanceRequestModel, MerchantBalanceBeneficiariesResponseModel> remoteOperationModel) {

    }

    @Override
    public AbstractAccessTokenRequestModel createAccessTokenRequestModel() {
        return buildSampleAccessTokenPGRequest(configuration);
    }

    @Override
    public RemoteOperationModel<MerchantBeneficiariesBalanceRequestModel, MerchantBalanceBeneficiariesResponseModel> buildTokenErrorResponse(MerchantBeneficiariesBalanceRequestModel requestModel, RemoteOperationModel<AbstractAccessTokenRequestModel, AccessTokenResponseModel> tokenOperationModel) {
        return this.buildResponse(
                requestModel,
                merchantBalanceBeneficiariesErrorOrDefault(null, null, sampleStatusInfoForTokenFailure()),
                new ResponseEntity(tokenOperationModel.getHttpStatus())
        );
    }

    @Override
    public RemoteOperationModel<MerchantBeneficiariesBalanceRequestModel, MerchantBalanceBeneficiariesResponseModel> handleError(MerchantBeneficiariesBalanceRequestModel requestModel, MerchantBalanceBeneficiariesResponseModel responseModel, Exception exception) {
        return this.buildResponse(
                requestModel,
                merchantBalanceBeneficiariesErrorOrDefault(responseModel, exception, sampleStatusInfoGenericError()),
                tryGetResponseEntity(exception, responseModel, MerchantBalanceBeneficiariesResponseModel.class)
        );
    }
}
