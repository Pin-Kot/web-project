<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>
<html>
<head>
    <title>To assign a discount</title>
</head>
<body>
<form name="edit_discount-form" action="${pageContext.request.contextPath}/controller?command=assign_discount"
      method="post">
    <label for="id-input">Enter user id:</label>
    <input id="id-input" type="text" name="userId" value=""/>
    <br>
    <label for="discount-input">Enter new amount of discount (from 0 to 20):</label>
    <input id="discount-input" type="text" name="discount" value=""/>
    <br/>
    <c:choose>
        <c:when test="${not empty requestScope.errorEnteredIdMessage}">
            <b style="color: orange">${requestScope.errorEnteredIdMessage}</b>
            <br/>
        </c:when>
        <c:when test="${not empty requestScope.errorEnteredDiscountMessage}">
            <b style="color: orange">${requestScope.errorEnteredDiscountMessage}</b>
            <br/>
        </c:when>
    </c:choose>
    <input type="submit" value="Assign"/>
</form>
</body>
</html>