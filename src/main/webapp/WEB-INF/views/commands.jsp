<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="en">
<o:head-component title="Commands"/>
<body style="height: 100%;">

<o:login-component />

<o:navbar-component balancesActive="active"/>
<o:header-component heading="..."/>

<div id="main-content-container-div" class="main-content-container-div">

    <o:command-list-component/>
    <o:command-result-component/>

</div>
<o:footer-component/>

<script type="text/javascript">
    var contextPath = "${contextPath}";
    function initConfiguration(builder) {
        builder
            .apiBasePath("${apiBasePath}")
            .appBaseUrl("${appBaseUrl}");
    }
</script>

<%--<script type="module" src="${contextPath}/web/resources/js/command-ui-util.js"></script>--%>
<script type="text/javascript" src="${contextPath}/web/resources/js/lib/noqodi-command-core.js"></script>

</body>
</html>



