<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.cart.title" var="pageCartTitle"/>
<fmt:message bundle="${loc}" key="label.cart.header" var="headerText"/>
<fmt:message bundle="${loc}" key="label.cart.symbolCurrency" var="currency"/>
<fmt:message bundle="${loc}" key="label.cart.table.caption" var="caption"/>
<fmt:message bundle="${loc}" key="label.cart.table.columnHeader.number" var="number"/>
<fmt:message bundle="${loc}" key="label.cart.table.columnHeader.title" var="title"/>
<fmt:message bundle="${loc}" key="label.cart.table.columnHeader.year" var="year"/>
<fmt:message bundle="${loc}" key="label.cart.table.columnHeader.price" var="price"/>
<fmt:message bundle="${loc}" key="label.cart.table.columnHeader.delete" var="deleteText"/>
<fmt:message bundle="${loc}" key="label.cart.table.context.submit" var="submitDeleteText"/>
<fmt:message bundle="${loc}" key="label.links.checkout" var="checkoutLink"/>

<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <title>${pageCartTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/text.css"%>
    <%@include file="/WEB-INF/css/main.css"%>
    <%@include file="/WEB-INF/css/table.css"%>
    <%@include file="/WEB-INF/css/form.css"%>
</style>
<c:if test="${not empty sessionScope.account}">
    <table class="table">
        <caption>${caption}</caption>
        <tr>
            <th>${number}</th>
            <th>${title}</th>
            <th>${year}</th>
            <th>${price}</th>
            <th>${deleteText}</th>
        </tr>
        <c:forEach var="track" items="${sessionScope.shoppingCart}">
            <tr>
                <td></td>
                <td>${track.title}</td>
                <td>${track.year}</td>
                <td>${track.price}</td>
                <form name="" action="${pageContext.request.contextPath}/controller?command=delete_from_cart"
                      method="post">
                    <input type="hidden" name="trackId" value="${track.id}"/>
                    <td><input type="submit" value="${submitDeleteText}"></td>
                </form>
            </tr>
        </c:forEach>
    </table>
    <c:if test="${sessionScope.totalPrice > 0}">
        <h3>${headerText} ${sessionScope.totalPrice}${currency}</h3>
        <li><a href="${pageContext.request.contextPath}/controller?command=show_checkout">${checkoutLink}</a></li>
    </c:if>
</c:if>
</body>
</html>