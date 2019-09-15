package com.noqodi.client.v2api.domain.commands.base;

import com.noqodi.client.v2api.domain.validation.NoqodiValidationException;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.base.AbstractResponseModel;
import com.noqodi.client.v2api.domain.models.base.RemoteOperationModel;
import org.springframework.http.ResponseEntity;

public abstract class AbstractRemoteOperationCommand<T extends AbstractRequestModel, R extends AbstractResponseModel> {


    public RemoteOperationModel<T, R> execute(T requestModel) {
        try {
            return RemoteOperationCommandExecutionSequence.
                    executionSequenceBuilder(this, requestModel)
                    .preHandle()
                    .validate()
                    .handle()
                    .postHandle()
                    .buildResult();
        } catch (Exception exception) {
            return this.handleError(requestModel, (R) null, exception);
        }
    }

    public abstract void validate(T requestModel) throws NoqodiValidationException;

    public abstract RemoteOperationModel<T, R> handle(T requestModel);

    public abstract void preHandle(T requestModel);

    public abstract void postHandle(RemoteOperationModel<T, R> remoteOperationModel);

    public abstract RemoteOperationModel<T, R> handleError(T requestModel, R responseModel, Exception exception);

    public abstract RemoteOperationModel<T, R> buildResponse(T requestModel, R responseModel, ResponseEntity responseEntity);

}
