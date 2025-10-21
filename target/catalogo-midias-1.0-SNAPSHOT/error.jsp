<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  // atributos padrão do container
  Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");
  String uri     = (String)  request.getAttribute("javax.servlet.error.request_uri");
  if (uri == null) uri = request.getRequestURI();
  String msg     = (String)  request.getAttribute("javax.servlet.error.message");
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Opa — algo saiu do previsto</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<div class="container">
  <div class="card" style="max-width:720px;margin:24px auto">
    <h2 style="margin-top:0">
      <c:choose>
        <c:when test="<%= status != null && status == 404 %>">Página não encontrada (404)</c:when>
        <c:when test="<%= status != null && status == 500 %>">Erro interno (500)</c:when>
        <c:otherwise>Algo deu errado</c:otherwise>
      </c:choose>
    </h2>

    <p style="color:var(--muted)">
      URL: <code><%= uri %></code><br>
      <c:if test="${not empty requestScope['javax.servlet.error.message']}">
        Detalhe: <code><%= msg %></code>
      </c:if>
    </p>

    <p>
      <a class="btn" href="${pageContext.request.contextPath}/lista.jsp">Ir para a lista</a>
      <a class="btn secondary" href="${pageContext.request.contextPath}/index.jsp">Ir para a página inicial</a>
    </p>
  </div>
</div>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>
