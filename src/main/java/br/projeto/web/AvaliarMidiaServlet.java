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

@WebServlet("/avaliar")
public class AvaliarMidiaServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(AvaliarMidiaServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String idStr = req.getParameter("id");
        String notaStr = req.getParameter("nota");

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Avaliar: id inválido id={0}", idStr);
            throw new ServletException("ID inválido", e);
        }

        Double nota = null;
        try {
            int n = Integer.parseInt(notaStr);
            if (n < 0) n = 0;
            if (n > 5) n = 5;
            nota = (double) n;
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Avaliar: nota inválida nota={0}", notaStr);
            throw new ServletException("Nota inválida", e);
        }

        try {
            new MidiaDAO().atualizarNota(id, nota);
            resp.sendRedirect(req.getContextPath() + "/detalhe.jsp?id=" + id);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Avaliar falhou id={0}", id);
            throw new ServletException("Erro ao avaliar", e);
        }
    }
}
