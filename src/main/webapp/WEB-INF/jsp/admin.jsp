<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.title.admin" var="pageTitle"/>
<fmt:message bundle="${loc}" key="label.links.users" var="usersLink"/>
<fmt:message bundle="${loc}" key="label.links.accounts" var="accountsLink"/>
<fmt:message bundle="${loc}" key="label.links.add_artist" var="addArtistLink"/>
<fmt:message bundle="${loc}" key="label.links.add_album" var="addAlbumLink"/>
<fmt:message bundle="${loc}" key="label.links.add_track" var="addTrackLink"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageTitle}</title>
</head>
<body>
<li><a href="${pageContext.request.contextPath}/controller?command=show_users">${usersLink}</a></li>
<li><a href="${pageContext.request.contextPath}/controller?command=show_accounts">${accountsLink}</a></li>
<br>
<li><a href="${pageContext.request.contextPath}/controller?command=show_add_artist">${addArtistLink}</a></li>
<li><a href="${pageContext.request.contextPath}/controller?command=show_add_album">${addAlbumLink}</a></li>
<li><a href="${pageContext.request.contextPath}/controller?command=show_add_track">${addTrackLink}</a></li>
</body>
</html>
