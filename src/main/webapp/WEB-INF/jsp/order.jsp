<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.order.title" var="pageOrderTitle"/>
<fmt:message bundle="${loc}" key="label.order.date" var="dateText"/>
<fmt:message bundle="${loc}" key="label.order.status" var="statusText"/>
<fmt:message bundle="${loc}" key="label.order.amount" var="amountText"/>
<fmt:message bundle="${loc}" key="label.order.header" var="headerTitle"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageOrderTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/text.css"%>
</style>
<c:set var="order" value="${requestScope.order}"/>
<p>
    ${dateText} <span>${order.date}</span>, ${statusText} <span>${order.status}</span>, ${amountText}
    <span>${order.value}$</span>
    <br>
</p>
<h3>${headerTitle}</h3>
<ol class="list_part">
    <c:forEach var="track" items="${requestScope.tracks}">
        <li>
                ${track.title} - ${track.year}
        </li>
    </c:forEach>
</ol>
</body>
</html>