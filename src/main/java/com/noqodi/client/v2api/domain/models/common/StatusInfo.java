
package com.noqodi.client.v2api.domain.models.common;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter(AccessLevel.NONE)
public class StatusInfo {

    private ErrorCode errorCode;
    private String status;
}
