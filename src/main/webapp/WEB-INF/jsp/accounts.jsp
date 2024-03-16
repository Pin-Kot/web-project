<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.accounts.title" var="pageAccountsTitle"/>
<fmt:message bundle="${loc}" key="label.accounts.table.caption" var="caption"/>
<fmt:message bundle="${loc}" key="label.accounts.table.columnHeader.number" var="number"/>
<fmt:message bundle="${loc}" key="label.accounts.table.columnHeader.login" var="login"/>
<fmt:message bundle="${loc}" key="label.accounts.table.columnHeader.id" var="id"/>
<fmt:message bundle="${loc}" key="label.accounts.table.columnHeader.role" var="role"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageAccountsTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/text.css"%>
    <%@include file="/WEB-INF/css/table.css"%>
</style>
<table class="table">
    <caption>${caption}</caption>
    <tr>
        <th>${number}</th>
        <th>${login}</th>
        <th>${id}</th>
        <th>${role}</th>
    </tr>
    <c:forEach var="account" items="${requestScope.accounts}">
        <tr>
            <td></td>
            <td>${account.login}</td>
            <td>${account.id}</td>
            <td>${account.role}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
