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
        <th>ID</th>
        <th>title</th>
        <th>year</th>
        <th>type</th>
    </tr>
    <c:forEach var="album" items="${requestScope.albums}">
        <tr>
            <td>${album.id}</td>
            <td>${album.title}</td>
            <td>${album.year}</td>
            <td>${album.type}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
