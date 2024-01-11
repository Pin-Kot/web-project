<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.epam.jwd.audiotrack_ordering.entity.Role" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp"%>
<html>
<head>
    <title>Reviews</title>
</head>
<body>
<h3>
    <span class="font-weight-bold">
        ${sessionScope.track.title}
    </span>
</h3>
<c:choose>
    <c:when test="${not empty sessionScope.account && sessionScope.account.role eq Role.USER}">
        <form name="post-review-form" action="${pageContext.request.contextPath}/controller?command=add_review" method="post">
            <p> Review: <br>
                <textarea name="text" cols="50" rows="4"></textarea></p>
            <p>
                <input type="submit" value="Send">
                <input type="reset" value="Clear">
            </p>
            <br/>
            <c:if test="${not empty requestScope.errorTextIsInvalidMessage}">
                <b style="color: red">${requestScope.errorTextIsInvalidMessage}</b>
                <br>
            </c:if>
        </form>
    </c:when>
    <c:otherwise>
        Only authorized users can leave reviews
    </c:otherwise>
</c:choose>
<c:forEach var="review" items="${requestScope.reviews}">
    <p style="text-align: center">${review.text}</p>
    <p style="text-align: start">${review.date} ${review.accountLogin}</p>
</c:forEach>
</body>
</html>