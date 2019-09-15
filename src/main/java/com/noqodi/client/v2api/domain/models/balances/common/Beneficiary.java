
package com.noqodi.client.v2api.domain.models.balances.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "BeneficiaryBuilder", toBuilder = true)
@JsonDeserialize(builder = Beneficiary.BeneficiaryBuilder.class)
@Getter
public class Beneficiary {

    private Double beneCurrentBalance;
    private Double benePendingPayoutAmount;
    private String beneficiaryAcctNumber;
    private String beneficiaryName;

    @JsonPOJOBuilder(withPrefix = "")
    public static class BeneficiaryBuilder {}
}
