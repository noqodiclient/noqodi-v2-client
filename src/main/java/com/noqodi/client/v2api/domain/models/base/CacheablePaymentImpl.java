package com.noqodi.client.v2api.domain.models.base;

import com.noqodi.client.v2api.domain.constants.ServiceType;
import lombok.Getter;

@Getter
public class CacheablePaymentImpl<T> extends AbstractCacheable<T> {

    public CacheablePaymentImpl(ServiceType serviceType, String key, T value) {
        super(serviceType.name()+ "_" + key, value);
    }
}
