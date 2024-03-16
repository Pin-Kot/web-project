<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.delete.title" var="pageDeleteTitle"/>
<fmt:message bundle="${loc}" key="label.delete.labelSelect" var="labelText"/>
<fmt:message bundle="${loc}" key="label.delete.selector1" var="album"/>
<fmt:message bundle="${loc}" key="label.delete.selector2" var="artist"/>
<fmt:message bundle="${loc}" key="label.delete.selector3" var="image"/>
<fmt:message bundle="${loc}" key="label.delete.selector4" var="track"/>
<fmt:message bundle="${loc}" key="label.delete.placeholder" var="placeholder"/>
<fmt:message bundle="${loc}" key="label.delete.submit" var="submitText"/>
<fmt:message bundle="${loc}" key="label.delete.warning" var="warningText"/>
<fmt:message bundle="${loc}" key="label.error.entityNotFoundMessage" var="errorEntityNotFoundMessage"/>
<fmt:message bundle="${loc}" key="label.error.wrongEntityNameMessage" var="errorWrongEntityNameMessage"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageDeleteTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/text.css"%>
    <%@include file="/WEB-INF/css/main.css"%>
    <%@include file="/WEB-INF/css/form.css"%>
</style>
<ul id="enter_data">
    <form class="search-form" action="${pageContext.request.contextPath}/controller?command=delete" method="post">
        <li>
            <label for="element">${labelText}</label>
            <select class="select-type" id="element" name="element">
                <option value="album">${album}</option>
                <option value="artist">${artist}</option>
                <option value="image">${image}</option>
                <option value="track">${track}</option>
            </select>
        </li>
        <input type="text" placeholder="${placeholder}" name="id" value=""/>
        <br>
        <input type="submit" class="search-submit" value="${submitText}">
        <br>
        <c:choose>
            <c:when test="${not empty requestScope.errorEntityNotFoundMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorEntityNotFoundMessage}
                </div>
            </c:when>
            <c:when test="${not empty requestScope.errorWrongEntityNameMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorWrongEntityNameMessage}
                </div>
            </c:when>
        </c:choose>
    </form>
</ul>
</body>
</html>