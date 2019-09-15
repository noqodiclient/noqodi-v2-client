
<%@attribute name="title" required="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<head>

    <base href="${appBaseUrl}">
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Demo Client Application for noqodi V2 API">
    <meta name="author" content="emaratech">

    <%--favicon--%>
    <link rel="icon" type="x/icon" href="${contextPath}/web/resources/images/favicon.ico">
    <link rel="shortcut icon" href="${contextPath}/web/resources/images/favicon.ico">

    <%--css--%>
    <link rel="stylesheet" href="${contextPath}/web/resources/css/style.css" />
    <link rel="stylesheet" href="${contextPath}/web/webjars/bootstrap/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="${contextPath}/web/resources/css/google-code-prettify/prettify.css" />
    <link rel="stylesheet" href="${contextPath}/web/webjars/font-awesome/css/all.css" />

    <%--js--%>
    <script src="${contextPath}/web/webjars/jquery/jquery.min.js"></script>
    <script src="${contextPath}/web/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="${contextPath}/web/resources/js/google-code-prettify/prettify.js"></script>
</head>
