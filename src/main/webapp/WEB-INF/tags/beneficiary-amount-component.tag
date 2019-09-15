

<div class="p-0 m-0">
    <table class="table table-sm" style="border-spacing: 0; border-collapse: collapse;">
        <thead class="thead-light">
        <tr>
            <th scope="col">#</th>
            <th scope="col">Account Number</th>
            <th scope="col">Current Balance</th>
            <th scope="col">Payout Amount</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <th scope="row">1</th>
            <td id="bene-one-account-number">...</td>
            <td>
                <span id="bene-one-current-balance">...</span>
                <div id="bene-one-previous-balance-div" class="bg-light m-0 px-2 py-0" style="display: none; font-size: 16px;">
                    <p class="text-info m-0 p-0">previous</p>
                    <p id="bene-one-previous-balance" class="text-secondary m-0 p-0">...</p>
                </div>
            </td>
            <td id="item-1-amount-td">
                <div class="d-flex flex-column">
                    <div id="item-1-amount-div" class="d-flex flex-row justify-content-between">
                        <div>
                            <span id="item-1-amount-text" class="">...</span>
                        </div>
                        <div class="text-warning">
                            <i id="item-1-amount-edit" class="fas fa-edit"></i>
                        </div>
                    </div>
                    <div id="bene-one-previous-payout-amount-div" class="bg-light m-0 px-2 py-0" style="display: none; font-size: 16px;">
                        <p class="text-info m-0 p-0">previous</p>
                        <p id="bene-one-previous-payout-amount" class="text-secondary m-0 p-0">...</p>
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <th scope="row">2</th>
            <td id="bene-two-account-number">...</td>
            <td>
                <span id="bene-two-current-balance">...</span>
                <div id="bene-two-previous-balance-div" class="bg-light m-0 px-2 py-0" style="display: none; font-size: 16px;">
                    <p class="text-info m-0 p-0">previous</p>
                    <p id="bene-two-previous-balance" class="text-secondary m-0 p-0">...</p>
                </div>
            </td>
            <td id="item-2-amount-td">
                <div class="d-flex flex-column">
                    <div id="item-2-amount-div" class="d-flex flex-row justify-content-between">
                        <div>
                            <span id="item-2-amount-text" class="">...</span>
                        </div>
                        <div class="text-warning">
                            <i  id="item-2-amount-edit" class="fas fa-edit"></i>
                        </div>
                    </div>
                    <div id="bene-two-previous-payout-amount-div" class="bg-light m-0 px-2 py-0" style="display: none; font-size: 16px;">
                        <p class="text-info m-0 p-0">previous</p>
                        <p id="bene-two-previous-payout-amount" class="text-secondary m-0 p-0">...</p>
                    </div>
                </div>
            </td>
        </tr>
        <tr class="table-light">
            <th scope="row"></th>
            <td></td>
            <td>
                <h3 class="text-warning text-right">Current Payout Total:</h3>
                <h5 id="prevoius-payout-total-label" style="display: none;"
                    class="bg-light m-0 px-2 py-0 text-info text-right">Previous Payout Total:</h5>
            </td>
            <td>
                <h3 id="order-total-text" class="text-danger">...</h3>
                <h5 id="prevoius-payout-total-text" style="display: none;"
                    class="bg-light m-0 px-2 py-0 text-dark">...</h5>
            </td>
        </tr>
        </tbody>
    </table>
    <div class="d-flex justify-content-end">
        <div class="btn-group" role="group" aria-label="execute payout">
            <button id="execute-payout-button" class="btn btn-info">Execute Payout</button>
        </div>
    </div>
</div>

