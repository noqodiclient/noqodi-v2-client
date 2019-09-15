
package com.noqodi.client.v2api.domain.models.common;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "TransactionBuilder", toBuilder = true)
@JsonDeserialize(builder = Transaction.TransactionBuilder.class)
@Getter
public class Transaction {

    private List<Beneficiary> beneficiaries;
    private String merchantCode;
    private String merchantReferenceId;
    private TransactionAmount transactionAmount;
    private String noqodiReferenceId;
    private String transactionDescription;
    private String noqodiRefundReferenceId;
    private StatusInfo statusInfo;

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransactionBuilder {
    }
}