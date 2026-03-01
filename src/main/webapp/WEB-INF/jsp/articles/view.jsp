<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${not empty sessionScope.userLocale ? sessionScope.userLocale : pageContext.request.locale}" scope="request" />
<fmt:setBundle basename="messages" var="msg" />
<jsp:include page="../include/header.jsp"/>
<article class="card">
  <c:if test="${not empty article.imagePath}">
    <div class="article-hero-image">
      <img src="${pageContext.request.contextPath}/${article.imagePath}" alt="" />
    </div>
  </c:if>
  <div class="article-header">
    <h2 class="page-title mb-0"><c:out value="${article.title}"/></h2>
    <p class="card-meta"><fmt:message key="articles.author" bundle="${msg}"/> <c:out value="${article.authorPseudo}"/> · <fmt:message key="articles.published" bundle="${msg}"/> <fmt:formatDate value="${article.createdAtAsDate}" type="both" dateStyle="long" timeStyle="short"/></p>
    <c:if test="${not empty sessionScope.currentMember && (sessionScope.currentMember.id == article.authorId || sessionScope.currentMember.role == 'ADMIN')}">
      <a href="${pageContext.request.contextPath}/articles/edit/${article.id}" class="btn btn-outline"><fmt:message key="articles.edit" bundle="${msg}"/></a>
      <form method="post" action="${pageContext.request.contextPath}/articles" style="display:inline;" onsubmit="return confirm('Supprimer cet article ?');">
        <input type="hidden" name="action" value="delete" />
        <input type="hidden" name="id" value="${article.id}" />
        <button type="submit" class="btn btn-danger"><fmt:message key="articles.delete" bundle="${msg}"/></button>
      </form>
    </c:if>
  </div>
  <div class="article-content"><c:out value="${article.content}"/></div>

  <section class="comments-section">
    <h3><fmt:message key="articles.addComment" bundle="${msg}"/></h3>
    <c:choose>
      <c:when test="${empty article.comments}">
        <p class="text-muted"><fmt:message key="articles.noComments" bundle="${msg}"/></p>
      </c:when>
      <c:otherwise>
        <c:forEach var="comment" items="${article.comments}">
          <div class="comment">
            <p class="comment-meta"><c:out value="${comment.authorPseudo}"/> · <fmt:formatDate value="${comment.createdAtAsDate}" type="both" dateStyle="short" timeStyle="short"/>
              <c:if test="${not empty sessionScope.currentMember && (sessionScope.currentMember.id == comment.authorId || sessionScope.currentMember.role == 'ADMIN')}">
                <form method="post" action="${pageContext.request.contextPath}/comment" style="display:inline;" onsubmit="return confirm('Supprimer ce commentaire ?');">
                  <input type="hidden" name="action" value="delete" />
                  <input type="hidden" name="articleId" value="${article.id}" />
                  <input type="hidden" name="commentId" value="${comment.id}" />
                  <button type="submit" class="btn btn-danger" style="padding:0.2rem 0.5rem;font-size:0.8rem;"><fmt:message key="articles.deleteComment" bundle="${msg}"/></button>
                </form>
              </c:if>
            </p>
            <p class="mb-0"><c:out value="${comment.content}"/></p>
          </div>
        </c:forEach>
      </c:otherwise>
    </c:choose>
    <c:if test="${not empty sessionScope.currentMember}">
      <div class="comment-form">
        <form method="post" action="${pageContext.request.contextPath}/comment">
          <input type="hidden" name="articleId" value="${article.id}" />
          <div class="form-group">
            <label for="content"><fmt:message key="articles.addComment" bundle="${msg}"/></label>
            <textarea id="content" name="content" class="form-control" placeholder="<fmt:message key="articles.commentPlaceholder" bundle="${msg}"/>" required></textarea>
          </div>
          <button type="submit" class="btn btn-primary"><fmt:message key="articles.submitComment" bundle="${msg}"/></button>
        </form>
      </div>
    </c:if>
  </section>
</article>
<jsp:include page="../include/footer.jsp"/>
