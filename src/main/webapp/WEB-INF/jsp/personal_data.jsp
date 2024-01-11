<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>
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
    <br>
    <a href="${pageContext.request.contextPath}/controller?command=show_password_editor">edit password</a>
</c:if>
<c:if test="${not empty requestScope.userOrders}">
    <p>My orders:</p>
    <table>
       <tr>
           <th>Date</th>
           <th>Status</th>
           <th>Value</th>
       </tr>
        <c:forEach var="order" items="${requestScope.userOrders}">
          <tr>
              <td>${order.date}</td>
              <td>${order.status}</td>
              <td>${order.value}</td>
          </tr>
        </c:forEach>
    </table>
</c:if>
<c:if test="${empty requestScope.user && not empty sessionScope.account}">
    <p>User data is empty, please enter data</p>
    <a href="${pageContext.request.contextPath}/controller?command=show_add_user">edit data</a>
</c:if>
<c:if test="${not empty requestScope.errorInvalidUserData}">
    <b style="color: red">${requestScope.errorInvalidUserData}</b>
</c:if>
</body>
</html>
