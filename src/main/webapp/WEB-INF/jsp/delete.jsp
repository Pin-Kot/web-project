<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>
<html>
<head>
    <title>Delete elements from base</title>
</head>
<body>
<form name="delete-form" action="${pageContext.request.contextPath}/controller?command=delete" method="post">
    <p>
        <label for="element">Select the type element:</label>
        <select id="element" name="element">
            <option value="album">Album</option>
            <option value="artist">Artist</option>
            <option value="image">Image</option>
            <option value="track">Track</option>
        </select>
    </p>
    <p>
        <label for="element-id-input">Enter element id:</label>
        <input id="element-id-input" type="text" name="id" value=""/>
    </p>
    <p>
        <input type="submit" value="Delete">
    </p>
    <c:choose>
        <c:when test="${not empty requestScope.errorEntityNotFoundMessage}">
            <b style="color: red">${requestScope.errorEntityNotFoundMessage}</b>
        </c:when>
        <c:when test="${not empty requestScope.errorWrongEntityNameFoundMessage}">
            <b style="color: red">${requestScope.errorWrongEntityNameFoundMessage}</b>
        </c:when>
    </c:choose>
</form>
</body>
</html>