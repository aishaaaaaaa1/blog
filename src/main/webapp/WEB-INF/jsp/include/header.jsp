<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${not empty sessionScope.userLocale ? sessionScope.userLocale : pageContext.request.locale}" scope="request" />
<fmt:setBundle basename="messages" var="msg" />
<!DOCTYPE html>
<html lang="${(not empty sessionScope.userLocale and not empty sessionScope.userLocale.language) ? sessionScope.userLocale.language : 'fr'}">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title><fmt:message key="app.name" bundle="${msg}"/> – <fmt:message key="app.tagline" bundle="${msg}"/></title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>" type="text/css" />
</head>
<body>
  <header class="site-header">
    <div class="container header-inner">
      <a href="${pageContext.request.contextPath}/home" class="site-logo">
        <span class="logo-icon">
          <span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span><span></span>
        </span>
        <span><fmt:message key="app.name" bundle="${msg}"/></span>
      </a>
      <nav class="header-nav">
        <a href="${pageContext.request.contextPath}/home"><fmt:message key="menu.home" bundle="${msg}"/></a>
        <a href="${pageContext.request.contextPath}/articles"><fmt:message key="menu.articles" bundle="${msg}"/></a>
        <div class="lang-selector" role="group" aria-label="<fmt:message key="languages" bundle="${msg}"/>">
          <span class="lang-selector-icon" aria-hidden="true">&#127760;</span>
          <a href="${pageContext.request.contextPath}/language?lang=fr" class="lang-opt ${(not empty sessionScope.userLocale and sessionScope.userLocale.language eq 'fr') ? 'active' : ''}" title="Français">FR</a>
          <span class="lang-sep" aria-hidden="true">|</span>
          <a href="${pageContext.request.contextPath}/language?lang=en" class="lang-opt ${(not empty sessionScope.userLocale and sessionScope.userLocale.language eq 'en') ? 'active' : ''}" title="English">EN</a>
        </div>
        <c:choose>
          <c:when test="${not empty sessionScope.currentMember}">
            <a href="${pageContext.request.contextPath}/member/profile"><fmt:message key="menu.profile" bundle="${msg}"/></a>
            <a href="${pageContext.request.contextPath}/articles/new" class="btn-cta"><fmt:message key="menu.newArticle" bundle="${msg}"/> <span class="arrow">&#8594;</span></a>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout-icon" title="<fmt:message key="menu.logout" bundle="${msg}"/>"><span class="logout-icon" aria-hidden="true"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg></span></a>
          </c:when>
          <c:otherwise>
            <a href="${pageContext.request.contextPath}/login"><fmt:message key="menu.login" bundle="${msg}"/></a>
            <a href="${pageContext.request.contextPath}/register" class="btn-cta"><fmt:message key="menu.register" bundle="${msg}"/> <span class="arrow">&#8594;</span></a>
          </c:otherwise>
        </c:choose>
      </nav>
    </div>
  </header>
  <main>
    <div class="container">
