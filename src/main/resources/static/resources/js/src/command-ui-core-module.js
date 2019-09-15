export class CommandGroupManager {

    constructor(commandViewManager, commandGroups, commandHeaderText) {
        this.commandViewManager = commandViewManager;
        this.commandGroups = commandGroups;
        this.commandHeaderText = commandHeaderText;
        this.setup();
    }

    setup() {
        let self = this;
        $(".command-nav-link").off().on("click", function (event) {
            let commandPahtname = contextPath ? contextPath+'/web/commands' : '/web/commands';
            if (window.location.pathname === commandPahtname) {
                event.preventDefault();
                let commandId = $(this)[0].id;
                if (!self.commandGroups[commandId]) {
                    alert('work in progress... ' +
                        '\nbut hey, you can try implementing it yourself... ' +
                        '\n - simply clone the git repo... ' +
                        '\n - and check out our online documentation...');
                    return;
                }
                CommandGroupManager.toggleActiveCommandGroupLink($(this));
                self.commandViewManager.hideView();
                self.updateCommandGroupHeaderTest(commandId);
                self.activateCommandGroupById(commandId);
            }
        });
    }

    static toggleActiveCommandGroupLink(reference) {
        let commandId = reference[0].id;
        reference.addClass('active');
        $('#' + commandId).siblings('.command-nav-link').removeClass('active');
    }

    updateCommandGroupHeaderTest(commandId, callback) {
        let apiGroupHeaderText = $('#api-group-header-text');
        apiGroupHeaderText.fadeOut('fast', function () {
            let commandHeaderHtml = "<sppan class='text-danger'>" + this.commandHeaderText[commandId] + '</sppan>';
            let headerText = 'Explore noqodi ' + commandHeaderHtml + ' API';
            apiGroupHeaderText.html(headerText);
            apiGroupHeaderText.fadeIn('slow', callback);
        }.bind(this));
    }

    activateCommandGroupById(commandId) {
        let currentCommand = this.commandGroups[commandId];
        let commandHeaderTextId = currentCommand.setup();
        this.updateCommandGroupHeaderTest(commandHeaderTextId, function () {
            currentCommand.commandGroupContainerDiv.siblings('.commands-container').hide();
            currentCommand.commandGroupContainerDiv.fadeIn(1500);
        });
    }
}

export class CommandResultViewManager {

    constructor() {
        CommandResultViewManager.styleStart = '<span class="text-danger font-weight-normal rounded px-1" style="background: #fdfde9;">';
        CommandResultViewManager.styleEnd = '</span>';

        $('#balance-commands-div-carousel').on('slide.bs.carousel', function () {
            this.hideView();
        }.bind(this));

        this.commandResponseJsonView = $('#command-response-json-view');
        this.commandRequestJsonView = $('#command-request-json-view');
        this.commandRequestText = $('#command-request-text');
        this.commandResponseText = $('#command-response-text');
        this.commandHttpMethodText = $('#command-http-method-text');
        this.commandHttpUrlText = $('#command-http-url-text');
        this.commandHttpStatusText = $('#command-http-status-text');
        this.commandHeaderText = $('#command-response-header-text');
        this.commandResultComponent = $("#command-result-component-div");
    }

    hideView() {
        this.unPpretify();
        this.toggleView('hide');
        Helper.scrollTop($('html, body'));
    }

    showView(data, offset) {
        setTimeout(function () {
            this.updateCommandResponseView(data);
            this.pretify();
            this.toggleView('show');
            Helper.scrollTop(this.commandResultComponent, offset);
        }.bind(this), 400);
    }

    toggleView(state) {
        this.commandResultComponent.collapse(state);
    }

    static disableExecuteCommandButton(buttonReference) {
        if (!buttonReference) return;
        CommandResultViewManager.toggleExecuteCommandButton(buttonReference, true);
    }

    static enableExecuteCommandButton(buttonReference) {
        if (!buttonReference) return;
        setTimeout(function () {
            CommandResultViewManager.toggleExecuteCommandButton(buttonReference, false);
        }, 1000);
    }

    static toggleExecuteCommandButton(buttonReference, state) {
        if (buttonReference) buttonReference.attr("disabled", state);
    }

    pretify() {
        PR.prettyPrint();
    }

    unPpretify() {
        this.commandResponseJsonView.removeClass('prettyprinted').removeAttr('style');
        this.commandRequestJsonView.removeClass('prettyprinted').removeAttr('style');
    }

    updateCommandResponseView(data) {
        if (data.isJsonRequest) {
            this.commandRequestText.hide();
            this.commandRequestJsonView.show().html(data.request);
        } else {
            this.commandRequestJsonView.hide();
            this.commandRequestText.show().html(data.request);
        }

        if (data.isJsonResponse) {
            this.commandResponseText.hide();
            this.commandResponseJsonView.show().html(data.response);
        } else {
            this.commandResponseJsonView.hide();
            this.commandResponseText.show().html(data.response);
        }

        this.commandHttpMethodText.text(data.httpMethod);
        this.commandHttpUrlText.html(data.requestUrl);
        this.commandHttpStatusText.text(data.httpStatus);
        this.commandHeaderText.text(data.headerText);
    }
}

export class BalanceCommandGroup {

    constructor(commands) {
        this.commands = commands;
        this.commandHeaderTextId = 'balances-command-nav-link';
        this.commandGroupContainerDiv = $("#balance-commands-container");
    }

