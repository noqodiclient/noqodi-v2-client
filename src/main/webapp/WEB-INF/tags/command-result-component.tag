

<div id="command-result-component-div" class="collapse p-4 m-0">

    <div class="payload-content">
        <h3 class="font-weight-light text-dark text-left" id="command-response-header-text">loading</h3>
        <%--request metadata--%>
        <div class="row payload-request-method px-3">
            <div class="col-sm-3 bg-dark">
                <p class="command-request-sub-header-text text-light font-weight-normal my-0">HTTP Method: </p>
            </div>
            <div class="col-sm-9 bg-light">
                <p id="command-http-method-text"
                   class="command-request-sub-header-text text-dark font-weight-light my-0">loading...</p>
            </div>
        </div>
        <div class="row payload-request-path mt-1 px-3">
            <div class="col-sm-3 bg-info">
                <p class="command-request-sub-header-text text-light font-weight-normal my-0">URL: </p>
            </div>
            <div class="col-sm-9 bg-light">
                <p id="command-http-url-text" style="word-wrap: break-word;"
                   class="command-request-sub-header-text text-dark font-weight-light my-0">loading...</p>
            </div>
        </div>
        <%--response metadata--%>
        <div class="row payload-response-status mt-1 px-3">
            <div class="col-sm-3 bg-secondary">
                <p class="command-request-sub-header-text text-light font-weight-normal my-0">HTTP Status: </p>
            </div>
            <div class="col-sm-9 bg-light">
                <p id="command-http-status-text"
                   class="command-request-sub-header-text text-dark font-weight-light my-0">loading...</p>
            </div>
        </div>
    </div>

    <%--command result pane--%>
    <div class="row payload-content">
        <%--request section--%>
        <div class="col col-md-6 pt-4" id="merchant-balance-request">
            <div class="row command-request-header-div">
                <div class="col">
                    <h4 class="command-request-header-text text-muted font-weight-light">Request</h4>
                </div>
            </div>
            <%--request data--%>
            <div class="row command-payload-div">
                <div class="col">
                    <p id="command-request-text" class="command-result-text"></p>
                    <pre id="command-request-json-view" class="prettyprint lang-js"></pre>
                </div>
            </div>
        </div>

        <%--response section--%>
        <div class="col col-md-6 pt-4" id="merchant-balance-response">
            <div class="row command-request-header-div">
                <div class="col">
                    <h4 class="command-request-header-text text-muted font-weight-light">Response</h4>
                </div>
            </div>
            <%--response data--%>
            <div class="row command-payload-div">
                <div class="col">
                    <p id="command-response-text" class="command-result-text"></p>
                    <pre id="command-response-json-view" class="prettyprint lang-js"></pre>
                </div>
            </div>
        </div>
    </div>

</div>


