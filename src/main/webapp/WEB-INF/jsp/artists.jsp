<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.artists.title" var="pageArtistsTitle"/>
<fmt:message bundle="${loc}" key="label.artists.header" var="headerTitle"/>
<fmt:message bundle="${loc}" key="label.artists.placeholder" var="placeholder"/>
<fmt:message bundle="${loc}" key="label.artists.submit" var="submitText"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageArtistsTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/text.css"%>
    <%@include file="/WEB-INF/css/form.css"%>
</style>

<form class="search-form" action="${pageContext.request.contextPath}/controller?command=show_artist_tracks"
      method="post">
    <div class="input-group">
        <div class="input-group2">
            <label>
                <input type="search" class="search-field" placeholder="${placeholder}" name="artistName"
                       value="">
            </label>
            <input type="submit" class="search-submit" value="${submitText}">
        </div>
    </div>
</form>
<h3>${headerTitle}</h3>
<div class="list_div">
    <ul class="list_block">
        <c:forEach var="artist" items="${requestScope.artists}">
            <form name="" action="${pageContext.request.contextPath}/controller?command=show_artist_albums" method="post">
                <input type="hidden" name="artistName" value="${artist.name}"/>
                <li><input type="submit" value="${artist.name}"></li>
            </form>
        </c:forEach>
    </ul>
</div>
</body>
</html>