    setup() {
        this.initMerchantOperation();
        this.initMerchantBeneOperation();
        this.initCustomerWalletOperation();
        return this.commandHeaderTextId;
    }

    initMerchantOperation() {
        let balanceMerchantCommandButton = $('#balance-merchant-command-button');
        balanceMerchantCommandButton.off().on("click", function (event) {
            BalanceCommandGroup.doCommonRoutine(event);
            this.commands.merchantBalanceCommand.execute();
        }.bind(this));
    }

    initMerchantBeneOperation() {
        let balanceMerchantBeneficiariesCommandButton = $('#balance-merchant-beneficiaries-command-button');
        balanceMerchantBeneficiariesCommandButton.off().on("click", function (event) {
            BalanceCommandGroup.doCommonRoutine(event);
            this.commands.merchantBalanceBeneficiariesCommand.execute(null, null, '/BALANCE');
        }.bind(this));
    }

    initCustomerWalletOperation() {
        let balanceCustomerWalletCommandButton = $('#balance-customer-wallet-command-button');
        balanceCustomerWalletCommandButton.off().on("click", function (event) {
            BalanceCommandGroup.doCommonRoutine(event);
            this.commands.customerWalletBalanceCommand.execute();
        }.bind(this));
    }

    static doCommonRoutine(event) {
        event.preventDefault();
    }
}

export class PaymentCommandGroup {

    constructor() {
        this.commandHeaderTextId = 'payments-command-nav-link';
        this.commandGroupContainerDiv = $("#payment-commands-container");
    }

    setup() {
        return this.commandHeaderTextId;
    }
}


export class RefundCommandGroup {

    constructor(commands, commandViewManager) {
        this.commands = commands;
        this.commandHeaderTextId = 'refunds-command-nav-link';
        this.commandGroupContainerDiv = $("#refunds-commands-container");
        this.commandViewManager = commandViewManager;
    }


    setup() {
        let self = this;
        $('#refund-nav-pills').off('shown.bs.tab').on('shown.bs.tab', function (event) {
            self.commandViewManager.hideView();
            let currentTabId = event.target.id;
            // let previousTabId = event.relatedTarget.id;
            switch (currentTabId) {
                case "nav-new-refund-tab":
                    self.initNewRefundOperation();
                    break;
                case "nav-complete-refund-tab":
                    self.initCompleteRefundOperation();
                    break;
                case "nav-refund-inquiry-tab":
                    self.initRefundInquiryOperation();
                    break;
            }
        });
        this.initNewRefundOperation();
        this.initCompleteRefundOperation();
        this.initRefundInquiryOperation();
        return this.commandHeaderTextId;
    }

    initNewRefundOperation() {
        $('#new-refund-items-feedback').hide();
        let captured = CompletedPaymentPhaseManager.getCapturedPayments();
        let refunded = CompletedPaymentPhaseManager.getRefundedPayments();
        let nonRefunded = captured.filter(x => !refunded.some(y => x.merchantInfo.merchantOrderId === y.merchantInfo.merchantOrderId));

        if (!nonRefunded || nonRefunded.length === 0) {
            $('#new-refund-items-feedback').show();
            nonRefunded = [];
            // return;
        }
        let self = this;
        TabulatedPaymentViewHelper.commandSetup( true,function(payment) {
            self.commands.newRefundCommand.execute(payment);
        }, nonRefunded, '#non-refunded-payment-list', 'refund', '-new-refund');
    }

    initCompleteRefundOperation() {
        $('#complete-refund-items-feedback').hide();
        let captured = CompletedPaymentPhaseManager.getCapturedPayments();

        if (!captured || captured.length === 0) {
            $('#complete-refund-items-feedback').show();
            captured = [];
            // return;
        }
        let self = this;
        TabulatedPaymentViewHelper.commandSetup(true,function(payment) {
            let request = {
                "serviceType": "REFUND",
                "merchantInfo": {
                    "merchantCode": payment.merchantInfo.merchantCode,
                    "merchantRequestId": payment.merchantInfo.merchantRequestId,
                    "merchantOrderId": payment.merchantInfo.merchantOrderId
                }
            };
            self.commands.completeRefundCommand.execute(request);
        }, captured, '#maybe-refunded-payment-list', 'complete refund', '-complete-refund');
    }

    initRefundInquiryOperation() {
        $('#refund-inquiry-items-feedback').hide();
        let refunded = CompletedPaymentPhaseManager.getRefundedPayments();

        if (!refunded || refunded.length === 0) {
            $('#refund-inquiry-items-feedback').show();
            refunded = [];
            // return;
        }
        let self = this;
        TabulatedPaymentViewHelper.commandSetup(true,function(payment) {
            self.commands.refundInquiryCommand.execute(null, null, '/'+payment.id);
        }, refunded, '#definitely-refunded-payment-list', 'refund inquiry', '-refund-inquiry');
    }

    static doCommonRoutine(event) {
        event.preventDefault();
    }
}


class TabulatedPaymentViewHelper {

    static getPaidOutTransactionDate(merchantOrderId) {
        return TabulatedPaymentViewHelper.getTransactionDate(merchantOrderId, CompletedPaymentPhaseManager.getPayouts());
    }

    static getCapturedTransactionDate(merchantOrderId) {
        return TabulatedPaymentViewHelper.getTransactionDate(merchantOrderId, CompletedPaymentPhaseManager.getCapturedPayments());
    }

