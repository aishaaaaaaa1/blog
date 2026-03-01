<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${not empty sessionScope.userLocale ? sessionScope.userLocale : pageContext.request.locale}" scope="request" />
<fmt:setBundle basename="messages" var="msg" />
<jsp:include page="../include/header.jsp"/>
<div class="card">
  <h2 class="page-title"><c:choose><c:when test="${empty article}"><fmt:message key="articles.new" bundle="${msg}"/></c:when><c:otherwise><fmt:message key="articles.edit" bundle="${msg}"/></c:otherwise></c:choose></h2>
  <form method="post" action="${pageContext.request.contextPath}/articles" enctype="multipart/form-data">
    <c:if test="${not empty article}">
      <input type="hidden" name="id" value="${article.id}" />
    </c:if>
    <div class="form-group">
      <label for="title"><fmt:message key="articles.title" bundle="${msg}"/></label>
      <input type="text" id="title" name="title" class="form-control" value="${not empty article ? article.title : ''}" required />
    </div>
    <div class="form-group">
      <label for="image">Photo / image (optionnel)</label>
      <input type="file" id="image" name="image" class="form-control" accept="image/jpeg,image/png,image/gif,image/webp" />
      <c:if test="${not empty article && not empty article.imagePath}">
        <p class="form-hint">Image actuelle : <img src="${pageContext.request.contextPath}/${article.imagePath}" alt="" style="max-width:120px;height:auto;" /></p>
      </c:if>
    </div>
    <div class="form-group">
      <label for="content">Contenu</label>
      <textarea id="content" name="content" class="form-control" required><c:out value="${not empty article ? article.content : ''}"/></textarea>
    </div>
    <button type="submit" class="btn btn-primary"><fmt:message key="profile.save" bundle="${msg}"/></button>
    <a href="${pageContext.request.contextPath}/articles" class="btn btn-outline">Annuler</a>
  </form>
</div>
<jsp:include page="../include/footer.jsp"/>
