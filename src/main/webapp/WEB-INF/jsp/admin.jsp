<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.title.admin" var="pageLoggingTitle"/>
<fmt:message bundle="${loc}" key="label.links.users" var="usersLink"/>
<fmt:message bundle="${loc}" key="label.links.accounts" var="accountsLink"/>
<fmt:message bundle="${loc}" key="label.links.add_artist" var="addArtistLink"/>
<fmt:message bundle="${loc}" key="label.links.add_album" var="addAlbumLink"/>
<fmt:message bundle="${loc}" key="label.links.add_track" var="addTrackLink"/>
<fmt:message bundle="${loc}" key="label.links.add_image" var="addImageLink"/>
<fmt:message bundle="${loc}" key="label.links.delete" var="deleteLink"/>
<fmt:message bundle="${loc}" key="label.links.assign_discount" var="discountLink"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageLoggingTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/text.css"%>
    <%@include file="/WEB-INF/css/main.css"%>
</style>
<li><a href="${pageContext.request.contextPath}/controller?command=show_users">${usersLink}</a></li>
<li><a href="${pageContext.request.contextPath}/controller?command=show_accounts">${accountsLink}</a></li>
<li><a href="${pageContext.request.contextPath}/controller?command=show_assign_discount">${discountLink}</a></li>
<br>
<li><a href="${pageContext.request.contextPath}/controller?command=show_add_artist">${addArtistLink}</a></li>
<li><a href="${pageContext.request.contextPath}/controller?command=show_add_album">${addAlbumLink}</a></li>
<li><a href="${pageContext.request.contextPath}/controller?command=show_add_track">${addTrackLink}</a></li>
<li><a href="${pageContext.request.contextPath}/controller?command=show_add_image">${addImageLink}</a></li>
<li><a href="${pageContext.request.contextPath}/controller?command=show_delete">${deleteLink}</a></li>
</body>
</html>