    static getTransactionDate(merchantOrderId, paymentList) {
        let result = paymentList.find(x => {
            return x.merchantInfo.merchantOrderId === merchantOrderId;
        });
        return result ? result.paymentInfo.transactionDate : "N/A";
    }

    static commandSetup(isRefund, callback, paymentList, paymentListContainer, commandButtonValue, commandButtonIdSuffix) {
        $(paymentListContainer).empty();
        paymentList.forEach(function (item, index) {
            let merchantOrderId = item.merchantInfo.merchantOrderId;
            let row = $("<tr></tr>");
            let col1 = $("<th scope='row'></th>").text(index+1);
            let col2 = $("<td></td>");
            let card = $("<div class='card border-0 p-0 m-0'></div>").css({
                width: "100%"
            });
            let cardBody = $("<div class='card-body text-muted p-0 m-0'></div>").css({
                'font-size': "16px"
            });
            let cardText = $("<div class='card-text'></div>");
            let orderAmountText = $("<p class='p-0 m-0'>Transaction Amount: </p>");
            let orderAmountTextValue = $("<span class='text-danger'>" + item.paymentInfo.amount.currency + ' ' + item.paymentInfo.amount.value + "</span>");
            let orderDateText = $("<p class='p-0 m-0'>Transaction Date: </p>");
            let paymentDate = isRefund ? TabulatedPaymentViewHelper.getCapturedTransactionDate(merchantOrderId) : TabulatedPaymentViewHelper.getPaidOutTransactionDate(merchantOrderId);
            let orderDateTextValue = $("<span class='text-danger'>" + paymentDate + "</span>");
            let orderIdText = $("<p class='p-0 m-0'>Merchant Order ID: </p>");
            let orderIdTextValue = $("<span class='text-danger'>" + merchantOrderId + "</span>");
            let commandDiv = $("<div class='my-2'></div>");
            let button = $("<button class='btn btn-muted'>"+commandButtonValue+"</button>").attr({
                id: item.merchantInfo.merchantOrderId + commandButtonIdSuffix,
                class: 'btn btn-muted'
            });
            let commandStatus = $("<span></span>");

            button.off().on('click', function(){
                commandTargetReferences.currentCommandButtonReference = button;
                commandTargetReferences.currentCommandStatusReference = commandStatus;
                CommandResultViewManager.disableExecuteCommandButton(button);
                RefundCommandGroup.doCommonRoutine(event);
                callback(item);
            });

            orderAmountText.append(orderAmountTextValue);
            orderDateText.append(orderDateTextValue);
            orderIdText.append(orderIdTextValue);
            commandDiv.append(button).append("&nbsp;&nbsp;").append(commandStatus);
            cardText.append(orderAmountText).append(orderDateText).append(orderIdText).append(commandDiv);
            cardBody.append(cardText);
            card.append(cardBody);
            col2.append(card);
            row.append(col1).append(col2);
            $(paymentListContainer).append(row);
        });
    }
}


export const beneficiaryAmount = {
    beneficiaryOneAmount: 3599.99,
    beneficiaryTwoAmount: 1999.99
};


export const previousBeneficiaryPayout = {
    beneficiaryOnePayoutAmount: 0,
    beneficiaryTwoPayoutAmount: 0,
    beneficiaryOneBalance: 0,
    beneficiaryTwoBalance: 0,
};


export class PayoutCommandGroup {

    constructor(commands, commandViewManager, beneficiaryBalanceCommandForPayout) {
        this.commands = commands;
        this.commandViewManager = commandViewManager;
        this.commandHeaderTextId = 'payouts-command-nav-link';
        this.commandGroupContainerDiv = $("#payouts-commands-container");
        this.beneficiaryBalanceCommandForPayout = beneficiaryBalanceCommandForPayout;
    }

    setup() {
        let self = this;
        $('#payout-nav-pills').off('shown.bs.tab').on('shown.bs.tab', function (event) {
            self.commandViewManager.hideView();
            let currentTabId = event.target.id;
            // let previousTabId = event.relatedTarget.id;
            switch (currentTabId) {
                case "nav-new-payout-tab":
                    self.initNewPayoutOperation();
                    self.beneficiaryBalanceCommandForPayout.execute(null, null, '/PAYOUT');
                    break;
                case "nav-payout-details-tab":
                    self.initPayoutDetailsOperation();
                    break;
            }
        });
        this.initNewPayoutOperation();
        this.initPayoutDetailsOperation();
        this.beneficiaryBalanceCommandForPayout.execute(null, null, '/PAYOUT');
        ItemAmountManager.init();
        return this.commandHeaderTextId;
    }

    initNewPayoutOperation() {
        let executePayoutButton = $('#execute-payout-button');
        executePayoutButton.off().on("click", function (event) {
            PayoutCommandGroup.doCommonRoutine(event);

            $('#bene-one-previous-balance-div').show();
            $('#bene-one-previous-payout-amount-div').show();
            $('#bene-two-previous-balance-div').show();
            $('#bene-two-previous-payout-amount-div').show();
            $('#prevoius-payout-total-label').show();
            $('#prevoius-payout-total-text').show();

            $('#bene-one-previous-balance').text('AED ' + previousBeneficiaryPayout.beneficiaryOneBalance);
            $('#bene-one-previous-payout-amount').text('AED ' + beneficiaryAmount.beneficiaryOneAmount);
            $('#bene-two-previous-balance').text('AED ' + previousBeneficiaryPayout.beneficiaryTwoBalance);
            $('#bene-two-previous-payout-amount').text('AED ' + beneficiaryAmount.beneficiaryTwoAmount);
            $('#prevoius-payout-total-text').text('AED ' + (+beneficiaryAmount.beneficiaryOneAmount + +beneficiaryAmount.beneficiaryTwoAmount).toFixed(2));
            this.commands.payoutCommand.execute(beneficiaryAmount);
        }.bind(this));
    }


