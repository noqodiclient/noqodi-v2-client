package com.noqodi.client.v2api.domain.commands.api;

import com.noqodi.client.v2api.configurations.NoqodiApiConfiguration;
import com.noqodi.client.v2api.domain.cache.AccessTokenCacheManager;
import com.noqodi.client.v2api.domain.commands.api.token.AbstractGetAccessTokenCommand;
import com.noqodi.client.v2api.domain.commands.base.AbstractRemoteOperationCommand;
import com.noqodi.client.v2api.domain.factory.ObjectFactory;
import com.noqodi.client.v2api.domain.models.base.AbstractRequestModel;
import com.noqodi.client.v2api.domain.models.base.AbstractResponseModel;
import com.noqodi.client.v2api.domain.models.base.RemoteOperationModel;
import com.noqodi.client.v2api.domain.models.token.AbstractAccessTokenRequestModel;
import com.noqodi.client.v2api.domain.models.token.AccessTokenResponseModel;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractApiCommand<T extends AbstractRequestModel, R extends AbstractResponseModel> extends AbstractRemoteOperationCommand<T, R> {

    private final AbstractGetAccessTokenCommand accessTokenBaseCommand;
    @Autowired
    protected AccessTokenCacheManager accessTokenCacheManager;
    @Autowired
    protected NoqodiApiConfiguration configuration;
    @Autowired
    protected ObjectFactory objectFactory;

    public AbstractApiCommand(AbstractGetAccessTokenCommand accessTokenBaseCommand) {
        this.accessTokenBaseCommand = accessTokenBaseCommand;
    }

    @Override
    public RemoteOperationModel handle(T requestModel) {
        return ApiCommandExecutionSequence
                .executionSequenceBuilder(this, requestModel, accessTokenCacheManager, accessTokenBaseCommand)
                .getTokenFromCache()
                .orElseGetNewAccessToken()
                .handleCommand()
                .retryGetNewTokenOnInvalidToken()
                .retryHandleCommandOnInvalidToken()
                .handleApiCommandError()
                .buildResponse();
    }

    public abstract RemoteOperationModel<T, R> handle(T requestModel, AccessTokenResponseModel accessTokenResponseModel);
    public abstract AbstractAccessTokenRequestModel createAccessTokenRequestModel();
    public abstract RemoteOperationModel<T, R> buildTokenErrorResponse(T requestModel, RemoteOperationModel<AbstractAccessTokenRequestModel, AccessTokenResponseModel> tokenOperationModel);

}


