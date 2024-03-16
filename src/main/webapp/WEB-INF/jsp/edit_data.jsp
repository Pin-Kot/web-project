<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.editData.title" var="pageEditDataTitle"/>
<fmt:message bundle="${loc}" key="label.editData.invitation" var="invitationMessage"/>
<fmt:message bundle="${loc}" key="label.editData.firstName" var="firstNameText"/>
<fmt:message bundle="${loc}" key="label.editData.lastName" var="lastNameText"/>
<fmt:message bundle="${loc}" key="label.editData.email" var="emailText"/>
<fmt:message bundle="${loc}" key="label.editData.birthDay" var="birthDayText"/>
<fmt:message bundle="${loc}" key="label.editData.birthMonth" var="birthMonthText"/>
<fmt:message bundle="${loc}" key="label.editData.birthYear" var="birthYearText"/>
<fmt:message bundle="${loc}" key="label.editData.warning" var="warningText"/>
<fmt:message bundle="${loc}" key="label.editData.submit" var="editingSubmit"/>
<fmt:message bundle="${loc}" key="label.error.accountDoesNotExistMessage" var="errorAccountDoesNotExistMessage"/>
<fmt:message bundle="${loc}" key="label.error.userDoesNotExistMessage" var="errorUserDoesNotExistMessage"/>
<fmt:message bundle="${loc}" key="label.error.enteredDateOfBirthdayMessage" var="errorEnteredDateOfBirthdayMessage"/>
<fmt:message bundle="${loc}" key="label.error.editPersonalDataMessage" var="errorEditPersonalDataMessage"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageEditDataTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/form.css"%>
    <%@include file="/WEB-INF/css/text.css"%>
</style>
<ul id="enter_data">
    <h3>${invitationMessage}</h3>
    <form name="edit_data-form" action="${pageContext.request.contextPath}/controller?command=edit_data" method="post">
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
        <input type="submit" value="${editingSubmit}">
    </form>
</ul>
</body>
</html>
