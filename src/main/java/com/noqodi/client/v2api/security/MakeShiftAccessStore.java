package com.noqodi.client.v2api.security;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.noqodi.client.v2api.domain.cache.AbstractCacheManager;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MakeShiftAccessStore extends AbstractCacheManager<MakeShiftAccessTokenModel> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault());

    private final Cache<String, MakeShiftAccessTokenModel> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .build();

    public boolean isValid(String authorization) {
        MakeShiftAccessTokenModel accessTokenModel = this.tryGetEntry(authorization);
        if (accessTokenModel == null) return false;

        Instant expiryInstant = formatter.parse(accessTokenModel.getExpires_at(), Instant::from);

        if (expiryInstant.isBefore(Instant.now())) {
            this.deleteEntry(authorization);
            return false;
        }
        return true;
    }

    public MakeShiftAccessTokenModel generateAccessToken() {
        Instant issuedInstant = Instant.now();
        String issuedAt = formatter.format(issuedInstant);

        Instant expiryInstant = issuedInstant.plus(20L, ChronoUnit.MINUTES);
        String expiresAt = formatter.format(expiryInstant);

        MakeShiftAccessTokenModel accessTokenModel = MakeShiftAccessTokenModel.builder()
                .access_token(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()))
                .token_type("Bearer")
                .issued_at(issuedAt)
                .expires_at(expiresAt)
                .expires_in("1199") // 1 second less than 20 minutes
                .build();

        this.saveEntry(accessTokenModel.getAccess_token(), accessTokenModel);
        return accessTokenModel;
    }

    @Override
    public void initCache(int cacheExpiryMinutes) {
    }

    @Override
    public void saveEntry(String key, MakeShiftAccessTokenModel value) {
        cache.put(key, value);
    }

    @Override
    public MakeShiftAccessTokenModel tryGetEntry(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void deleteEntry(String key) {
        cache.invalidate(key);
    }
}