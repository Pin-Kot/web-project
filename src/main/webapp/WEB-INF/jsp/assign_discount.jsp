<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.assignDiscount.title" var="pageAssigningDiscountTitle"/>
<fmt:message bundle="${loc}" key="label.assignDiscount.invitation" var="invitationMessage"/>
<fmt:message bundle="${loc}" key="label.assignDiscount.userId" var="userIdText"/>
<fmt:message bundle="${loc}" key="label.assignDiscount.discount" var="discountText"/>
<fmt:message bundle="${loc}" key="label.assignDiscount.warning" var="warningText"/>
<fmt:message bundle="${loc}" key="label.assignDiscount.submit" var="assignText"/>
<fmt:message bundle="${loc}" key="label.error.enteredIdMessage" var="errorEnteredIdMessage"/>
<fmt:message bundle="${loc}" key="label.error.enteredDiscountMessage" var="errorEnteredDiscountMessage"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageAssigningDiscountTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/form.css"%>
    <%@include file="/WEB-INF/css/text.css"%>
</style>
<h3>${invitationMessage}</h3>
<ul id="enter_data">
    <form name="edit_discount-form" action="${pageContext.request.contextPath}/controller?command=assign_discount"
          method="post">
        <label for="id-input">${userIdText}</label>
        <input id="id-input" type="text" name="userId" value=""/>
        <br>
        <label for="discount-input">${discountText}</label>
        <input id="discount-input" type="text" name="discount" value=""/>
        <br/>
        <c:choose>
            <c:when test="${not empty requestScope.errorEnteredIdMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorEnteredIdMessage}
                </div>
            </c:when>
            <c:when test="${not empty requestScope.errorEnteredDiscountMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorEnteredDiscountMessage}
                </div>
            </c:when>
        </c:choose>
        <input type="submit" value="${assignText}">
    </form>
</ul>
</body>
</html>