<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.registration.title" var="pageRegistrationTitle"/>
<fmt:message bundle="${loc}" key="label.registration.invitation" var="invitationMessage"/>
<fmt:message bundle="${loc}" key="label.registration.login" var="loginText"/>
<fmt:message bundle="${loc}" key="label.registration.password" var="passwordText"/>
<fmt:message bundle="${loc}" key="label.registration.doublePassword" var="doublePasswordText"/>
<fmt:message bundle="${loc}" key="label.registration.warning" var="warningText"/>
<fmt:message bundle="${loc}" key="label.registration.submit" var="registrationSubmit"/>
<fmt:message bundle="${loc}" key="label.error.accountExistsMessage" var="errorAccountExistsMessage"/>
<fmt:message bundle="${loc}" key="label.error.signUpPassMessage" var="errorSignUpPassMessage"/>
<fmt:message bundle="${loc}" key="label.error.passwordMismatchMessage" var="errorPasswordMismatchMessage"/>
<fmt:message bundle="${loc}" key="label.registration.congratulation" var="congratulationText"/>
<fmt:message bundle="${loc}" key="label.service.newAccountCreatedMessage" var="serviceNewAccountCreatedMessage"/>
<fmt:message bundle="${loc}" key="label.registration.loginInvitation" var="loginInvitationMessage"/>
<fmt:message bundle="${loc}" key="label.links.login" var="loginLink"/>

<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <title>${pageRegistrationTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/form.css"%>
    <%@include file="/WEB-INF/css/text.css"%>
</style>
<h3>${invitationMessage}</h3>
<ul id="enter_data">
    <form name="sign_up-form" action="${pageContext.request.contextPath}/controller?command=sign_up" method="post">
        <label for="login-input">${loginText}</label>
        <input id="login-input" type="text" name="login" value=""/>
        <br>
        <label for="password-input">${passwordText}</label>
        <input id="password-input" type="password" name="password" value=""/>
        <br>
        <label for="doublePassword-input">${doublePasswordText}</label>
        <input id="doublePassword-input" type="password" name="doublePassword" value=""/>
        <br/>
        <c:choose>
            <c:when test="${not empty requestScope.errorAccountExistsMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorAccountExistsMessage}
                </div>
            </c:when>
            <c:when test="${not empty requestScope.errorSignUpPassMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorSignUpPassMessage}
                </div>
            </c:when>
            <c:when test="${not empty requestScope.errorPasswordMismatchMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorPasswordMismatchMessage}
                </div>
            </c:when>
        </c:choose>
        <input type="submit" value="${registrationSubmit}">
    </form>
    <c:if test="${not empty requestScope.serviceNewAccountCreatedMessage}">
        <div class="service">
            <p><strong>${congratulationText}</strong> ${serviceNewAccountCreatedMessage}</p>
            <p>${loginInvitationMessage}</p>
            <a href="${pageContext.request.contextPath}/controller?command=show_login">${loginLink}</a>
        </div>
    </c:if>
</ul>
</body>
</html>