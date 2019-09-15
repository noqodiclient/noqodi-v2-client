package com.noqodi.client.v2api.domain.error;

import org.springframework.web.client.HttpClientErrorException;

public class GenericErrorHandler {

    public static <T,R> void handleError(T requestModel, R responseModel, Exception exception) {
        if (exception instanceof HttpClientErrorException.BadRequest) {
            // handle business error
        } else {
            // handle server error
        }
    }
}
