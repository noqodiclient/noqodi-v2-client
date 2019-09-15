<%@attribute name="homeActive" required="false" %>
<%@attribute name="balancesActive" required="false" %>
<%@attribute name="paymentsActive" required="false" %>
<%@attribute name="payoutsActive" required="false" %>
<%@attribute name="refundsActive" required="false" %>
<%@attribute name="vouchersActive" required="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<nav class="navbar navbar-expand-md navbar-light fixed-top bg-light font-weight-light shadow-sm m-0">

    <%--brand--%>
    <a class="navbar-brand" href="${contextPath}/">
        <img src="${contextPath}/resources/images/noqodi-brand.png" width="30" height="30" alt="">
        <span class="text-dark font-weight-bold">noqodi</span>
    </a>

    <%--api links--%>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#noqodi-client-app-navbar"
            aria-controls="noqodi-client-app-navbar" aria-expanded="false" aria-label="">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse justify-content-around p-0 m-0" id="noqodi-client-app-navbar">
        <div class="navbar-nav">
            <a class="nav-link ${homeActive}" href="${contextPath}/"><i class="fas fa-home"></i></a>
            <a id="balances-command-nav-link" class="nav-link command-nav-link ${balancesActive}" href="${contextPath}/web/commands">Balances</a>
            <a id="payments-command-nav-link" class="nav-link command-nav-link ${paymentsActive}" href="${contextPath}/web/commands">Payments</a>
            <a id="payouts-command-nav-link" class="nav-link command-nav-link ${payoutsActive}" href="${contextPath}/web/commands">Payouts</a>
            <a id="refunds-command-nav-link" class="nav-link command-nav-link ${refundsActive}" href="${contextPath}/web/commands">Refunds</a>
            <a id="vouchers-command-nav-link" class="nav-link command-nav-link ${vouchersActive}" href="${contextPath}/web/commands">Vouchers</a>
        </div>
    </div>
</nav>


