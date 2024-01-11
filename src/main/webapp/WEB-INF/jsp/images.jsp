<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>
<html>
<head>
    <title>Images</title>
</head>
<body>
<h3>arts</h3>
<div align="center">
    <c:forEach var="image" items="${requestScope.images}">
        <img src="data:image/jpg;base64,${image.image}" width="240" height="300"/>
    </c:forEach>
</div>
</body>
</html>
