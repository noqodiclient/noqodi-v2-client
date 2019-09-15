
package com.noqodi.client.v2api.domain.models.balances;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.noqodi.client.v2api.domain.models.base.AbstractResponseModel;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "MerchantBalanceBeneficiariesResponseModelBuilder", toBuilder = true)
@JsonDeserialize(builder = MerchantBalanceBeneficiariesResponseModel.MerchantBalanceBeneficiariesResponseModelBuilder.class)
@Getter
public class MerchantBalanceBeneficiariesResponseModel extends AbstractResponseModel {

    private MerchantInquiryInfo merchantInquiryInfo;
    private String balanceAsOf;
    private StatusInfo statusInfo;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MerchantBalanceBeneficiariesResponseModelBuilder {}
}
