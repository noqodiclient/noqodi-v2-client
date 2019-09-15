package com.noqodi.client.v2api.domain.helpers;

import com.noqodi.client.v2api.domain.factory.ObjectFactory;
import com.noqodi.client.v2api.domain.models.payments.PaymentAuthResponseModel;
import com.noqodi.client.v2api.domain.models.payments.PaymentCaptureResponseModel;
import com.noqodi.client.v2api.domain.models.payments.PaymentPreAuthResponseModel;
import com.noqodi.client.v2api.domain.models.payments.PaymentVoidAuthResponseModel;
import com.noqodi.client.v2api.domain.models.payouts.PayoutResponseModel;
import com.noqodi.client.v2api.domain.models.refunds.RefundResponseModel;
import com.noqodi.client.v2api.domain.validation.NoqodiValidationException;
import com.noqodi.client.v2api.domain.models.balances.CustomerBalanceResponseModel;
import com.noqodi.client.v2api.domain.models.balances.MerchantBalanceBeneficiariesResponseModel;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.base.AbstractResponseModel;
import com.noqodi.client.v2api.domain.models.base.RemoteOperationModel;
import com.noqodi.client.v2api.domain.models.common.ErrorCode;
import com.noqodi.client.v2api.domain.models.balances.MerchantBalanceResponseModel;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import com.noqodi.client.v2api.domain.models.token.AccessTokenResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.ConstraintViolation;
import java.util.HashMap;
import java.util.Set;

import static com.noqodi.client.v2api.domain.models.mappers.AccessTokenResponseModelMapper.toAccessTokenResponseModel;

public class ResponseHelper {
    public static AccessTokenResponseModel sampleAccessTokenResponseGenericError(Exception exception) {
        String code;
        String message = exception.getMessage();
        if (exception instanceof NoqodiValidationException) {
            code = "VALIDATION_ERROR";
            Set<ConstraintViolation<AbstractRequestModel>> violations = ((NoqodiValidationException) exception).getConstraintViolations();
            message += " >> violations: " +
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .reduce((x, y) -> x + ", " + y)
                            .orElse("");
        } else {
            code = "SOMETHING_WENT_WRONG";
        }
        StatusInfo statusInfo = getFailureStatusInfo(code, message);
        return sampleAccessTokenResponseGenericError(statusInfo);
    }

    private static AccessTokenResponseModel sampleAccessTokenResponseGenericError(StatusInfo statusInfo) {
        return toAccessTokenResponseModel(null, statusInfo);
    }

    public static StatusInfo sampleStatusInfoGenericError() {
        return getFailureStatusInfo("SOMETHING_WENT_WRONG", "Something went Wrong");
    }

    public static StatusInfo sampleStatusInfoForTokenFailure() {
        return getFailureStatusInfo("TOKEN_REQUEST_FAILURE", "Unable to get new access token");
    }

    public static CustomerBalanceResponseModel customerBalanceErrorOrDefault(CustomerBalanceResponseModel responseModel, Exception exception, StatusInfo statusInfo) {
        CustomerBalanceResponseModel response = ResponseHelper.responseErrorOrDefault(
                CustomerBalanceResponseModel.class,
                responseModel,
                exception,
                statusInfo
        );
        return response == null ? CustomerBalanceResponseModel.builder().statusInfo(statusInfo).build() : response;
    }

    public static MerchantBalanceResponseModel merchantBalanceErrorOrDefault(MerchantBalanceResponseModel responseModel, Exception exception, StatusInfo statusInfo) {
        MerchantBalanceResponseModel response = ResponseHelper.responseErrorOrDefault(
                MerchantBalanceResponseModel.class,
                responseModel,
                exception,
                statusInfo
        );
        return response == null ? MerchantBalanceResponseModel.builder().statusInfo(statusInfo).build() : response;
    }

    public static MerchantBalanceBeneficiariesResponseModel merchantBalanceBeneficiariesErrorOrDefault(MerchantBalanceBeneficiariesResponseModel responseModel, Exception exception, StatusInfo statusInfo) {
        MerchantBalanceBeneficiariesResponseModel response = ResponseHelper.responseErrorOrDefault(
                MerchantBalanceBeneficiariesResponseModel.class,
                responseModel,
                exception,
                statusInfo
        );
        return response == null ? MerchantBalanceBeneficiariesResponseModel.builder().statusInfo(statusInfo).build() : response;
    }

    public static PaymentPreAuthResponseModel paymentPreAuthErrorOrDefault(PaymentPreAuthResponseModel responseModel, Exception exception, StatusInfo statusInfo) {
        PaymentPreAuthResponseModel response = ResponseHelper.responseErrorOrDefault(
                PaymentPreAuthResponseModel.class,
                responseModel,
                exception,
                statusInfo
        );
        return response == null ? PaymentPreAuthResponseModel.builder().statusInfo(statusInfo).build() : response;
    }

    public static PaymentVoidAuthResponseModel paymentVoidAuthErrorOrDefault(PaymentVoidAuthResponseModel responseModel, Exception exception, StatusInfo statusInfo) {
        PaymentVoidAuthResponseModel response = ResponseHelper.responseErrorOrDefault(
                PaymentVoidAuthResponseModel.class,
                responseModel,
                exception,
                statusInfo
        );
        return response == null ? PaymentVoidAuthResponseModel.builder().statusInfo(statusInfo).build() : response;
    }

