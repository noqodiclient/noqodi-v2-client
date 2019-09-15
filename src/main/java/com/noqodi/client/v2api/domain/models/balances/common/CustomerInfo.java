
package com.noqodi.client.v2api.domain.models.balances.common;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter(AccessLevel.NONE)
public class CustomerInfo {

    private Double customerCurrentBalance;
    private String wallet;

}
