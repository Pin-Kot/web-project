<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit password</title>
</head>
<body>
<h3>Please add new password</h3>
</body>
<form name="edit_password-form" action="${pageContext.request.contextPath}/controller?command=edit_password" method="post">
    <label for="password-input">Create a password:</label>
    <input id="password-input" type="password" name="password" value=""/>
    <br>
    <label for="doublePassword-input">Confirm a password:</label>
    <input id="doublePassword-input" type="password" name="doublePassword" value=""/>
    <br/>
    <c:choose>
        <c:when test="${not empty requestScope.errorEditPasswordMessage}">
            <b style="color: orange">${requestScope.errorEditPasswordMessage}</b>
            <br/>
        </c:when>
    </c:choose>
    <input type="submit" value="Edit"/>
</form>
</html>
