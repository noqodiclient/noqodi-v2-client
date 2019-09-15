package com.noqodi.client.v2api.domain.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.noqodi.client.v2api.domain.models.base.CacheablePaymentImpl;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentCommandCacheManager<T> extends AbstractCacheManager<T> {

    private final Cache<String, T> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    public void saveCacheable(CacheablePaymentImpl<T> cacheablePayment) {
        if (cacheablePayment == null) return;
        saveEntry(cacheablePayment.getKey(), cacheablePayment.getValue());
    }

    public Optional<T> getCacheable(String key) {
        T value = tryGetEntry(key);
        if (value == null) return Optional.empty();
        return Optional.of(value);
    }

    public void removeCacheable(String key) {
        deleteEntry(key);
    }

    @Override
    public void initCache(int cacheExpirySeconds) {
    }

    @Override
    public void saveEntry(String key, T value) {
        cache.put(key, value);
    }

    @Override
    public T tryGetEntry(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void deleteEntry(String key) {
        cache.invalidate(key);
    }
}
