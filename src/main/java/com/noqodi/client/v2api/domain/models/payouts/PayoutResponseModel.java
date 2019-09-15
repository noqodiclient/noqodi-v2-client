
package com.noqodi.client.v2api.domain.models.payouts;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.noqodi.client.v2api.domain.models.base.AbstractResponseModel;
import com.noqodi.client.v2api.domain.models.common.MerchantInfo;
import com.noqodi.client.v2api.domain.models.common.PaymentInfo;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "PayoutResponseModelBuilder", toBuilder = true)
@JsonDeserialize(builder = PayoutResponseModel.PayoutResponseModelBuilder.class)
@Getter
public class PayoutResponseModel extends AbstractResponseModel {

    private MerchantInfo merchantInfo;
    private PaymentInfo paymentInfo;
    private StatusInfo statusInfo;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PayoutResponseModelBuilder {}
}
