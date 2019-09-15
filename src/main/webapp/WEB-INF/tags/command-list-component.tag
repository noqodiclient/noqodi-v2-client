
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>

<div id="balance-commands-container" class="row text-white text-center align-middle p-0 m-0 commands-container"
     style="height: 450px; font-size: 20px; background-color: #696a6c">
    <o:balance-commands-component />
</div>


<div id="payment-commands-container" class="row text-dark text-center align-middle p-0 m-0 pt-4 commands-container"
     style="min-height: 300px; font-size: 20px; display: none;">
    <o:payment-commands-component />
</div>



<div id="refunds-commands-container" class="row text-dark text-center align-middle p-0 m-0 pt-4 commands-container"
     style="min-height: 300px; font-size: 20px; display: none;">
    <o:refund-commands-component />
</div>


<div id="payouts-commands-container" class="row text-dark text-center align-middle p-0 m-0 pt-4 commands-container"
     style="min-height: 300px; font-size: 20px; display: none;">
    <o:payout-commands-component />
</div>

