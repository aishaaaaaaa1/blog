<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${not empty sessionScope.userLocale ? sessionScope.userLocale : pageContext.request.locale}" scope="request" />
<fmt:setBundle basename="messages" var="msg" />
<jsp:include page="../include/header.jsp"/>
<div class="card profile-card">
  <h2 class="page-title"><fmt:message key="profile.title" bundle="${msg}"/></h2>
  <p><strong><fmt:message key="profile.pseudo" bundle="${msg}"/> :</strong> <c:out value="${member.pseudo}"/></p>
  <p><strong><fmt:message key="profile.email" bundle="${msg}"/> :</strong> <c:out value="${member.email}"/></p>
  <c:if test="${not empty profile.displayName}">
    <p><strong><fmt:message key="profile.displayName" bundle="${msg}"/> :</strong> <c:out value="${profile.displayName}"/></p>
  </c:if>
  <c:if test="${not empty profile.bio}">
    <p><strong><fmt:message key="profile.bio" bundle="${msg}"/> :</strong><br/><c:out value="${profile.bio}"/></p>
  </c:if>
  <div class="profile-actions">
    <a href="${pageContext.request.contextPath}/member/profile/edit" class="btn btn-primary"><fmt:message key="profile.edit" bundle="${msg}"/></a>
  </div>
</div>
<jsp:include page="../include/footer.jsp"/>
