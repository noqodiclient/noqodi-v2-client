package com.noqodi.client.v2api.domain.models.mappers;

import com.noqodi.client.v2api.domain.constants.ServiceType;
import com.noqodi.client.v2api.domain.models.base.CacheablePaymentImpl;

public class CacheableMapper {

    public static <T> CacheablePaymentImpl<T> toCacheablePayment(ServiceType serviceType, String merchantOrderId, T value) {
        return new CacheablePaymentImpl<T>(serviceType, merchantOrderId, value);
    }

    public static String toCacheablePaymentIdentifier(ServiceType serviceType, String merchantOrderId) {
        return serviceType.name()+ "_" + merchantOrderId;
    }
}
