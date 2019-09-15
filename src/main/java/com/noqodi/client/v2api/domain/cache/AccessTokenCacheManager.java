package com.noqodi.client.v2api.domain.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.noqodi.client.v2api.domain.helpers.Helper;
import com.noqodi.client.v2api.domain.models.token.AccessTokenResponseModel;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AccessTokenCacheManager extends AbstractCacheManager<AccessTokenResponseModel> {

    private static final String cacheKey = UUID.randomUUID().toString();

    private Cache<String, AccessTokenResponseModel> cache;

    public void saveAccessTokenToCache(AccessTokenResponseModel accessToken) {
        if (!accessToken.getStatus().equalsIgnoreCase("approved")) return;
        if (cache == null) initCache(Helper.getDesiredValueOrDefault(Integer.parseInt(accessToken.getExpiresIn()), 120));
        saveEntry(cacheKey, accessToken);
    }

    public Optional<AccessTokenResponseModel> getAccessTokenFromCache() {
        AccessTokenResponseModel accessToken = tryGetEntry(cacheKey);
        if (accessToken == null) return Optional.empty();
        return Optional.of(accessToken);
    }

    public void removeAccessTokenFromCache() {
        deleteEntry(cacheKey);
    }

    @Override
    public void initCache(int cacheExpirySeconds) {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheExpirySeconds - 2, TimeUnit.SECONDS) // -2 seconds latency
                .build();
    }

    @Override
    public void saveEntry(String key, AccessTokenResponseModel value) {
        if (cache != null) cache.put(key, value);
    }

    @Override
    public AccessTokenResponseModel tryGetEntry(String key) {
        return (cache != null) ? cache.getIfPresent(key) : null;
    }

    @Override
    public void deleteEntry(String key) {
        if (cache != null) cache.invalidate(key);
    }
}
