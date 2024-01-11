<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>
<html>
<head>
    <title>Edit data</title>
</head>
<body>
<h3>Please edit data</h3>
<form name="edit_data-form" action="${pageContext.request.contextPath}/controller?command=edit_data" method="post">
    <label for="first_name-input">Add your first name:</label>
    <input id="first_name-input" type="text" name="firstName" value=""/>
    <br>
    <label for="last_name-input">Add your last name:</label>
    <input id="last_name-input" type="text" name="lastName" value=""/>
    <br>
    <label for="email-input">Add your email:</label>
    <input id="email-input" type="text" name="email" value=""/>
    <br>
    <label for="day_of_birth-input">Add your day of birth:</label>
    <input id="day_of_birth-input" type="text" name="day" value=""/>
    <br>
    <label for="month_of_birth-input">Add your month of birth:</label>
    <input id="month_of_birth-input" type="text" name="month" value=""/>
    <br>
    <label for="year_of_birth-input">Add your year of birth:</label>
    <input id="year_of_birth-input" type="text" name="year" value=""/>
    <br>
    <c:choose>
        <c:when test="${not empty requestScope.errorAccountDoesNotExistMessage}">
            <b style="color: #ff0000">${requestScope.errorAccountDoesNotExistMessage}</b>
            <br>
        </c:when>
        <c:when test="${not empty requestScope.errorUserDoesNotExistMessage}">
            <b style="color: #ff0000">${requestScope.errorUserDoesNotExistMessage}</b>
            <br>
        </c:when>
        <c:when test="${not empty requestScope.errorEnteredDateOfBirthdayMessage}">
            <b style="color: #ff0000">${requestScope.errorEnteredDateOfBirthdayMessage}</b>
            <br>
        </c:when>
        <c:when test="${not empty requestScope.errorEditPersonalDataMessage}">
            <b style="color: #ff0000">${requestScope.errorEditPersonalDataMessage}</b>
            <br>
        </c:when>
    </c:choose>
    <input type="submit" value="Edit"/>
</form>
</body>
</html>
