<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<html>
<head>
    <title>Order details</title>
</head>
<body>
<c:set var="order" value="${requestScope.order}"/>
<p>Date of transaction ${order.date}, status transaction ${order.status}, order amount ${order.value} $</p>
<p>Order's tracks:</p>
<ol>
    <c:forEach var="track" items="${requestScope.tracks}">
        <li>
                ${track.title} - ${track.year}
        </li>
    </c:forEach>
</ol>
</body>
</html>