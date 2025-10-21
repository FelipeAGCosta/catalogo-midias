package br.projeto.web;

import br.projeto.dao.MidiaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Exclui a mídia indicada por ID e redireciona para a lista.
 */
@WebServlet("/excluir")
public class ExcluirMidiaServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(ExcluirMidiaServlet.class.getName());

    /**
     * Processa a exclusão via GET (?id=123) e redireciona para a lista.
     *
     * @param req  request com parâmetro {@code id}
     * @param resp response para redirecionamento
     * @throws ServletException se o ID for inválido ou ocorrer falha no DAO
     * @throws IOException      em falhas de IO durante o redirect
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idStr = req.getParameter("id");
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Excluir: id inválido id={0}", idStr);
            throw new ServletException("ID inválido", e);
        }

        try {
            new MidiaDAO().excluir(id);
            resp.sendRedirect(req.getContextPath() + "/lista.jsp");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Excluir falhou id={0}", id);
            throw new ServletException("Erro ao excluir", e);
        }
    }
}
