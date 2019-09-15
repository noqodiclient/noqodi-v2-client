
package com.noqodi.client.v2api.domain.models.payments;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.noqodi.client.v2api.domain.models.base.AbstractResponseModel;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import com.noqodi.client.v2api.domain.models.common.MerchantInfo;
import com.noqodi.client.v2api.domain.models.common.PaymentInfo;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "PaymentPreAuthResponseModelBuilder", toBuilder = true)
@JsonDeserialize(builder = PaymentPreAuthResponseModel.PaymentPreAuthResponseModelBuilder.class)
@Getter
public class PaymentPreAuthResponseModel extends AbstractResponseModel {

    private String serviceType;
    private String serviceMode;
    private MerchantInfo merchantInfo;
    private PaymentInfo paymentInfo;
    private StatusInfo statusInfo;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PaymentPreAuthResponseModelBuilder {}

}
