<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${not empty sessionScope.userLocale ? sessionScope.userLocale : pageContext.request.locale}" scope="request" />
<fmt:setBundle basename="messages" var="msg" />
<jsp:include page="../include/header.jsp"/>
<div class="card auth-card">
  <h1><fmt:message key="registerSuccess.title" bundle="${msg}"/></h1>
  <div class="alert alert-info"><fmt:message key="registerSuccess.message" bundle="${msg}"/></div>
  <p class="mt-2"><a href="${pageContext.request.contextPath}/login"><fmt:message key="menu.login" bundle="${msg}"/></a></p>
</div>
<jsp:include page="../include/footer.jsp"/>
