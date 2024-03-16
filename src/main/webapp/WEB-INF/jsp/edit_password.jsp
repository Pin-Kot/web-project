<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.editPassword.title" var="pageEditPasswordTitle"/>
<fmt:message bundle="${loc}" key="label.editPassword.invitation" var="invitationMessage"/>
<fmt:message bundle="${loc}" key="label.editPassword.password" var="passwordText"/>
<fmt:message bundle="${loc}" key="label.editPassword.doublePassword" var="doublePasswordText"/>
<fmt:message bundle="${loc}" key="label.editPassword.warning" var="warningText"/>
<fmt:message bundle="${loc}" key="label.editPassword.submit" var="editingSubmit"/>
<fmt:message bundle="${loc}" key="label.error.editPasswordMessage" var="errorEditPasswordMessage"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageEditPasswordTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/form.css"%>
    <%@include file="/WEB-INF/css/text.css"%>
</style>
<h3>${invitationMessage}</h3>
</body>
<ul id="enter_data">
    <form name="edit_password-form" action="${pageContext.request.contextPath}/controller?command=edit_password"
          method="post">
        <label for="password-input">${passwordText}</label>
        <input id="password-input" type="password" name="password" value=""/>
        <br>
        <label for="doublePassword-input">${doublePasswordText}</label>
        <input id="doublePassword-input" type="password" name="doublePassword" value=""/>
        <br/>
        <c:choose>
            <c:when test="${not empty requestScope.errorEditPasswordMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorEditPasswordMessage}
                </div>
            </c:when>
        </c:choose>
        <input type="submit" value="${editingSubmit}"/>
    </form>
</ul>
</html>
