<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jwds" uri="online-store.jwd.epam.com" %>
<%--<%@ page import="com.epam.jwd.audiotrack_ordering.entity.Role" %>--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.lang}"/>

<fmt:setBundle basename="l10n.page.main" var="loc" />
<fmt:message bundle="${loc}" key="label.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.links.logout" var="logoutLink"/>
<fmt:message bundle="${loc}" key="label.links.login" var="loginLink"/>
<fmt:message bundle="${loc}" key="label.links.home_page" var="home_pageLink"/>
<html>
<head>
    <title>${pageTitle}</title>
</head>
<body>

<style>
    <%@include file="/WEB-INF/css/header.css"%>
</style>

<ul id="menu">
    <li><a href="${pageContext.request.contextPath}/controller?command=main_page">${home_pageLink}</a></li>
    <li><form name="lang" action="${pageContext.request.contextPath}/controller?command=change_language" method="post">
        <select class="select-css" name="lang" onchange="submit()">
            <option>
                <c:choose>
                    <c:when test="${sessionScope.lang eq 'en_US'}">
                        English
                    </c:when>
                    <c:when test="${sessionScope.lang eq 'ru_RU'}">
                        Русский
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>
            </option>
            <option>English</option>
            <option>Russian</option>
        </select>
        <%--    </label>--%>
    </form></li>

<c:choose>
    <c:when test="${not empty sessionScope.account}">
        <a href="${pageContext.request.contextPath}/controller?command=logout">${logoutLink}</a>
    </c:when>
    <c:otherwise>
        <a href="${pageContext.request.contextPath}/controller?command=show_login">${loginLink}</a>
    </c:otherwise>
</c:choose>
</ul>
</body>
</html>