
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

@Builder(builderClassName = "PaymentVoidAuthResponseModelBuilder", toBuilder = true)
@JsonDeserialize(builder = PaymentVoidAuthResponseModel.PaymentVoidAuthResponseModelBuilder.class)
@Getter
public class PaymentVoidAuthResponseModel extends AbstractResponseModel {

    private String id;
    private String serviceType;
    private CustomerInfo customerInfo;
    private MerchantInfo merchantInfo;
    private PaymentInfo paymentInfo;
    private StatusInfo statusInfo;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PaymentVoidAuthResponseModelBuilder {}
}
