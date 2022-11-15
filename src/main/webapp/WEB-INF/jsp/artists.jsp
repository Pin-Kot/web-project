<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h3>artists</h3>
<ul>
    <c:forEach var="artist" items="${requestScope.artists}">
        <li><span>ID # ${artist.id}.</span> ${artist.name}.</li>
    </c:forEach>
</ul>
</body>
</html>
