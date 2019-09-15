
package com.noqodi.client.v2api.domain.models.common;

import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter(AccessLevel.NONE)
public class InquiryRequestModel extends AbstractRequestModel {

    private String noqodiResponseId;
}
