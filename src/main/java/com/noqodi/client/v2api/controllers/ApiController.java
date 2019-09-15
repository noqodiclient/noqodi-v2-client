package com.noqodi.client.v2api.controllers;

import com.noqodi.client.v2api.configurations.NoqodiApiConfiguration;
import com.noqodi.client.v2api.configurations.WebAppConfiguration;
import com.noqodi.client.v2api.domain.cache.OrderBreakdownCacheManager;
import com.noqodi.client.v2api.domain.cache.PaymentCommandCacheManager;
import com.noqodi.client.v2api.domain.commands.api.balances.GetCustomerBalanceCommandImpl;
import com.noqodi.client.v2api.domain.commands.api.balances.GetMerchantBalanceCommandImpl;
import com.noqodi.client.v2api.domain.commands.api.balances.GetMerchantBeniesBalanceCommandImpl;
import com.noqodi.client.v2api.domain.commands.api.payments.PaymentCaptureCommandImpl;
import com.noqodi.client.v2api.domain.commands.api.payments.PaymentCompleteAuthCommandImpl;
import com.noqodi.client.v2api.domain.commands.api.payments.PaymentPreAuthCommandImpl;
import com.noqodi.client.v2api.domain.commands.api.payouts.GetPayoutDetailsCommandImpl;
import com.noqodi.client.v2api.domain.commands.api.payouts.PayoutCommandImpl;
import com.noqodi.client.v2api.domain.commands.api.refunds.CompleteRefundCommandImpl;
import com.noqodi.client.v2api.domain.commands.api.refunds.RefundCommandImpl;
import com.noqodi.client.v2api.domain.commands.api.refunds.RefundInquiryCommandImpl;
import com.noqodi.client.v2api.domain.commands.api.token.GetAccessTokenPGCommandImpl;
import com.noqodi.client.v2api.domain.constants.ServiceType;
import com.noqodi.client.v2api.domain.helpers.RequestHelper;
import com.noqodi.client.v2api.domain.models.balances.*;
import com.noqodi.client.v2api.domain.models.common.*;
import com.noqodi.client.v2api.domain.models.payments.*;
import com.noqodi.client.v2api.domain.commands.api.payments.PaymentVoidAuthCommandImpl;
import com.noqodi.client.v2api.domain.commands.api.token.GetAccessTokenCCGCommandImpl;
import com.noqodi.client.v2api.domain.helpers.ResponseHelper;
import com.noqodi.client.v2api.domain.models.base.RemoteOperationModel;
import com.noqodi.client.v2api.domain.models.payouts.GetPayoutDetailsRequestModel;
import com.noqodi.client.v2api.domain.models.payouts.PayoutRequestModel;
import com.noqodi.client.v2api.domain.models.payouts.PayoutResponseModel;
import com.noqodi.client.v2api.domain.models.refunds.RefundRequestModel;
import com.noqodi.client.v2api.domain.models.refunds.RefundResponseModel;
import com.noqodi.client.v2api.security.AppAuthenticationManager;
import com.noqodi.client.v2api.security.MakeShiftAccessStore;
import com.noqodi.client.v2api.security.MakeShiftAccessTokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Consumer;

