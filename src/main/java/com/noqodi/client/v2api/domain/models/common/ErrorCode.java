
package com.noqodi.client.v2api.domain.models.common;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter(AccessLevel.NONE)
public class ErrorCode {

    private String code;
    private String message;

}
