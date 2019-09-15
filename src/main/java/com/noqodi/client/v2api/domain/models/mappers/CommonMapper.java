package com.noqodi.client.v2api.domain.models.mappers;

import com.noqodi.client.v2api.domain.models.common.CustomerInfo;
import com.noqodi.client.v2api.domain.models.common.MerchantInfo;
import com.noqodi.client.v2api.domain.models.common.PaymentInfo;
import com.noqodi.client.v2api.domain.models.common.PaymentMethod;

public class CommonMapper {

    public static CustomerInfo toRefundModelCustomerInfo(CustomerInfo customerInfo) {
        return CustomerInfo.builder()
                .paymentMethod(new PaymentMethod(customerInfo.getPaymentMethod().getType()))
                .build();
    }

    public static MerchantInfo toRefundModelMerchantInfo(MerchantInfo merchantInfo) {
        return MerchantInfo.builder()
                .merchantCode(merchantInfo.getMerchantCode())
                .merchantOrderId(merchantInfo.getMerchantOrderId())
                .merchantRequestId(merchantInfo.getMerchantRequestId())
                .build();
    }

    public static PaymentInfo toRefundModelPaymentInfo(PaymentInfo paymentInfo) {
        return PaymentInfo.builder()
                .amount(paymentInfo.getAmount())
                .noqodiOrderId(paymentInfo.getNoqodiOrderId())
                .transactions(paymentInfo.getTransactions())
                .build();
    }
}
