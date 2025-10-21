<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Catálogo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
  <style>
    /* página sem rolagem */
    html, body { height: 100%; }
    body { overflow: hidden; }

    /* HERO de tela inteira */
    .hero {
      position: relative;
      height: 100vh;
      width: 100%;
      isolation: isolate; /* garante que o overlay não vaze */
      display: grid;
      place-items: center;
      padding: 0 24px;
    }

    /* colagem de pôsteres ao fundo */
    .hero .bg {
      position: absolute;
      inset: 0;
      display: grid;
      grid-template-columns: repeat(5, 1fr);
      grid-auto-rows: 24vh;
      gap: 10px;
      padding: 40px;
      filter: blur(6px);
      opacity: .28;
      transform: scale(1.06);
    }
    .hero .bg img {
      width: 100%; height: 100%;
      object-fit: cover;
      border-radius: 12px;
      border: 1px solid #202338;
      background:#0f1118;
    }

    /* máscara escura + vinheta para contraste */
    .hero::after{
      content:"";
      position:absolute; inset:0; z-index:-1;
      background:
        radial-gradient(1200px 600px at 50% 40%, #0000 0%, #0008 60%, #000C 100%),
        linear-gradient(180deg, #0009 0%, #0008 40%, #000C 100%);
    }

    /* conteúdo central */
    .welcome {
      position: relative;
      z-index: 1;
      width: min(600px, 92vw);
      background: color-mix(in srgb, var(--panel) 80%, #000 20%);
      border: 1px solid #202338;
      border-radius: 16px;
      padding: 28px;
      box-shadow: 0 10px 30px rgba(0,0,0,.35);
    }
    .welcome h1{
      margin: 0 0 10px 0;
      font-size: clamp(26px, 4vw, 40px);
    }
    .welcome p{
      margin: 0 0 20px 0;
      color: var(--muted);
      text-align: center;   /* centraliza o parágrafo */
    }

    /* CTAs centralizados, lado a lado */
    .cta{
      display:flex; gap:12px; justify-content:center; flex-wrap:wrap;
    }
    .btn.equal{ min-width:150px; } /* garante mesmo “peso” visual */
  </style>
</head>
<body>

  <section class="hero">
    <!-- colagem de pôsteres: troque pelos seus arquivos em assets/img/hero/ -->
    <div class="bg" aria-hidden="true">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h1.png" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h2.png" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h3.png" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h4.png" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h5.png" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h6.png" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h7.png" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h8.png" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h9.png" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h10.png" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h11.jpg" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h12.jpg" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h13.jpg" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h14.jpg" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h15.jpg" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h16.jpg" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h17.jpg" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h18.jpg" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h19.jpg" alt="">
      <img src="${pageContext.request.contextPath}/assets/img/hero/h20.jpg" alt="">
    </div>

  

       <!-- conteúdo central -->
      <div class="welcome">
    <h1 style="text-align: center;">Bem-vindo ao Catálogo</h1>

    <p>Navegue pela lista ou cadastre novos itens.</p>
    <div class="cta">
      <a class="btn equal"           href="${pageContext.request.contextPath}/lista.jsp">Ver lista</a>
      <a class="btn secondary equal" href="${pageContext.request.contextPath}/novo.jsp">Cadastrar</a>
    </div>
  </div>
</section>

</body>
</html>
