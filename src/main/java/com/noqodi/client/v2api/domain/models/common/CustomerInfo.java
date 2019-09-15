
package com.noqodi.client.v2api.domain.models.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "CustomerInfoBuilder", toBuilder = true)
@JsonDeserialize(builder = CustomerInfo.CustomerInfoBuilder.class)
@Getter
public class CustomerInfo {

    private String walletId;
    private PaymentMethod paymentMethod;
    private String remarks;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CustomerInfoBuilder {
    }

}
