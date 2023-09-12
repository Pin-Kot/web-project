<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Tracks</title>
</head>
<body>
<h3>tracks</h3>
<ol>
    <c:forEach var="track" items="${requestScope.tracks}">
        <li><span>${track.id}.</span> ${track.title} - ${track.year}, price: ${track.price} $</li>
    </c:forEach>
</ol>
</body>
</html>
