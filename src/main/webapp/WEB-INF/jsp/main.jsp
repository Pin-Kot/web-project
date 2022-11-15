<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Welcome at the online store !</h1>
<c:if test="${not empty sessionScope.account}">
    <p>Hello, ${sessionScope.account.login}</p>
</c:if>

<a href="/controller?command=show_users">users page</a>
<a href="/controller?command=show_accounts">accounts page</a>
<a href="/controller?command=show_artists">artists page</a>
<a href="/controller?command=show_albums&page=albums">albums page</a>
<br>
<c:choose>
    <c:when test="${not empty sessionScope.account}">
        <a href="/controller?command=logout">logout</a>
    </c:when>
    <c:otherwise>
        <a href="/controller?command=show_login">login</a>
    </c:otherwise>
</c:choose>
</body>
</html>
