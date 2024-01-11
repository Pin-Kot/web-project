<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>
<html>
<head>
    <title>Sign up</title>
</head>
<body>
<h3>Please register:</h3>
<form name="sign_up-form" action="${pageContext.request.contextPath}/controller?command=sign_up" method="post">
    <label for="login-input">Create a login:</label>
    <input id="login-input" type="text" name="login" value=""/>
    <br>
    <label for="password-input">Create a password:</label>
    <input id="password-input" type="password" name="password" value=""/>
    <br>
    <label for="doublePassword-input">Confirm a password:</label>
    <input id="doublePassword-input" type="password" name="doublePassword" value=""/>
    <br/>
    <c:choose>
        <c:when test="${not empty requestScope.errorAccountExistsMessage}">
            <b style="color: red">${requestScope.errorAccountExistsMessage}</b>
            <br>
        </c:when>
        <c:when test="${not empty requestScope.errorSignUpPassMessage}">
            <b style="color: red">${requestScope.errorSignUpPassMessage}</b>
            <br>
        </c:when>
        <c:when test="${not empty requestScope.errorPasswordMismatchMessage}">
            <b style="color: orange">${requestScope.errorPasswordMismatchMessage}</b>
            <br/>
        </c:when>
    </c:choose>
    <input type="submit" value="Sign up"/>
</form>
<c:if test="${not empty requestScope.serviceNewAccountCreatedMessage}">
    <b style="color: #0022ff">${requestScope.serviceNewAccountCreatedMessage}</b>
    <br>
    <p>Now you can login </p>
    <a href="${pageContext.request.contextPath}/controller?command=show_login">login</a>
</c:if>
</body>