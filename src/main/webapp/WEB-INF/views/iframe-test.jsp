<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<o:head-component title="iframe-test"/>
<body>

<button id="mock-pay-now-btn" class="btn btn-success">(mock) pay now</button>

<script>
    $(document).ready(function () {

        window.parent.postMessage(
            "noqodi ready",
            "http://localhost:8080"
        );

        $('#mock-pay-now-btn').on('click', function () {
            window.parent.postMessage(
                "http://localhost:8080/callback?response=xx-xxx-xxxx-xxxxx",
                "http://localhost:8080"
            );
        });
    });
</script>
</body>
</html>
