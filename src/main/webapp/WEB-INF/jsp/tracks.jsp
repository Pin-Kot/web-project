<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.epam.jwd.audiotrack_ordering.entity.Role" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.tracks.title" var="pageTracksTitle"/>
<fmt:message bundle="${loc}" key="label.tracks.header" var="headerTitle"/>
<fmt:message bundle="${loc}" key="label.tracks.submitLink" var="submitTextLink"/>
<fmt:message bundle="${loc}" key="label.tracks.submitButton" var="submitTextButton"/>
<fmt:message bundle="${loc}" key="label.tracks.warning" var="warningText"/>
<fmt:message bundle="${loc}" key="label.error.accountDoesNotLoggedMessage" var="errorAccountDoesNotLoggedMessage"/>
<fmt:message bundle="${loc}" key="label.error.idTrackMessage" var="errorIdTrackMessage"/>
<fmt:message bundle="${loc}" key="label.error.shoppingCartDoesNotExistMessage"
             var="errorShoppingCartDoesNotExistMessage"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageTracksTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/text.css"%>
    <%@include file="/WEB-INF/css/form.css"%>
</style>
<h3>${headerTitle}</h3>
<c:choose>
    <c:when test="${not empty requestScope.errorAccountDoesNotLoggedMessage}">
        <div class="alert">
            <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
            <strong>${warningText}</strong> ${errorAccountDoesNotLoggedMessage}
        </div>
    </c:when>
    <c:when test="${not empty requestScope.errorIdTrackMessage}">
        <div class="alert">
            <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
            <strong>${warningText}</strong> ${errorIdTrackMessage}
        </div>
    </c:when>
    <c:when test="${not empty requestScope.errorShoppingCartDoesNotExistMessage}">
        <div class="alert">
            <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
            <strong>${warningText}</strong> ${errorShoppingCartDoesNotExistMessage}
        </div>
    </c:when>
</c:choose>
<ol class="list_part">
    <c:forEach var="track" items="${requestScope.tracks}">
        <li>
            <c:if test="${not empty sessionScope.account && sessionScope.account.role eq Role.ADMIN}">
                ID ${track.id}.
            </c:if>
                ${track.title} - ${track.year}, price: ${track.price} $
            <div class="inner-btn-link">
                <form name="" action="${pageContext.request.contextPath}/controller?command=show_track_reviews"
                      method="post">
                    <input type="hidden" name="trackId" value="${track.id}"/>
                    <input type="submit" value="${submitTextLink}">
                </form>
            </div>
            <c:if test="${not empty sessionScope.account && sessionScope.account.role eq Role.USER}">
                <div class="inner-btn">
                    <form name="" action="${pageContext.request.contextPath}/controller?command=add_to_cart"
                          method="post">
                        <input type="hidden" name="trackId" value="${track.id}"/>
                        <input type="submit" value="${submitTextButton}">
                    </form>
                </div>
            </c:if>
        </li>
    </c:forEach>
</ol>
</body>
</html>
