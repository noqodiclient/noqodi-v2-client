package com.noqodi.client.v2api.domain.commands.api.payments;

import com.noqodi.client.v2api.domain.cache.PaymentCommandCacheManager;
import com.noqodi.client.v2api.domain.commands.api.AbstractApiCommand;
import com.noqodi.client.v2api.domain.commands.api.token.GetAccessTokenCCGCommandImpl;
import com.noqodi.client.v2api.domain.constants.Constants;
import com.noqodi.client.v2api.domain.constants.ServiceType;
import com.noqodi.client.v2api.domain.helpers.ResponseHelper;
import com.noqodi.client.v2api.domain.helpers.RestTemplateParameters;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.base.CacheablePaymentImpl;
import com.noqodi.client.v2api.domain.models.base.RemoteOperationModel;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import com.noqodi.client.v2api.domain.models.payments.PaymentAuthRequestModel;
import com.noqodi.client.v2api.domain.models.payments.PaymentAuthResponseModel;
import com.noqodi.client.v2api.domain.models.token.AbstractAccessTokenRequestModel;
import com.noqodi.client.v2api.domain.models.token.AccessTokenResponseModel;
import com.noqodi.client.v2api.domain.models.token.PGAccessTokenRequestModel;
import com.noqodi.client.v2api.domain.validation.DefaultValidationManager;
import com.noqodi.client.v2api.domain.validation.NoqodiValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class PaymentCompleteAuthCommandImpl extends AbstractApiCommand<PaymentAuthRequestModel, PaymentAuthResponseModel> {

    @Autowired
    private DefaultValidationManager validationManager;

    @Autowired
    private PaymentCommandCacheManager<RemoteOperationModel> paymentCommandCacheManager;

    @Autowired
    public PaymentCompleteAuthCommandImpl(GetAccessTokenCCGCommandImpl accessTokenBaseCommand) {
        super(accessTokenBaseCommand);
    }

    @Override
    public RemoteOperationModel<PaymentAuthRequestModel, PaymentAuthResponseModel> handle(PaymentAuthRequestModel requestModel, AccessTokenResponseModel accessTokenResponseModel) {

        String accessToken = extractAccessToken(accessTokenResponseModel);
        HttpHeaders requestHeaders = getRequestHeaders(accessToken);
        RestTemplateParameters<PaymentAuthRequestModel, PaymentAuthResponseModel> restTemplateParameters =
                RestTemplateParameters
                        .<PaymentAuthRequestModel, PaymentAuthResponseModel>builder()
                        .restTemplate(objectFactory.getRestTemplate())
                        .requestUrl(getRequestUrl(requestModel))
                        .httpEntity(createHeaderAndPayloadRequestEntity(requestModel, requestHeaders))
                        .responseType(PaymentAuthResponseModel.class)
                        .httpMethod(HttpMethod.POST)
                        .build();
        ResponseEntity<PaymentAuthResponseModel> responseEntity = executeHttpRequest(restTemplateParameters);
        return this.buildResponse(requestModel, responseEntity.getBody(), responseEntity);
    }

    @Override
    public RemoteOperationModel<PaymentAuthRequestModel, PaymentAuthResponseModel> buildResponse(PaymentAuthRequestModel requestModel, PaymentAuthResponseModel responseModel, ResponseEntity responseEntity) {
        StatusInfo statusInfo = responseModel != null ? responseModel.getStatusInfo() : null;
        return RemoteOperationModel
                .<PaymentAuthRequestModel, PaymentAuthResponseModel>builder()
                .httpMethod(HttpMethod.POST)
                .httpStatus(responseEntity.getStatusCode())
                .requestUrl(getRequestUrl(requestModel))
                .request(requestModel)
                .response(responseModel)
                .statusInfo(statusInfo)
                .build();
    }

    private String getRequestUrl(PaymentAuthRequestModel requestModel) {
        return buildUrl(
                configuration.getBaseUrl(),
                null,
                configuration.getEndpoints().getPaymentUris().getCompleteUri()
        );
    }

    @Override
    public void validate(PaymentAuthRequestModel requestModel) throws NoqodiValidationException {
        Set<ConstraintViolation<AbstractRequestModel>> constraintViolations =
                validationManager
                        .getValidator()
                        .validate(requestModel);

        if (!constraintViolations.isEmpty())
            throw new NoqodiValidationException(Constants.VALIDATION_ERROR_MESSAGE, constraintViolations);
    }

    @Override
    public void preHandle(PaymentAuthRequestModel requestModel) {

    }

    @Override
    public void postHandle(RemoteOperationModel<PaymentAuthRequestModel, PaymentAuthResponseModel> remoteOperationModel) {
        if (!ResponseHelper.isSuccess(remoteOperationModel)) return;
        CacheablePaymentImpl<RemoteOperationModel> cacheablePayment = toCacheablePayment(ServiceType.AUTH, remoteOperationModel.getResponse().getMerchantInfo().getMerchantOrderId(), remoteOperationModel);
        paymentCommandCacheManager.saveCacheable(cacheablePayment);
    }

    @Override
    public AbstractAccessTokenRequestModel createAccessTokenRequestModel() {
        return buildSampleAccessTokenPGRequest(configuration);
    }

    @Override
    public RemoteOperationModel<PaymentAuthRequestModel, PaymentAuthResponseModel> buildTokenErrorResponse(PaymentAuthRequestModel requestModel, RemoteOperationModel<AbstractAccessTokenRequestModel, AccessTokenResponseModel> tokenOperationModel) {
        return this.buildResponse(
                requestModel,
                paymentCompleteAuthErrorOrDefault(null, null, sampleStatusInfoForTokenFailure()),
                new ResponseEntity(tokenOperationModel.getHttpStatus())
        );
    }


    @Override
    public RemoteOperationModel<PaymentAuthRequestModel, PaymentAuthResponseModel> handleError(PaymentAuthRequestModel requestModel, PaymentAuthResponseModel responseModel, Exception exception) {
        return this.buildResponse(
                requestModel,
                paymentCompleteAuthErrorOrDefault(responseModel, exception, sampleStatusInfoGenericError()),
                tryGetResponseEntity(exception, responseModel, PaymentAuthResponseModel.class)
        );
    }
}
