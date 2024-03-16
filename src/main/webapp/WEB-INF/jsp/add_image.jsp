<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.addImage.title" var="pageAddImageTitle"/>
<fmt:message bundle="${loc}" key="label.addImage.invitation" var="invitationMessage"/>
<fmt:message bundle="${loc}" key="label.addImage.albumTitle" var="albumTitleText"/>
<fmt:message bundle="${loc}" key="label.addImage.albumYear" var="albumYearText"/>
<fmt:message bundle="${loc}" key="label.addImage.albumType" var="albumTypeText"/>
<fmt:message bundle="${loc}" key="label.addImage.image" var="imageText"/>
<fmt:message bundle="${loc}" key="label.addImage.warning" var="warningText"/>
<fmt:message bundle="${loc}" key="label.addImage.submit" var="addText"/>
<fmt:message bundle="${loc}" key="label.error.incorrectAlbumDataMessage" var="errorIncorrectAlbumDataMessage"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageAddImageTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/form.css"%>
    <%@include file="/WEB-INF/css/text.css"%>
</style>
<h3>${invitationMessage}</h3>
<ul id="enter_data">
<form name="add_image-form" action="${pageContext.request.contextPath}/controller?command=add_image"
      enctype="multipart/form-data" method="post">
    <label for="album_title-input">${albumTitleText}</label>
    <input id="album_title-input" type="text" name="albumTitle" value=""/>
    <br>
    <label for="album_year-input">${albumYearText}</label>
    <input id="album_year-input" type="text" name="albumYear" value=""/>
    <br>
    <label for="album_type-input">${albumTypeText}</label>
    <input id="album_type-input" type="text" name="albumType" value=""/>
    <br>
    <label class="image_file-input">
        <input type="file" name="image" accept="image/png, image/jpeg">
        <span>${imageText}</span>
    </label>
    <c:choose>
        <c:when test="${not empty requestScope.errorIncorrectAlbumDataMessage}">
            <div class="alert">
                <span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span>
                <strong>${warningText}</strong> ${errorIncorrectAlbumDataMessage}
            </div>
        </c:when>
    </c:choose>
    <input type="submit" value="${addText}">
</form>
</ul>
</body>
</html>