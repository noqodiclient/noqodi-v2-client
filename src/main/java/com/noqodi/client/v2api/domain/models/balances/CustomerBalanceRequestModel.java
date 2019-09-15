
package com.noqodi.client.v2api.domain.models.balances;

import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter(AccessLevel.NONE)
public class CustomerBalanceRequestModel extends AbstractRequestModel {

    private String walletId;

}
