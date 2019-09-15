
package com.noqodi.client.v2api.domain.models.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "MerchantInfoBuilder", toBuilder = true)
@JsonDeserialize(builder = MerchantInfo.MerchantInfoBuilder.class)
@Getter
public class MerchantInfo {

    private String merchantCode;
    private String merchantLandingURL;
    private String merchantOrderId;
    private String merchantRequestId;
    private String merchantDescription;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MerchantInfoBuilder {
    }

}
