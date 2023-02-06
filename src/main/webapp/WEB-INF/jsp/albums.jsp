<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h3>albums</h3>
<ul>
<%--    <li>album1</li>--%>
<%--    <li>album2</li>--%>
<%--    <li>album3</li>--%>
</ul>
<table>
    <tr>
<%--        <th>ID</th>--%>
        <th>title</th>
        <th>year</th>
        <th>type</th>
    </tr>
    <c:forEach var="album" items="${requestScope.albums}">
        <tr>
<%--            <td>${album.id}</td>--%>
    <form name="" action="${pageContext.request.contextPath}/controller?command=show_album_tracks" method="post">
        <input type="hidden" name="album_title" value="${album.title}"/>
        <td><input type="submit" value="${album.title}"></td>
    </form>
<%--            <td>${album.title}</td>--%>
            <td>${album.year}</td>
            <td>${album.type}</td>
        </tr>
    </c:forEach>

    <form name="album_title-form" action="${pageContext.request.contextPath}/controller?command=show_album_tracks"
          method="post">

        <label for="album_title-input">Album title:</label>
        <input id="album_title-input" type="text" name="album_title" value=""/>
        <br>

        <input type="submit" value="find"/>
    </form>
</table>
</body>
</html>
