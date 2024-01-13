<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>
<html>
<head>
    <title>Add image</title>
</head>
<body>
<h3>To add a new image fill the form</h3>
<form name="add_image-form" action="${pageContext.request.contextPath}/controller?command=add_image"
      enctype="multipart/form-data" method="post">
    <label for="album_title-input">Enter the album title:</label>
    <input id="album_title-input" type="text" name="albumTitle" value=""/>
    <br>
    <label for="album_year-input">Enter the album year:</label>
    <input id="album_year-input" type="text" name="albumYear" value=""/>
    <br>
    <label for="album_type-input">Enter the album type:</label>
    <input id="album_type-input" type="text" name="albumType" value=""/>
    <br>
    <label for="image_file-input">Choose an image</label>
    <input id="image_file-input" type="file" name="image" accept="image/png, image/jpeg">
    <c:choose>
        <c:when test="${not empty requestScope.errorIncorrectAlbumDataMessage}">
            <b style="color: red">${requestScope.errorIncorrectAlbumDataMessage}</b>
        </c:when>
    </c:choose>
    <input type="submit" value="Add"/>
</form>
</body>
</html>