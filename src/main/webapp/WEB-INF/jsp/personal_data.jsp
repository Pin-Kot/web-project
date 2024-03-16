<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.personalData.title" var="pagePersonalDataTitle"/>
<fmt:message bundle="${loc}" key="label.personalData.name" var="fullNameText"/>
<fmt:message bundle="${loc}" key="label.personalData.email" var="emailText"/>
<fmt:message bundle="${loc}" key="label.personalData.birthday" var="birthdayText"/>
<fmt:message bundle="${loc}" key="label.personalData.discount" var="discountText"/>
<fmt:message bundle="${loc}" key="label.personalData.links.editDataLink" var="editDataLinkText"/>
<fmt:message bundle="${loc}" key="label.personalData.links.editPasswordLink" var="editPasswordLinkText"/>
<fmt:message bundle="${loc}" key="label.personalData.table.caption" var="captationText"/>
<fmt:message bundle="${loc}" key="label.personalData.table.columnHeader.number" var="numberText"/>
<fmt:message bundle="${loc}" key="label.personalData.table.columnHeader.date" var="dateText"/>
<fmt:message bundle="${loc}" key="label.personalData.table.columnHeader.status" var="statusText"/>
<fmt:message bundle="${loc}" key="label.personalData.table.columnHeader.value" var="valueText"/>
<fmt:message bundle="${loc}" key="label.personalData.table.columnHeader.details" var="detailsText"/>
<fmt:message bundle="${loc}" key="label.personalData.table.column.submitLink" var="submitLinkText"/>
<fmt:message bundle="${loc}" key="label.personalData.header" var="headerText"/>
<fmt:message bundle="${loc}" key="label.personalData.links.addDataLink" var="addDataLinkText"/>
<fmt:message bundle="${loc}" key="label.personalData.warning" var="warningText"/>
<fmt:message bundle="${loc}" key="label.error.invalidUserDataMessage" var="errorInvalidUserDataMessage"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pagePersonalDataTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/main.css"%>
    <%@include file="/WEB-INF/css/text.css"%>
    <%@include file="/WEB-INF/css/profile.css"%>
    <%@include file="/WEB-INF/css/table.css"%>
</style>
<c:if test="${not empty requestScope.user}">
    <div class="card">
        <div class="card-body">
            <div class="row">
                <h4>${fullNameText}</h4>
                    ${requestScope.user.firstName} ${requestScope.user.lastName}
                <hr>
                <h4>${emailText}</h4>
                    ${requestScope.user.email}
                <hr>
                <h4>${birthdayText}</h4>
                    ${requestScope.user.birthday}
                <hr>
                <h4>${discountText}</h4>
                    ${requestScope.user.discount} %
                <hr>
            </div>
        </div>
    </div>
    <li><a href="${pageContext.request.contextPath}/controller?command=show_editor">${editDataLinkText}</a></li>
    <li><a href="${pageContext.request.contextPath}/controller?command=show_password_editor">${editPasswordLinkText}</a></li>
</c:if>
<c:if test="${not empty requestScope.userOrders}">
    <table class="table">
        <caption>${captationText}</caption>
        <tr>
            <th>${numberText}</th>
            <th>${dateText}</th>
            <th>${statusText}</th>
            <th>${valueText}</th>
            <th>${detailsText}</th>
        </tr>
        <c:forEach var="order" items="${requestScope.userOrders}">
            <tr>
                <td></td>
                <td>${order.date}</td>
                <td>${order.status}</td>
                <td>${order.value}</td>
                <form name="form" action="${pageContext.request.contextPath}/controller?command=show_order_tracks"
                      method="post">
                    <input type="hidden" name="orderId" value="${order.id}"/>
                    <td><input type="submit" value="${submitLinkText}"></td>
                </form>
            </tr>
        </c:forEach>
    </table>
</c:if>
<c:if test="${empty requestScope.user && not empty sessionScope.account}">
    <h4>${headerText}</h4>
    <a href="${pageContext.request.contextPath}/controller?command=show_add_user">${addDataLinkText}</a>
</c:if>
<c:if test="${not empty requestScope.errorInvalidUserData}">
    <div class="alert">
        <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
        <strong>${warningText}</strong> ${errorInvalidUserData}
    </div>
</c:if>
</body>
</html>
