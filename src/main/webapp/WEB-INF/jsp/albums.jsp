<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h3>albums</h3>
<table>
    <tr>
        <th>title</th>
        <th>year</th>
        <th>type</th>
    </tr>
    <c:forEach var="album" items="${requestScope.albums}">
        <tr>
            <form name="" action="${pageContext.request.contextPath}/controller?command=show_album_tracks"
                  method="post">
                <input type="hidden" name="albumTitle" value="${album.title}"/>
                <td><input type="submit" value="${album.title}"></td>
            </form>
            <td>${album.year}</td>
            <td>${album.type}</td>
            <form name="" action="${pageContext.request.contextPath}/controller?command=show_album_images"
                  method="post">
                <input type="hidden" name="albumTitle" value="${album.title}"/>
                <td><input type="submit" value="show arts"></td>
            </form>
        </tr>
    </c:forEach>

    <form name="album_title-form" action="${pageContext.request.contextPath}/controller?command=show_album_tracks"
          method="post">
        <label for="album_title-input">Album title:</label>
        <input id="album_title-input" type="text" name="albumTitle" value=""/>
        <br>
        <input type="submit" value="find"/>
    </form>
</table>
</body>
</html>