    initPayoutDetailsOperation() {
        $('#get-payout-details-items-feedback').hide();
        let paidOut = CompletedPaymentPhaseManager.getPayouts();

        if (!paidOut || paidOut.length === 0) {
            $('#get-payout-details-items-feedback').show();
            paidOut = [];
            // return;
        }
        let self = this;
        TabulatedPaymentViewHelper.commandSetup(false,function(payment) {
            let merchantCode = payment.merchantInfo.merchantCode;
            let merchantOrderId = payment.merchantInfo.merchantOrderId;
            let pathVariable = '/' + merchantCode + '/' + merchantOrderId;
            self.commands.getPayoutDetailsCommand.execute(null, null, pathVariable);
        }, paidOut, '#payouts-payment-list', 'get payout details', '-get-payout-details');
    }

    static doCommonRoutine(event) {
        event.preventDefault();
    }
}




const popoverTemplate = `<div class="popover shadow border-0 amount-editor-popover" role="tooltip">
    <div class="arrow"></div>
    <h3 class="popover-header bg-danger text-white"></h3>
    <div class="popover-body"></div>
</div>`;


const editableOrderItem = `
<div class="row px-3">
    <div class="col-9 m-0 p-0 pr-1">    
        <div class="input-group my-auto">
            <div class="input-group-prepend">
                <span class="input-group-text">AED</span>
            </div>
            <input id="order-item-new-amount" type="number" class="form-control input-sm" placeholder="Amount" aria-label="Amount">
        </div>
    </div>
    <div class="col-3 row m-0 p-0 pl-1">
        <div class="col m-0 p-0 pr-1">
            <h3 id="update-item-amount-button" class="text-success m-0"><i class="fas fa-check"></i></h3>
        </div>
        <div class="col m-0 p-0 pl-1">
            <h3 id="cancel-update-item-amount-button" class="text-danger m-0"><i class="fas fa-times"></i></h3>
        </div>
    </div>
</div>`;


export class ItemAmountManager {

    static get itemOneEditButton() {
        return $('#item-1-amount-edit');
    }
    static get itemTwoEditButton() {
        return $('#item-2-amount-edit');
    }
    static get editDisabled() {
        return ItemAmountManager._editDisabled;
    }
    static set editDisabled(value) {
        ItemAmountManager._editDisabled = value;
    }

    static init() {
        ItemAmountManager.initItemEditorPopover(ItemAmountManager.itemOneEditButton);
        ItemAmountManager.initItemEditorPopover(ItemAmountManager.itemTwoEditButton);
        ItemAmountManager.initItemEditorToggle();
        ItemAmountManager.initItemEditorActions(ItemAmountManager.itemOneEditButton, 'beneficiaryOneAmount');
        ItemAmountManager.initItemEditorActions(ItemAmountManager.itemTwoEditButton, 'beneficiaryTwoAmount');
        ItemAmountManager.updateOrderAmount(beneficiaryAmount.beneficiaryOneAmount, beneficiaryAmount.beneficiaryTwoAmount);
    }

    static updateItemAmount(itemAmount, target) {
        if (isNaN(itemAmount)) return;
        if (target === 'beneficiaryOneAmount') {
            beneficiaryAmount.beneficiaryOneAmount = (+itemAmount).toFixed(2);
        } else if (target === 'beneficiaryTwoAmount') {
            beneficiaryAmount.beneficiaryTwoAmount = (+itemAmount).toFixed(2);
        }
        ItemAmountManager.updateOrderAmount(beneficiaryAmount.beneficiaryOneAmount, beneficiaryAmount.beneficiaryTwoAmount);
    }

    static updateOrderAmount(beneficiaryOneAmount, beneficiaryTwoAmount) {
        $('#item-1-amount-text').text('AED ' + beneficiaryOneAmount);
        $('#item-2-amount-text').text('AED ' + beneficiaryTwoAmount);
        $('#order-total-text').text('AED ' + (+beneficiaryOneAmount + +beneficiaryTwoAmount).toFixed(2));
    }

    static initItemEditorPopover(element) {
        element.popover({
            container: 'body',
            html: true,
            placement: 'left',
            title: 'Update Amount',
            trigger: 'manual',
            animation: false,
            template: popoverTemplate,
            content: editableOrderItem
        });
    }

    static initItemEditorActions(itemEditButton, target) {
        itemEditButton.on('shown.bs.popover', function () {
            let updateButton = $('#update-item-amount-button');
            let cancelButton = $('#cancel-update-item-amount-button');
            let orderItemNewAmount = $('#order-item-new-amount');
            orderItemNewAmount.val(ItemAmountManager.getCurrentValue(target));
            updateButton.off().on('click', function () {
                ItemAmountManager.updateItemAmount(orderItemNewAmount.val(), target);
                ItemAmountManager.hideAll();
            });
            cancelButton.off().on('click', function () {
                ItemAmountManager.hideAll();
            });
        });
    }

