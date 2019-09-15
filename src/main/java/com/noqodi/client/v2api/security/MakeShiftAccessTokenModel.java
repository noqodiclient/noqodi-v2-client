package com.noqodi.client.v2api.security;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import java.time.Instant;

@Builder(builderClassName = "MakeShiftAccessTokenModelBuilder", toBuilder = true)
@JsonDeserialize(builder = MakeShiftAccessTokenModel.MakeShiftAccessTokenModelBuilder.class)
@Getter
public class MakeShiftAccessTokenModel {
    private String access_token;
    private String token_type;
    private String issued_at;
    private String expires_at;
    private String expires_in;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MakeShiftAccessTokenModelBuilder {}
}
