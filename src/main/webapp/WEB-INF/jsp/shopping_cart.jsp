<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Shopping cart</title>
</head>
<body>
Your shopping cart:
<table>
    <tr>
        <th>title</th>
        <th>year</th>
        <th>price</th>
    </tr>
    <c:forEach var="track" items="${sessionScope.shoppingCart}">
        <tr>
            <td>${track.title} </td>
            <td>${track.year} </td>
            <td>${track.price} $</td>
            <form name="" action="${pageContext.request.contextPath}/controller?command=delete_from_cart"
                  method="post">
                <input type="hidden" name="trackId" value="${track.id}"/>
                <td><input type="submit" value="delete"></td>
            </form>
        </tr>
    </c:forEach>
</table>
<p>Total price: ${sessionScope.totalPrice}$</p>
</body>
</html>