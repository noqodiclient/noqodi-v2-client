package com.noqodi.client.v2api.domain.helpers;

import com.noqodi.client.v2api.domain.factory.ObjectFactory;
import com.noqodi.client.v2api.domain.models.common.*;
import com.noqodi.client.v2api.configurations.NoqodiApiConfiguration;
import com.noqodi.client.v2api.domain.constants.OAuthGrantType;
import com.noqodi.client.v2api.domain.models.token.AbstractAccessTokenRequestModel;
import com.noqodi.client.v2api.domain.models.token.CCGAccessTokenRequestModel;
import com.noqodi.client.v2api.domain.models.token.PGAccessTokenRequestModel;

import java.math.BigDecimal;
import java.util.*;

import static com.noqodi.client.v2api.domain.helpers.Helper.buildUrl;

public class RequestHelper {

    public static CCGAccessTokenRequestModel buildSampleAccessTokenCCGRequest(NoqodiApiConfiguration configuration) {
        String requestUrl = buildUrl(configuration.getBaseUrl(), null, configuration.getEndpoints().getOauthUris().getClientCredentialsUri());
        String clientId = configuration.getCredentials().getClientId();
        String clientSecret = configuration.getCredentials().getClientSecret();

        return CCGAccessTokenRequestModel
                .builder(OAuthGrantType.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .requestUrl(requestUrl)
                .build();
    }

    public static AbstractAccessTokenRequestModel buildSampleAccessTokenPGRequest(NoqodiApiConfiguration configuration) {
        return getCCGAccessTokenRequestModel(configuration);
//        return getPGAccessTokenRequestModel(configuration);
    }

    private static AbstractAccessTokenRequestModel getCCGAccessTokenRequestModel(NoqodiApiConfiguration configuration) {
        String clientId = configuration.getCredentials().getClientId();
        String clientSecret = configuration.getCredentials().getClientSecret();
        String requestUrl = buildUrl(configuration.getBaseUrl(), null, configuration.getEndpoints().getOauthUris().getClientCredentialsUri());

        return CCGAccessTokenRequestModel
                .builder(OAuthGrantType.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .requestUrl(requestUrl)
                .build();
    }

    private static AbstractAccessTokenRequestModel getPGAccessTokenRequestModel(NoqodiApiConfiguration configuration) {
        NoqodiApiConfiguration.User user = configuration.getCredentials()
                .getUsers()
                .stream()
                .filter(x -> x.getRole() == NoqodiApiConfiguration.Role.ROLE_ADMIN)
                .findFirst()
                .get();

        String clientId = configuration.getCredentials().getClientId();
        String clientSecret = configuration.getCredentials().getClientSecret();
        String username = user.getUsername();
        String password = user.getPassword();
        String requestUrl = buildUrl(configuration.getBaseUrl(), null, configuration.getEndpoints().getOauthUris().getPasswordUri());

        return PGAccessTokenRequestModel
                .builder(OAuthGrantType.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .password(password)
                .requestUrl(requestUrl)
                .build();
    }


    private static List<Beneficiary> getSampleBeneficiaries(Map<String, BigDecimal> beneficiaryAmountMap, String beneficiaryAccounts) {
        String[] beneficiaries = beneficiaryAccounts.split(",");

        BeneficiaryAmount bene1Amount = new BeneficiaryAmount("AED", beneficiaryAmountMap.get("beneficiaryOneAmount"));
        Beneficiary bene1 = Beneficiary.builder()
                .beneficiaryAcctNumber(beneficiaries[0])
                .beneficiaryName("DubaiStore Commission")
                .beneficiaryAmount(bene1Amount)
                .build();

        BeneficiaryAmount bene2Amount = new BeneficiaryAmount("AED", beneficiaryAmountMap.get("beneficiaryTwoAmount"));
        Beneficiary bene2 = Beneficiary.builder()
                .beneficiaryAcctNumber(beneficiaries[1])
                .beneficiaryName("DubaiStore Commission VAT")
                .beneficiaryAmount(bene2Amount)
                .build();

        return Arrays.asList(bene1, bene2);
    }


    public static List<Transaction> buildTransactionsForCapture(String merchantCode, TransactionAmount transactionAmount, Map<String, BigDecimal> orderAmount) {
        String beneficiaryAccounts = ObjectFactory.getInstance().getConfiguration().getTestData().get("payment-merchant-beneficiaries");
        List<Beneficiary> beneficiaries = getSampleBeneficiaries(orderAmount, beneficiaryAccounts);
        Transaction transaction = Transaction.builder()
                .merchantReferenceId(UUID.randomUUID().toString()) // unique per request
                .merchantCode(merchantCode)
                .transactionAmount(transactionAmount)
                .beneficiaries(beneficiaries)
                .build();

        return Collections.singletonList(transaction);
    }

    public static MerchantInfo buildMerchantInfoForPreAuth(String merchantCode, String landingUrl) {
        return MerchantInfo.builder()
                .merchantLandingURL(landingUrl)
                .merchantCode(merchantCode)
                .merchantOrderId(UUID.randomUUID().toString()) // unique for entire cycle
                .merchantRequestId(UUID.randomUUID().toString()) // unique per request
                .build();
    }

    public static List<Transaction> buildTransactionsForPayout(
            String merchantCode, TransactionAmount transactionAmount,
            Map<String, BigDecimal> beneficiaryAmountMap) {
        String beneficiaryAccounts = ObjectFactory.getInstance().getConfiguration().getTestData().get("payouts-merchant-beneficiaries");
        List<Beneficiary> beneficiaries = getSampleBeneficiaries(beneficiaryAmountMap, beneficiaryAccounts);
        Transaction transaction = Transaction.builder()
                .transactionDescription("Payout for merchant: " + merchantCode)
                .merchantReferenceId(UUID.randomUUID().toString()) // unique per request
                .merchantCode(merchantCode)
                .transactionAmount(transactionAmount)
                .beneficiaries(beneficiaries)
                .build();

        return Collections.singletonList(transaction);
    }


}
