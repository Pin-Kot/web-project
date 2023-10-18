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
        <li>
                ${track.title} - ${track.year}, price: ${track.price} $ <p>Add to cart</p>
                    <form name="" action="${pageContext.request.contextPath}/controller?command=show_track_reviews"
                          method="post">
                        <input type="hidden" name="trackId" value="${track.id}"/>
                        <td><input type="submit" value="show reviews"></td>
                    </form>
        </li>
    </c:forEach>
</ol>
</body>
</html>
