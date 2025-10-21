<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="br.projeto.dao.MidiaDAO, br.projeto.model.Midia" %>
<%@ include file="/WEB-INF/includes/site-bg.jspf" %>
<%
    request.setCharacterEncoding("UTF-8");

    int id = Integer.parseInt(request.getParameter("id"));
    MidiaDAO dao = new MidiaDAO();
    Midia m = dao.buscarPorId(id);
    if (m == null) {
        response.sendError(404);
        return;
    }

    double notaAtual = (m.getNota() == null) ? 0.0 : m.getNota();
    int intNota = (int) Math.round(notaAtual); // 0..5

    String poster = (m.getPosterUrl() == null ? "" : m.getPosterUrl().trim());
    boolean vazio = poster.isEmpty();
    String placeholder = request.getContextPath() + "/assets/img/placeholder.jpg"; // <- aqui é o fix
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title><%= m.getTitulo() %></title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
  <style>
    .rating{display:flex;gap:6px;align-items:center}
    .star{font-size:24px;color:#ffd166;opacity:.4;line-height:1}
    .star.on{opacity:1}
    .rating-note{color:var(--muted);margin-left:8px}
    .poster{
      width:240px;aspect-ratio:2/3;height:auto;object-fit:contain;object-position:center;
      border-radius:12px;border:1px solid #202338;background:#0f1118;display:block;max-width:100%;
    }
    .detail-grid{display:grid;grid-template-columns:240px 1fr;gap:16px}
    @media (max-width:720px){.detail-grid{grid-template-columns:1fr}}
  </style>
</head>
<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<div class="container">
  <div class="card">
    <h2><%= m.getTitulo() %></h2>

    <div class="detail-grid">
      <img class="poster"
           src="<%= vazio ? placeholder : poster %>"
           alt="Pôster"
           onerror="this.onerror=null; this.src='<%= placeholder %>';" />

      <div>
        <p><strong>Tipo:</strong> <%= m.getTipo() %></p>
        <p><strong>Ano:</strong> <%= m.getAno() %></p>
        <p><strong>Autor:</strong> <%= (m.getAutor()==null?"":m.getAutor()) %></p>
        <p><strong>Sinopse:</strong><br><%= (m.getSinopse()==null?"":m.getSinopse()) %></p>

        <div class="rating">
          <% for (int i = 1; i <= 5; i++) { %>
            <span class="star <%= (intNota >= i ? "on" : "") %>">★</span>
          <% } %>
          <span class="rating-note"><%= (intNota == 0 ? "sem nota" : (intNota + "/5")) %></span>
        </div>

        <p style="margin-top:10px">
          <a class="btn secondary" href="${pageContext.request.contextPath}/lista.jsp">Voltar para a lista</a>
          <a class="btn" href="${pageContext.request.contextPath}/editar.jsp?id=<%= id %>">Editar</a>
        </p>
      </div>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>