    static getCurrentValue(target) {
        if (target === 'beneficiaryOneAmount') {
            return beneficiaryAmount.beneficiaryOneAmount;
        } else if (target === 'beneficiaryTwoAmount') {
            return beneficiaryAmount.beneficiaryTwoAmount;
        } else {
            return 0;
        }
    }

    static hideAll() {
        ItemAmountManager.itemOneEditButton.popover('hide');
        ItemAmountManager.itemTwoEditButton.popover('hide');
    }

    static initItemEditorToggle() {
        ItemAmountManager.itemOneEditButton.off().on('click', function () {
            if (ItemAmountManager.editDisabled) return;
            ItemAmountManager.itemOneEditButton.popover('toggle');
        });
        ItemAmountManager.itemOneEditButton.on('show.bs.popover', function () {
            ItemAmountManager.itemTwoEditButton.popover('hide');
        });
        ItemAmountManager.itemTwoEditButton.off().on('click', function () {
            if (ItemAmountManager.editDisabled) return;
            ItemAmountManager.itemTwoEditButton.popover('toggle');
        });
        ItemAmountManager.itemTwoEditButton.on('show.bs.popover', function () {
            ItemAmountManager.itemOneEditButton.popover('hide');
        });
    }
}








export class DefaultCommandHandler {
    constructor(commandViewManager, commandButton, customTransformer) {
        this.commandViewManager = commandViewManager;
        this.commandButton = commandButton;
        this.customTransformer = customTransformer;
    }

    preActionHandler() {
        CommandResultViewManager.disableExecuteCommandButton(this.commandButton);
        this.commandViewManager.hideView();
    }

    successHandler(commandResponse) {
        let transformedData = this.customTransformer ? Helper.finalizeCommandResult(this.customTransformer(commandResponse)) : Helper.finalizeCommandResult(commandResponse);
        this.commandViewManager.showView(transformedData, 140);
        CommandResultViewManager.enableExecuteCommandButton(this.commandButton);
    }

    errorHandler() {
        CommandResultViewManager.enableExecuteCommandButton(this.commandButton);
    }
}



export class GetAppApiAccessTokenCommandHandler {

    constructor(preActionHandler, successHandler, errorHandler) {
        this.preActionHandler = preActionHandler;
        this.successHandler = successHandler;
        this.errorHandler = errorHandler;
    }

    preActionHandler() {
        this.preActionHandler();
    }

    successHandler(commandResponse) {
        this.successHandler(commandResponse);
    }

    errorHandler() {
        this.errorHandler();
    }
}


export class BaseCommand {
    constructor(uri, type, commandHandler, isJsonRequest, isJsonResponse, headerText) {
        this.uri = uri;
        this.type = type;
        this.commandHandler = commandHandler;
        this.isJsonRequest = isJsonRequest;
        this.isJsonResponse = isJsonResponse;
        this.headerText = headerText + ' - API Request/Response';
    }

    execute(request, basicCredentials, pathVariable) {
        let isAuthenticationRequest = !!basicCredentials;

        this.commandHandler.preActionHandler();

        let self = this;
        let fullUrl = configuration.appBaseUrl + configuration.apiBasePath + this.uri + (pathVariable ? pathVariable : '');
        let authorization = isAuthenticationRequest ?
            "Basic " + btoa(
            basicCredentials.username  + ":" + basicCredentials.password
            ) : "Bearer " + AccessCredentialsManager.getApiAccessToken();

        let options = {
            xhrFields: {
                withCredentials: true
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader (
                    "Authorization", authorization);
            },
            success: function (commandResponse) {
                commandResponse.isJsonRequest = self.isJsonRequest;
                commandResponse.isJsonResponse = self.isJsonResponse;
                commandResponse.headerText = self.headerText;
                self.commandHandler.successHandler(commandResponse);

                if (isAuthenticationRequest) return;
                // clear any saved API call reference, if any
                AccessCredentialsManager.setFailedApiCommandAndOptions(null, null);
            },
            error: function (xhr) {
                if (xhr.status === 401 || xhr.status === 403) {
                    let errorMessage = '';
                    if (basicCredentials) {
                        errorMessage = 'The server is not happy with the credentials you provided...' +
                            '\n- you can either keep guessing...' +
                            '\n- or phone a friend...' +
                            '\n- or as a last resort, ask us nicely...';
                    } else {
                        errorMessage = 'Ummm... your session expired many many many centuries ago...' +
                            '\n2000 BC, to be precise...' +
                            '\nplease re-login if you want to stay happy on this site...';
                    }
                    alert(errorMessage);
                    AccessCredentialsManager.deleteAccessCredentials();
                    AccessCredentialsManager.showAccessCredentialsForm();

                    if (isAuthenticationRequest) return;
                    // save API call reference, to be retried when a new access token is obtained
                    let options = {
                        request: request,
                        pathVariable: pathVariable
                    };
                    AccessCredentialsManager.setFailedApiCommandAndOptions(self, options);
                } else {
                    alert("Oops! here goes an undocumented feature :(" +
                        "\nplease tell us about it so we can fix it.");
                }
                self.commandHandler.errorHandler();
            },
            type: self.type,
            url: fullUrl
        };

        if (request) {
            options.data = JSON.stringify(request);
            options.contentType = "application/json";
        }

        $.ajax(options);
    }


    static encode(rawString) {
        if (rawString) return '/'+ encodeURI(rawString);
        return '';
    }
}

