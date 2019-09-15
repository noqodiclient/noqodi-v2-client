
package com.noqodi.client.v2api.domain.models.balances;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.noqodi.client.v2api.domain.models.base.AbstractResponseModel;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import com.noqodi.client.v2api.domain.models.balances.common.CustomerInfo;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "CustomerBalanceResponseModelBuilder", toBuilder = true)
@JsonDeserialize(builder = CustomerBalanceResponseModel.CustomerBalanceResponseModelBuilder.class)
@Getter
public class CustomerBalanceResponseModel extends AbstractResponseModel {

    private CustomerInfo customerInfo;
    private String balanceAsOf;
    private StatusInfo statusInfo;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CustomerBalanceResponseModelBuilder {}
}
