<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${not empty sessionScope.userLocale ? sessionScope.userLocale : pageContext.request.locale}" scope="request" />
<fmt:setBundle basename="messages" var="msg" />
<jsp:include page="../include/header.jsp"/>
<div class="card auth-card">
  <h1><fmt:message key="login.title" bundle="${msg}"/></h1>
  <c:if test="${not empty error}">
    <div class="alert alert-error"><fmt:message key="${error}" bundle="${msg}"/></div>
  </c:if>
  <form method="post" action="${pageContext.request.contextPath}/login">
    <div class="form-group">
      <label for="email"><fmt:message key="login.email" bundle="${msg}"/></label>
      <input type="email" id="email" name="email" class="form-control" required autofocus />
    </div>
    <div class="form-group">
      <label for="password"><fmt:message key="login.password" bundle="${msg}"/></label>
      <input type="password" id="password" name="password" class="form-control" required />
    </div>
    <button type="submit" class="btn btn-primary"><fmt:message key="login.submit" bundle="${msg}"/></button>
  </form>
  <p class="mt-2"><fmt:message key="login.noAccount" bundle="${msg}"/> <a href="${pageContext.request.contextPath}/register"><fmt:message key="menu.register" bundle="${msg}"/></a></p>
</div>
<jsp:include page="../include/footer.jsp"/>
