<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.addArtist.title" var="pageAddArtistTitle"/>
<fmt:message bundle="${loc}" key="label.addArtist.invitation" var="invitationMessage"/>
<fmt:message bundle="${loc}" key="label.addArtist.artistName" var="artistNameText"/>
<fmt:message bundle="${loc}" key="label.addArtist.warning" var="warningText"/>
<fmt:message bundle="${loc}" key="label.addArtist.submit" var="addSubmit"/>
<fmt:message bundle="${loc}" key="label.error.incorrectArtistNameMessage" var="errorIncorrectArtistNameMessage"/>
<fmt:message bundle="${loc}" key="label.error.artistExistsMessage" var="errorArtistExistsMessage"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageAddArtistTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/form.css"%>
    <%@include file="/WEB-INF/css/text.css"%>
</style>
<h3>${invitationMessage}</h3>
<ul id="enter_data">
    <form name="add_artist-form" action="${pageContext.request.contextPath}/controller?command=add_artist" method="post">
        <label for="artist_name-input">${artistNameText}</label>
        <input id="artist_name-input" type="text" name="artistName" value=""/>
        <br>
        <c:choose>
            <c:when test="${not empty requestScope.errorIncorrectArtistNameMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorIncorrectArtistNameMessage}
                </div>
            </c:when>
            <c:when test="${not empty requestScope.errorArtistExistsMessage}">
                <div class="alert">
                    <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                    <strong>${warningText}</strong> ${errorArtistExistsMessage}
                </div>
            </c:when>
        </c:choose>
        <input type="submit" value="${addSubmit}">
    </form>
</ul>
</body>
</html>
