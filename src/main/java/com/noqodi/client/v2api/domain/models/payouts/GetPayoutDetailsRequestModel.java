
package com.noqodi.client.v2api.domain.models.payouts;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "GetPayoutDetailsRequestModelBuilder", toBuilder = true)
@JsonDeserialize(builder = GetPayoutDetailsRequestModel.GetPayoutDetailsRequestModelBuilder.class)
@Getter
public class GetPayoutDetailsRequestModel extends AbstractRequestModel {

    private String merchantCode;
    private String merchantOrderId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class GetPayoutDetailsRequestModelBuilder {}
}
