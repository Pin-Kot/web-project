<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.users.title" var="pageUsersTitle"/>
<fmt:message bundle="${loc}" key="label.users.table.caption" var="caption"/>
<fmt:message bundle="${loc}" key="label.users.table.columnHeader.number" var="number"/>
<fmt:message bundle="${loc}" key="label.users.table.columnHeader.firstName" var="firstName"/>
<fmt:message bundle="${loc}" key="label.users.table.columnHeader.lastName" var="lastName"/>
<fmt:message bundle="${loc}" key="label.users.table.columnHeader.id" var="id"/>
<fmt:message bundle="${loc}" key="label.users.table.columnHeader.email" var="email"/>
<fmt:message bundle="${loc}" key="label.users.table.columnHeader.birthDate" var="birthDate"/>
<fmt:message bundle="${loc}" key="label.users.table.columnHeader.discount" var="discount"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageUsersTitle}</title>
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
        <th>${firstName}</th>
        <th>${lastName}</th>
        <th>${id}</th>
        <th>${email}</th>
        <th>${birthDate}</th>
        <th>${discount}</th>
    </tr>
    <c:forEach var="user" items="${requestScope.users}">
        <tr>
            <td></td>
            <td>${user.firstName}</td>
            <td>${user.lastName}</td>
            <td>${user.id}</td>
            <td>${user.email}</td>
            <td>${user.birthday}</td>
            <td>${user.discount}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
