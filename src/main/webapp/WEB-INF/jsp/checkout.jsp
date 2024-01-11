<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<html>
<head>
    <title>Checkout</title>
</head>
<body>
<c:if test="${not empty requestScope.user}">
    <p>Recipient</p>
    <p>${requestScope.user.firstName} ${requestScope.user.lastName}</p>
    <p>Items for payment:</p>
    <c:forEach var="track" items="${sessionScope.shoppingCart}">
        <li>
                ${track.title} - ${track.year} - ${track.price}$
        </li>
    </c:forEach>
    <p>Payment method</p>
    <p>enter pay card information</p>
    <form name="payment-form" action="${pageContext.request.contextPath}/controller?command=pay" method="post">
        <label for="card_number-input">card number:</label>
        <input id="card_number-input" type="text" name="cardNumber" value="">
        <br>
        <p>Total price: ${sessionScope.totalPrice}$</p>
        <c:set var="price" value="${sessionScope.totalPrice}"/>
        <c:set var="percent" value="${requestScope.user.discount}"/>
        <c:set var="discount" value="${price * percent / 100}"/>
        <p> <c:out value="Discount: -${discount}$"/></p>
        <c:set var="payment" value="${price-discount}"/>
        <p> <c:out value="To pay: ${payment}$"/></p>
        <input type="hidden" name="payment" value="${payment}"/>
        <input type="submit" value="Pay"/>
    </form>
    <c:choose>
        <c:when test="${not empty requestScope.errorCardNotFoundMessage}">
            <b style="color: #ff0000">${requestScope.errorCardNotFoundMessage}</b>
            <br>
        </c:when>
        <c:when test="${not empty requestScope.errorAmountMoneyMessage}">
            <b style="color: #ff0000">${requestScope.errorAmountMoneyMessage}</b>
            <br>
        </c:when>
    </c:choose>
</c:if>
</body>
</html>