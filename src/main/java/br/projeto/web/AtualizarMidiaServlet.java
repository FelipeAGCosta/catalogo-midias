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
 * Recebe POST para atualizar uma mídia existente.
 * Normaliza nota (0..5), sanitiza posterUrl e registra log objetivo em caso de erro.
 */
@WebServlet("/atualizar")
public class AtualizarMidiaServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(AtualizarMidiaServlet.class.getName());

    /**
     * Processa a atualização e redireciona para a lista em caso de sucesso.
     *
     * @param req  request com os campos do formulário (incluindo id)
     * @param resp response para redirecionamento
     * @throws ServletException se ID for inválido ou ocorrer falha no DAO
     * @throws IOException      em falhas de IO durante o redirect
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String idStr     = req.getParameter("id");
        String titulo    = req.getParameter("titulo");
        String autor     = req.getParameter("autor");
        String tipo      = req.getParameter("tipo");
        String anoStr    = req.getParameter("ano");
        String notaStr   = req.getParameter("nota");
        String sinopse   = req.getParameter("sinopse");
        String posterUrl = req.getParameter("posterUrl");

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Atualizar: id inválido id={0}", idStr);
            throw new ServletException("ID inválido", e);
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

        // Sanear poster
        posterUrl = sanitizePoster(posterUrl);

        Midia m = new Midia();
        m.setId(id);
        m.setTitulo(titulo);
        m.setAutor(autor);
        m.setTipo(tipo);
        m.setAno(ano);
        m.setNota(nota);
        m.setSinopse(sinopse);
        m.setPosterUrl(posterUrl);

        try {
            new MidiaDAO().atualizar(m);
            resp.sendRedirect(req.getContextPath() + "/lista.jsp");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Atualizar falhou id={0}", id);
            throw new ServletException("Erro ao atualizar", e);
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
