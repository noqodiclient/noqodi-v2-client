
package com.noqodi.client.v2api.domain.models.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "BeneficiaryBuilder", toBuilder = true)
@JsonDeserialize(builder = Beneficiary.BeneficiaryBuilder.class)
@Getter
public class Beneficiary {

    private String beneficiaryAcctNumber;
    private String beneficiaryName;
    private BeneficiaryAmount beneficiaryAmount;
    private NoqodiChargeAmount noqodiChargeAmount;
    private NoqodiChargeVATAmount noqodiChargeVATAmount;
    private TransferredAmount transferredAmount;

    @JsonPOJOBuilder(withPrefix = "")
    public static class BeneficiaryBuilder {
    }
}
