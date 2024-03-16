<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="header.jsp" %>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<fmt:message bundle="${loc}" key="label.images.title" var="pageImagesTitle"/>
<fmt:message bundle="${loc}" key="label.images.header" var="headerTitle"/>

<html lang="${sessionScope.lang}">
<head>
    <title>${pageImagesTitle}</title>
</head>
<body>
<style>
    <%@include file="/WEB-INF/css/text.css"%>
</style>
<h3>${headerTitle}</h3>
<div align="center">
    <c:forEach var="image" items="${requestScope.images}">
        <img src="data:image/jpg;base64,${image.image}" width="240" height="300"/>
    </c:forEach>
</div>
</body>
</html>