// get access token
class GetAppApiAccessTokenCommand extends BaseCommand {
    constructor(commandHandler) {
        super('/token', 'POST', commandHandler, false, false, '');
    }
}

// balances
export class MerchantBalanceCommand extends BaseCommand {
    constructor(commandHandler) {
        super('/balances/merchant', 'GET', commandHandler, false, true, "Merchant Beneficiaries' Balance");
    }
}

export class MerchantBalanceBeneficiariesCommand extends BaseCommand {
    constructor(commandHandler) {
        super('/balances/merchant/beneficiaries', 'GET', commandHandler, false, true, 'Merchant Beneficiary List Balance');
    }
}

export class CustomerWalletBalanceCommand extends BaseCommand {
    constructor(commandHandler) {
        super('/balances/customer', 'GET', commandHandler, false, true, 'Customer Wallet Balance');
    }
}

// payments
export class PaymentPreAuthCommand extends BaseCommand {
    constructor(commandHandler) {
        super('/payments/preAuth', 'POST', commandHandler, true, true, 'Pre-Auth Payment');
    }
}

export class PaymentLoadIFrameCommand {
    constructor(commandHandler, iFrameManager) {
        this.commandHandler = commandHandler;
        this.iFrameManager = iFrameManager;
    }

    execute(source) {
        if (source) {
            this.commandHandler.preActionHandler(source, true);
            this.iFrameManager.load(source);
            this.commandHandler.successHandler(source, true);
        } else {
            this.commandHandler.preActionHandler(source, false);
            this.iFrameManager.unload();
            this.commandHandler.successHandler(source, false);
        }
    }
}


export class PaymentAuthResponseCommand extends BaseCommand {
    constructor(commandHandler) {
        super('/payments/auth', 'POST', commandHandler, false, true, 'Auth Response Payment');
    }
}

export class PaymentVoidAuthCommand extends BaseCommand {
    constructor(commandHandler) {
        super('/payments/voidAuth', 'POST', commandHandler, true, true, 'Void Auth Payment');
    }
}

export class PaymentCaptureCommand extends BaseCommand {
    constructor(commandHandler) {
        super('/payments/capture', 'POST', commandHandler, true, true, 'Capture Payment');
    }
}

export class PaymentCompleteCommand extends BaseCommand {
    constructor(commandHandler) {
        super('/payments/complete', 'POST', commandHandler, true, true, 'Complete Payment');
    }
}

export class PaymentXpressCommand extends BaseCommand {
    constructor(commandHandler) {
        super('/payments/xpress', 'POST', commandHandler, true, true, 'Xpress Payment');
    }
}


export class NewRefundCommand extends BaseCommand {
    constructor(commandHandler) {
        super('/refunds', 'POST', commandHandler, true, true, 'Refund');
    }
}

export class CompleteRefundCommand extends BaseCommand {
    constructor(commandHandler) {
        super('/refunds/complete', 'POST', commandHandler, true, true, 'Complete Refund');
    }
}

export class RefundInquiryCommand extends BaseCommand {
    constructor(commandHandler) {
        super('/refunds', 'GET', commandHandler, false, true, 'Refund Inquiry');
    }
}


export class PayoutCommand extends BaseCommand {
    constructor(commandHandler) {
        super('/payouts', 'POST', commandHandler, true, true, 'Payout');
    }
}

export class GetPayoutDetailsCommand extends BaseCommand {
    constructor(commandHandler) {
        super('/payouts', 'GET', commandHandler, false, true, 'Get Payout Details');
    }
}



export class IFrameManager {
    constructor(noqodiPaymentIFrame, noqodiPaymentIFrameDiv) {
        this.noqodiPaymentIFrame = noqodiPaymentIFrame;
        this.noqodiPaymentIFrameDiv = noqodiPaymentIFrameDiv;
    }

    load(source) {
        this.noqodiPaymentIFrame.src = source;
        this.noqodiPaymentIFrameDiv.collapse('show');
    }

    unload() {
        this.noqodiPaymentIFrameDiv.collapse('hide');
        this.noqodiPaymentIFrame.src = '';
        this.noqodiPaymentIFrame.innerHTML = '';
    }
}

export const configuration = {
    noqodiPaymentPage: null,
    noqodiPaymentPageOrigin: null,
    callbackUrl: null,
    noqodiReadyData: null,
    apiBasePath: null,
    appBaseUrl: null
};



export const commandTargetReferences = {
    currentCommandButtonReference: {},
    currentCommandStatusReference: {}
};

export class ConfigurationHelper {

    static get instance() {
        return ConfigurationHelper._instance;
    }

    static set instance(value) {
        ConfigurationHelper._instance = value;
    }

    static builder() {
        if (ConfigurationHelper.instance) return ConfigurationHelper.instance;
        else return ConfigurationHelper.instance = new ConfigurationHelper();
        // return new ConfigurationHelper();
    }

    noqodiPaymentPage(noqodiPaymentPage){
        if (noqodiPaymentPage) configuration.noqodiPaymentPage = noqodiPaymentPage;
        return this;
    }
    noqodiPaymentPageOrigin(noqodiPaymentPageOrigin){
        if (noqodiPaymentPageOrigin) configuration.noqodiPaymentPageOrigin = noqodiPaymentPageOrigin;
        return this;
    }
    callbackUrl(callbackUrl){
        if (callbackUrl) configuration.callbackUrl = callbackUrl;
        return this;
    }
    noqodiReadyData(noqodiReadyData){
        if (noqodiReadyData) configuration.noqodiReadyData = noqodiReadyData;
        return this;
    }
    apiBasePath(apiBasePath){
        if (apiBasePath) configuration.apiBasePath = apiBasePath;
        return this;
    }
    appBaseUrl(appBaseUrl){
        if (appBaseUrl) configuration.appBaseUrl = appBaseUrl;
        return this;
    }
}

