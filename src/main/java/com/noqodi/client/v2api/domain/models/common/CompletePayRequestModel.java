
package com.noqodi.client.v2api.domain.models.common;

import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.common.MerchantInfo;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter(AccessLevel.NONE)
public class CompletePayRequestModel extends AbstractRequestModel {

    private String serviceType;
    private MerchantInfo merchantInfo;
}
