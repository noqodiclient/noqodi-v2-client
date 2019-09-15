
package com.noqodi.client.v2api.domain.models.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder(builderClassName = "PaymentInfoBuilder", toBuilder = true)
@JsonDeserialize(builder = PaymentInfo.PaymentInfoBuilder.class)
@Getter
public class PaymentInfo {

    private Amount amount;
    private PricingInfo pricingInfo;
    private String transactionDate;
    private String preAuthToken;
    private String noqodiOrderId;
    private List<Transaction> transactions;
    private String customerReferenceId;
    private String noqodiOrderTransactionId;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PaymentInfoBuilder {
    }
}
