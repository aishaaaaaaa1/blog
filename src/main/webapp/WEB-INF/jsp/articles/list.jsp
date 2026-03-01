<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${not empty sessionScope.userLocale ? sessionScope.userLocale : pageContext.request.locale}" scope="request" />
<fmt:setBundle basename="messages" var="msg" />
<jsp:include page="../include/header.jsp"/>

<div class="blog-head">
  <h1 class="blog-title"><fmt:message key="articles.title" bundle="${msg}"/></h1>
  <div class="filter-bar">
    <a href="${pageContext.request.contextPath}/home"><fmt:message key="home.all" bundle="${msg}"/></a>
    <a href="${pageContext.request.contextPath}/articles" class="active"><fmt:message key="menu.articles" bundle="${msg}"/></a>
  </div>
</div>

<c:if test="${not empty sessionScope.currentMember}">
  <p style="margin-bottom:1.5rem;"><a href="${pageContext.request.contextPath}/articles/new" class="btn btn-primary"><fmt:message key="articles.new" bundle="${msg}"/></a></p>
</c:if>

<c:choose>
  <c:when test="${empty articles}">
    <p class="text-muted"><fmt:message key="home.noArticles" bundle="${msg}"/></p>
  </c:when>
  <c:otherwise>
    <div class="articles-grid">
      <c:forEach var="article" items="${articles}">
        <article class="card article-card">
          <c:choose>
            <c:when test="${not empty article.imagePath}">
              <a href="${pageContext.request.contextPath}/articles/${article.id}" class="article-card-image">
                <img src="${pageContext.request.contextPath}/${article.imagePath}" alt="" />
              </a>
            </c:when>
            <c:otherwise>
              <a href="${pageContext.request.contextPath}/articles/${article.id}" class="article-card-image" style="background:#e5e7eb;display:flex;align-items:center;justify-content:center;color:#9ca3af;font-size:2.5rem;text-decoration:none;">&#9998;</a>
            </c:otherwise>
          </c:choose>
          <div class="article-card-body">
            <span class="card-tag"><fmt:message key="menu.articles" bundle="${msg}"/></span>
            <p class="card-meta">
              <span><fmt:formatDate value="${article.createdAtAsDate}" type="date" dateStyle="long"/></span>
            </p>
            <h3 class="card-title"><a href="${pageContext.request.contextPath}/articles/${article.id}"><c:out value="${article.title}"/></a></h3>
            <p class="card-excerpt"><c:out value="${article.content.length() > 120 ? article.content.substring(0, 120).concat('...') : article.content}"/></p>
            <div class="card-author">
              <span class="card-author-avatar"><c:out value="${not empty article.authorPseudo ? article.authorPseudo.substring(0, 1).toUpperCase() : '?'}"/></span>
              <span><c:out value="${article.authorPseudo}"/></span>
            </div>
          </div>
        </article>
      </c:forEach>
    </div>
  </c:otherwise>
</c:choose>

<jsp:include page="../include/footer.jsp"/>