import static com.noqodi.client.v2api.domain.helpers.ResponseHelper.isPaymentAuthResponseSuccess;
import static com.noqodi.client.v2api.domain.helpers.ResponseHelper.isPaymentCaptureResponseSuccess;
import static com.noqodi.client.v2api.domain.models.mappers.CacheableMapper.toCacheablePaymentIdentifier;
import static com.noqodi.client.v2api.domain.models.mappers.PaymentModelMapper.toVoidAuthModelMerchantInfo;
import static com.noqodi.client.v2api.domain.models.mappers.PaymentModelMapper.toVoidAuthModelPaymentInfo;
import static com.noqodi.client.v2api.domain.models.mappers.RefundModelMapper.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private NoqodiApiConfiguration noqodiApiConfiguration;

    @Autowired
    private WebAppConfiguration webAppConfiguration;

    @Autowired
    private GetAccessTokenCCGCommandImpl getAccessTokenCCGCommandImpl;

    @Autowired
    private GetAccessTokenPGCommandImpl getAccessTokenPGCommandImpl;

    @Autowired
    private GetMerchantBalanceCommandImpl getMerchantBalanceCommandImpl;

    @Autowired
    private GetMerchantBeniesBalanceCommandImpl getMerchantBeniesBalanceCommandImpl;

    @Autowired
    private GetCustomerBalanceCommandImpl getCustomerBalanceCommandImpl;

    @Autowired
    private PaymentPreAuthCommandImpl paymentPreAuthCommandImpl;

    @Autowired
    private PaymentVoidAuthCommandImpl paymentVoidAuthCommandImpl;

    @Autowired
    private PaymentCompleteAuthCommandImpl paymentCompleteAuthCommandImpl;

    @Autowired
    private PaymentCaptureCommandImpl paymentCaptureCommandImpl;

    @Autowired
    private RefundCommandImpl refundCommandImpl;

    @Autowired
    private CompleteRefundCommandImpl completeRefundCommandImpl;

    @Autowired
    private RefundInquiryCommandImpl refundInquiryCommandImpl;

    @Autowired
    private PayoutCommandImpl payoutCommandImpl;

    @Autowired
    private GetPayoutDetailsCommandImpl getPayoutDetailsCommandImpl;

    @Autowired
    private PaymentCommandCacheManager<RemoteOperationModel> paymentCommandCacheManager;

    @Autowired
    private OrderBreakdownCacheManager orderBreakdownCacheManager;

    @Autowired
    private MakeShiftAccessStore makeShiftAccessStore;

    @Autowired
    private AppAuthenticationManager appAuthenticationManager;

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<MakeShiftAccessTokenModel> accessToken(@RequestHeader(value = "Authorization") String authorizationHeader) {
        String basicAuthCredential = AppAuthenticationManager.extractBasicAuth(authorizationHeader);
        if (appAuthenticationManager.isValidCredentials(basicAuthCredential)) {
            return new ResponseEntity<MakeShiftAccessTokenModel>(makeShiftAccessStore.generateAccessToken(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

//    @RequestMapping(value = "/oauth/client-credentials", method = RequestMethod.GET)
//    public RemoteOperationModel<CCGAccessTokenRequestModel, AccessTokenResponseModel> oauthClientCredentials() {
//        CCGAccessTokenRequestModel request = RequestHelper.buildSampleAccessTokenCCGRequest(noqodiApiConfiguration);
//        return getAccessTokenCCGCommandImpl.execute(request);
//    }
//
//
//    @RequestMapping(value = "/oauth/password", method = RequestMethod.GET)
//    public RemoteOperationModel<PGAccessTokenRequestModel, AccessTokenResponseModel> oauthPassword() {
//        PGAccessTokenRequestModel request = RequestHelper.buildSampleAccessTokenPGRequest(noqodiApiConfiguration);
//        return getAccessTokenPGCommandImpl.execute(request);
//    }

    @RequestMapping(value = "/balances/merchant", method = RequestMethod.GET)
    public RemoteOperationModel<MerchantBalanceRequestModel, MerchantBalanceResponseModel> balancesMerchant() {
        String merchantCode = noqodiApiConfiguration.getTestData().get("balance-merchant-code");
        return getMerchantBalanceCommandImpl.execute(new MerchantBalanceRequestModel(merchantCode));
    }

    @RequestMapping(value = "/balances/merchant/beneficiaries/{option}", method = RequestMethod.GET)
    public RemoteOperationModel<MerchantBeneficiariesBalanceRequestModel, MerchantBalanceBeneficiariesResponseModel> balancesMerchantBeneficiaries(
            @PathVariable("option") String option
    ) {
        String merchantCode, beneficiaries;
        if ("PAYOUT".equalsIgnoreCase(option)) {
            merchantCode = noqodiApiConfiguration.getTestData().get("payouts-child-merchant-code");
            beneficiaries = noqodiApiConfiguration.getTestData().get("payouts-merchant-beneficiaries");
        } else {
            merchantCode = noqodiApiConfiguration.getTestData().get("balance-merchant-code");
            beneficiaries = noqodiApiConfiguration.getTestData().get("balance-merchant-beneficiaries");
        }
        return getMerchantBeniesBalanceCommandImpl.execute(new MerchantBeneficiariesBalanceRequestModel(merchantCode, beneficiaries));
    }

    @RequestMapping(value = "/balances/customer", method = RequestMethod.GET)
    public RemoteOperationModel<CustomerBalanceRequestModel, CustomerBalanceResponseModel> balancesCustomer() {
        String walletId = noqodiApiConfiguration.getTestData().get("customer-wallet");
        return getCustomerBalanceCommandImpl.execute(new CustomerBalanceRequestModel(walletId));
    }


    @RequestMapping(value = "/payments/preAuth", method = RequestMethod.POST)
    public RemoteOperationModel<PaymentPreAuthRequestModel, PaymentPreAuthResponseModel> paymentsPreAuth(
            @RequestBody Map<String, BigDecimal> beneficiaryAmountMap
    ) {
        String merchantCode = noqodiApiConfiguration.getTestData().get("payment-merchant-code");

        BigDecimal orderTotal = beneficiaryAmountMap.get("beneficiaryOneAmount")
                .add(beneficiaryAmountMap.get("beneficiaryTwoAmount"))
                .setScale(2, RoundingMode.HALF_UP);

        Amount totalAmount = new Amount("AED", orderTotal);
        PricingInfo pricingInfo = PricingInfo.builder().paymentTypes(Arrays.asList("ECA")).build();

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .amount(totalAmount)
                .pricingInfo(pricingInfo)
                .build();

        MerchantInfo merchantInfo = RequestHelper.buildMerchantInfoForPreAuth(merchantCode, webAppConfiguration.getCallbackUrl() + "?response=");

        PaymentPreAuthRequestModel requestModel = PaymentPreAuthRequestModel
                .builder()
                .serviceType(ServiceType.PRE_AUTH.name())
                .serviceMode("NORMAL")
                .paymentInfo(paymentInfo)
                .merchantInfo(merchantInfo)
                .build();

        Consumer<String> postHandleConsumer = (merchantOrderId) -> {
            orderBreakdownCacheManager.saveEntry(merchantOrderId, beneficiaryAmountMap);
        };

        RemoteOperationModel<PaymentPreAuthRequestModel, PaymentPreAuthResponseModel> result = paymentPreAuthCommandImpl.execute(requestModel);

        if (ResponseHelper.isSuccess(result))
            postHandleConsumer.accept(result.getResponse().getMerchantInfo().getMerchantOrderId());
        return result;
    }

    @RequestMapping(value = "/payments/auth", method = RequestMethod.POST)
    public RemoteOperationModel<PaymentAuthRequestModel, PaymentAuthResponseModel> paymentsAuthResponse(
            @RequestBody PaymentAuthResponseModel responseModel
    ) {
//        test only..., replace with actual auth response
//        use complete AUTH in the meantime
        PaymentAuthRequestModel requestModel = PaymentAuthRequestModel.builder()
                .serviceType(ServiceType.AUTH.name())
                .merchantInfo(responseModel.getMerchantInfo())
                .build();

        return paymentCompleteAuthCommandImpl.execute(requestModel);
    }

    @RequestMapping(value = "/payments/voidAuth", method = RequestMethod.POST)
    public RemoteOperationModel<PaymentVoidAuthRequestModel, PaymentVoidAuthResponseModel> voidAuth(
            @RequestBody PaymentAuthResponseModel responseModel
    ) {
        PaymentVoidAuthRequestModel requestModel = PaymentVoidAuthRequestModel.builder()
                .serviceType(ServiceType.VOID_AUTH.name())
                .merchantInfo(toVoidAuthModelMerchantInfo(responseModel.getMerchantInfo()))
                .paymentInfo(toVoidAuthModelPaymentInfo(responseModel.getPaymentInfo()))
                .build();

        return paymentVoidAuthCommandImpl.execute(requestModel);
    }


    @RequestMapping(value = "/payments/capture", method = RequestMethod.POST)
    public RemoteOperationModel<PaymentCaptureRequestModel, PaymentCaptureResponseModel> paymentsCapture(
            @RequestBody PaymentAuthResponseModel paymentAuthResponseModelFromIframe
    ) {
        Optional<RemoteOperationModel> cachedAuthResponse = Optional.empty();

        if (isPaymentAuthResponseSuccess(paymentAuthResponseModelFromIframe)) {
            String key = toCacheablePaymentIdentifier(ServiceType.AUTH, paymentAuthResponseModelFromIframe.getMerchantInfo().getMerchantOrderId());
            cachedAuthResponse = paymentCommandCacheManager.getCacheable(key);
        }

        if (!cachedAuthResponse.isPresent()) {
            return RemoteOperationModel
                    .<PaymentCaptureRequestModel, PaymentCaptureResponseModel>builder()
                    .build();
        }

        RemoteOperationModel<PaymentAuthRequestModel, PaymentAuthResponseModel> authRemoteOperationModel = cachedAuthResponse.get();
        PaymentAuthResponseModel savedPaymentAuthResponseModel = authRemoteOperationModel.getResponse();


        MerchantInfo merchantInfo = MerchantInfo.builder()
                .merchantCode(savedPaymentAuthResponseModel.getMerchantInfo().getMerchantCode()) // same for entire cycle
                .merchantOrderId(savedPaymentAuthResponseModel.getMerchantInfo().getMerchantOrderId()) // same for entire cycle
                .merchantRequestId(UUID.randomUUID().toString()) // unique per request
                .build();

        List<Transaction> transactions = RequestHelper.buildTransactionsForCapture(
                savedPaymentAuthResponseModel.getMerchantInfo().getMerchantCode(),
                new TransactionAmount(
                        savedPaymentAuthResponseModel.getPaymentInfo().getAmount().getCurrency(), // from pre-auth amount
                        savedPaymentAuthResponseModel.getPaymentInfo().getAmount().getValue() // from pre-auth amount
                ),
                orderBreakdownCacheManager.tryGetEntry(savedPaymentAuthResponseModel.getMerchantInfo().getMerchantOrderId())
        );

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .amount(savedPaymentAuthResponseModel.getPaymentInfo().getAmount())
                .noqodiOrderId(paymentAuthResponseModelFromIframe.getPaymentInfo().getNoqodiOrderId())
                .transactions(transactions)
                .build();

        PaymentCaptureRequestModel requestModel = PaymentCaptureRequestModel
                .builder()
                .serviceType(ServiceType.CAPTURE.name())
                .merchantInfo(merchantInfo)
                .paymentInfo(paymentInfo)
                .build();

        return paymentCaptureCommandImpl.execute(requestModel);
    }

    @RequestMapping(value = "/refunds", method = RequestMethod.POST)
    public RemoteOperationModel<RefundRequestModel, RefundResponseModel> refund(
            @RequestBody PaymentCaptureResponseModel paymentCaptureResponseModel
    ) {

        if (!isPaymentCaptureResponseSuccess(paymentCaptureResponseModel)) {
            return RemoteOperationModel
                    .<RefundRequestModel, RefundResponseModel>builder()
                    .build();
        }

        RefundRequestModel requestModel = RefundRequestModel.builder()
                .serviceType(ServiceType.REFUND.name())
                .customerInfo(toRefundModelCustomerInfo(paymentCaptureResponseModel.getCustomerInfo()))
                .merchantInfo(toRefundModelMerchantInfo(paymentCaptureResponseModel.getMerchantInfo()))
                .paymentInfo(toRefundModelPaymentInfo(paymentCaptureResponseModel.getPaymentInfo()))
                .build();

        return refundCommandImpl.execute(requestModel);
    }

    @RequestMapping(value = "/refunds/complete", method = RequestMethod.POST)
    public RemoteOperationModel<CompletePayRequestModel, RefundResponseModel> completeRefund(
            @RequestBody CompletePayRequestModel completePayRequestModel
    ) {

        if (completePayRequestModel == null) {
            return RemoteOperationModel
                    .<CompletePayRequestModel, RefundResponseModel>builder()
                    .build();
        }

        return completeRefundCommandImpl.execute(completePayRequestModel);
    }

    @RequestMapping(value = "/refunds/{noqodiResponseId}", method = RequestMethod.GET)
    public RemoteOperationModel<InquiryRequestModel, RefundResponseModel> refundInquiry(
            @PathVariable("noqodiResponseId") String noqodiResponseId
    ) {

        if (StringUtils.isEmpty(noqodiResponseId)) {
            return RemoteOperationModel
                    .<InquiryRequestModel, RefundResponseModel>builder()
                    .build();
        }

        return refundInquiryCommandImpl.execute(new InquiryRequestModel(noqodiResponseId));
    }


    @RequestMapping(value = "/payouts", method = RequestMethod.POST)
    public RemoteOperationModel<PayoutRequestModel, PayoutResponseModel> payout(
            @RequestBody Map<String, BigDecimal> beneficiaryAmountMap
    ) {
        String parentMerchantCode = noqodiApiConfiguration.getTestData().get("payouts-parent-merchant-code");
        String childMerchantCode = noqodiApiConfiguration.getTestData().get("payouts-child-merchant-code");

        MerchantInfo merchantInfo = MerchantInfo.builder()
                .merchantCode(parentMerchantCode)
                .merchantRequestId(UUID.randomUUID().toString())
                .merchantOrderId(UUID.randomUUID().toString())
                .build();

        Amount amount = new Amount("AED", beneficiaryAmountMap.get("beneficiaryOneAmount")
                .add(beneficiaryAmountMap.get("beneficiaryTwoAmount"))
                .setScale(2, RoundingMode.HALF_UP));

        List<Transaction> transactions = RequestHelper.buildTransactionsForPayout(
                childMerchantCode,
                new TransactionAmount(
                        amount.getCurrency(),
                        amount.getValue()  // transaction amount
                ),
                beneficiaryAmountMap
        );

        // PaymentInfo.amount is the sum of all TransactionAmount.amount
        // but in this case, PaymentInfo.amount is is equal to TransactionAmount.amount because we have only one transaction
        PaymentInfo paymentInfo = PaymentInfo.builder()
                .amount(amount)
                .transactions(transactions)
                .build();

        PayoutRequestModel requestModel = PayoutRequestModel.builder()
                .merchantInfo(merchantInfo)
                .paymentInfo(paymentInfo)
                .requestSource("MERCHANT")
                .build();
        return payoutCommandImpl.execute(requestModel);
    }


    @RequestMapping(value = "/payouts/{merchantCode}/{merchantOrderId}", method = RequestMethod.GET)
    public RemoteOperationModel<GetPayoutDetailsRequestModel, PayoutResponseModel> getPayoutDetails(
            @PathVariable("merchantCode") String merchantCode,
            @PathVariable("merchantOrderId") String merchantOrderId
    ) {

        if (StringUtils.isEmpty(merchantCode) || StringUtils.isEmpty(merchantOrderId)) {
            return RemoteOperationModel
                    .<GetPayoutDetailsRequestModel, PayoutResponseModel>builder()
                    .build();
        }

        GetPayoutDetailsRequestModel requestModel = GetPayoutDetailsRequestModel.builder()
                .merchantCode(merchantCode)
                .merchantOrderId(merchantOrderId)
                .build();
        return getPayoutDetailsCommandImpl.execute(requestModel);
    }

}
