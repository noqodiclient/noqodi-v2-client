package com.noqodi.client.v2api.domain.models.mappers;

import com.noqodi.client.v2api.domain.factory.ObjectFactory;
import com.noqodi.client.v2api.domain.models.common.CustomerInfo;
import com.noqodi.client.v2api.domain.models.common.MerchantInfo;
import com.noqodi.client.v2api.domain.models.common.PaymentInfo;
import com.noqodi.client.v2api.domain.models.common.PaymentMethod;
import com.noqodi.client.v2api.domain.models.payments.PaymentAuthResponseModel;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class PaymentModelMapper {

    public static PaymentAuthResponseModel toPaymentAuthResponseModel(String authResponse) {
        try {
            String result = java.net.URLDecoder.decode(authResponse, StandardCharsets.UTF_8.name());
            return ObjectFactory.getInstance().getGson().fromJson(result, PaymentAuthResponseModel.class);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static MerchantInfo toVoidAuthModelMerchantInfo(MerchantInfo merchantInfo) {
        return MerchantInfo.builder()
                .merchantCode(merchantInfo.getMerchantCode())
                .merchantOrderId(merchantInfo.getMerchantOrderId())
                .merchantRequestId(merchantInfo.getMerchantRequestId())
                .build();
    }

    public static PaymentInfo toVoidAuthModelPaymentInfo(PaymentInfo paymentInfo) {
        return PaymentInfo.builder()
                .noqodiOrderId(paymentInfo.getNoqodiOrderId())
                .build();
    }

}
