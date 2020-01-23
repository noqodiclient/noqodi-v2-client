

import {
    previousBeneficiaryPayout,
    beneficiaryAmount,
    ConfigurationHelper,
    BalanceCommandGroup,
    RefundCommandGroup,
    PayoutCommandGroup,
    PaymentCommandGroup,
    CommandGroupManager,
    CommandResultViewManager,
    DefaultCommandHandler,
    CustomerWalletBalanceCommand,
    MerchantBalanceBeneficiariesCommand,
    MerchantBalanceCommand,
    NewRefundCommand,
    CompleteRefundCommand,
    RefundInquiryCommand,
    PayoutCommand,
    GetPayoutDetailsCommand,
    Helper,
    AccessCredentialsManager,
    CompletedPaymentPhaseManager,
    commandTargetReferences,
    ItemAmountManager
} from "./command-ui-core-module.js";


const replacedRequestText = "This endpoint does not require a request payload. The required path variable(s) are " +
    "<span class=\"text-danger font-weight-normal rounded px-1\" style=\"background: #fdfde9;\">" +
    "highlighted" +
    "</span> " +
    "in the URL above";


export class NewRefundCommandHandler {
    constructor(commandViewManager) {
        this.commandViewManager = commandViewManager;
    }

    preActionHandler() {
        this.commandViewManager.hideView();
    }

    successHandler(commandResponse) {
        if (Helper.isCommandResponseSuccess(commandResponse)) {
            CompletedPaymentPhaseManager.addRefundedPayment(commandResponse.response);
            commandTargetReferences.currentCommandStatusReference.html( '<span class="text-success">refunded</span>' );
        } else {
            CommandResultViewManager.enableExecuteCommandButton(commandTargetReferences.currentCommandButtonReference);
            commandTargetReferences.currentCommandStatusReference.html( '<span class="text-danger">error</span>' );
        }
        this.commandViewManager.showView(Helper.finalizeCommandResult(commandResponse), 140);
    }

    errorHandler() {
        CommandResultViewManager.enableExecuteCommandButton(commandTargetReferences.currentCommandButtonReference);
        commandTargetReferences.currentCommandStatusReference.html( '<span class="text-danger">error</span>' );
    }
}

export class CompleteRefundCommandHandler {
    constructor(commandViewManager) {
        this.commandViewManager = commandViewManager;
    }

    preActionHandler() {
        this.commandViewManager.hideView();
    }

    successHandler(commandResponse) {
        if (Helper.isCommandResponseSuccess(commandResponse)) {
            CompletedPaymentPhaseManager.addRefundedPayment(commandResponse.response);
            commandTargetReferences.currentCommandStatusReference.html( '<span class="text-success">completed</span>' );
        } else {
            CommandResultViewManager.enableExecuteCommandButton(commandTargetReferences.currentCommandButtonReference);
            commandTargetReferences.currentCommandStatusReference.html( '<span class="text-danger">error</span>' );
        }
        this.commandViewManager.showView(Helper.finalizeCommandResult(commandResponse), 140);
    }

    errorHandler() {
        CommandResultViewManager.enableExecuteCommandButton(commandTargetReferences.currentCommandButtonReference);
        commandTargetReferences.currentCommandStatusReference.html( '<span class="text-danger">error</span>' );
    }
}


export class RefundInquiryCommandHandler {
    constructor(commandViewManager) {
        this.commandViewManager = commandViewManager;
    }

    preActionHandler() {
        this.commandViewManager.hideView();
    }

    successHandler(commandResponse) {
        if (Helper.isCommandResponseSuccess(commandResponse)) {
            commandTargetReferences.currentCommandStatusReference.html( '<span class="text-success">done</span>' );
        } else {
            CommandResultViewManager.enableExecuteCommandButton(commandTargetReferences.currentCommandButtonReference);
            commandTargetReferences.currentCommandStatusReference.html( '<span class="text-danger">error</span>' );
        }
        let transformedData = Helper.finalizeCommandResult(this.customTransformer(commandResponse));
        this.commandViewManager.showView(transformedData, 140);
    }

    errorHandler() {
        CommandResultViewManager.enableExecuteCommandButton(commandTargetReferences.currentCommandButtonReference);
        commandTargetReferences.currentCommandStatusReference.html( '<span class="text-danger">error</span>' );
    }

    customTransformer(rawData) {
        let noqodiResponseId = rawData.request.noqodiResponseId;
        let requestUrl = rawData.requestUrl;
        let styledUrl = requestUrl.replace(noqodiResponseId, CommandResultViewManager.styleStart + noqodiResponseId + CommandResultViewManager.styleEnd);
        rawData.request = replacedRequestText;
        rawData.requestUrl = styledUrl;
        return rawData;
    }
}




