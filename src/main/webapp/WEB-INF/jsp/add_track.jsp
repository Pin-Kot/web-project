<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add track</title>
</head>
<body>
<h3>To add a new track fill the form</h3>
<form name="add_track-form" action="${pageContext.request.contextPath}/controller?command=add_track" method="post">
    <label for="artist_name-input">Track's artist name:</label>
    <input id="artist_name-input" type="text" name="artistName" value=""/>
    <br>
    <label for="album_title-input">Track's album title:</label>
    <input id="album_title-input" type="text" name="albumTitle" value=""/>
    <br>
    <label for="album_year-input">Track's album year:</label>
    <input id="album_year-input" type="text" name="albumYear" value=""/>
    <br>
    <label for="album_type-input">Track's album type:</label>
    <input id="album_type-input" type="text" name="albumType" value=""/>
    <br>
    <label for="track_title-input">Track title:</label>
    <input id="track_title-input" type="text" name="trackTitle" value=""/>
    <br>
    <label for="track_year-input">Track year:</label>
    <input id="track_year-input" type="text" name="trackYear" value=""/>
    <br>
    <label for="track_price-input">Track price:</label>
    <input id="track_price-input" type="text" name="trackPrice" value=""/>
    <c:choose>
        <c:when test="${not empty requestScope.errorIncorrectArtistNameMessage}">
            <b style="color: red">${requestScope.errorIncorrectArtistNameMessage}</b>
        </c:when>
        <c:when test="${not empty requestScope.errorIncorrectAlbumDataMessage}">
            <b style="color: red">${requestScope.errorIncorrectAlbumDataMessage}</b>
        </c:when>
        <c:when test="${not empty requestScope.errorArtistOrAlbumDoNotExistMessage}">
            <b style="color: red">${requestScope.errorArtistOrAlbumDoNotExistMessage}</b>
        </c:when>
        <c:when test="${not empty requestScope.errorIncorrectTitleDataMessage}">
            <b style="color: red">${requestScope.errorIncorrectTitleDataMessage}</b>
        </c:when>
    </c:choose>
    <input type="submit" value="add"/>
</form>
</body>
</html>
