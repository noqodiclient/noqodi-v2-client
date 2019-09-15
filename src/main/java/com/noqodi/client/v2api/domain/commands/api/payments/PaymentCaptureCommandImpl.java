package com.noqodi.client.v2api.domain.commands.api.payments;

import com.noqodi.client.v2api.domain.commands.api.AbstractApiCommand;
import com.noqodi.client.v2api.domain.helpers.Helper;
import com.noqodi.client.v2api.domain.models.base.CacheablePaymentImpl;
import com.noqodi.client.v2api.domain.cache.PaymentCommandCacheManager;
import com.noqodi.client.v2api.domain.commands.api.token.GetAccessTokenCCGCommandImpl;
import com.noqodi.client.v2api.domain.constants.Constants;
import com.noqodi.client.v2api.domain.constants.ServiceType;
import com.noqodi.client.v2api.domain.helpers.ResponseHelper;
import com.noqodi.client.v2api.domain.helpers.RestTemplateParameters;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.base.RemoteOperationModel;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import com.noqodi.client.v2api.domain.models.payments.PaymentCaptureRequestModel;
import com.noqodi.client.v2api.domain.models.payments.PaymentCaptureResponseModel;
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

import static com.noqodi.client.v2api.domain.helpers.Helper.*;
import static com.noqodi.client.v2api.domain.helpers.RequestHelper.buildSampleAccessTokenPGRequest;
import static com.noqodi.client.v2api.domain.helpers.ResponseHelper.*;
import static com.noqodi.client.v2api.domain.helpers.RestTemplateHelper.executeHttpRequest;
import static com.noqodi.client.v2api.domain.models.mappers.CacheableMapper.toCacheablePayment;

@Service
public class PaymentCaptureCommandImpl extends AbstractApiCommand<PaymentCaptureRequestModel, PaymentCaptureResponseModel> {

    @Autowired
    private DefaultValidationManager validationManager;

    @Autowired
    private PaymentCommandCacheManager<RemoteOperationModel> paymentCommandCacheManager;

    @Autowired
    public PaymentCaptureCommandImpl(GetAccessTokenCCGCommandImpl accessTokenBaseCommand) {
        super(accessTokenBaseCommand);
    }

    @Override
    public RemoteOperationModel<PaymentCaptureRequestModel, PaymentCaptureResponseModel> handle(PaymentCaptureRequestModel requestModel, AccessTokenResponseModel accessTokenResponseModel) {

        String accessToken = Helper.extractAccessToken(accessTokenResponseModel);
        HttpHeaders requestHeaders = Helper.getRequestHeaders(accessToken);
        RestTemplateParameters<PaymentCaptureRequestModel, PaymentCaptureResponseModel> restTemplateParameters =
                RestTemplateParameters
                        .<PaymentCaptureRequestModel, PaymentCaptureResponseModel>builder()
                        .restTemplate(objectFactory.getRestTemplate())
                        .requestUrl(getRequestUrl(requestModel))
                        .httpEntity(Helper.createHeaderAndPayloadRequestEntity(requestModel, requestHeaders))
                        .responseType(PaymentCaptureResponseModel.class)
                        .httpMethod(HttpMethod.POST)
                        .build();
        ResponseEntity<PaymentCaptureResponseModel> responseEntity = executeHttpRequest(restTemplateParameters);
        return this.buildResponse(requestModel, responseEntity.getBody(), responseEntity);
    }

    @Override
    public RemoteOperationModel<PaymentCaptureRequestModel, PaymentCaptureResponseModel> buildResponse(PaymentCaptureRequestModel requestModel, PaymentCaptureResponseModel responseModel, ResponseEntity responseEntity) {
        StatusInfo statusInfo = responseModel != null ? responseModel.getStatusInfo() : null;
        return RemoteOperationModel
                .<PaymentCaptureRequestModel, PaymentCaptureResponseModel>builder()
                .httpMethod(HttpMethod.POST)
                .httpStatus(responseEntity.getStatusCode())
                .requestUrl(getRequestUrl(requestModel))
                .request(requestModel)
                .response(responseModel)
                .statusInfo(statusInfo)
                .build();
    }

    private String getRequestUrl(PaymentCaptureRequestModel requestModel) {
        return buildUrl(
                configuration.getBaseUrl(),
                null,
                configuration.getEndpoints().getPaymentUris().getCaptureUri()
        );
    }

    @Override
    public void validate(PaymentCaptureRequestModel requestModel) throws NoqodiValidationException {
        // implement your validation logic here
        Set<ConstraintViolation<AbstractRequestModel>> constraintViolations =
                validationManager
                        .getValidator()
                        .validate(requestModel);

        if (!constraintViolations.isEmpty())
            throw new NoqodiValidationException(Constants.VALIDATION_ERROR_MESSAGE, constraintViolations);
    }

    @Override
    public void preHandle(PaymentCaptureRequestModel requestModel) {

    }

    @Override
    public void postHandle(RemoteOperationModel<PaymentCaptureRequestModel, PaymentCaptureResponseModel> remoteOperationModel) {
        if (!ResponseHelper.isSuccess(remoteOperationModel)) return;
        CacheablePaymentImpl<RemoteOperationModel> cacheablePayment = toCacheablePayment(ServiceType.CAPTURE, remoteOperationModel.getResponse().getMerchantInfo().getMerchantOrderId(), remoteOperationModel);
        paymentCommandCacheManager.saveCacheable(cacheablePayment);
    }

    @Override
    public AbstractAccessTokenRequestModel createAccessTokenRequestModel() {
        return buildSampleAccessTokenPGRequest(configuration);
    }

    @Override
    public RemoteOperationModel<PaymentCaptureRequestModel, PaymentCaptureResponseModel> buildTokenErrorResponse(PaymentCaptureRequestModel requestModel, RemoteOperationModel<AbstractAccessTokenRequestModel, AccessTokenResponseModel> tokenOperationModel) {
        return this.buildResponse(
                requestModel,
                paymentCaptureErrorOrDefault(null, null, sampleStatusInfoForTokenFailure()),
                new ResponseEntity(tokenOperationModel.getHttpStatus())
        );
    }

    @Override
    public RemoteOperationModel<PaymentCaptureRequestModel, PaymentCaptureResponseModel> handleError(PaymentCaptureRequestModel requestModel, PaymentCaptureResponseModel responseModel, Exception exception) {
        return this.buildResponse(
                requestModel,
                paymentCaptureErrorOrDefault(responseModel, exception, sampleStatusInfoGenericError()),
                tryGetResponseEntity(exception, responseModel, PaymentCaptureResponseModel.class)
        );
    }
}
