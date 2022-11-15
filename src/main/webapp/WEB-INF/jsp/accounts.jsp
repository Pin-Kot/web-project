<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h3>accounts</h3>
<table>
    <tr>
        <th>ID</th>
        <th>Login</th>
        <th>Password</th>
        <th>Role</th>
    </tr>
    <c:forEach var="account" items="${requestScope.accounts}">
        <tr>
            <td>${account.id}</td>
            <td>${account.login}</td>
            <td>${account.password}</td>
            <td>${account.role}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
