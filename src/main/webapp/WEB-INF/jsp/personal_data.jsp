<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Personal data</title>
</head>
<body>
<c:if test="${not empty requestScope.user}">
    <p>first name: ${requestScope.user.firstName}</p>
    <p>last name: ${requestScope.user.lastName}</p>
    <p>email: ${requestScope.user.email}</p>
    <p>birthday: ${requestScope.user.birthday}</p>
    <p>discount: ${requestScope.user.discount}</p>
    <br>
    <a href="${pageContext.request.contextPath}/controller?command=show_editor">edit data</a>
</c:if>
<c:if test="${not empty requestScope.errorInvalidUserData}">
    <b style="color: red">${requestScope.errorInvalidUserData}</b>
</c:if>
</body>
</html>