export class Helper {

    static bindEvent(element, eventName, callback) {
        Helper.unbindEvent(element, eventName, callback);
        if (element.addEventListener) {
            element.addEventListener(eventName, callback);
        } else if (element.attachEvent) {
            element.attachEvent("on" + eventName, callback);
        }
    }

    static unbindEvent(element, eventName, callback) {
        if (element.removeEventListener) {
            element.removeEventListener(eventName, callback);
        } else if (element.detachEvent) {
            element.detachEvent("on" + eventName, callback);
        }
    }

    static toggleTraceElementAnimation(element, state) {
        if (state) {
            setTimeout(function () {
                element.removeClass('animated-trace-element');
            }, 1000);
        } else {
            element.removeClass('animated-trace-element').addClass('animated-trace-element');
        }
    }

    static toggleTraceElement(element, state) {
        element.attr("disabled", !state);
        element.removeClass(state ? "btn-secondary" : "btn-outline-success").addClass(state ? "btn-outline-success" : "btn-secondary");
    }


    static finalizeCommandResult(rawData) {
        let rawDataWithDefaults = Helper.setDefaults(rawData);
        let transformedData = {};
        transformedData.request = rawDataWithDefaults.isJsonRequest ? Helper.jsonStringifyOmitNulls(rawDataWithDefaults.request, null, 2) : rawDataWithDefaults.request;
        transformedData.response = rawDataWithDefaults.isJsonResponse ? Helper.jsonStringifyOmitNulls(rawDataWithDefaults.response, null, 2) : rawDataWithDefaults.response;
        transformedData.httpMethod = rawDataWithDefaults.httpMethod;
        transformedData.requestUrl = rawDataWithDefaults.requestUrl;
        transformedData.httpStatus = rawDataWithDefaults.httpStatus;
        transformedData.isJsonRequest = rawDataWithDefaults.isJsonRequest;
        transformedData.isJsonResponse = rawDataWithDefaults.isJsonResponse;
        transformedData.headerText = rawDataWithDefaults.headerText;
        return transformedData;
    }

    static setDefaults(rawData) {
        let dataWithDefaults = {};
        dataWithDefaults.request = rawData.request ? rawData.request : 'N/A';
        dataWithDefaults.response = rawData.response ? rawData.response : 'N/A';
        dataWithDefaults.httpMethod = rawData.httpMethod ? rawData.httpMethod : 'N/A';
        dataWithDefaults.requestUrl = rawData.requestUrl ? rawData.requestUrl : 'N/A';
        dataWithDefaults.httpStatus = rawData.httpStatus ? rawData.httpStatus : 'N/A';
        dataWithDefaults.isJsonRequest = rawData.isJsonRequest;
        dataWithDefaults.isJsonResponse = rawData.isJsonResponse;
        dataWithDefaults.headerText = rawData.headerText ? rawData.headerText : 'N/A';
        return dataWithDefaults;
    }

    static jsonStringifyOmitNulls(data) {
        return JSON.stringify(data, (key, value) => {
            if (value !== null) return value
        }, 2);
    }

    static uuidv4() {
        return ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
            (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
        )
    }

    static scrollTop(element, offset) {
        $('html, body').animate({
            scrollTop: element.offset().top - (offset? offset : 0)
        }, 500);
    }

    static isCommandResponseSuccess(commandResponse) {
        return (commandResponse.response &&
        commandResponse.response.statusInfo &&
        commandResponse.response.statusInfo.status &&
        commandResponse.response.statusInfo.status === "SUCCESS");
    }
}


export class CompletedPaymentPhaseManager {

    static setup() {
        CompletedPaymentPhaseManager.payments = {
            authorized: [],
            captured: [],
            voided: [],
            refunded: [],
            payouts: []
        };
        CompletedPaymentPhaseManager.loadFromStorage();
    }

    static addAuthorizedPayment(payment) {
        CompletedPaymentPhaseManager.payments.authorized.push(payment);
        CompletedPaymentPhaseManager.updateToStorage();
    }
    static addCapturedPayment(payment) {
        CompletedPaymentPhaseManager.payments.captured.push(payment);
        CompletedPaymentPhaseManager.updateToStorage();
    }
    static addVoidedPayment(payment) {
        CompletedPaymentPhaseManager.payments.voided.push(payment);
        CompletedPaymentPhaseManager.updateToStorage();
    }
    static addRefundedPayment(payment) {
        if (CompletedPaymentPhaseManager.payments.refunded.some(x => x.merchantInfo.merchantOrderId === payment.merchantInfo.merchantOrderId)) return;
        CompletedPaymentPhaseManager.payments.refunded.push(payment);
        CompletedPaymentPhaseManager.updateToStorage();
    }
    static addRPayouts(payment) {
        CompletedPaymentPhaseManager.payments.payouts.push(payment);
        CompletedPaymentPhaseManager.updateToStorage();
    }

