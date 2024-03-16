<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.checkout.title" var="pageCheckoutTitle"/>
<fmt:message bundle="${loc}" key="label.checkout.recipientHeader" var="recipientHeader"/>
<fmt:message bundle="${loc}" key="label.checkout.cartHeader" var="cartHeader"/>
<fmt:message bundle="${loc}" key="label.checkout.paymentHeader" var="paymentHeader"/>
<fmt:message bundle="${loc}" key="label.checkout.enquiryPayment" var="enquiryText"/>
<fmt:message bundle="${loc}" key="label.checkout.cardNumber" var="cardNumberText"/>
<fmt:message bundle="${loc}" key="label.checkout.discount" var="discountText"/>
<fmt:message bundle="${loc}" key="label.checkout.pay" var="payText"/>
<fmt:message bundle="${loc}" key="label.checkout.symbolCurrency" var="currency"/>
<fmt:message bundle="${loc}" key="label.checkout.summary" var="summaryText"/>
<fmt:message bundle="${loc}" key="label.checkout.submit" var="submitText"/>
<fmt:message bundle="${loc}" key="label.checkout.warning" var="warningText"/>
<fmt:message bundle="${loc}" key="label.error.cardNotFoundMessage" var="errorCardNotFoundMessage"/>
<fmt:message bundle="${loc}" key="label.error.amountMoneyMessage" var="errorAmountMoneyMessage"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageCheckoutTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/form.css"%>
    <%@include file="/WEB-INF/css/text.css"%>
</style>
<ul id="enter_data">
    <c:if test="${not empty requestScope.user}">
        <h3>${recipientHeader} ${requestScope.user.firstName} ${requestScope.user.lastName}</h3>
        <h3>${cartHeader}</h3>
        <c:forEach var="track" items="${sessionScope.shoppingCart}">
            <li>
                    ${track.title} - ${track.year} - ${track.price}$
            </li>
        </c:forEach>
        <h3>${paymentHeader}</h3>
        <h4>${enquiryText}</h4>
        <form name="payment-form" action="${pageContext.request.contextPath}/controller?command=pay" method="post">
            <label for="card_number-input">card number:</label>
            <input id="card_number-input" type="text" name="cardNumber" value="">
            <br>
            <p>${summaryText} ${sessionScope.totalPrice}${currency}</p>
            <c:set var="price" value="${sessionScope.totalPrice}"/>
            <c:set var="percent" value="${requestScope.user.discount}"/>
            <c:set var="discount" value="${price * percent / 100}"/>
            <p><c:out value="${discountText} -${discount}${currency}"/></p>
            <c:set var="payment" value="${price-discount}"/>
            <p><c:out value="${payText} ${payment}${currency}"/></p>
            <input type="hidden" name="payment" value="${payment}"/>
            <input type="submit" value="${submitText}">
        </form>
    </c:if>
    <c:choose>
        <c:when test="${not empty requestScope.errorCardNotFoundMessage}">
            <div class="alert">
                <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                <strong>${warningText}</strong> ${errorCardNotFoundMessage}
            </div>
        </c:when>
        <c:when test="${not empty requestScope.errorAmountMoneyMessage}">
            <div class="alert">
                <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                <strong>${warningText}</strong> ${errorAmountMoneyMessage}
            </div>
        </c:when>
    </c:choose>
</ul>
</body>
</html>