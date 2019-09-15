
package com.noqodi.client.v2api.domain.models.payments;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.noqodi.client.v2api.domain.models.base.AbstractResponseModel;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import com.noqodi.client.v2api.domain.models.common.CustomerInfo;
import com.noqodi.client.v2api.domain.models.common.MerchantInfo;
import com.noqodi.client.v2api.domain.models.common.PaymentInfo;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "PaymentAuthResponseModelBuilder", toBuilder = true)
@JsonDeserialize(builder = PaymentAuthResponseModel.PaymentAuthResponseModelBuilder.class)
@Getter
public class PaymentAuthResponseModel extends AbstractResponseModel {

    private String id;
    private String serviceType;
    private String serviceMode;
    private CustomerInfo customerInfo;
    private MerchantInfo merchantInfo;
    private PaymentInfo paymentInfo;
    private StatusInfo statusInfo;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PaymentAuthResponseModelBuilder {}
}
