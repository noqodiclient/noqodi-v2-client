<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="en">
<o:head-component title="Payments"/>
<body style="height: 100%;">

<o:login-component />

<div class="sticky-top bg-light shadow-sm p-0 m-0 d-flex justify-content-start align-items-center font-weight-light"
     style="height: auto; font-size: 20px;">
    <div class="d-flex align-self-stretch align-items-center bg-secondary px-4">
        <h1 class="text-light font-weight-light noqodi-demo-store-brand"><i class="fas fa-store"></i>&nbsp;dxbmart</h1>
    </div>
    <div class="d-flex align-self-stretch align-items-center pl-4">
        <h1 class="text-info font-weight-light noqodi-demo-store-brand">noqodi</h1>
    </div>
    <div class="d-flex align-self-stretch align-items-center">
        <h1 class="text-danger font-weight-light noqodi-demo-store-brand">payment</h1>
    </div>
    <div class="d-flex align-self-stretch align-items-center">
        <h1 class="text-info font-weight-light noqodi-demo-store-brand">demo</h1>
    </div>
</div>

<div id="main-content-container-div" class="main-content-container-div emaratech-brand-div">

    <div class="m-0 p-0">

        <div class="row m-0 p-4">

            <div class="col-12 col-lg-5 shadow-sm border border-light bg-white">
                <o:order-summary-component></o:order-summary-component>
            </div>

            <%--shadow-sm border border-light bg-transparent--%>
            <div class="col-12 col-lg-7 p-4 m-0 shadow-sm border border-light bg-white">
                <div>
                    <o:trace-component />
                </div>
            </div>
        </div>


        <div class="row collapse payment-command-iframe-div m-0 p-4" id="noqodi-payment-iframe-div">
            <div class="col m-0 p-0 h-100 shadow-sm border border-light bg-white" style="overflow:hidden;">
                <iframe src="" frameborder="0" id="noqodi-payment-iframe"
                        style="overflow:hidden; padding: 0; margin: 0; width: 100%; height: 550px;"></iframe>
            </div>
        </div>


    </div>

    <div class="m-0 p-4">
        <div id="command-result-component-wrapper">
            <o:command-result-component/>
        </div>
    </div>

</div>



<div class="void-capture-overlay-div" id="void-capture-overlay-div">
    <div class="d-flex flex-row w-100 h-100 justify-content-center align-items-center">
        <div class="void-capture-dialog bg-white p-4 shadow-sm text-center">
            <p class="text-muted p-0 m-0">After the Customer authorizes the payment, like we mentioned in the payment intro (remember?), you can then do either of the following:</h3>
            <div class="btn-group btn-group-lg void-capture-button-group my-4" role="group" aria-label="payment buttons">
                <button type="button" id="void-auth-command-button"
                        class="btn btn-danger"><i class="fas fa-window-close"></i>&nbsp;&nbsp;VOID AUTH</button>
                <button type="button" id="capture-command-button"
                        class="btn btn-success"><i class="fas fa-handshake"></i>&nbsp;&nbsp;CAPTURE</button>
            </div>
            <h3 class="text-warning font-weight-light p-0 m-0">So, take your pick&nbsp;&nbsp;<i class="far fa-smile-wink"></i></h3>
        </div>
    </div>
</div>


<o:footer-component/>

<script type="text/javascript">
    function initConfiguration(builder) {
        builder
            .noqodiPaymentPage("${noqodiPaymentPage}")
            .noqodiPaymentPageOrigin("${noqodiPaymentPageOrigin}")
            .callbackUrl("${callbackUrl}")
            .noqodiReadyData("${noqodiReadyData}")
            .apiBasePath("${apiBasePath}")
            .appBaseUrl("${appBaseUrl}");
    }
</script>

<%--<script type="module" src="${contextPath}/web/resources/js/payment-ui-util.js"></script>--%>
<script type="text/javascript" src="${contextPath}/web/resources/js/lib/noqodi-payment-core.js"></script>

</body>
</html>



