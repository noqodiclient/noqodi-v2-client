package com.noqodi.client.v2api.domain.commands.api.refunds;

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
import com.noqodi.client.v2api.domain.models.refunds.RefundRequestModel;
import com.noqodi.client.v2api.domain.models.refunds.RefundResponseModel;
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
public class RefundCommandImpl extends AbstractApiCommand<RefundRequestModel, RefundResponseModel> {

    @Autowired
    private DefaultValidationManager validationManager;

    @Autowired
    private PaymentCommandCacheManager<RemoteOperationModel> paymentCommandCacheManager;

    private Consumer<String> postHandleConsumer;

    @Autowired
    public RefundCommandImpl(GetAccessTokenCCGCommandImpl accessTokenBaseCommand) {
        super(accessTokenBaseCommand);
    }

    @Override
    public RemoteOperationModel<RefundRequestModel, RefundResponseModel> handle(RefundRequestModel requestModel, AccessTokenResponseModel accessTokenResponseModel) {

        String accessToken = Helper.extractAccessToken(accessTokenResponseModel);
        HttpHeaders requestHeaders = Helper.getRequestHeaders(accessToken);
        RestTemplateParameters<RefundRequestModel, RefundResponseModel> restTemplateParameters =
                RestTemplateParameters
                        .<RefundRequestModel, RefundResponseModel>builder()
                        .restTemplate(objectFactory.getRestTemplate())
                        .requestUrl(getRequestUrl(requestModel))
                        .httpEntity(Helper.createHeaderAndPayloadRequestEntity(requestModel, requestHeaders))
                        .responseType(RefundResponseModel.class)
                        .httpMethod(HttpMethod.POST)
                        .build();
        ResponseEntity<RefundResponseModel> responseEntity = executeHttpRequest(restTemplateParameters);
        return this.buildResponse(requestModel, responseEntity.getBody(), responseEntity);
    }

    @Override
    public RemoteOperationModel<RefundRequestModel, RefundResponseModel> buildResponse(RefundRequestModel requestModel, RefundResponseModel responseModel, ResponseEntity responseEntity) {
        StatusInfo statusInfo = responseModel != null ? responseModel.getStatusInfo() : null;
        return RemoteOperationModel
                .<RefundRequestModel, RefundResponseModel>builder()
                .httpMethod(HttpMethod.POST)
                .httpStatus(responseEntity.getStatusCode())
                .requestUrl(getRequestUrl(requestModel))
                .request(requestModel)
                .response(responseModel)
                .statusInfo(statusInfo)
                .build();
    }

    private String getRequestUrl(RefundRequestModel requestModel) {
        return Helper.buildUrl(
                configuration.getBaseUrl(),
                null,
                configuration.getEndpoints().getRefundsUris().getRefundsUri()
        );
    }

    @Override
    public void validate(RefundRequestModel requestModel) throws NoqodiValidationException {
        Set<ConstraintViolation<AbstractRequestModel>> constraintViolations =
                validationManager
                        .getValidator()
                        .validate(requestModel);

        if (!constraintViolations.isEmpty())
            throw new NoqodiValidationException(Constants.VALIDATION_ERROR_MESSAGE, constraintViolations);
    }

    @Override
    public void preHandle(RefundRequestModel requestModel) {

    }

    @Override
    public void postHandle(RemoteOperationModel<RefundRequestModel, RefundResponseModel> remoteOperationModel) {
        if (!ResponseHelper.isSuccess(remoteOperationModel)) return;
        CacheablePaymentImpl<RemoteOperationModel> cacheablePayment = toCacheablePayment(ServiceType.PRE_AUTH, remoteOperationModel.getResponse().getMerchantInfo().getMerchantOrderId(), remoteOperationModel);
        paymentCommandCacheManager.saveCacheable(cacheablePayment);
    }

    @Override
    public AbstractAccessTokenRequestModel createAccessTokenRequestModel() {
        return buildSampleAccessTokenPGRequest(configuration);
    }

    @Override
    public RemoteOperationModel<RefundRequestModel, RefundResponseModel> buildTokenErrorResponse(RefundRequestModel requestModel, RemoteOperationModel<AbstractAccessTokenRequestModel, AccessTokenResponseModel> tokenOperationModel) {
        return this.buildResponse(
                requestModel,
                refundErrorOrDefault(null, null, sampleStatusInfoForTokenFailure()),
                new ResponseEntity(tokenOperationModel.getHttpStatus())
        );
    }

    @Override
    public RemoteOperationModel<RefundRequestModel, RefundResponseModel> handleError(RefundRequestModel requestModel, RefundResponseModel responseModel, Exception exception) {
        return this.buildResponse(
                requestModel,
                refundErrorOrDefault(responseModel, exception, sampleStatusInfoGenericError()),
                tryGetResponseEntity(exception, responseModel, RefundResponseModel.class)
        );
    }
}
