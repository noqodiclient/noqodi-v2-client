
package com.noqodi.client.v2api.domain.models.balances;

import java.util.List;

import com.noqodi.client.v2api.domain.models.balances.common.Beneficiary;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter(AccessLevel.NONE)
public class MerchantInquiryInfo {

    private String merchantCode;
    private List<Beneficiary> beneficiaries;
}
