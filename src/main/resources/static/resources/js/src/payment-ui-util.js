import {
    configuration,
    beneficiaryAmount,
    ConfigurationHelper,
    PaymentCaptureCommand,
    PaymentVoidAuthCommand,
    PaymentPreAuthCommand,
    PaymentLoadIFrameCommand,
    PaymentAuthResponseCommand,
    IFrameManager,
    Helper,
    CommandResultViewManager,
    AccessCredentialsManager,
    CompletedPaymentPhaseManager,
    ItemAmountManager
} from "./command-ui-core-module.js";


class PaymentPreAuthCommandHandler {

    constructor(commandButton, traceElement, isHostedPayment) {
        this.commandButton = commandButton;
        this.traceElement = traceElement;
        this.testIframeUrl = 'http://localhost:8080/web/iframe-test?paymentRequestToken=';
        this.isHostedPayment = isHostedPayment;
    }

    preActionHandler() {
        noqodiPaymentIFrameDiv.collapse('hide');
        this.commandButton.attr("disabled", true);
        PaymentPreAuthCommandHandler.toggleAllTraceElements(false);
        commandResultViewManager.hideView();
    }

    successHandler(commandResponse) {
        traceData.preAuth = commandResponse;
        if (
            Helper.isCommandResponseSuccess(commandResponse) &&
            commandResponse.response.paymentInfo &&
            commandResponse.response.paymentInfo.preAuthToken
        ) {
            let noqodiPaymentPageUrl = configuration.noqodiPaymentPage + commandResponse.response.paymentInfo.preAuthToken;
            if (this.isHostedPayment) {
                window.location.href = configuration.noqodiPaymentPage + commandResponse.response.paymentInfo.preAuthToken
                    + "&hosted=true";
            } else {
                setTimeout(function () {
                    this.toggleTraceElement(true);
                    paymentCommands.paymentLoadIframeCommand.execute(noqodiPaymentPageUrl);
                }.bind(this), 200);
            }
        } else {
            let noqodiPaymentPageUrl = this.testIframeUrl + 'xxx-xxxx-xxxxx-xxxxxx-xxxxxxx';
            setTimeout(function () {
                this.toggleTraceElement(true);
                paymentCommands.paymentLoadIframeCommand.execute(noqodiPaymentPageUrl);
            }.bind(this), 200);
        }
    }

    errorHandler() {
        setTimeout(function () {
            this.commandButton.attr("disabled", false);
            this.toggleTraceElement(false);
        }.bind(this), 200);
    }

    toggleTraceElement(state) {
        Helper.toggleTraceElement(this.traceElement, state);
        Helper.toggleTraceElementAnimation(this.traceElement, state);
    }

    static toggleAllTraceElements(state) {
        paymentPreAuthCommandHandler.toggleTraceElement(state);
        paymentLoadIframeCommandHandler.toggleTraceElement(state);
        paymentAuthResponseCommandHandler.toggleTraceElement(state);
        paymentCaptureCommandHandler.toggleTraceElement(state);
    }
}


class PaymentLoadIframeCommandHandler {

    constructor(traceElement, proceedToPaymentButton) {
        this.traceElement = traceElement;
        this.proceedToPaymentButton = proceedToPaymentButton;
        this.noqodiReady = function (event) {
            if (event.origin === configuration.noqodiPaymentPageOrigin && event.source === noqodiPaymentIFrameContentWindow) {
                if (event.data === configuration.noqodiReadyData) {
                    this.toggleTraceElement(true);
                }
            }
        }.bind(this);

        this.noqodiAuthResponse = function (event) {
            let authResponseModel = JSON.parse(event.data);
            paymentCommands.paymentAuthResponseCommand.execute(authResponseModel);
            /*            if (event.origin === configuration.noqodiPaymentPageOrigin && event.source === noqodiPaymentIFrameContentWindow) {
                            let decodedEventData = decodeURIComponent(event.data);
                            let indexOfAuthResponse = decodedEventData.indexOf("response=");
                            let indexOfCallbackUrl = decodedEventData.indexOf("?response=");
                            if (indexOfAuthResponse > -1 && indexOfCallbackUrl > -1) {
                                let authResponse = decodedEventData.substr(indexOfAuthResponse + 9);
                                let callback = decodedEventData.substr(0, indexOfCallbackUrl);
                                if (authResponse && callback === configuration.callbackUrl) {
                                    // console.log('valid auth response received ----------------');
                                    // paymentCommands.paymentAuthResponseCommand.execute(authResponse);
                                    // test only..., replace with actual auth response

                                    let authResponseModel = {
                                        serviceType: "AUTH",
                                        merchantInfo: {
                                            merchantCode: traceData.preAuth.request.merchantInfo.merchantCode,
                                            merchantOrderId: traceData.preAuth.request.merchantInfo.merchantOrderId,
                                            merchantRequestId: traceData.preAuth.request.merchantInfo.merchantRequestId
                                        }
                                    };
                                    paymentCommands.paymentAuthResponseCommand.execute(authResponseModel);
                                }
                            }
                        }*/
        }.bind(this);
    }

