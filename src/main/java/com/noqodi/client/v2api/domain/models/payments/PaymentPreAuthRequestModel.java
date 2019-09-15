package com.noqodi.client.v2api.domain.models.payments;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.common.MerchantInfo;
import com.noqodi.client.v2api.domain.models.common.PaymentInfo;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "PaymentPreAuthRequestModelBuilder", toBuilder = true)
@JsonDeserialize(builder = PaymentPreAuthRequestModel.PaymentPreAuthRequestModelBuilder.class)
@Getter
public class PaymentPreAuthRequestModel extends AbstractRequestModel {

    private String serviceType;
    private String serviceMode;
    private MerchantInfo merchantInfo;
    private PaymentInfo paymentInfo;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PaymentPreAuthRequestModelBuilder {
    }
}