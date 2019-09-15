package com.noqodi.client.v2api.domain.models.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class AbstractCacheable<T> {
    private final String key;
    private final T value;
}