export class PayoutCommandHandler {
    constructor(commandViewManager, commandButtonId, beneficiaryBalanceCommandForPayout) {
        this.commandViewManager = commandViewManager;
        this.commandButtonId = commandButtonId;
        this.beneficiaryBalanceCommandForPayout = beneficiaryBalanceCommandForPayout;
    }

    preActionHandler() {
        CommandResultViewManager.disableExecuteCommandButton($(this.commandButtonId));
        this.commandViewManager.hideView();
    }

    successHandler(commandResponse) {
        if (Helper.isCommandResponseSuccess(commandResponse)) {
            CompletedPaymentPhaseManager.addRPayouts(commandResponse.response);
            this.beneficiaryBalanceCommandForPayout.execute(null, null, '/PAYOUT');
        }
        this.commandViewManager.showView(Helper.finalizeCommandResult(commandResponse), 140);
        CommandResultViewManager.enableExecuteCommandButton($(this.commandButtonId));
    }

    errorHandler() {
        CommandResultViewManager.enableExecuteCommandButton($(this.commandButtonId));
    }
}


export class GetPayoutDetailsCommandHandler {
    constructor(commandViewManager, customTransformer) {
        this.commandViewManager = commandViewManager;
        this.customTransformer = customTransformer;
    }

    preActionHandler() {
        CommandResultViewManager.disableExecuteCommandButton(commandTargetReferences.currentCommandButtonReference);
        this.commandViewManager.hideView();
    }

    successHandler(commandResponse) {
        if (Helper.isCommandResponseSuccess(commandResponse)) {
            commandTargetReferences.currentCommandStatusReference.html( '<span class="text-success">success</span>' );
        } else {
            commandTargetReferences.currentCommandStatusReference.html( '<span class="text-danger">error</span>' );
        }
        let transformedData = this.customTransformer ? Helper.finalizeCommandResult(this.customTransformer(commandResponse)) : Helper.finalizeCommandResult(commandResponse);
        this.commandViewManager.showView(transformedData, 140);
        CommandResultViewManager.enableExecuteCommandButton(commandTargetReferences.currentCommandButtonReference);
    }

    errorHandler() {
        CommandResultViewManager.enableExecuteCommandButton(commandTargetReferences.currentCommandButtonReference);
        commandTargetReferences.currentCommandStatusReference.html( '<span class="text-danger">error</span>' );
    }
}


export class BeneficiaryBalanceCommandHandlerForPayout {

    preActionHandler() {
    }

    successHandler(commandResponse) {
         /*if (Helper.isCommandResponseSuccess(commandResponse)) {
            let beneOneBalance = commandResponse.response.merchantInquiryInfo.beneficiaries[0].beneCurrentBalance.toFixed(2);
            let beneTwoBalance = commandResponse.response.merchantInquiryInfo.beneficiaries[1].beneCurrentBalance.toFixed(2);
            previousBeneficiaryPayout.beneficiaryOneBalance = beneOneBalance;
            previousBeneficiaryPayout.beneficiaryTwoBalance = beneTwoBalance;
            previousBeneficiaryPayout.beneficiaryOnePayoutAmount = beneOneBalance;
            previousBeneficiaryPayout.beneficiaryTwoPayoutAmount = beneTwoBalance;

            let beneOneAccount = commandResponse.response.merchantInquiryInfo.beneficiaries[0].beneficiaryAcctNumber;
            let beneTwoAccount = commandResponse.response.merchantInquiryInfo.beneficiaries[1].beneficiaryAcctNumber;

            $('#bene-one-account-number').text(beneOneAccount);
            $('#bene-one-current-balance').text('AED ' + beneOneBalance);
            ItemAmountManager.updateItemAmount(beneOneBalance, "beneficiaryOneAmount");
            $('#bene-two-account-number').text(beneTwoAccount);
            $('#bene-two-current-balance').text('AED ' + beneTwoBalance);
            ItemAmountManager.updateItemAmount(beneTwoBalance, "beneficiaryTwoAmount");
        }*/
    }

    errorHandler() {
        ItemAmountManager.updateItemAmount(0, "beneficiaryOneAmount");
        ItemAmountManager.updateItemAmount(0, "beneficiaryTwoAmount");
    }
}


