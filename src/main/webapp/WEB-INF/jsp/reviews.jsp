<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Reviews</title>
</head>
<body>
<h3>reviews</h3>
<c:forEach var="review" items="${requestScope.reviews}">
    <p style="text-align: center">${review.text}</p>
    <p style="text-align: start">${review.date} ${review.accountLogin}</p>
</c:forEach>
</body>
</html>