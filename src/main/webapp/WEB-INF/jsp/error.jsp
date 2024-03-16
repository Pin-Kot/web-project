<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.error.title" var="pageErrorTitle"/>
<fmt:message bundle="${loc}" key="label.error.warning" var="warningText"/>
<fmt:message bundle="${loc}" key="label.error.header" var="headerText"/>

<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <title>${pageErrorTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/text.css"%>
</style>
<h3><strong>${warningText}</strong> ${headerText}</h3>
</body>
</html>
