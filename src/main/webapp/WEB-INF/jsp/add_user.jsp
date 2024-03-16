<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.addUser.title" var="pageAddUserTitle"/>
<fmt:message bundle="${loc}" key="label.addUser.invitation" var="invitationMessage"/>
<fmt:message bundle="${loc}" key="label.addUser.firstName" var="firstNameText"/>
<fmt:message bundle="${loc}" key="label.addUser.lastName" var="lastNameText"/>
<fmt:message bundle="${loc}" key="label.addUser.email" var="emailText"/>
<fmt:message bundle="${loc}" key="label.addUser.birthDay" var="birthDayText"/>
<fmt:message bundle="${loc}" key="label.addUser.birthMonth" var="birthMonthText"/>
<fmt:message bundle="${loc}" key="label.addUser.birthYear" var="birthYearText"/>
<fmt:message bundle="${loc}" key="label.addUser.warning" var="warningText"/>
<fmt:message bundle="${loc}" key="label.addUser.submit" var="addSubmit"/>
<fmt:message bundle="${loc}" key="label.error.accountDoesNotExistMessage" var="errorAccountDoesNotExistMessage"/>
<fmt:message bundle="${loc}" key="label.error.userDoesNotExistMessage" var="errorUserDoesNotExistMessage"/>
<fmt:message bundle="${loc}" key="label.error.enteredDateOfBirthdayMessage" var="errorEnteredDateOfBirthdayMessage"/>
<fmt:message bundle="${loc}" key="label.error.editPersonalDataMessage" var="errorEditPersonalDataMessage"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageAddUserTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/form.css"%>
    <%@include file="/WEB-INF/css/text.css"%>
</style>
<h3>${invitationMessage}</h3>
<ul id="enter_data">
    <form name="add_user_data-form" action="${pageContext.request.contextPath}/controller?command=add_user" method="post">
        <label for="first_name-input">${firstNameText}</label>
        <input id="first_name-input" type="text" name="firstName" value=""/>
        <br>
        <label for="last_name-input">${lastNameText}</label>
        <input id="last_name-input" type="text" name="lastName" value=""/>
        <br>
        <label for="email-input">${emailText}</label>
        <input id="email-input" type="text" name="email" value=""/>
        <br>
        <label for="day_of_birth-input">${birthDayText}</label>
        <input id="day_of_birth-input" type="text" name="day" value=""/>
        <br>
        <label for="month_of_birth-input">${birthMonthText}</label>
        <input id="month_of_birth-input" type="text" name="month" value=""/>
        <br>
        <label for="year_of_birth-input">${birthYearText}</label>
        <input id="year_of_birth-input" type="text" name="year" value=""/>
        <br>
        <c:choose>
            <c:when test="${not empty requestScope.errorAccountDoesNotExistMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorAccountDoesNotExistMessage}
                </div>
            </c:when>
            <c:when test="${not empty requestScope.errorUserDoesNotExistMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorUserDoesNotExistMessage}
                </div>
            </c:when>
            <c:when test="${not empty requestScope.errorEnteredDateOfBirthdayMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorEnteredDateOfBirthdayMessage}
                </div>
            </c:when>
            <c:when test="${not empty requestScope.errorEditPersonalDataMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorEditPersonalDataMessage}
                </div>
            </c:when>
        </c:choose>
        <input type="submit" value="${addSubmit}"/>
    </form>
</ul>
</body>
</html>
