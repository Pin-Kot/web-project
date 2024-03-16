<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.addAlbum.title" var="pageAddAlbumTitle"/>
<fmt:message bundle="${loc}" key="label.addAlbum.invitation" var="invitationMessage"/>
<fmt:message bundle="${loc}" key="label.addAlbum.artistName" var="artistNameText"/>
<fmt:message bundle="${loc}" key="label.addAlbum.albumTitle" var="albumTitleText"/>
<fmt:message bundle="${loc}" key="label.addAlbum.albumYear" var="albumYearText"/>
<fmt:message bundle="${loc}" key="label.addAlbum.albumType" var="albumTypeText"/>
<fmt:message bundle="${loc}" key="label.addAlbum.warning" var="warningText"/>
<fmt:message bundle="${loc}" key="label.addAlbum.submit" var="addSubmit"/>
<fmt:message bundle="${loc}" key="label.error.incorrectAlbumDataMessage" var="errorIncorrectAlbumDataMessage"/>
<fmt:message bundle="${loc}" key="label.error.incorrectArtistNameMessage" var="errorIncorrectArtistNameMessage"/>
<fmt:message bundle="${loc}" key="label.error.artistDoesNotExistMessage" var="errorArtistDoesNotExistMessage"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageAddAlbumTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/form.css"%>
    <%@include file="/WEB-INF/css/text.css"%>
</style>
<h3>${invitationMessage}</h3>
<ul id="enter_data">
    <form name="add_album-form" action="${pageContext.request.contextPath}/controller?command=add_album" method="post">
        <label for="artist_name-input">${artistNameText}</label>
        <input id="artist_name-input" type="text" name="artistName" value=""/>
        <br>
        <label for="album_title-input">${albumTitleText}</label>
        <input id="album_title-input" type="text" name="albumTitle" value=""/>
        <br>
        <label for="album_year-input">${albumYearText}</label>
        <input id="album_year-input" type="text" name="albumYear" value=""/>
        <br>
        <label for="album_type-input">${albumTypeText}</label>
        <input id="album_type-input" type="text" name="albumType" value=""/>
        <br>
        <c:choose>
            <c:when test="${not empty requestScope.errorIncorrectAlbumDataMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorIncorrectAlbumDataMessage}
                </div>
            </c:when>
            <c:when test="${not empty requestScope.errorIncorrectArtistNameMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorIncorrectArtistNameMessage}
                </div>
            </c:when>
            <c:when test="${not empty requestScope.errorArtistDoesNotExistMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorArtistDoesNotExistMessage}
                </div>
            </c:when>
        </c:choose>
        <input type="submit" value="${addSubmit}">
    </form>
</ul>
</body>
</html>
