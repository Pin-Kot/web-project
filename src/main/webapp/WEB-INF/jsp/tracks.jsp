<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.epam.jwd.audiotrack_ordering.entity.Role" %>
<%@include file="header.jsp" %>
<html>
<head>
    <title>Tracks</title>
</head>
<body>
<h3>tracks</h3>
<c:choose>
    <c:when test="${not empty requestScope.errorAccountDoesNotExistMessage}">
        <b style="color: red">${requestScope.errorAccountDoesNotExistMessage}</b>
    </c:when>
    <c:when test="${not empty requestScope.errorIdTrackMessage}">
        <b style="color: red">${requestScope.errorIdTrackMessage}</b>
    </c:when>
    <c:when test="${not empty requestScope.errorShoppingCartDoesNotExistMessage}">
        <b style="color: red">${requestScope.errorShoppingCartDoesNotExistMessage}</b>
    </c:when>
</c:choose>
<ol>
    <c:forEach var="track" items="${requestScope.tracks}">
        <li>
            <c:if test="${not empty sessionScope.account && sessionScope.account.role eq Role.ADMIN}">
                ID ${track.id}.
            </c:if>
                ${track.title} - ${track.year}, price: ${track.price} $
            <form name="" action="${pageContext.request.contextPath}/controller?command=show_track_reviews"
                  method="post">
                <input type="hidden" name="trackId" value="${track.id}"/>
                <td><input type="submit" value="show reviews"></td>
            </form>
            <form name="" action="${pageContext.request.contextPath}/controller?command=add_to_cart"
                  method="post">
                <input type="hidden" name="trackId" value="${track.id}"/>
                <td><input type="submit" value="add to cart"></td>
            </form>
        </li>
    </c:forEach>
</ol>
<c:choose>
    <c:when test="${not empty requestScope.errorAccountDoesNotExistMessage}">
        <b style="color: red">${requestScope.errorAccountDoesNotExistMessage}</b>
        <br>
    </c:when>
    <c:when test="${not empty requestScope.errorIdTrackMessage}">
        <b style="color: red">${requestScope.errorIdTrackMessage}</b>
        <br>
    </c:when>
</c:choose>
</body>
</html>
