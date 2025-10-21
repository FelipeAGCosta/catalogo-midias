package br.projeto.web;

import br.projeto.dao.MidiaDAO;
import br.projeto.model.Midia;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Recebe POST para cadastrar uma mídia.
 * Valida campos obrigatórios (título/autor), normaliza nota (0..5) e sanitiza posterUrl.
 * Em caso de falha, registra log e dispara erro 500 tratado pelo error.jsp.
 */
@WebServlet("/cadastrar")
public class CadastrarMidiaServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(CadastrarMidiaServlet.class.getName());

    /**
     * Processa o cadastro e redireciona para a lista em caso de sucesso.
     *
     * @param req  request com os campos do formulário
     * @param resp response para redirecionamento
     * @throws ServletException em validação/persistência
     * @throws IOException      em falhas de IO durante o redirect
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String titulo    = req.getParameter("titulo");
        String autor     = req.getParameter("autor");     // obrigatório
        String tipo      = req.getParameter("tipo");
        String anoStr    = req.getParameter("ano");
        String notaStr   = req.getParameter("nota");
        String sinopse   = req.getParameter("sinopse");
        String posterUrl = req.getParameter("posterUrl");

        if (titulo == null || titulo.isBlank() || autor == null || autor.isBlank()) {
            LOG.log(Level.WARNING, "Cadastro: campos obrigatórios faltando (titulo/autor)");
            throw new ServletException("Título e Autor são obrigatórios.");
        }

        int ano = 0;
        try {
            if (anoStr != null && !anoStr.isBlank()) {
                ano = Integer.parseInt(anoStr.trim());
            }
        } catch (Exception ignore) { }

        Double nota = null;
        if (notaStr != null && !notaStr.isBlank()) {
            try {
                int n = Integer.parseInt(notaStr.trim());
                if (n < 0) n = 0;
                if (n > 5) n = 5;
                nota = (double) n;
            } catch (Exception ignore) { }
        }

        // Sanear poster (aceita .jpg/.png/.webp/.gif ou caminho local assets/)
        posterUrl = sanitizePoster(posterUrl);

        Midia m = new Midia();
        m.setTitulo(titulo);
        m.setAutor(autor);
        m.setTipo(tipo);
        m.setAno(ano);
        m.setNota(nota);
        m.setSinopse(sinopse);
        m.setPosterUrl(posterUrl);

        try {
            new MidiaDAO().inserir(m);
            resp.sendRedirect(req.getContextPath() + "/lista.jsp");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Cadastro falhou titulo={0}", titulo);
            throw new ServletException("Erro ao cadastrar", e);
        }
    }

    /**
     * Sanitiza a URL do pôster, aceitando apenas:
     * <ul>
     *   <li>URLs HTTP/HTTPS que terminem com .jpg/.jpeg/.png/.webp/.gif</li>
     *   <li>caminhos locais iniciando com {@code /} ou {@code assets/}</li>
     * </ul>
     *
     * @param url texto informado no formulário
     * @return URL sanitizada ou {@code null} se inválida/vazia
     */
    private String sanitizePoster(String url) {
        if (url == null) return null;
        String u = url.trim();
        if (u.isEmpty()) return null;

        boolean isImageHttp = u.matches("(?i)^https?://.+\\.(jpg|jpeg|png|webp|gif)(\\?.*)?$");
        boolean isLocalAsset = u.startsWith("/") || u.startsWith("assets/");

        return (isImageHttp || isLocalAsset) ? u : null;
    }
}
