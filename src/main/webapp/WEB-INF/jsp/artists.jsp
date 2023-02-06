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
        <form name="" action="${pageContext.request.contextPath}/controller?command=show_artist_tracks" method="post">
            <input type="hidden" name="artist_name" value="${artist.name}"/>
            <li><input type="submit" value="${artist.name}"></li>
        </form>
    </c:forEach>

    <form name="artist_name-form" action="${pageContext.request.contextPath}/controller?command=show_artist_tracks"
          method="post">

        <label for="artist_name-input">Artist name:</label>
        <input id="artist_name-input" type="text" name="artist_name" value=""/>
        <br>

        <input type="submit" value="find"/>
    </form>
</ul>
</body>
</html>