    preActionHandler(source, loaded) {
        if (loaded) {
            Helper.bindEvent(window, 'message', this.noqodiReady);
        }
    }

    successHandler(source, loaded) {
        if (loaded) {
            traceData.iframe = source;
            Helper.bindEvent(window, 'message', this.noqodiAuthResponse);
            setTimeout(function () {
                Helper.scrollTop(noqodiPaymentIFrameDiv, 60);
            }, 100);
            ItemAmountManager.editDisabled = true;
        } else {
            this.proceedToPaymentButton.attr("disabled", false);
            ItemAmountManager.editDisabled = false;
            Helper.scrollTop($('html, body'));
        }
    }

    errorHandler() {
        this.proceedToPaymentButton.attr("disabled", false);
        ItemAmountManager.editDisabled = false;
        this.toggleTraceElement(false);
    }

    toggleTraceElement(state) {
        Helper.toggleTraceElement(this.traceElement, state);
        Helper.toggleTraceElementAnimation(this.traceElement, state);
    }
}


class PaymentAuthResponseCommandHandler {

    constructor(traceElement) {
        this.traceElement = traceElement;
    }

    preActionHandler() {
        this.toggleTraceElement(false);
    }

    successHandler(commandResponse) {
        traceData.authResponse = commandResponse;
        this.toggleTraceElement(true);
        if (Helper.isCommandResponseSuccess(commandResponse)) {
            CompletedPaymentPhaseManager.addAuthorizedPayment(commandResponse.response);
            PaymentAuthResponseCommandHandler.showPaymentOptionsDialog(commandResponse);
        }
    }

    errorHandler() {
        this.toggleTraceElement(false);
    }

    toggleTraceElement(state) {
        Helper.toggleTraceElement(this.traceElement, state);
        Helper.toggleTraceElementAnimation(this.traceElement, state);
    }

    static showPaymentOptionsDialog(commandResponse) {
        VoidCaptureOptionsViewHelper.show(function () {
            $('#capture-command-button').off().on('click', function () {
                VoidCaptureOptionsViewHelper.hide();
                paymentCommands.paymentCaptureCommand.execute(commandResponse.response);
            });
            $('#void-auth-command-button').off().on('click', function () {
                VoidCaptureOptionsViewHelper.hide();
                paymentCommands.paymentVoidAuthCommand.execute(commandResponse.response);
            });
        });
    }
}


class VoidCaptureOptionsViewHelper {
    static show(callback) {
        $('#void-capture-overlay-div').show('fast', callback);
    }
    static hide(callback) {
        $('#void-capture-overlay-div').hide('fast');
    }
}


class PaymentCaptureCommandHandler {

    constructor(traceElement) {
        this.traceElement = traceElement;
    }

    preActionHandler() {
        this.toggleTraceElement(false);
    }

    successHandler(commandResponse) {
        traceData.capture = commandResponse;
        if (Helper.isCommandResponseSuccess(commandResponse)) {
            CompletedPaymentPhaseManager.addCapturedPayment(commandResponse.response);
        }
        this.toggleTraceElement(true);
        paymentCommands.paymentLoadIframeCommand.execute();
    }

    errorHandler() {
        this.toggleTraceElement(false);
    }

    toggleTraceElement(state) {
        Helper.toggleTraceElement(this.traceElement, state);
        Helper.toggleTraceElementAnimation(this.traceElement, state);
    }
}

class PaymentVoidAuthCommandHandler {

    constructor(traceElement) {
        this.traceElement = traceElement;
    }

    preActionHandler() {
        this.toggleTraceElement(false);
    }

    successHandler(commandResponse) {
        traceData.capture = commandResponse;
        if (Helper.isCommandResponseSuccess(commandResponse)) {
            CompletedPaymentPhaseManager.addVoidedPayment(commandResponse.response);
        }
        this.toggleTraceElement(true);
        paymentCommands.paymentLoadIframeCommand.execute();
    }

    errorHandler() {
        this.toggleTraceElement(false);
    }

    toggleTraceElement(state) {
        Helper.toggleTraceElement(this.traceElement, state);
        Helper.toggleTraceElementAnimation(this.traceElement, state);
    }
}


