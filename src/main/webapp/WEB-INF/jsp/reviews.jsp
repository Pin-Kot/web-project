<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.epam.jwd.audiotrack_ordering.entity.Role" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.reviews.title" var="pageReviewsTitle"/>
<fmt:message bundle="${loc}" key="label.reviews.formHeader" var="formHeaderMessage"/>
<fmt:message bundle="${loc}" key="label.reviews.formButtonReset" var="buttonReset"/>
<fmt:message bundle="${loc}" key="label.reviews.formButtonSubmit" var="buttonSubmit"/>
<fmt:message bundle="${loc}" key="label.reviews.warning" var="warningText"/>
<fmt:message bundle="${loc}" key="label.error.textIsInvalidMessage" var="errorTextIsInvalidMessage"/>
<fmt:message bundle="${loc}" key="label.reviews.service" var="serviceText"/>
<fmt:message bundle="${loc}" key="label.reviews.listHeader" var="listHeaderMessage"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageReviewsTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/text.css"%>
    <%@include file="/WEB-INF/css/form.css"%>
</style>
<h3>
    ${sessionScope.track.title}
</h3>
<c:choose>
    <c:when test="${not empty sessionScope.account && sessionScope.account.role eq Role.USER}">
        <form name="post-review-form" action="${pageContext.request.contextPath}/controller?command=add_review"
              method="post">
            <p>${formHeaderMessage}</p>
            <textarea name="text" cols="50" rows="4"></textarea>
            <c:if test="${not empty requestScope.errorTextIsInvalidMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorTextIsInvalidMessage}
                </div>
            </c:if>
            <input id="sndbttn" type="submit" value="${buttonSubmit}">
            <input id="clrbttn" type="reset" value="${buttonReset}">
        </form>
        <br>
    </c:when>
    <c:otherwise>
        <h4>${serviceText}</h4>
    </c:otherwise>
</c:choose>
<br>
<h2>${listHeaderMessage}</h2>
<hr>
<c:forEach var="review" items="${requestScope.reviews}">
    <span class="review">${review.text}</span>
    <span class="author">${review.accountLogin},</span> <span class="pblctn-date">${review.date}</span>
    <hr>
</c:forEach>
</body>
</html>