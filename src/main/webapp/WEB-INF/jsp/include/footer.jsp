<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${not empty sessionScope.userLocale ? sessionScope.userLocale : pageContext.request.locale}" scope="request" />
<fmt:setBundle basename="messages" var="msg" />
    </div>
  </main>
  <footer class="site-footer">
    <div class="container">
      <fmt:message key="app.name" bundle="${msg}"/> – <fmt:message key="app.tagline" bundle="${msg}"/>
    </div>
  </footer>
</body>
</html>
