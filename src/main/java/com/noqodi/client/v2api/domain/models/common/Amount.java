
package com.noqodi.client.v2api.domain.models.common;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter(AccessLevel.NONE)
public class Amount {

    private String currency;
    private BigDecimal value;
}
