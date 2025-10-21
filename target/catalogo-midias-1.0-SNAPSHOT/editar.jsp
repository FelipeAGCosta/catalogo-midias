<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="br.projeto.dao.MidiaDAO, br.projeto.model.Midia" %>
<%@ include file="/WEB-INF/includes/site-bg.jspf" %>
<%
    // carrega a mídia pelo id
    String idStr = request.getParameter("id");
    Midia m = null;
    try {
        int id = Integer.parseInt(idStr);
        m = new MidiaDAO().buscarPorId(id);
    } catch (Exception e) {
        throw new ServletException("ID inválido", e);
    }
    if (m == null) {
        response.sendError(404, "Mídia não encontrada");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Editar</title></head>
<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<div class="card center" style="max-width:720px;margin:0 auto">
  <h2 style="margin-top:0">Editar mídia</h2>
  
  <form action="${pageContext.request.contextPath}/atualizar" method="post" accept-charset="UTF-8">
    <input type="hidden" name="id" value="<%= m.getId() %>"/>

    <label>Título
      <input class="input" type="text" name="titulo" required value="<%= m.getTitulo()==null? "": m.getTitulo() %>">
    </label>

    <label>Autor
      <input class="input" type="text" name="autor" required value="<%= m.getAutor()==null? "": m.getAutor() %>">
    </label>

    <label>Tipo
      <select class="select" name="tipo" required>
        <option value="FILME" <%= "FILME".equals(m.getTipo()) ? "selected" : "" %>>FILME</option>
        <option value="LIVRO" <%= "LIVRO".equals(m.getTipo()) ? "selected" : "" %>>LIVRO</option>
      </select>
    </label>

    <label>Ano
      <input class="input" type="number" name="ano" min="0" max="3000" required value="<%= m.getAno()==null? "": m.getAno() %>">
    </label>

    <label>Nota (0–5)
      <input class="input" type="number" name="nota" min="0" max="5" step="1" inputmode="numeric" pattern="[0-5]" value="<%= (m.getNota()==null? "" : String.valueOf(m.getNota().intValue())) %>">
    </label>



    <label>Sinopse
      <textarea class="textarea" name="sinopse" rows="4"><%= m.getSinopse()==null? "": m.getSinopse() %></textarea>
    </label>

    <label>Poster URL
      <input class="input" type="url" name="posterUrl"
       placeholder="https://.../poster.jpg ou assets/img/poster.jpg"
       pattern="(https?://.*\.(jpg|jpeg|png|webp|gif)(\?.*)?$|assets/.+)"
       title="Informe uma URL direta de imagem (.jpg, .png, .webp, .gif) ou um caminho de assets/">

    </label>

    <div style="display:flex;gap:10px;justify-content:flex-end">
      <a class="btn secondary equal" href="${pageContext.request.contextPath}/lista.jsp">Cancelar</a>
      <button class="btn equal" type="submit">Salvar alterações</button>
    </div>
  </form>
</div>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>
