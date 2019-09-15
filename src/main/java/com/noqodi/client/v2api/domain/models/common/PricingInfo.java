
package com.noqodi.client.v2api.domain.models.common;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "PricingInfoBuilder", toBuilder = true)
@JsonDeserialize(builder = PricingInfo.PricingInfoBuilder.class)
@Getter
public class PricingInfo {

    private List<String> paymentTypes;
    private List<Pricing> pricing;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PricingInfoBuilder {
    }
}
