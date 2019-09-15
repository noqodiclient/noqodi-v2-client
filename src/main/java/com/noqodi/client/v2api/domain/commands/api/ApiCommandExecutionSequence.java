package com.noqodi.client.v2api.domain.commands.api;

import com.noqodi.client.v2api.domain.commands.api.token.AbstractGetAccessTokenCommand;
import com.noqodi.client.v2api.domain.helpers.Helper;
import com.noqodi.client.v2api.domain.cache.AccessTokenCacheManager;
import com.noqodi.client.v2api.domain.helpers.ResponseHelper;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.base.AbstractResponseModel;
import com.noqodi.client.v2api.domain.models.base.RemoteOperationModel;
import com.noqodi.client.v2api.domain.models.token.AbstractAccessTokenRequestModel;
import com.noqodi.client.v2api.domain.models.token.AccessTokenResponseModel;

import java.util.Optional;

import static com.noqodi.client.v2api.domain.helpers.ResponseHelper.isInvalidAccessTokenError;

public class ApiCommandExecutionSequence<T extends AbstractRequestModel, R extends AbstractResponseModel> {
    private final AbstractApiCommand<T, R> apiCommand;
    private final T apiRequestModel;
    private final AccessTokenCacheManager accessTokenCacheManager;
    private final AbstractGetAccessTokenCommand<AbstractAccessTokenRequestModel> getAccessTokenCommand;
    private Optional<AccessTokenResponseModel> optionalOfAccessToken;
    private RemoteOperationModel<T, R> apiCommandResponse;
    private RemoteOperationModel<AbstractAccessTokenRequestModel, AccessTokenResponseModel> accessTokenResponse;
    private Exception commandError;

    private ApiCommandExecutionSequence(
            AbstractApiCommand<T, R> apiCommand,
            T apiRequestModel,
            AccessTokenCacheManager accessTokenCacheManager,
            AbstractGetAccessTokenCommand<AbstractAccessTokenRequestModel> getAccessTokenCommand
    ) {
        this.apiCommand = apiCommand;
        this.apiRequestModel = apiRequestModel;
        this.accessTokenCacheManager = accessTokenCacheManager;
        this.getAccessTokenCommand = getAccessTokenCommand;
    }

    static <E extends AbstractRequestModel, F extends AbstractResponseModel> ApiCommandExecutionSequence executionSequenceBuilder(
            AbstractApiCommand<E, F> apiCommand,
            E requestModel,
            AccessTokenCacheManager accessTokenCacheManager,
            AbstractGetAccessTokenCommand<AbstractAccessTokenRequestModel> accessTokenBaseCommand
    ) {
        return new ApiCommandExecutionSequence<E, F>(
                apiCommand,
                requestModel,
                accessTokenCacheManager,
                accessTokenBaseCommand
        );
    }


    ApiCommandExecutionSequence<T, R> getTokenFromCache() {
        optionalOfAccessToken = accessTokenCacheManager.getAccessTokenFromCache();
        return this;
    }


    ApiCommandExecutionSequence<T, R> orElseGetNewAccessToken() {
        if (!optionalOfAccessToken.isPresent()) {
            getNewToken();
        }
        return this;
    }


    ApiCommandExecutionSequence<T, R> handleCommand() {
        optionalOfAccessToken = accessTokenCacheManager.getAccessTokenFromCache();
        optionalOfAccessToken.ifPresent(accessTokenModel -> {
            if (Helper.isApprovedAccessToken(accessTokenModel)) {
                try {
                    apiCommandResponse = apiCommand.handle(apiRequestModel, accessTokenModel);
                } catch (Exception exception) {
                    commandError = exception;
                }
            }
        });
        return this;
    }


    ApiCommandExecutionSequence<T, R> retryGetNewTokenOnInvalidToken() {
        if (isInvalidAccessTokenError(commandError)) {
            getNewToken();
        }
        return this;
    }


    ApiCommandExecutionSequence<T, R> retryHandleCommandOnInvalidToken() {
        if (isInvalidAccessTokenError(commandError)) {
            optionalOfAccessToken = accessTokenCacheManager.getAccessTokenFromCache();
            optionalOfAccessToken.ifPresent(accessTokenModel -> {
                if (Helper.isApprovedAccessToken(accessTokenModel)) {
                    apiCommandResponse = apiCommand.handle(apiRequestModel, accessTokenModel);
                }
            });
        }
        return this;
    }


    ApiCommandExecutionSequence<T, R> handleApiCommandError() {
        if (!optionalOfAccessToken.isPresent() || !Helper.isApprovedAccessToken(optionalOfAccessToken.get())) {
            apiCommandResponse = apiCommand.buildTokenErrorResponse(apiRequestModel, accessTokenResponse);
        } else if (!ResponseHelper.isSuccess(apiCommandResponse)) {
            apiCommandResponse = apiCommand.handleError(
                    apiRequestModel,
                    apiCommandResponse != null ? apiCommandResponse.getResponse() : null,
                    commandError);
        }
        return this;
    }

    public RemoteOperationModel<T, R> buildResponse() {
        return apiCommandResponse;
    }


    private void getNewToken() {
        AbstractAccessTokenRequestModel accessTokenRequestModel = apiCommand.createAccessTokenRequestModel();
        accessTokenResponse = getAccessTokenCommand.execute(accessTokenRequestModel);
    }

}
