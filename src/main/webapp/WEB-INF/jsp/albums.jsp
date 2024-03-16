<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.albums.title" var="pageAlbumsTitle"/>
<fmt:message bundle="${loc}" key="label.albums.placeholder" var="placeholder"/>
<fmt:message bundle="${loc}" key="label.albums.submit" var="submitText"/>
<fmt:message bundle="${loc}" key="label.albums.table.caption" var="caption"/>
<fmt:message bundle="${loc}" key="label.albums.table.columnHeader.number" var="number"/>
<fmt:message bundle="${loc}" key="label.albums.table.columnHeader.title" var="title"/>
<fmt:message bundle="${loc}" key="label.albums.table.columnHeader.year" var="year"/>
<fmt:message bundle="${loc}" key="label.albums.table.columnHeader.type" var="type"/>
<fmt:message bundle="${loc}" key="label.albums.table.columnHeader.coversLink" var="coversLink"/>
<fmt:message bundle="${loc}" key="label.albums.table.context.submit" var="submitTableText"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageAlbumsTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/text.css"%>
    <%@include file="/WEB-INF/css/main.css"%>
    <%@include file="/WEB-INF/css/table.css"%>
    <%@include file="/WEB-INF/css/form.css"%>
</style>
<form class="search-form" action="${pageContext.request.contextPath}/controller?command=show_album_tracks"
      method="post">
    <div class="input-group">
        <div class="input-group2">
            <label>
                <input type="search" class="search-field" placeholder="${placeholder}" name="albumTitle"
                       value="">
            </label>
            <input type="submit" class="search-submit" value="${submitText}">
        </div>
    </div>
</form>
<table class="table">
    <caption>${caption}</caption>
    <tr>
        <th>${number}</th>
        <th>${title}</th>
        <th>${year}</th>
        <th>${type}</th>
        <th>${coversLink}</th>
    </tr>
    <c:forEach var="album" items="${requestScope.albums}">
        <tr>
            <td></td>
            <form name="" action="${pageContext.request.contextPath}/controller?command=show_album_tracks"
                  method="post">
                <input type="hidden" name="albumTitle" value="${album.title}"/>
                <td><input type="submit" value="${album.title}"></td>
            </form>
            <td>${album.year}</td>
            <td>${album.type}</td>
            <form name="" action="${pageContext.request.contextPath}/controller?command=show_album_images"
                  method="post">
                <input type="hidden" name="albumTitle" value="${album.title}"/>
                <td><input type="submit" value="${submitTableText}"></td>
            </form>
        </tr>
    </c:forEach>
</table>
</body>
</html>
