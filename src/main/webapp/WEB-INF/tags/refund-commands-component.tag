
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>

<div id="refund-commands-div" class="col d-flex flex-column mb-5">

    <nav class="nav nav-pills nav-fill shadow-sm" id="refund-nav-pills">
        <a class="nav-item nav-link active" id="nav-new-refund-tab" data-toggle="tab" href="#nav-new-refund" role="tab" aria-controls="nav-new-refund" aria-selected="true">New Refund</a>
        <a class="nav-item nav-link" id="nav-complete-refund-tab" data-toggle="tab" href="#nav-complete-refund" role="tab" aria-controls="nav-complete-refund" aria-selected="false">Complete Refund</a>
        <a class="nav-item nav-link" id="nav-refund-inquiry-tab" data-toggle="tab" href="#nav-refund-inquiry" role="tab" aria-controls="nav-refund-inquiry" aria-selected="false">Refund Inquiry</a>
    </nav>

    <div class="tab-content text-left refund-tab-content shadow-sm" id="refund-nav-tab-content">
        <div class="tab-pane fade show active"
             id="nav-new-refund" role="tabpanel" aria-labelledby="nav-new-refund-tab">
            <h3 class="text-dark font-weight-light p-0 m-0">New Refund - <span class="text-warning">initiate a refund transaction</span></h3>
            <p id="new-refund-items-feedback" class="text-secondary font-weight-light p-0 m-0"
               style="font-size: 16px">So, unnn... you've got to have an already captured payment -
                <span class="text-danger">which hasn't be refunded</span> - before you can initiate a new <span class="text-danger">Refund</span> transaction.
            So, go make one or two payments then come back here when you're done :)</p>
            <br>
            <o:saved-payments-table-component tableBodyId="non-refunded-payment-list" tableHeaderText="Payment Details" />
        </div>
        <div class="tab-pane fade"
             id="nav-complete-refund" role="tabpanel" aria-labelledby="nav-complete-refund-tab">
            <h3 class="text-dark font-weight-light p-0 m-0">Complete Refund - <span class="text-warning">complete a pending refund (if incomplete)</span></h3>
            <p id="complete-refund-items-feedback" class="text-secondary font-weight-light p-0 m-0"
               style="font-size: 16px">You should have at least one captured payment before you can initiate a <span class="text-danger">Complete Refund</span> transaction.
                So, go make one or two payments then come back here when you're done :)</p>
            <br>
            <o:saved-payments-table-component tableBodyId="maybe-refunded-payment-list" tableHeaderText="Payment Details" />
        </div>
        <div class="tab-pane fade"
             id="nav-refund-inquiry" role="tabpanel" aria-labelledby="nav-refund-inquiry-tab">
            <h3 class="text-dark font-weight-light p-0 m-0">Refund Inquiry - <span class="text-warning">get details of a refund</span></h3>
            <p id="refund-inquiry-items-feedback" class="text-secondary font-weight-light p-0 m-0"
               style="font-size: 16px">There should at least one refunded payment before you can execute a <span class="text-danger">Refund Inquiry</span>.
                So, go make some refunds then come back here when you're done :)</p>
            <br>
            <o:saved-payments-table-component tableBodyId="definitely-refunded-payment-list" tableHeaderText="Payment Details" />
        </div>
    </div>

</div>