class TraceElementManager {
    setup() {
        preAuthTraceButton.off().on("click", function (event) {
            commandResultViewManager.hideView();
            commandResultViewManager.showView(Helper.finalizeCommandResult(traceData.preAuth), 50);
        }.bind(this));

        loadIframeTraceButton.off().on("click", function (event) {
            let commandResult = {};
            commandResult.request = '<span class="text-info">Below (noqodi Payment Page) URL was loaded into the iframe</span><br>' + traceData.iframe;
            commandResult.isJsonRequest = false;
            commandResult.isJsonResponse = false;
            commandResult.headerText = 'Load noqodi Payment Page (into iframe)';
            commandResultViewManager.hideView();
            commandResultViewManager.showView(Helper.finalizeCommandResult(commandResult), 50);
        }.bind(this));

        authResponseTraceButton.off().on("click", function (event) {
            traceData.authResponse.isJsonRequest = false;
            traceData.authResponse.isJsonResponse = true;
            traceData.authResponse.headerText = 'Payment Auth Response';
            traceData.authResponse.request = undefined;
            traceData.authResponse.httpMethod = undefined;
            traceData.authResponse.requestUrl = undefined;
            traceData.authResponse.httpStatus = undefined;
            commandResultViewManager.hideView();
            commandResultViewManager.showView(Helper.finalizeCommandResult(traceData.authResponse), 50);
        }.bind(this));

        voidCaptureTraceButton.off().on("click", function (event) {
            commandResultViewManager.hideView();
            commandResultViewManager.showView(Helper.finalizeCommandResult(traceData.capture), 50);
        }.bind(this));
    }
}



const proceedToPaymentButton = $('#proceed-to-payment-button');
const proceedToHostedPaymentButton = $('#proceed-to-hosted-payment-button');
const noqodiPaymentIFrameDiv = $('#noqodi-payment-iframe-div');
const noqodiPaymentIFrame = document.getElementById("noqodi-payment-iframe");
const noqodiPaymentIFrameContentWindow = noqodiPaymentIFrame.contentWindow;

const commandResultViewManager = new CommandResultViewManager();
const iFrameManager = new IFrameManager(noqodiPaymentIFrame, noqodiPaymentIFrameDiv);

const preAuthTraceButton = $('#trace-element-preauth');
const loadIframeTraceButton = $('#trace-element-load-iframe');
const authResponseTraceButton = $('#trace-element-auth-response');
const voidCaptureTraceButton = $('#trace-element-capture');

const commandResultComponent = $("#command-result-component-div");
const commandResultComponentWrapper = $('#command-result-component-wrapper');

const paymentPreAuthCommandHandler = new PaymentPreAuthCommandHandler(proceedToPaymentButton, preAuthTraceButton, false);
const paymentLoadIframeCommandHandler = new PaymentLoadIframeCommandHandler(loadIframeTraceButton, proceedToPaymentButton);
const paymentAuthResponseCommandHandler = new PaymentAuthResponseCommandHandler(authResponseTraceButton);
const paymentCaptureCommandHandler = new PaymentCaptureCommandHandler(voidCaptureTraceButton);
const paymentVoidAuthCommandHandler = new PaymentVoidAuthCommandHandler(voidCaptureTraceButton);
const paymentLoadHostedPageCommandHandler = new PaymentPreAuthCommandHandler(proceedToPaymentButton, preAuthTraceButton, true)

const paymentCommands = {
    "paymentPreAuthCommand": new PaymentPreAuthCommand(paymentPreAuthCommandHandler),
    "paymentLoadIframeCommand": new PaymentLoadIFrameCommand(paymentLoadIframeCommandHandler, iFrameManager),
    "paymentAuthResponseCommand": new PaymentAuthResponseCommand(paymentAuthResponseCommandHandler),
    "paymentCaptureCommand": new PaymentCaptureCommand(paymentCaptureCommandHandler),
    "paymentVoidAuthCommand": new PaymentVoidAuthCommand(paymentVoidAuthCommandHandler),
    "paymentLoadHostedPageCommand": new PaymentPreAuthCommand(paymentLoadHostedPageCommandHandler)
};
const traceData = {
    preAuth: null,
    iframe: null,
    authResponse: null,
    capture: null,
};




(function () {

    initConfiguration(ConfigurationHelper.builder());

    proceedToPaymentButton.off().on("click", function (event) {
        paymentCommands.paymentPreAuthCommand.execute(beneficiaryAmount);
    }.bind(this));

    proceedToHostedPaymentButton.off().on("click", function (event) {
        paymentCommands.paymentLoadHostedPageCommand.execute(beneficiaryAmount);
    }.bind(this));

    $(document).ready(function () {
        $('#payment-command-iframe-div').on('hidden.bs.collapse', function () {
            noqodiPaymentIFrame.src = '';
            noqodiPaymentIFrame.innerHTML = '';
        });

        ItemAmountManager.init();

        AccessCredentialsManager.setup();
        CompletedPaymentPhaseManager.setup();
    });


    commandResultComponent.on('show.bs.collapse', function () {
        commandResultComponentWrapper.removeClass('bg-white shadow-sm border border-light')
            .addClass('bg-white shadow-sm border border-light');
    });
    commandResultComponent.on('hidden.bs.collapse', function () {
        commandResultComponentWrapper.removeClass('bg-white shadow-sm border border-light');
    });


    // setup trace button events
    new TraceElementManager().setup();

    if (configuration.noqodiReadyData !== undefined && configuration.noqodiReadyData != '') {
        var decoded = decodeURIComponent(configuration.noqodiReadyData);
        paymentCommands.paymentAuthResponseCommand.execute(JSON.parse(decoded));
    }

})();
