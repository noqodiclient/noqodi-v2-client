package com.noqodi.client.v2api.domain.validation;

import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import lombok.Getter;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Getter
public class NoqodiValidationException extends Exception {

    private final Set<ConstraintViolation<AbstractRequestModel>> constraintViolations;

    public NoqodiValidationException(String message, Set<ConstraintViolation<AbstractRequestModel>> constraintViolations) {
        super(message);
        this.constraintViolations = constraintViolations;
    }
}
