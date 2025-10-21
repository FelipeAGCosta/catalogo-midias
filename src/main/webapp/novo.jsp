<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/includes/site-bg.jspf" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Novo</title>
  <style>
    html, body { height: 100%; }
    body { overflow: hidden; } /* sem rolagem da página */

    /* área útil levando em conta a altura do header (~72px) */
    .stage {
      height: calc(100vh - 72px);
      display: flex; align-items: center; justify-content: center;
      padding: 0 16px;
    }

    /* card com altura fixa dentro da viewport, sem overflow */
    .card.center {
      margin: 0; width: 100%; max-width: 720px;
      /* altura calculada para caber sem barra */
      max-height: calc(100vh - 96px);
    }

    /* compacta o formulário para caber em telas típicas 768p */
    .form-compact label{display:block;margin:6px 0 4px;color:var(--muted)}
    .form-compact .input,.form-compact .select,.form-compact .textarea{
      padding:8px;border-radius:10px
    }
    .form-compact .textarea{min-height:96px} /* 3–4 linhas */
    .form-compact .row{display:grid;grid-template-columns:1fr 1fr;gap:12px}
    @media (max-height: 760px){
      .form-compact .textarea{min-height:80px}
    }
  </style>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
<%@ include file="/WEB-INF/includes/header.jspf" %>

<div class="stage">
  <div class="page-width" style="width:100%">
    <div class="card center">
      <h2 style="margin-top:0">Cadastrar nova mídia</h2>

      <form action="${pageContext.request.contextPath}/cadastrar"
            method="post" accept-charset="UTF-8" class="form-compact">

        <label>Título</label>
        <input class="input" type="text" name="titulo" required>

        <div class="row">
          <div>
            <label>Autor</label>
            <input class="input" type="text" name="autor" required>
          </div>
          <div>
            <label>Tipo</label>
            <select class="select" name="tipo" required>
              <option value="FILME">FILME</option>
              <option value="LIVRO">LIVRO</option>
            </select>
          </div>
        </div>

        <div class="row">
          <div>
            <label>Ano</label>
            <input class="input" type="number" name="ano" min="0" max="3000" required>
          </div>
    <div>
        <label>Nota (0–5)</label>
        <input class="input" type="number" name="nota" min="0" max="5" step="1" inputmode="numeric" pattern="[0-5]">
    </div>


        </div>

        <label>Sinopse</label>
        <textarea class="textarea" name="sinopse" rows="3"></textarea>

        <label>Poster URL</label>
        <input class="input" type="url" name="posterUrl"
       placeholder="https://.../poster.jpg ou assets/img/poster.jpg"
       pattern="(https?://.*\.(jpg|jpeg|png|webp|gif)(\?.*)?$|assets/.+)"
       title="Informe uma URL direta de imagem (.jpg, .png, .webp, .gif) ou um caminho de assets/">


        <div style="display:flex;gap:10px;justify-content:flex-end;margin-top:12px">
          <a class="btn secondary equal" href="${pageContext.request.contextPath}/lista.jsp">Cancelar</a>
          <button class="btn equal" type="submit">Salvar</button>
        </div>
      </form>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>
