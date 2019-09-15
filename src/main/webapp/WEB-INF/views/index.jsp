<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html lang="en">
<o:head-component title="Home"/>
<body style="height: 100%;">

<div class="index-wrap-div">
    <%--welcome--%>
    <div id="index-welcome-div" class="fade-onload justify-content-center align-items-center py-5"
         style="display: none">
        <div class="d-flex justify-content-center align-items-center flex-column">
            <h1 style="font-size: 8.8em;" class="">
                <span style="color: #337ab7" class="font-weight-normal">noqodi</span>
            </h1>

            <h2 style="font-size: 10em; margin-top: -50px" class="font-weight-light">
                <span class="text-secondary">V2</span>
                <span class="text-warning">API</span>
            </h2>

            <h2 style="font-size: 1.35em; margin-top: -35px" class="font-weight-normal bg-dark py-1 mb-0 shadow-sm">
                <span class="text-warning">R E F E R E N C E</span>
                <span class="text-light">I M P L E M E N T A T I O N</span>
            </h2>
        </div>
    </div>

    <%--divider--%>
    <hr class="home-divider shadow rounded mx-auto" align="center" width="80%">

    <%--get started--%>
    <div id="index-intro-div"
         class="index-intro-div fade-onload justify-content-center align-items-center py-5 mx-auto"
         style="display: none">
        <div class="d-flex justify-content-center align-items-center flex-column">
            <h2 class="display-2 font-weight-light text-info">
                Tea or Coffee? <span class="text-danger"><i class="fas fa-coffee"></i></span>
            </h2>

            <div class="p-0 m-0">
                <h3 class="font-weight-light d-inline-block align-middle text-danger m-0 p-0">
                    NO THANKS, I'M HERE FOR THE&nbsp;</h3>
                <a href="${contextPath}/web/commands" class="btn btn-outline-dark d-inline-block p-3 shadow-sm">
                    <h3 class="font-weight-light p-0 m-0" style="vertical-align: middle;">
                        <i class="fas fa-code">&nbsp;CODE</i>
                    </h3>
                </a>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        $("#index-welcome-div").fadeIn(1500);
        setTimeout(function() {
            $("#index-intro-div").fadeIn(1500);
        }, 250);
    });
</script>

</body>
</html>


