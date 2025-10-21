<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/includes/site-bg.jspf" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String q        = request.getParameter("q");
    String tipo     = request.getParameter("tipo");
    String sort     = request.getParameter("sort");
    String dirRaw   = request.getParameter("dir"); // Crescente / Decrescente

    int pageNumber = 1;
    try { pageNumber = Integer.parseInt(request.getParameter("page")); if (pageNumber < 1) pageNumber = 1; } catch(Exception ignore){}

    int pageSize = 10;

    java.util.List cols = java.util.Arrays.asList(new String[]{"id","titulo","autor","tipo","ano","nota"});
    if (sort == null || !cols.contains(sort)) sort = "id";

    String dir;
    String dirLabel;
    if ("Decrescente".equalsIgnoreCase(dirRaw) || "DESC".equalsIgnoreCase(dirRaw)) {
        dir = "DESC";
        dirLabel = "Decrescente";
    } else {
        dir = "ASC";
        dirLabel = "Crescente";
    }

    int offset = (pageNumber - 1) * pageSize;

    br.projeto.dao.MidiaDAO dao = new br.projeto.dao.MidiaDAO();
    String tipoFiltro = (tipo != null && tipo.length() > 0) ? tipo : null;
    int total = dao.contar(q, tipoFiltro);
    java.util.List midias = dao.listar(q, tipoFiltro, sort, dir, offset, pageSize);
    int totalPages = (int) Math.ceil(total / (double) pageSize);

    request.setAttribute("midias", midias);
    request.setAttribute("q", q);
    request.setAttribute("tipo", tipo);
    request.setAttribute("sort", sort);
    request.setAttribute("dirLabel", dirLabel);
    request.setAttribute("pageNumber", pageNumber);
    request.setAttribute("total", total);
    request.setAttribute("totalPages", totalPages);
%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Lista</title></head>
<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<div class="card">

  <form method="get" action="${pageContext.request.contextPath}/lista.jsp"
        style="display:flex;gap:8px;align-items:center;margin-bottom:12px;flex-wrap:wrap">
    <input class="input" type="text" name="q" placeholder="Buscar por título..."
           value="${fn:escapeXml(param.q)}" style="min-width:220px"/>

    <select class="select" name="tipo" style="width:180px">
      <option value=""      <c:if test="${empty param.tipo}">selected</c:if> >Todos os tipos</option>
      <option value="FILME" <c:if test="${param.tipo=='FILME'}">selected</c:if> >FILME</option>
      <option value="LIVRO" <c:if test="${param.tipo=='LIVRO'}">selected</c:if> >LIVRO</option>
    </select>

    <select class="select" name="sort" style="width:200px">
      <option value="id"     <c:if test="${param.sort=='id'     || empty param.sort}">selected</c:if> >Ordenar por: ID</option>
      <option value="titulo" <c:if test="${param.sort=='titulo'}">selected</c:if> >Título</option>
      <option value="autor"  <c:if test="${param.sort=='autor'}">selected</c:if>  >Autor</option>
      <option value="tipo"   <c:if test="${param.sort=='tipo'}">selected</c:if>   >Tipo</option>
      <option value="ano"    <c:if test="${param.sort=='ano'}">selected</c:if>    >Ano</option>
      <option value="nota"   <c:if test="${param.sort=='nota'}">selected</c:if>   >Nota</option>
    </select>

    <select class="select" name="dir" style="width:180px">
      <option value="Crescente"   <c:if test="${param.dir=='Crescente' || empty param.dir}">selected</c:if>   >Crescente</option>
      <option value="Decrescente" <c:if test="${param.dir=='Decrescente'}">selected</c:if> >Decrescente</option>
    </select>

    <input type="hidden" name="page" value="1"/>
      <button class="btn equal" type="submit">Aplicar</button>
      <c:if test="${not empty param.q or not empty param.tipo or not empty param.sort or not empty param.dir}">
  <a class="btn secondary equal" href="${pageContext.request.contextPath}/lista.jsp">Limpar</a>
