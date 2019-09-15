
package com.noqodi.client.v2api.domain.models.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "PricingBuilder", toBuilder = true)
@JsonDeserialize(builder = Pricing.PricingBuilder.class)
@Getter
public class Pricing {

    private String borneBy;
    private Fees fees;
    private String paymentType;
    private Vat vat;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PricingBuilder {
    }
}