    static loadFromStorage() {
        if (window.sessionStorage && sessionStorage.payments) {
            let storedPayments = JSON.parse(sessionStorage.payments);
            CompletedPaymentPhaseManager.payments.authorized = storedPayments.authorized ? storedPayments.authorized : [];
            CompletedPaymentPhaseManager.payments.captured = storedPayments.captured ? storedPayments.captured : [];
            CompletedPaymentPhaseManager.payments.voided = storedPayments.voided ? storedPayments.voided : [];
            CompletedPaymentPhaseManager.payments.refunded = storedPayments.refunded ? storedPayments.refunded : [];
            CompletedPaymentPhaseManager.payments.payouts = storedPayments.payouts ? storedPayments.payouts : [];
        }
    }

    static deleteFromStorage() {
        if (window.sessionStorage && sessionStorage.payments) sessionStorage.removeItem("payments");
    }

    static updateToStorage() {
        if (window.sessionStorage) sessionStorage.payments = JSON.stringify(CompletedPaymentPhaseManager.payments);
    }

    static getAuthorizedPayments() {
        return CompletedPaymentPhaseManager.payments.authorized;
    }
    static getCapturedPayments() {
        return CompletedPaymentPhaseManager.payments.captured;
    }
    static getVoidedPayments() {
        return CompletedPaymentPhaseManager.payments.voided;
    }
    static getRefundedPayments() {
        return CompletedPaymentPhaseManager.payments.refunded;
    }
    static getPayouts() {
        return CompletedPaymentPhaseManager.payments.payouts;
    }
}


export class AccessCredentialsManager {

    static get accessCredentialsButton() {
        return $("#save-access-credentials-button");
    }
    static get usernameInput() {
        return $("#access-credentials-username");
    }
    static get passwordInput() {
        return $("#access-credentials-password");
    }
    static get accessCredentialsWrapper() {
        return $("#login-page-container");
    }
    static get failedApiCommand() {
        return AccessCredentialsManager._failedApiCommand;
    }
    static get failedApiCommandOptions() {
        return AccessCredentialsManager._failedApiCommandOptions;
    }
    static setFailedApiCommandAndOptions(command, options) {
        AccessCredentialsManager._failedApiCommand = command;
        AccessCredentialsManager._failedApiCommandOptions = options;
    }

    static get appApiAccessTokenCommand() {
        if (AccessCredentialsManager._appApiAccessTokenCommand) return AccessCredentialsManager._appApiAccessTokenCommand;
        else {
            let handler = new GetAppApiAccessTokenCommandHandler(
                function () {},
                function (commandResponse) {
                    AccessCredentialsManager.updateAccessCredentials(commandResponse.access_token);
                    if (window.sessionStorage) sessionStorage.apiAccessToken = commandResponse.access_token;
                    AccessCredentialsManager.hideAccessCredentialsForm();
                    if (AccessCredentialsManager.failedApiCommand && AccessCredentialsManager.failedApiCommandOptions) {
                        // retry the API call that failed on access token expiry
                        AccessCredentialsManager.failedApiCommand.execute(
                            AccessCredentialsManager.failedApiCommandOptions.request,
                            null,
                            AccessCredentialsManager.failedApiCommandOptions.pathVariable
                        );
                    }
                },
                function () {}
                );
            return AccessCredentialsManager._appApiAccessTokenCommand = new GetAppApiAccessTokenCommand(handler);
        }
    }

    static setup() {
        setTimeout(function () {
            if (window.sessionStorage && sessionStorage.apiAccessToken) {
                AccessCredentialsManager.updateAccessCredentials(sessionStorage.apiAccessToken);
                AccessCredentialsManager.hideAccessCredentialsForm();
            } else {
                AccessCredentialsManager.showAccessCredentialsForm();
            }
        }, 1000);

        AccessCredentialsManager.accessCredentialsButton.off().on('click', function () {
            AccessCredentialsManager.executeApiAccessTokenRequest();
        });
    }

    static executeApiAccessTokenRequest() {
        let username = AccessCredentialsManager.usernameInput.val();
        let password = AccessCredentialsManager.passwordInput.val();
        if (username && password) {
            let basicCredentials = {
                username: username,
                password: password
            };
            AccessCredentialsManager.appApiAccessTokenCommand.execute(null, basicCredentials);
        }
    }

    static getApiAccessToken() {
        if (!AccessCredentialsManager.apiAccessToken) AccessCredentialsManager.loadApiAccessTokenFromSession();
        return AccessCredentialsManager.apiAccessToken;
    }

    static loadApiAccessTokenFromSession() {
        if (window.sessionStorage && sessionStorage.apiAccessToken) {
            AccessCredentialsManager.updateAccessCredentials(sessionStorage.apiAccessToken);
        }
    }


    static deleteAccessCredentials() {
        AccessCredentialsManager.usernameInput.val("");
        AccessCredentialsManager.passwordInput.val("");
        if (window.sessionStorage && sessionStorage.apiAccessToken) sessionStorage.removeItem("apiAccessToken");
    }

    static updateAccessCredentials(apiAccessToken) {
        AccessCredentialsManager.apiAccessToken = apiAccessToken;
    }

    static hideAccessCredentialsForm() {
        AccessCredentialsManager.accessCredentialsWrapper.fadeOut("slow", function () {
            AccessCredentialsManager.accessCredentialsWrapper.removeClass('d-flex');
        });
    }

    static showAccessCredentialsForm() {
        AccessCredentialsManager.accessCredentialsWrapper.fadeIn("slow", function () {
            AccessCredentialsManager.accessCredentialsWrapper.addClass('d-flex');
        });
    }
}


