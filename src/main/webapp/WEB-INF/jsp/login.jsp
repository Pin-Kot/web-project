<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.logging.title" var="pageLoggingTitle"/>
<fmt:message bundle="${loc}" key="label.logging.invitation" var="invitationMessage"/>
<fmt:message bundle="${loc}" key="label.logging.login" var="loginText"/>
<fmt:message bundle="${loc}" key="label.logging.password" var="passwordText"/>
<fmt:message bundle="${loc}" key="label.logging.warning" var="warningText"/>
<fmt:message bundle="${loc}" key="label.logging.submit" var="loginSubmit"/>
<fmt:message bundle="${loc}" key="label.error.loginPassMessage" var="errorLoginPassMessage"/>

<html lang="${sessionScope.lang}">
<head>
    <meta charset="UTF-8">
    <title>${pageLoggingTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/form.css"%>
    <%@include file="/WEB-INF/css/text.css"%>
</style>
<h3>${invitationMessage}</h3>
<ul id="enter_data">
    <form name="login-form" action="${pageContext.request.contextPath}/controller?command=login" method="post">
        <label for="login-input">${loginText}</label>
        <input id="login-input" type="text" name="login" value=""/>
        <br>
        <label for="password-input">${passwordText}</label>
        <input id="password-input" type="password" name="password" value=""/>
        <br/>
        <c:if test="${not empty requestScope.errorLoginPassMessage}">
            <div class="alert">
                <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                <strong>${warningText}</strong> ${errorLoginPassMessage}
            </div>
            <br>
        </c:if>
        <input type="submit" value="${loginSubmit}">
    </form>
</ul>
</body>
</html>