(function () {

    initConfiguration(ConfigurationHelper.builder());

    const commandHeaderText = {
        "balances-command-nav-link": "Balances",
        "payments-command-nav-link": "Payments",
        "payouts-command-nav-link": "Payouts",
        "refunds-command-nav-link": "Refunds",
        "vouchers-command-nav-link": "Vouchers"
    };

    const commandResultViewManager = new CommandResultViewManager();

    const balanceMerchantCommandButton = $('#balance-merchant-command-button');
    const balanceMerchantBeneficiariesCommandButton = $('#balance-merchant-beneficiaries-command-button');
    const balanceCustomerWalletCommandButton = $('#balance-customer-wallet-command-button');


    const merchantBalanceCommandHandler = new DefaultCommandHandler(commandResultViewManager, balanceMerchantCommandButton,function (rawData) {
        let merchantCode = rawData.request.merchantCode;
        let requestUrl = rawData.requestUrl;
        let styledUrl = requestUrl.replace(merchantCode, CommandResultViewManager.styleStart + merchantCode + CommandResultViewManager.styleEnd);
        rawData.request = replacedRequestText;
        rawData.requestUrl = styledUrl;
        return rawData;
    });

    const merchantBalanceBeneficiariesCommandHandler = new DefaultCommandHandler(commandResultViewManager, balanceMerchantBeneficiariesCommandButton,function (rawData) {
        let merchantCode = rawData.request.merchantCode;
        let beneficiaries = rawData.request.beneficiaries;
        let requestUrl = rawData.requestUrl;
        let styledUrl = requestUrl.replace(merchantCode, CommandResultViewManager.styleStart + merchantCode + CommandResultViewManager.styleEnd);
        styledUrl = styledUrl.replace(beneficiaries, CommandResultViewManager.styleStart + beneficiaries + CommandResultViewManager.styleEnd);
        rawData.request = replacedRequestText;
        rawData.requestUrl = styledUrl;
        return rawData;
    });

    const customerWalletBalanceCommandHandler = new DefaultCommandHandler(commandResultViewManager, balanceCustomerWalletCommandButton, function (rawData) {
        let walletId = rawData.request.walletId;
        let requestUrl = rawData.requestUrl;
        let styledUrl = requestUrl.replace(walletId, CommandResultViewManager.styleStart + walletId + CommandResultViewManager.styleEnd);
        rawData.request = replacedRequestText;
        rawData.requestUrl = styledUrl;
        return rawData;
    });

    const beneficiaryBalanceCommandHandlerForPayout = new BeneficiaryBalanceCommandHandlerForPayout();
    const beneficiaryBalanceCommandForPayout = new MerchantBalanceBeneficiariesCommand(beneficiaryBalanceCommandHandlerForPayout);

    const payoutCommandHandler = new PayoutCommandHandler(commandResultViewManager, "#execute-payout-button", beneficiaryBalanceCommandForPayout);
    const payoutDetailsCommandHandler = new GetPayoutDetailsCommandHandler(commandResultViewManager, function (rawData) {
        let merchantCode = rawData.request.merchantCode;
        let merchantOrderId = rawData.request.merchantOrderId;
        let requestUrl = rawData.requestUrl;
        let styledUrl = requestUrl.replace(merchantCode, CommandResultViewManager.styleStart + merchantCode + CommandResultViewManager.styleEnd);
        styledUrl = styledUrl.replace(merchantOrderId, CommandResultViewManager.styleStart + merchantOrderId + CommandResultViewManager.styleEnd);
        rawData.request = replacedRequestText;
        rawData.requestUrl = styledUrl;
        return rawData;
    });


    // initialize balance commands
    const balanceCommands = {
        "merchantBalanceCommand": new MerchantBalanceCommand(merchantBalanceCommandHandler),
        "merchantBalanceBeneficiariesCommand": new MerchantBalanceBeneficiariesCommand(merchantBalanceBeneficiariesCommandHandler),
        "customerWalletBalanceCommand": new CustomerWalletBalanceCommand(customerWalletBalanceCommandHandler)
    };


    // initialize refund commands
    const refundCommands = {
        "newRefundCommand": new NewRefundCommand(new NewRefundCommandHandler(commandResultViewManager)),
        "completeRefundCommand": new CompleteRefundCommand(new CompleteRefundCommandHandler(commandResultViewManager)),
        "refundInquiryCommand": new RefundInquiryCommand(new RefundInquiryCommandHandler(commandResultViewManager))
    };

    // initialize payout commands
    const payoutCommands = {
        "payoutCommand": new PayoutCommand(payoutCommandHandler),
        "getPayoutDetailsCommand": new GetPayoutDetailsCommand(payoutDetailsCommandHandler)
    };

    const commandGroups = {
        "balances-command-nav-link": new BalanceCommandGroup(balanceCommands),
        "payments-command-nav-link": new PaymentCommandGroup(),
        "refunds-command-nav-link": new RefundCommandGroup(refundCommands, commandResultViewManager),
        "payouts-command-nav-link": new PayoutCommandGroup(payoutCommands, commandResultViewManager, beneficiaryBalanceCommandForPayout)
    };

    const commandGroupManager = new CommandGroupManager(commandResultViewManager, commandGroups, commandHeaderText);
    // defaults to balance operation onload
    CommandResultViewManager.enableExecuteCommandButton(balanceMerchantCommandButton);
    commandGroupManager.activateCommandGroupById('balances-command-nav-link');

    $(document).ready(function () {
        AccessCredentialsManager.setup();
        CompletedPaymentPhaseManager.setup();
    });
})();

