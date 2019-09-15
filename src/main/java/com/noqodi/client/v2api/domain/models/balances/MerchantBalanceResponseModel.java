
package com.noqodi.client.v2api.domain.models.balances;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.noqodi.client.v2api.domain.models.base.AbstractResponseModel;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "MerchantBalanceResponseModelBuilder", toBuilder = true)
@JsonDeserialize(builder = MerchantBalanceResponseModel.MerchantBalanceResponseModelBuilder.class)
@Getter
public class MerchantBalanceResponseModel extends AbstractResponseModel {

    private MerchantInquiryInfo merchantInquiryInfo;
    private String balanceAsOf;
    private StatusInfo statusInfo;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MerchantBalanceResponseModelBuilder {}
}
