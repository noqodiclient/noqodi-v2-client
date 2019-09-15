
package com.noqodi.client.v2api.domain.models.payouts;

import javax.annotation.Generated;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.common.MerchantInfo;
import com.noqodi.client.v2api.domain.models.common.PaymentInfo;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "PayoutRequestModelBuilder", toBuilder = true)
@JsonDeserialize(builder = PayoutRequestModel.PayoutRequestModelBuilder.class)
@Getter
public class PayoutRequestModel extends AbstractRequestModel {

    private MerchantInfo merchantInfo;
    private PaymentInfo paymentInfo;
    private String requestSource;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PaymentAuthRequestModelBuilder {}
}
