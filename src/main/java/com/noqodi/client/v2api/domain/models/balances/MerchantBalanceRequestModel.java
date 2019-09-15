package com.noqodi.client.v2api.domain.models.balances;

import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter(AccessLevel.NONE)
public class MerchantBalanceRequestModel extends AbstractRequestModel {

    @NotEmpty(message = "merchantCode should not be empty")
    private String merchantCode;
}
