<div id="payment-commands-div" class="col d-flex flex-column mb-5">



    <div class="d-flex flex-row justify-content-center p-0">
        <div class="d-flex flex-column justify-content-center border border-light shadow-sm bg-white">

            <h3 class="font-weight-light text-warning mb-0 mt-2">First, do these:</h3>

            <div class="d-flex flex-row p-2 justify-content-center">
                <div class="card bg-light border-0 m-2 shadow-sm" style="width: 300px">
                    <div class="card-body d-flex flex-column bg-white m-2">
                        <h1 class="display-3 text-info"><i class="fas fa-shopping-cart"></i></h1>
                        <h4 class="font-weight-light text-success">Prepare for Payment</h4>
                        <p class="font-weight-normal text-info" style="font-size: medium;">
                            invoke the Pre-Auth service to get a payment request token required to initiate the payment cycle
                        </p>
                        <h3 class="text-muted mt-auto"><i class="fas fa-hand-point-right"></i></h3>
                    </div>
                </div>

                <div class="card bg-light border-0 m-2 shadow-sm" style="width: 300px">
                    <div class="card-body d-flex flex-column bg-white m-2">
                        <h1 class="display-3 text-warning"><i class="fas fa-window-restore"></i></h1>
                        <h4 class="font-weight-light text-success">Load noqodi iFrame</h4>
                        <p class="font-weight-normal text-info" style="font-size: medium;">
                            pass the payment request token from the pre-auth response to the noqodi iFrame
                        </p>
                        <h3 class="text-muted mt-auto"><i class="fas fa-hand-point-down"></i></h3>
                    </div>
                </div>
            </div>
        </div>
    </div>





    <div class="d-flex flex-row justify-content-center p-0 mt-4">
        <div class="d-flex flex-column justify-content-center border border-light shadow-sm bg-white">

            <h3 class="font-weight-light text-warning mb-0 mt-2">... then, <i class="fas fa-user-shield"></i> Customer selects either:</h3>

            <div class="d-flex flex-row p-2 justify-content-center">
                <div class="card bg-white border-0 m-2 shadow-sm" style="width: 300px">
                    <div class="card-body d-flex flex-column bg-light m-2">
                        <h1 class="display-3 text-primary"><i class="fas fa-wallet"></i></h1>
                        <h4 class="font-weight-light text-success">Digital Wallet</h4>
                        <p class="font-weight-normal text-info" style="font-size: medium;">
                            customer logs in to authorize the payment
                        </p>
                        <h3 class="text-muted mt-auto"><i class="fas fa-hand-point-down"></i></h3>
                    </div>
                </div>

                <div class="card bg-white border-0 m-2 shadow-sm" style="width: 300px">
                    <div class="card-body d-flex flex-column bg-light m-2">
                        <h1 class="display-3 text-dark"><i class="fas fa-credit-card"></i></h1>
                        <h4 class="font-weight-light text-success">Credit Card</h4>
                        <p class="font-weight-normal text-info" style="font-size: medium;">
                            customer enters credit card details to authorize the payment
                        </p>
                        <h3 class="text-muted mt-auto"><i class="fas fa-hand-point-down"></i></h3>
                    </div>
                </div>
            </div>
        </div>
    </div>




    <div class="d-flex flex-row justify-content-center p-0 mt-4">
        <div class="d-flex flex-column justify-content-center border border-light shadow-sm bg-white">

            <h3 class="font-weight-light text-warning mb-0 mt-2">... listen for a "message" event from the iframe:</h3>

            <div class="d-flex flex-row p-2 justify-content-center">
                <div class="card bg-light border-0 m-2 shadow-sm" style="width: 600px">
                    <div class="card-body d-flex flex-column bg-white m-2">
                        <h1 class="display-3 text-muted"><i class="fas fa-check-double"></i></h1>
                        <h4 class="font-weight-light text-success">Auth Response</h4>
                        <p class="font-weight-normal text-info" style="font-size: medium;">
                            after the Customer clicks the "pay now" button, noqodi payment iframe will emit a "message" event containing the <span class="text-danger">AUTH RESPONSE</span>.
                        </p>
                        <h3 class="text-muted mt-auto"><i class="fas fa-hand-point-down"></i></h3>
                    </div>
                </div>
            </div>
        </div>
    </div>




    <div class="d-flex flex-row justify-content-center p-0 mt-4">
        <div class="d-flex flex-column justify-content-center border border-light shadow-sm bg-white">

            <h3 class="font-weight-light text-warning mb-0 mt-2">... then, you do either of these:</h3>

            <div class="d-flex flex-row p-2 justify-content-center">
                <div class="card bg-white border-0 m-2 shadow-sm" style="width: 300px">
                    <div class="card-body d-flex flex-column bg-light m-2">
                        <h1 class="display-3 text-danger"><i class="fas fa-window-close"></i></h1>
                        <h4 class="font-weight-light text-success">Void Auth</h4>
                        <p class="font-weight-normal text-info" style="font-size: medium;">
                            cancel the authorized payment made by the customer in the previous step - after receiving the AUTH response from noqodi
                        </p>
                        <h3 class="text-danger mt-auto"><i class="fas fa-hourglass-end"></i></h3>
                    </div>
                </div>

                <div class="card bg-white border-0 m-2 shadow-sm border-bottom" style="width: 300px">
                    <div class="card-body d-flex flex-column bg-light m-2">
                        <h1 class="display-3 text-success"><i class="fas fa-handshake"></i></h1>
                        <h4 class="font-weight-light text-success">Capture Payment</h4>
                        <p class="font-weight-normal text-info" style="font-size: medium;">
                            provide beneficiary/transaction breakdown to capture the authorized payment amount - after receiving the AUTH response from noqodi
                        </p>
                        <h3 class="text-success mt-auto"><i class="fas fa-hourglass-end"></i></h3>
                    </div>
                </div>
            </div>
        </div>
    </div>




    <div class="d-flex flex-row justify-content-center p-0 mt-4">
        <div class="d-flex flex-column justify-content-center border border-light shadow-sm bg-white">

            <h3 class="font-weight-light text-warning mb-0 mt-2">... and just in case:</h3>

            <div class="d-flex flex-row p-2 justify-content-center">
                <div class="card bg-light border-0 m-2 shadow-sm" style="width: 600px">
                    <div class="card-body d-flex flex-column bg-white m-2">
                        <h1 class="display-3 text-muted"><i class="fas fa-laptop-code"></i></h1>
                        <h4 class="font-weight-light text-success">Complete Pay</h4>
                        <p class="font-weight-normal text-info" style="font-size: medium;">
                            if you don't get expected response at any stage in the payment cycle, then you can invoke the "complete pay service" for that stage
                        </p>
                        <h3 class="text-muted mt-auto"><i class="fas fa-redo"></i></h3>
                    </div>
                </div>
            </div>
        </div>
    </div>




    <div class="d-flex flex-row justify-content-center p-0 mt-4">
        <div class="d-flex flex-column justify-content-center">
            <div class="p-0 m-0">
                <h3 class="font-weight-bold d-inline-block align-middle m-0 p-0 text-success">
                    ...&nbsp;&nbsp;
                    <span class="text-warning font-weight-light">
                        lights&nbsp;<i class="fas fa-lightbulb"></i>
                    </span>
                    &nbsp;&nbsp;...&nbsp;&nbsp;
                    <span class="text-info font-weight-light">
                        camera&nbsp;<i class="fas fa-video"></i>
                    </span>
                    &nbsp;&nbsp;...&nbsp;&nbsp;
                </h3>
                <a href="${contextPath}/web/payment" class="btn btn-danger d-inline-block p-3 shadow-sm">
                    <h3 class="font-weight-light p-0 m-0" style="vertical-align: middle;">
                        action! <i class="fas fa-film"></i>
                    </h3>
                </a>
            </div>
        </div>
    </div>



</div>