    public static PaymentCaptureResponseModel paymentCaptureErrorOrDefault(PaymentCaptureResponseModel responseModel, Exception exception, StatusInfo statusInfo) {
        PaymentCaptureResponseModel response = ResponseHelper.responseErrorOrDefault(
                PaymentCaptureResponseModel.class,
                responseModel,
                exception,
                statusInfo
        );
        return response == null ? PaymentCaptureResponseModel.builder().statusInfo(statusInfo).build() : response;
    }

    public static PaymentAuthResponseModel paymentCompleteAuthErrorOrDefault(PaymentAuthResponseModel responseModel, Exception exception, StatusInfo statusInfo) {
        PaymentAuthResponseModel response = ResponseHelper.responseErrorOrDefault(
                PaymentAuthResponseModel.class,
                responseModel,
                exception,
                statusInfo
        );
        return response == null ? PaymentAuthResponseModel.builder().statusInfo(statusInfo).build() : response;
    }

    public static RefundResponseModel refundErrorOrDefault(RefundResponseModel responseModel, Exception exception, StatusInfo statusInfo) {
        RefundResponseModel response = ResponseHelper.responseErrorOrDefault(
                RefundResponseModel.class,
                responseModel,
                exception,
                statusInfo
        );
        return response == null ? RefundResponseModel.builder().statusInfo(statusInfo).build() : response;
    }

    public static PayoutResponseModel payoutErrorOrDefault(PayoutResponseModel responseModel, Exception exception, StatusInfo statusInfo) {
        PayoutResponseModel response = ResponseHelper.responseErrorOrDefault(
                PayoutResponseModel.class,
                responseModel,
                exception,
                statusInfo
        );
        return response == null ? PayoutResponseModel.builder().statusInfo(statusInfo).build() : response;
    }

    public static <T> T responseErrorOrDefault(Class<T> clazz, T responseModel, Exception exception, StatusInfo statusInfo) {
        if (responseModel != null) return responseModel;
        T response = null;
        if (exception != null) {
            response = tryGetResponseBody(exception, clazz);
        }
        return response;
    }


    public static StatusInfo getFailureStatusInfo(String code, String message) {
        return new StatusInfo(new ErrorCode(code, message), "FAILURE");
    }

    public static StatusInfo getSuccessStatusInfo() {
        return new StatusInfo(null, "SUCCESS");
    }

    private static final Logger logger = LoggerFactory.getLogger(ResponseHelper.class);

    public static <T extends AbstractRequestModel, R extends AbstractResponseModel> boolean isSuccess(RemoteOperationModel<T, R> remoteOperationModel) {

        if (remoteOperationModel == null) return false;
        if (remoteOperationModel.getStatusInfo() == null) return false;
        if (remoteOperationModel.getStatusInfo().getStatus() == null) return false;
        return remoteOperationModel.getStatusInfo().getStatus().equalsIgnoreCase("SUCCESS");
    }

    public static <T> ResponseEntity<T> tryGetResponseEntity(Exception exception, T response, Class<T> clazz) {
        return getResponseEntity(exception, response, clazz);
    }

    private static <T> ResponseEntity<T> getResponseEntity(Exception exception, T response, Class<T> clazz) {
        ResponseEntity<T> responseEntity;
        T responseBody = response == null ? tryGetResponseBody(exception, clazz) : response;
        if (exception instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientErrorException = (HttpClientErrorException) exception;
            responseEntity = new ResponseEntity<T>(
                    responseBody,
                    httpClientErrorException.getResponseHeaders(),
                    httpClientErrorException.getStatusCode()
            );
        } else {
            responseEntity = new ResponseEntity<T>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    private static <T> T tryGetResponseBody(Exception exception, Class<T> clazz) {
        String responseString = null;
        if (exception instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientErrorException = (HttpClientErrorException) exception;
            responseString = httpClientErrorException.getResponseBodyAsString();
        }

        if (StringUtils.isEmpty(responseString)) return null;
        return ObjectFactory.getInstance().getGson().fromJson(responseString, clazz);
    }

    public static boolean isInvalidAccessTokenError(Exception exception) {
        if (exception instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientErrorException = (HttpClientErrorException) exception;
            String responseString = httpClientErrorException.getResponseBodyAsString();

            if (StringUtils.isEmpty(responseString)) return false;

            HashMap accessTokenResponse = ObjectFactory.getInstance().getGson().fromJson(responseString, HashMap.class);
            StatusInfo statusInfo = null;
            if (accessTokenResponse.get("statusInfo") != null) {
                statusInfo = ObjectFactory.getInstance().getGson().fromJson(
                        ObjectFactory.getInstance().getGson().toJson(accessTokenResponse.get("statusInfo")),
                        StatusInfo.class
                );
            }

            return statusInfo != null
                    && statusInfo.getErrorCode() != null
                    && statusInfo.getErrorCode().getCode().equalsIgnoreCase("INVALID_ACCESS_TOKEN");
        }

        return false;
    }


    public static boolean isPaymentAuthResponseSuccess(PaymentAuthResponseModel responseModel) {
        return responseModel != null
                && responseModel.getStatusInfo() != null
                && responseModel.getStatusInfo().getStatus() != null
                && responseModel.getStatusInfo().getStatus().equalsIgnoreCase("SUCCESS");
    }


    public static boolean isPaymentCaptureResponseSuccess(PaymentCaptureResponseModel responseModel) {
        return responseModel != null
                && responseModel.getStatusInfo() != null
                && responseModel.getStatusInfo().getStatus() != null
                && responseModel.getStatusInfo().getStatus().equalsIgnoreCase("SUCCESS");
    }
}
