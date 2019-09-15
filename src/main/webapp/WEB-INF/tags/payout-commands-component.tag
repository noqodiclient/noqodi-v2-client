
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>

<div id="payout-commands-div" class="col d-flex flex-column mb-5">

    <nav class="nav nav-pills nav-fill shadow-sm" id="payout-nav-pills">
        <a class="nav-item nav-link active" id="nav-new-payout-tab" data-toggle="tab" href="#nav-new-payout" role="tab" aria-controls="nav-new-payout" aria-selected="true">Process Payout</a>
        <a class="nav-item nav-link" id="nav-payout-details-tab" data-toggle="tab" href="#nav-payout-details" role="tab" aria-controls="nav-payout-details" aria-selected="false">Get Payout Details</a>
    </nav>

    <div class="tab-content text-left payout-tab-content shadow-sm" id="payout-nav-tab-content">
        <div class="tab-pane fade show active"
             id="nav-new-payout" role="tabpanel" aria-labelledby="nav-new-payout-tab">
            <h3 class="text-dark font-weight-light p-0 m-0">Process Payout - <span class="text-warning">initiate a payout transaction</span></h3>
            <br>
            <o:beneficiary-amount-component />
        </div>
        <div class="tab-pane fade"
             id="nav-payout-details" role="tabpanel" aria-labelledby="nav-payout-details-tab">
            <h3 class="text-dark font-weight-light p-0 m-0">Get Payout Details - <span class="text-warning">get details of a payout</span></h3>
            <p id="get-payout-details-items-feedback" class="text-secondary font-weight-light p-0 m-0"
               style="font-size: 16px">There must be at least one <span class="text-danger">Payout</span> transaction before you can execute a <span class="text-danger">Get Payout Details</span>.
                Go execute a Payout transaction first, then come back here :)</p>
            <br>
            <o:saved-payments-table-component tableBodyId="payouts-payment-list" tableHeaderText="Payout Transactions" />
        </div>
    </div>

</div>




