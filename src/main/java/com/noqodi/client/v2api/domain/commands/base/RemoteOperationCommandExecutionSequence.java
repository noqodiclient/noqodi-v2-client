package com.noqodi.client.v2api.domain.commands.base;

import com.noqodi.client.v2api.domain.commands.api.ApiCommandExecutionSequence;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.base.AbstractResponseModel;
import com.noqodi.client.v2api.domain.models.base.RemoteOperationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.noqodi.client.v2api.domain.helpers.Helper.extractCommandPayload;

class RemoteOperationCommandExecutionSequence<T extends AbstractRequestModel, R extends AbstractResponseModel> {

    private static Logger logger = LoggerFactory.getLogger(ApiCommandExecutionSequence.class);

    private AbstractRemoteOperationCommand<T, R> command;
    private final T requestModel;
    private RemoteOperationModel<T, R> commandResult;

    private RemoteOperationCommandExecutionSequence(
            AbstractRemoteOperationCommand<T, R> command,
            T requestModel
    ) {
        this.command = command;
        this.requestModel = requestModel;
    }

    static <E extends AbstractRequestModel, F extends AbstractResponseModel> RemoteOperationCommandExecutionSequence<E,F> executionSequenceBuilder(
            AbstractRemoteOperationCommand<E, F> command,
            E requestModel
    ) {
        return new RemoteOperationCommandExecutionSequence<E,F>(command, requestModel);
    }

    RemoteOperationCommandExecutionSequence<T,R> preHandle() {
        command.preHandle(requestModel);
        return this;
    }

    RemoteOperationCommandExecutionSequence<T,R> validate() throws Exception {
        command.validate(requestModel);
        return this;
    }

    RemoteOperationCommandExecutionSequence<T,R> handle() {
        logger.info("------------- --------------------- ------------");
        logger.info("------------- noqodi V2 API request ------------");
        logger.info("request payload => {}", extractCommandPayload(requestModel, "AbstractRequestModel"));

        commandResult = command.handle(requestModel);

        logger.info("------------- --------------------- ------------");
        logger.info("------------- noqodi V2 API response ------------");
        logger.info("response payload => {} ", extractCommandPayload(commandResult, "AbstractResponseModel"));

        return this;
    }

    RemoteOperationCommandExecutionSequence<T,R> postHandle() {
        command.postHandle(commandResult);
        return this;
    }

    RemoteOperationModel<T, R> buildResult() {
        return commandResult;
    }
}
