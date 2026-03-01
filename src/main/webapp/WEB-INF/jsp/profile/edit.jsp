<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${not empty sessionScope.userLocale ? sessionScope.userLocale : pageContext.request.locale}" scope="request" />
<fmt:setBundle basename="messages" var="msg" />
<jsp:include page="../include/header.jsp"/>
<div class="card profile-card">
  <h2 class="page-title"><fmt:message key="profile.edit" bundle="${msg}"/></h2>
  <form method="post" action="${pageContext.request.contextPath}/member/profile">
    <div class="form-group">
      <label for="displayName"><fmt:message key="profile.displayName" bundle="${msg}"/></label>
      <input type="text" id="displayName" name="displayName" class="form-control" value="${profile.displayName}" />
    </div>
    <div class="form-group">
      <label for="bio"><fmt:message key="profile.bio" bundle="${msg}"/></label>
      <textarea id="bio" name="bio" class="form-control"><c:out value="${profile.bio}"/></textarea>
    </div>
    <button type="submit" class="btn btn-primary"><fmt:message key="profile.save" bundle="${msg}"/></button>
    <a href="${pageContext.request.contextPath}/member/profile" class="btn btn-outline">Annuler</a>
  </form>
</div>
<jsp:include page="../include/footer.jsp"/>