</c:if>

  </form>

  <h2>
    Lista de Mídias
    <small style="color:var(--muted)">
      (<c:out value="${total}"/>
      <c:if test="${not empty param.q}"> resultado(s) para "<c:out value='${param.q}'/>"</c:if>
      <c:if test="${not empty param.tipo}"> — tipo: <c:out value="${param.tipo}"/></c:if>)
    </small>
  </h2>

  <table class="table">
    <thead>
    <tr><th>ID</th><th>Título</th><th>Autor</th><th>Tipo</th><th>Ano</th><th>Nota</th><th>Ações</th></tr>
    </thead>
    <tbody>
    <c:forEach var="m" items="${midias}">
      <tr>
        <td>${m.id}</td>
        <td><a href="${pageContext.request.contextPath}/detalhe.jsp?id=${m.id}">${m.titulo}</a></td>
        <td><c:out value="${m.autor}"/></td>
        <td>${m.tipo}</td>
        <td>${m.ano}</td>
        <td>
          <c:choose>
            <c:when test="${m.nota != null}">
              <fmt:formatNumber value="${m.nota}" maxFractionDigits="0" />
            </c:when>
            <c:otherwise></c:otherwise>
          </c:choose>
        </td>
        <td>
          <a href="${pageContext.request.contextPath}/editar.jsp?id=${m.id}">Editar</a> |
          <a href="${pageContext.request.contextPath}/excluir?id=${m.id}" onclick="return confirm('Excluir este registro?');">Excluir</a>
        </td>
      </tr>
    </c:forEach>
    <c:if test="${empty midias}">
      <tr><td colspan="7">Nenhum registro.</td></tr>
    </c:if>
    </tbody>
  </table>

  <c:if test="${totalPages > 1}">
  <!-- base de parâmetros comuns -->
  <c:url var="urlBase" value="/lista.jsp">
    <c:param name="q"    value="${param.q}"/>
    <c:param name="tipo" value="${param.tipo}"/>
    <c:param name="sort" value="${sort}"/>
    <c:param name="dir"  value="${empty param.dir ? 'Crescente' : param.dir}"/>
  </c:url>

  <!-- URLs finais -->
  <c:url var="urlFirst"  value="/lista.jsp">
    <c:param name="q"    value="${param.q}"/>
    <c:param name="tipo" value="${param.tipo}"/>
    <c:param name="sort" value="${sort}"/>
    <c:param name="dir"  value="${empty param.dir ? 'Crescente' : param.dir}"/>
    <c:param name="page" value="1"/>
  </c:url>

  <c:url var="urlPrev" value="/lista.jsp">
    <c:param name="q"    value="${param.q}"/>
    <c:param name="tipo" value="${param.tipo}"/>
    <c:param name="sort" value="${sort}"/>
    <c:param name="dir"  value="${empty param.dir ? 'Crescente' : param.dir}"/>
    <c:param name="page" value="${pageNumber-1}"/>
  </c:url>

  <c:url var="urlNext" value="/lista.jsp">
    <c:param name="q"    value="${param.q}"/>
    <c:param name="tipo" value="${param.tipo}"/>
    <c:param name="sort" value="${sort}"/>
    <c:param name="dir"  value="${empty param.dir ? 'Crescente' : param.dir}"/>
    <c:param name="page" value="${pageNumber+1}"/>
  </c:url>

  <c:url var="urlLast" value="/lista.jsp">
    <c:param name="q"    value="${param.q}"/>
    <c:param name="tipo" value="${param.tipo}"/>
    <c:param name="sort" value="${sort}"/>
    <c:param name="dir"  value="${empty param.dir ? 'Crescente' : param.dir}"/>
    <c:param name="page" value="${totalPages}"/>
  </c:url>

  <div style="display:flex;gap:8px;align-items:center;justify-content:flex-end;margin-top:12px">
    <span style="color:var(--muted)">Página ${pageNumber} de ${totalPages}</span>

    <c:choose>
      <c:when test="${pageNumber == 1}">
        <span class="btn secondary" style="pointer-events:none;opacity:.5">« Primeiro</span>
      </c:when>
      <c:otherwise>
        <a class="btn secondary" href="${urlFirst}">« Primeiro</a>
      </c:otherwise>
    </c:choose>

    <c:choose>
      <c:when test="${pageNumber == 1}">
        <span class="btn secondary" style="pointer-events:none;opacity:.5">‹ Anterior</span>
      </c:when>
      <c:otherwise>
        <a class="btn secondary" href="${urlPrev}">‹ Anterior</a>
      </c:otherwise>
    </c:choose>

    <c:choose>
      <c:when test="${pageNumber == totalPages}">
        <span class="btn secondary" style="pointer-events:none;opacity:.5">Próximo ›</span>
      </c:when>
      <c:otherwise>
        <a class="btn secondary" href="${urlNext}">Próximo ›</a>
      </c:otherwise>
    </c:choose>

    <c:choose>
      <c:when test="${pageNumber == totalPages}">
        <span class="btn secondary" style="pointer-events:none;opacity:.5">Último »</span>
      </c:when>
      <c:otherwise>
        <a class="btn secondary" href="${urlLast}">Último »</a>
      </c:otherwise>
    </c:choose>
  </div>
</c:if>

</div>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>
