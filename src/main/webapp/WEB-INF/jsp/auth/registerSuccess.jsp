<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${not empty sessionScope.userLocale ? sessionScope.userLocale : pageContext.request.locale}" scope="request" />
<fmt:setBundle basename="messages" var="msg" />
<jsp:include page="../include/header.jsp"/>
<div class="card auth-card">
  <h1><fmt:message key="registerSuccess.title" bundle="${msg}"/></h1>
  <c:choose>
    <c:when test="${not empty validationUrl}">
      <div class="alert alert-info"><fmt:message key="message.register.validateEmailFallback" bundle="${msg}"/></div>
      <p class="mt-2"><a href="${validationUrl}" class="btn-cta"><fmt:message key="message.register.clickToValidate" bundle="${msg}"/></a></p>
    </c:when>
    <c:otherwise>
      <div class="alert alert-info"><fmt:message key="message.register.validateEmail" bundle="${msg}"/></div>
    </c:otherwise>
  </c:choose>
  <p class="mt-2"><a href="${pageContext.request.contextPath}/login"><fmt:message key="menu.login" bundle="${msg}"/></a></p>
</div>
<jsp:include page="../include/footer.jsp"/>
