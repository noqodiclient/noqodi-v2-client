
package com.noqodi.client.v2api.domain.models.payments;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.noqodi.client.v2api.domain.models.base.AbstractResponseModel;
import com.noqodi.client.v2api.domain.models.common.StatusInfo;
import com.noqodi.client.v2api.domain.models.common.CustomerInfo;
import com.noqodi.client.v2api.domain.models.common.MerchantInfo;
import com.noqodi.client.v2api.domain.models.common.PaymentInfo;
import lombok.Builder;
import lombok.Getter;

@Builder(builderClassName = "PaymentAuthResponseModelBuilder", toBuilder = true)
@JsonDeserialize(builder = PaymentAuthResponseModel.PaymentAuthResponseModelBuilder.class)
public class PaymentAuthResponseModel extends AbstractResponseModel {

    private String id;
    private String serviceType;
    private String serviceMode;
    private CustomerInfo customerInfo;
    private MerchantInfo merchantInfo;
    private PaymentInfo paymentInfo;
    private StatusInfo statusInfo;

    public PaymentAuthResponseModel(String id, String serviceType, String serviceMode, CustomerInfo customerInfo, MerchantInfo merchantInfo, PaymentInfo paymentInfo, StatusInfo statusInfo) {
        this.id = id;
        this.serviceType = serviceType;
        this.serviceMode = serviceMode;
        this.customerInfo = customerInfo;
        this.merchantInfo = merchantInfo;
        this.paymentInfo = paymentInfo;
        this.statusInfo = statusInfo;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class PaymentAuthResponseModelBuilder {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(String serviceMode) {
        this.serviceMode = serviceMode;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public MerchantInfo getMerchantInfo() {
        return merchantInfo;
    }

    public void setMerchantInfo(MerchantInfo merchantInfo) {
        this.merchantInfo = merchantInfo;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public StatusInfo getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(StatusInfo statusInfo) {
        this.statusInfo = statusInfo;
    }
}
