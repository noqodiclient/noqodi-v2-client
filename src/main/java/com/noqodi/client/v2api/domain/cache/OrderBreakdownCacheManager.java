package com.noqodi.client.v2api.domain.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class OrderBreakdownCacheManager extends AbstractCacheManager<Map<String, BigDecimal>> {

    private final Cache<String, Map<String, BigDecimal>> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    public void removeCacheable(String key) {
        deleteEntry(key);
    }

    @Override
    public void initCache(int cacheExpirySeconds) {
    }

    @Override
    public void saveEntry(String key, Map<String, BigDecimal> value) {
        cache.put(key, value);
    }

    @Override
    public Map<String, BigDecimal> tryGetEntry(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void deleteEntry(String key) {
        cache.invalidate(key);
    }
}

