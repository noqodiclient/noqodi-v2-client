package com.noqodi.client.v2api.domain.commands.api.payments;

import com.noqodi.client.v2api.domain.cache.PaymentCommandCacheManager;
import com.noqodi.client.v2api.domain.commands.api.AbstractApiCommand;
import com.noqodi.client.v2api.domain.commands.api.token.GetAccessTokenCCGCommandImpl;
import com.noqodi.client.v2api.domain.constants.Constants;
import com.noqodi.client.v2api.domain.constants.ServiceType;
import com.noqodi.client.v2api.domain.helpers.Helper;
import com.noqodi.client.v2api.domain.helpers.RequestHelper;
import com.noqodi.client.v2api.domain.helpers.RestTemplateParameters;
import com.noqodi.client.v2api.domain.models.base.CacheablePaymentImpl;
import com.noqodi.client.v2api.domain.models.payments.PaymentPreAuthRequestModel;
import com.noqodi.client.v2api.domain.models.payments.PaymentPreAuthResponseModel;
import com.noqodi.client.v2api.domain.validation.DefaultValidationManager;
import com.noqodi.client.v2api.domain.validation.NoqodiValidationException;
import com.noqodi.client.v2api.domain.helpers.ResponseHelper;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.base.RemoteOperationModel;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import com.noqodi.client.v2api.domain.models.token.AbstractAccessTokenRequestModel;
import com.noqodi.client.v2api.domain.models.token.AccessTokenResponseModel;
import com.noqodi.client.v2api.domain.models.token.PGAccessTokenRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.function.Consumer;

import static com.noqodi.client.v2api.domain.helpers.RequestHelper.buildSampleAccessTokenPGRequest;
import static com.noqodi.client.v2api.domain.helpers.ResponseHelper.*;
import static com.noqodi.client.v2api.domain.helpers.RestTemplateHelper.executeHttpRequest;
import static com.noqodi.client.v2api.domain.models.mappers.CacheableMapper.toCacheablePayment;

@Service
public class PaymentPreAuthCommandImpl extends AbstractApiCommand<PaymentPreAuthRequestModel, PaymentPreAuthResponseModel> {

    @Autowired
    private DefaultValidationManager validationManager;

    @Autowired
    private PaymentCommandCacheManager<RemoteOperationModel> paymentCommandCacheManager;

    private Consumer<String> postHandleConsumer;

    @Autowired
    public PaymentPreAuthCommandImpl(GetAccessTokenCCGCommandImpl accessTokenBaseCommand) {
        super(accessTokenBaseCommand);
    }

    @Override
    public RemoteOperationModel<PaymentPreAuthRequestModel, PaymentPreAuthResponseModel> handle(PaymentPreAuthRequestModel requestModel, AccessTokenResponseModel accessTokenResponseModel) {

        String accessToken = Helper.extractAccessToken(accessTokenResponseModel);
        HttpHeaders requestHeaders = Helper.getRequestHeaders(accessToken);
        RestTemplateParameters<PaymentPreAuthRequestModel, PaymentPreAuthResponseModel> restTemplateParameters =
                RestTemplateParameters
                        .<PaymentPreAuthRequestModel, PaymentPreAuthResponseModel>builder()
                        .restTemplate(objectFactory.getRestTemplate())
                        .requestUrl(getRequestUrl(requestModel))
                        .httpEntity(Helper.createHeaderAndPayloadRequestEntity(requestModel, requestHeaders))
                        .responseType(PaymentPreAuthResponseModel.class)
                        .httpMethod(HttpMethod.POST)
                        .build();
        ResponseEntity<PaymentPreAuthResponseModel> responseEntity = executeHttpRequest(restTemplateParameters);
        return this.buildResponse(requestModel, responseEntity.getBody(), responseEntity);
    }

    @Override
    public RemoteOperationModel<PaymentPreAuthRequestModel, PaymentPreAuthResponseModel> buildResponse(PaymentPreAuthRequestModel requestModel, PaymentPreAuthResponseModel responseModel, ResponseEntity responseEntity) {
        StatusInfo statusInfo = responseModel != null ? responseModel.getStatusInfo() : null;
        return RemoteOperationModel
                .<PaymentPreAuthRequestModel, PaymentPreAuthResponseModel>builder()
                .httpMethod(HttpMethod.POST)
                .httpStatus(responseEntity.getStatusCode())
                .requestUrl(getRequestUrl(requestModel))
                .request(requestModel)
                .response(responseModel)
                .statusInfo(statusInfo)
                .build();
    }

    private String getRequestUrl(PaymentPreAuthRequestModel requestModel) {
        return Helper.buildUrl(
                configuration.getBaseUrl(),
                null,
                configuration.getEndpoints().getPaymentUris().getPreAuthUri()
        );
    }

    @Override
    public void validate(PaymentPreAuthRequestModel requestModel) throws NoqodiValidationException {
        Set<ConstraintViolation<AbstractRequestModel>> constraintViolations =
                validationManager
                        .getValidator()
                        .validate(requestModel);

        if (!constraintViolations.isEmpty())
            throw new NoqodiValidationException(Constants.VALIDATION_ERROR_MESSAGE, constraintViolations);
    }

    @Override
    public void preHandle(PaymentPreAuthRequestModel requestModel) {

    }

    @Override
    public void postHandle(RemoteOperationModel<PaymentPreAuthRequestModel, PaymentPreAuthResponseModel> remoteOperationModel) {
        if (!ResponseHelper.isSuccess(remoteOperationModel)) return;
        CacheablePaymentImpl<RemoteOperationModel> cacheablePayment = toCacheablePayment(ServiceType.PRE_AUTH, remoteOperationModel.getResponse().getMerchantInfo().getMerchantOrderId(), remoteOperationModel);
        paymentCommandCacheManager.saveCacheable(cacheablePayment);
    }

    @Override
    public AbstractAccessTokenRequestModel createAccessTokenRequestModel() {
        return buildSampleAccessTokenPGRequest(configuration);
    }

    @Override
    public RemoteOperationModel<PaymentPreAuthRequestModel, PaymentPreAuthResponseModel> buildTokenErrorResponse(PaymentPreAuthRequestModel requestModel, RemoteOperationModel<AbstractAccessTokenRequestModel, AccessTokenResponseModel> tokenOperationModel) {
        return this.buildResponse(
                requestModel,
                paymentPreAuthErrorOrDefault(null, null, sampleStatusInfoForTokenFailure()),
                new ResponseEntity(tokenOperationModel.getHttpStatus())
        );
    }

    @Override
    public RemoteOperationModel<PaymentPreAuthRequestModel, PaymentPreAuthResponseModel> handleError(PaymentPreAuthRequestModel requestModel, PaymentPreAuthResponseModel responseModel, Exception exception) {
        return this.buildResponse(
                requestModel,
                paymentPreAuthErrorOrDefault(responseModel, exception, sampleStatusInfoGenericError()),
                tryGetResponseEntity(exception, responseModel, PaymentPreAuthResponseModel.class)
        );
    }
}
