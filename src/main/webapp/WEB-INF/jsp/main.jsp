<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jwds" uri="online-store.jwd.epam.com" %>
<%@ page import="com.epam.jwd.audiotrack_ordering.entity.Role" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8" %>

<%@include file="header.jsp"%>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc" />

<fmt:message bundle="${loc}" key="label.title" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.invitation" var="invitationMessage"/>
<fmt:message bundle="${loc}" key="label.welcome" var="welcomeMessage"/>
<fmt:message bundle="${loc}" key="label.links.users" var="usersLink"/>
<fmt:message bundle="${loc}" key="label.links.accounts" var="accountsLink"/>
<fmt:message bundle="${loc}" key="label.links.admin" var="adminLink"/>
<fmt:message bundle="${loc}" key="label.links.artists" var="artistsLink"/>
<fmt:message bundle="${loc}" key="label.links.albums" var="albumsLink"/>
<fmt:message bundle="${loc}" key="label.links.tracks" var="tracksLink"/>
<fmt:message bundle="${loc}" key="label.links.logout" var="logoutLink"/>
<fmt:message bundle="${loc}" key="label.links.login" var="loginLink"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/text.css"%>
    <%@include file="/WEB-INF/css/main.css"%>

</style>
<h1>${invitationMessage}</h1>
<jwds:welcomeAccount text="${welcomeMessage}"/>
<br>

<c:if test="${not empty sessionScope.account && sessionScope.account.role eq Role.ADMIN}">
    <li><a href="${pageContext.request.contextPath}/controller?command=show_admin">${adminLink}</a> </li>
    <br>
</c:if>

<li><a href="${pageContext.request.contextPath}/controller?command=show_artists">${artistsLink}</a></li>
<li><a href="${pageContext.request.contextPath}/controller?command=show_albums&page=albums">${albumsLink}</a></li>
<li><a href="${pageContext.request.contextPath}/controller?command=show_tracks">${tracksLink}</a></li>
<br>
<%--<c:choose>--%>
<%--    <c:when test="${not empty sessionScope.account}">--%>
<%--        <a href="${pageContext.request.cont  extPath}/controller?command=logout">${logoutLink}</a>--%>
<%--    </c:when>--%>
<%--    <c:otherwise>--%>
<%--        <a href="${pageContext.request.contextPath}/controller?command=show_login">${loginLink}</a>--%>
<%--    </c:otherwise>--%>
<%--</c:choose>--%>
</body>
</html>
