
package com.noqodi.client.v2api.domain.models.refunds;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.noqodi.client.v2api.domain.models.base.AbstractResponseModel;
import com.noqodi.client.v2api.domain.models.common.CustomerInfo;
import com.noqodi.client.v2api.domain.models.common.MerchantInfo;
import com.noqodi.client.v2api.domain.models.common.PaymentInfo;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "RefundResponseModelBuilder", toBuilder = true)
@JsonDeserialize(builder = RefundResponseModel.RefundResponseModelBuilder.class)
@Getter
public class RefundResponseModel extends AbstractResponseModel {

    private String id;
    private String serviceType;
    private CustomerInfo customerInfo;
    private MerchantInfo merchantInfo;
    private PaymentInfo paymentInfo;
    private StatusInfo statusInfo;

    @JsonPOJOBuilder(withPrefix = "")
    public static class RefundResponseModelBuilder {
    }
}
