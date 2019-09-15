
package com.noqodi.client.v2api.domain.models.refunds;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.common.CustomerInfo;
import com.noqodi.client.v2api.domain.models.common.MerchantInfo;
import com.noqodi.client.v2api.domain.models.common.PaymentInfo;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "RefundRequestModelBuilder", toBuilder = true)
@JsonDeserialize(builder = RefundRequestModel.RefundRequestModelBuilder.class)
@Getter
public class RefundRequestModel extends AbstractRequestModel {

    private String serviceType;
    private CustomerInfo customerInfo;
    private MerchantInfo merchantInfo;
    private PaymentInfo paymentInfo;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RefundRequestModelBuilder {
    }
}
