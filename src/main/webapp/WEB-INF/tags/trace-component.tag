
<fieldset class="border trace-fieldset shadow-sm border border-secondary bg-transparent d-block">
    <legend class="m-0 w-auto text-secondary font-weight-light payment-trace">&nbsp;&nbsp;Trace&nbsp;&nbsp;</legend>
    <div class="d-flex flex-row m-0 p-0 justify-content-start align-items-center">
        <div class="trace-element-div">
            <button id="trace-element-preauth"
                    class="btn btn-block btn-secondary animated-trace-element" disabled>pre-auth
            </button>
        </div>
        <div class="trace-element-arrow-div">
            <h3 class="text-secondary m-auto"><i class="fas fa-arrow-right"></i></h3>
        </div>
        <div class="trace-element-div">
            <button id="trace-element-load-iframe"
                    class="btn btn-block btn-secondary animated-trace-element" disabled>iframe
            </button>
        </div>
        <div class="trace-element-arrow-div">
            <h3 class="text-secondary m-auto"><i class="fas fa-arrow-right"></i></h3>
        </div>
        <div class="trace-element-div">
            <button id="trace-element-auth-response"
                    class="btn btn-block btn-secondary animated-trace-element" disabled>
                auth-response
            </button>
        </div>
        <div class="trace-element-arrow-div">
            <h3 class="text-secondary m-auto"><i class="fas fa-arrow-right"></i></h3>
        </div>
        <div class="trace-element-div">
            <button id="trace-element-capture"
                    class="btn btn-block btn-secondary animated-trace-element" disabled>void/capture
            </button>
        </div>
    </div>
    <p class="lead text-info font-weight-light">You can click <span
            class="text-danger font-weight-bold"><i class="fas fa-hand-pointer"></i></span> each
        trace step to view the raw request/response log.</p>
</fieldset>

