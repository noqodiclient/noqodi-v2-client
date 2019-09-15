
package com.noqodi.client.v2api.domain.models.payments;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.common.MerchantInfo;
import com.noqodi.client.v2api.domain.models.common.PaymentInfo;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "PaymentVoidAuthRequestModelBuilder", toBuilder = true)
@JsonDeserialize(builder = PaymentVoidAuthRequestModel.PaymentVoidAuthRequestModelBuilder.class)
@Getter
public class PaymentVoidAuthRequestModel extends AbstractRequestModel {

    private String serviceType;
    private MerchantInfo merchantInfo;
    private PaymentInfo paymentInfo;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PaymentVoidAuthRequestModelBuilder {}

}
