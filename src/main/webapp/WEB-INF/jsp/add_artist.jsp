<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add artist</title>
</head>
<body>
<h3>Please add an artist</h3>
<form name="add_artist-form" action="${pageContext.request.contextPath}/controller?command=add_artist" method="post">
    <label for="artist_name-input">Artist name:</label>
    <input id="artist_name-input" type="text" name="artistName" value=""/>
    <br>

    <c:choose>
        <c:when test="${not empty requestScope.errorIncorrectArtistNameMessage}">
            <b style="color: red">${requestScope.errorIncorrectArtistNameMessage}</b>
            <br>
        </c:when>
        <c:when test="${not empty requestScope.errorArtistExistsMessage}">
            <b style="color: red">${requestScope.errorArtistExistsMessage}</b>
            <br>
        </c:when>
    </c:choose>
    <input type="submit" value="Add"/>
</form>
</body>
</html>
