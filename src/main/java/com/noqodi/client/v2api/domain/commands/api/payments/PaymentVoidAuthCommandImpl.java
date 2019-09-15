package com.noqodi.client.v2api.domain.commands.api.payments;

import com.noqodi.client.v2api.domain.cache.PaymentCommandCacheManager;
import com.noqodi.client.v2api.domain.commands.api.AbstractApiCommand;
import com.noqodi.client.v2api.domain.commands.api.token.GetAccessTokenCCGCommandImpl;
import com.noqodi.client.v2api.domain.constants.Constants;
import com.noqodi.client.v2api.domain.constants.ServiceType;
import com.noqodi.client.v2api.domain.helpers.*;
import com.noqodi.client.v2api.domain.models.base.CacheablePaymentImpl;
import com.noqodi.client.v2api.domain.validation.DefaultValidationManager;
import com.noqodi.client.v2api.domain.validation.NoqodiValidationException;
import com.noqodi.client.v2api.domain.helpers.ResponseHelper;
import com.noqodi.client.v2api.domain.helpers.RestTemplateParameters;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.base.RemoteOperationModel;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import com.noqodi.client.v2api.domain.models.payments.PaymentVoidAuthRequestModel;
import com.noqodi.client.v2api.domain.models.payments.PaymentVoidAuthResponseModel;
import com.noqodi.client.v2api.domain.models.token.AbstractAccessTokenRequestModel;
import com.noqodi.client.v2api.domain.models.token.AccessTokenResponseModel;
import com.noqodi.client.v2api.domain.models.token.PGAccessTokenRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static com.noqodi.client.v2api.domain.helpers.RequestHelper.buildSampleAccessTokenPGRequest;
import static com.noqodi.client.v2api.domain.models.mappers.CacheableMapper.toCacheablePayment;

@Service
public class PaymentVoidAuthCommandImpl extends AbstractApiCommand<PaymentVoidAuthRequestModel, PaymentVoidAuthResponseModel> {

    @Autowired
    private DefaultValidationManager validationManager;

    @Autowired
    private PaymentCommandCacheManager<RemoteOperationModel> paymentCommandCacheManager;

    @Autowired
    public PaymentVoidAuthCommandImpl(GetAccessTokenCCGCommandImpl accessTokenBaseCommand) {
        super(accessTokenBaseCommand);
    }

    @Override
    public RemoteOperationModel<PaymentVoidAuthRequestModel, PaymentVoidAuthResponseModel> handle(PaymentVoidAuthRequestModel requestModel, AccessTokenResponseModel accessTokenResponseModel) {

        String accessToken = Helper.extractAccessToken(accessTokenResponseModel);
        HttpHeaders requestHeaders = Helper.getRequestHeaders(accessToken);
        RestTemplateParameters restTemplateParameters =
                RestTemplateParameters
                        .<PaymentVoidAuthRequestModel, PaymentVoidAuthResponseModel>builder()
                        .restTemplate(objectFactory.getRestTemplate())
                        .requestUrl(getRequestUrl(requestModel))
                        .httpEntity(Helper.createHeaderAndPayloadRequestEntity(requestModel, requestHeaders))
                        .responseType(PaymentVoidAuthResponseModel.class)
                        .httpMethod(HttpMethod.POST)
                        .build();
        ResponseEntity<PaymentVoidAuthResponseModel> responseEntity = RestTemplateHelper.executeHttpRequest(restTemplateParameters);
        return this.buildResponse(requestModel, responseEntity.getBody(), responseEntity);
    }

    @Override
    public RemoteOperationModel<PaymentVoidAuthRequestModel, PaymentVoidAuthResponseModel> buildResponse(PaymentVoidAuthRequestModel requestModel, PaymentVoidAuthResponseModel responseModel, ResponseEntity responseEntity) {
        StatusInfo statusInfo = responseModel != null ? responseModel.getStatusInfo() : null;
        return RemoteOperationModel
                .<PaymentVoidAuthRequestModel, PaymentVoidAuthResponseModel>builder()
                .httpMethod(HttpMethod.POST)
                .httpStatus(responseEntity.getStatusCode())
                .requestUrl(getRequestUrl(requestModel))
                .request(requestModel)
                .response(responseModel)
                .statusInfo(statusInfo)
                .build();
    }

    private String getRequestUrl(PaymentVoidAuthRequestModel requestModel) {
        return Helper.buildUrl(
                configuration.getBaseUrl(),
                null,
                configuration.getEndpoints().getPaymentUris().getVoidAuthUri()
        );
    }

    @Override
    public void validate(PaymentVoidAuthRequestModel requestModel) throws NoqodiValidationException {
        Set<ConstraintViolation<AbstractRequestModel>> constraintViolations =
                validationManager
                        .getValidator()
                        .validate(requestModel);

        if (!constraintViolations.isEmpty())
            throw new NoqodiValidationException(Constants.VALIDATION_ERROR_MESSAGE, constraintViolations);
    }

    @Override
    public void preHandle(PaymentVoidAuthRequestModel requestModel) {

    }

    @Override
    public void postHandle(RemoteOperationModel<PaymentVoidAuthRequestModel, PaymentVoidAuthResponseModel> remoteOperationModel) {
        if (!ResponseHelper.isSuccess(remoteOperationModel)) return;
        CacheablePaymentImpl<RemoteOperationModel> cacheablePayment = toCacheablePayment(ServiceType.VOID_AUTH, remoteOperationModel.getResponse().getMerchantInfo().getMerchantOrderId(), remoteOperationModel);
        paymentCommandCacheManager.saveCacheable(cacheablePayment);
    }

    @Override
    public AbstractAccessTokenRequestModel createAccessTokenRequestModel() {
        return buildSampleAccessTokenPGRequest(configuration);
    }

    @Override
    public RemoteOperationModel<PaymentVoidAuthRequestModel, PaymentVoidAuthResponseModel> buildTokenErrorResponse(PaymentVoidAuthRequestModel requestModel, RemoteOperationModel<AbstractAccessTokenRequestModel, AccessTokenResponseModel> tokenOperationModel) {
        return this.buildResponse(
                requestModel,
                ResponseHelper.paymentVoidAuthErrorOrDefault(null, null, ResponseHelper.sampleStatusInfoForTokenFailure()),
                new ResponseEntity(tokenOperationModel.getHttpStatus())
        );
    }

    @Override
    public RemoteOperationModel<PaymentVoidAuthRequestModel, PaymentVoidAuthResponseModel> handleError(PaymentVoidAuthRequestModel requestModel, PaymentVoidAuthResponseModel responseModel, Exception exception) {
        return this.buildResponse(
                requestModel,
                ResponseHelper.paymentVoidAuthErrorOrDefault(responseModel, exception, ResponseHelper.sampleStatusInfoGenericError()),
                ResponseHelper.tryGetResponseEntity(exception, responseModel, PaymentVoidAuthResponseModel.class)
        );
    }
}
