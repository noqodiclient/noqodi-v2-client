
package com.noqodi.client.v2api.domain.models.payments;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.common.MerchantInfo;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "PaymentAuthRequestModelBuilder", toBuilder = true)
@JsonDeserialize(builder = PaymentAuthRequestModel.PaymentAuthRequestModelBuilder.class)
@Getter
public class PaymentAuthRequestModel extends AbstractRequestModel {

    private String serviceType;
    private MerchantInfo merchantInfo;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PaymentAuthRequestModelBuilder {}
}
