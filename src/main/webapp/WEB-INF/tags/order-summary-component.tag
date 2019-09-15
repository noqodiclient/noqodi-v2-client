

<div class="p-4 m-0">
    <h3 class="text-secondary font-weight-light">Sample Order Summary</h3>
    <table class="table table-sm">
        <thead class="thead-light">
        <tr>
            <th scope="col">#</th>
            <th scope="col">Item</th>
            <th scope="col">Quantity</th>
            <th scope="col">Amount</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <th scope="row">1</th>
            <td>iPhone X</td>
            <td>1</td>
            <td id="item-1-amount-td">
                <div id="item-1-amount-div" class="d-flex flex-row justify-content-between">
                    <div id="item-1-amount-text" class="">...</div>
                    <div class="text-warning">
                        <i id="item-1-amount-edit" class="fas fa-edit"></i>
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <th scope="row">2</th>
            <td>Samsung Note 9</td>
            <td>1</td>
            <td id="item-2-amount-td">
                <div id="item-2-amount-div" class="d-flex flex-row justify-content-between">
                    <div id="item-2-amount-text" class="">...</div>
                    <div class="text-warning">
                        <i  id="item-2-amount-edit" class="fas fa-edit"></i>
                    </div>
                </div>
            </td>
        </tr>
        <tr class="table-light">
            <th scope="row"></th>
            <td colspan="2"><h5 class="text-warning text-right">Order total:</h5></td>
            <td><h5 id="order-total-text" class="text-danger">...</h5></td>
        </tr>
        </tbody>
    </table>
    <div class="d-flex justify-content-end">
        <div class="btn-group" role="group" aria-label="execute payment or go back to main commands">
            <a class="btn btn-light" href="${contextPath}/web/commands"><i class="fas fa-arrow-circle-left"></i>&nbsp;&nbsp;Go
                back</a>
            <button id="proceed-to-payment-button" class="btn btn-info">Proceed to Payment</button>
        </div>
    </div>
</div>
