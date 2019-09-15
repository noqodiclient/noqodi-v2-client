package com.noqodi.client.v2api.domain.commands.api.payouts;

import com.noqodi.client.v2api.domain.cache.PaymentCommandCacheManager;
import com.noqodi.client.v2api.domain.commands.api.AbstractApiCommand;
import com.noqodi.client.v2api.domain.commands.api.token.GetAccessTokenCCGCommandImpl;
import com.noqodi.client.v2api.domain.constants.Constants;
import com.noqodi.client.v2api.domain.constants.ServiceType;
import com.noqodi.client.v2api.domain.helpers.Helper;
import com.noqodi.client.v2api.domain.helpers.RequestHelper;
import com.noqodi.client.v2api.domain.helpers.ResponseHelper;
import com.noqodi.client.v2api.domain.helpers.RestTemplateParameters;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.base.CacheablePaymentImpl;
import com.noqodi.client.v2api.domain.models.base.RemoteOperationModel;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import com.noqodi.client.v2api.domain.models.payouts.GetPayoutDetailsRequestModel;
import com.noqodi.client.v2api.domain.models.payouts.PayoutResponseModel;
import com.noqodi.client.v2api.domain.models.token.AbstractAccessTokenRequestModel;
import com.noqodi.client.v2api.domain.models.token.AccessTokenResponseModel;
import com.noqodi.client.v2api.domain.models.token.PGAccessTokenRequestModel;
import com.noqodi.client.v2api.domain.validation.DefaultValidationManager;
import com.noqodi.client.v2api.domain.validation.NoqodiValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.function.Consumer;

import static com.noqodi.client.v2api.domain.helpers.RequestHelper.buildSampleAccessTokenPGRequest;
import static com.noqodi.client.v2api.domain.helpers.ResponseHelper.*;
import static com.noqodi.client.v2api.domain.helpers.RestTemplateHelper.executeHttpRequest;
import static com.noqodi.client.v2api.domain.models.mappers.CacheableMapper.toCacheablePayment;

@Service
public class GetPayoutDetailsCommandImpl extends AbstractApiCommand<GetPayoutDetailsRequestModel, PayoutResponseModel> {

    @Autowired
    private DefaultValidationManager validationManager;

    @Autowired
    private PaymentCommandCacheManager<RemoteOperationModel> paymentCommandCacheManager;

    private Consumer<String> postHandleConsumer;

    @Autowired
    public GetPayoutDetailsCommandImpl(GetAccessTokenCCGCommandImpl accessTokenBaseCommand) {
        super(accessTokenBaseCommand);
    }

    @Override
    public RemoteOperationModel<GetPayoutDetailsRequestModel, PayoutResponseModel> handle(GetPayoutDetailsRequestModel requestModel, AccessTokenResponseModel accessTokenResponseModel) {

        String accessToken = Helper.extractAccessToken(accessTokenResponseModel);
        HttpHeaders requestHeaders = Helper.getRequestHeaders(accessToken);
        RestTemplateParameters restTemplateParameters =
                RestTemplateParameters
                        .<GetPayoutDetailsRequestModel, PayoutResponseModel>builder()
                        .restTemplate(objectFactory.getRestTemplate())
                        .requestUrl(getRequestUrl(requestModel))
                        .httpEntity(Helper.createHeaderAndPayloadRequestEntity(requestModel, requestHeaders))
                        .responseType(PayoutResponseModel.class)
                        .httpMethod(HttpMethod.GET)
                        .build();
        ResponseEntity<PayoutResponseModel> responseEntity = executeHttpRequest(restTemplateParameters);
        return this.buildResponse(requestModel, responseEntity.getBody(), responseEntity);
    }

    @Override
    public RemoteOperationModel<GetPayoutDetailsRequestModel, PayoutResponseModel> buildResponse(GetPayoutDetailsRequestModel requestModel, PayoutResponseModel responseModel, ResponseEntity responseEntity) {
        StatusInfo statusInfo = responseModel != null ? responseModel.getStatusInfo() : null;
        return RemoteOperationModel
                .<GetPayoutDetailsRequestModel, PayoutResponseModel>builder()
                .httpMethod(HttpMethod.GET)
                .httpStatus(responseEntity.getStatusCode())
                .requestUrl(getRequestUrl(requestModel))
                .request(requestModel)
                .response(responseModel)
                .statusInfo(statusInfo)
                .build();
    }

    private String getRequestUrl(GetPayoutDetailsRequestModel requestModel) {
        return Helper.buildUrl(
                configuration.getBaseUrl(),
                null,
                configuration.getEndpoints().getPayoutsUris().getPayoutsUri(),
                "merchant",
                requestModel.getMerchantCode(),
                "batchId",
                requestModel.getMerchantOrderId()
        );
    }

    @Override
    public void validate(GetPayoutDetailsRequestModel requestModel) throws NoqodiValidationException {
        Set<ConstraintViolation<AbstractRequestModel>> constraintViolations =
                validationManager
                        .getValidator()
                        .validate(requestModel);

        if (!constraintViolations.isEmpty())
            throw new NoqodiValidationException(Constants.VALIDATION_ERROR_MESSAGE, constraintViolations);
    }

    @Override
    public void preHandle(GetPayoutDetailsRequestModel requestModel) {

    }

    @Override
    public void postHandle(RemoteOperationModel<GetPayoutDetailsRequestModel, PayoutResponseModel> remoteOperationModel) {
        if (!ResponseHelper.isSuccess(remoteOperationModel)) return;
        CacheablePaymentImpl<RemoteOperationModel> cacheablePayment = toCacheablePayment(ServiceType.PRE_AUTH, remoteOperationModel.getResponse().getMerchantInfo().getMerchantOrderId(), remoteOperationModel);
        paymentCommandCacheManager.saveCacheable(cacheablePayment);
    }

    @Override
    public AbstractAccessTokenRequestModel createAccessTokenRequestModel() {
        return buildSampleAccessTokenPGRequest(configuration);
    }

    @Override
    public RemoteOperationModel<GetPayoutDetailsRequestModel, PayoutResponseModel> buildTokenErrorResponse(GetPayoutDetailsRequestModel requestModel, RemoteOperationModel<AbstractAccessTokenRequestModel, AccessTokenResponseModel> tokenOperationModel) {
        return this.buildResponse(
                requestModel,
                payoutErrorOrDefault(null, null, sampleStatusInfoForTokenFailure()),
                new ResponseEntity(tokenOperationModel.getHttpStatus())
        );
    }

    @Override
    public RemoteOperationModel<GetPayoutDetailsRequestModel, PayoutResponseModel> handleError(GetPayoutDetailsRequestModel requestModel, PayoutResponseModel responseModel, Exception exception) {
        return this.buildResponse(
                requestModel,
                payoutErrorOrDefault(responseModel, exception, sampleStatusInfoGenericError()),
                tryGetResponseEntity(exception, responseModel, PayoutResponseModel.class)
        );
    }
}
