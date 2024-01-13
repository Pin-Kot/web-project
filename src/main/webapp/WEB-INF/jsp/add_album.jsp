<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>
<html>
<head>
    <title>Add album</title>
</head>
<body>
<h3>To add a new album fill the form</h3>
<form name="add_album-form" action="${pageContext.request.contextPath}/controller?command=add_album" method="post">
    <label for="artist_name-input">Album's artist name:</label>
    <input id="artist_name-input" type="text" name="artistName" value=""/>
    <br>
    <label for="album_title-input">Album title:</label>
    <input id="album_title-input" type="text" name="albumTitle" value=""/>
    <br>
    <label for="album_year-input">Album year:</label>
    <input id="album_year-input" type="text" name="albumYear" value=""/>
    <br>
    <label for="album_type-input">Album type:</label>
    <input id="album_type-input" type="text" name="albumType" value=""/>
    <c:choose>
        <c:when test="${not empty requestScope.errorIncorrectAlbumDataMessage}">
            <b style="color: red">${requestScope.errorIncorrectAlbumDataMessage}</b>
        </c:when>
        <c:when test="${not empty requestScope.errorIncorrectArtistNameMessage}">
            <b style="color: red">${requestScope.errorIncorrectArtistNameMessage}</b>
        </c:when>
        <c:when test="${not empty requestScope.errorArtistDoesNotExistMessage}">
            <b style="color: red">${requestScope.errorArtistDoesNotExistMessage}</b>
        </c:when>
    </c:choose>
    <input type="submit" value="add"/>
    </form>
<br>
</body>
</html>
