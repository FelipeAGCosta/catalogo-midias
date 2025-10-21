package br.projeto.dao;

import br.projeto.infra.ConnectionFactory;
import br.projeto.model.Midia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de {@link br.projeto.model.Midia} com operações de CRUD,
 * busca por título e listagem com filtros/ordenação/paginação.
    Todas as consultas usam {@link PreparedStatement}.
 */
public class MidiaDAO {

    /**
     * Retorna todas as mídias ordenadas por ID.
     
     * @return lista completa de mídias
     * @throws Exception em falhas de acesso ao banco
     
     */
    public List<Midia> listarTodas() throws Exception {
        String sql = "SELECT id, titulo, autor, tipo, ano, nota, sinopse, poster_url " +
                     "FROM midia ORDER BY id";
        List<Midia> lista = new ArrayList<>();

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapper(rs));
            }
        }
        return lista;
    }

    /**
     * Busca mídias cujo título contenha o termo (case-insensitive).
     
     * @param termo trecho do título a pesquisar
     * @return lista de mídias compatíveis
     * @throws Exception em falhas de acesso ao banco
     
     */
    public List<Midia> buscarPorTitulo(String termo) throws Exception {
        String sql = "SELECT id, titulo, autor, tipo, ano, nota, sinopse, poster_url " +
                     "FROM midia WHERE LOWER(titulo) LIKE LOWER(?) ORDER BY id";
        List<Midia> lista = new ArrayList<>();

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + termo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapper(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Busca uma mídia pelo ID.
     
     * @param id identificador da mídia
     * @return a mídia encontrada ou {@code null} se não existir
     * @throws Exception em falhas de acesso ao banco
     
     */
    public Midia buscarPorId(int id) throws Exception {
        String sql = "SELECT id, titulo, autor, tipo, ano, nota, sinopse, poster_url " +
                     "FROM midia WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapper(rs);
                return null;
            }
        }
    }

    /**
     * Atualiza apenas a nota (0..5) da mídia.
     
     * @param id   identificador da mídia
     * @param nota valor entre 0 e 5 (ou {@code null} para remover a nota)
     * @throws Exception em falhas de acesso ao banco
     
     */
    public void atualizarNota(int id, Double nota) throws Exception {
        String sql = "UPDATE midia SET nota = ? WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (nota == null) ps.setNull(1, java.sql.Types.DECIMAL);
            else ps.setDouble(1, nota);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    /**
     * Insere uma nova mídia.
     
     * @param m objeto preenchido (titulo, autor, tipo, ano, nota opcional, sinopse, posterUrl)
     * @throws Exception em falhas de acesso ao banco
    
     */
    public void inserir(Midia m) throws Exception {
        String sql = "INSERT INTO midia (titulo, autor, tipo, ano, nota, sinopse, poster_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getTitulo());
            ps.setString(2, m.getAutor());
            ps.setString(3, m.getTipo());
            ps.setInt(4, m.getAno());
            if (m.getNota() == null) ps.setNull(5, java.sql.Types.DECIMAL);
            else ps.setDouble(5, m.getNota());
            ps.setString(6, m.getSinopse());
            ps.setString(7, m.getPosterUrl());
            ps.executeUpdate();
        }
    }

    /**
     * Atualiza todos os campos de uma mídia existente.
     *
     * @param m objeto com ID e campos atualizados
     * @throws Exception em falhas de acesso ao banco
     
     */
    public void atualizar(Midia m) throws Exception {
        String sql = "UPDATE midia SET titulo=?, autor=?, tipo=?, ano=?, nota=?, sinopse=?, poster_url=? " +
                     "WHERE id=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getTitulo());
            ps.setString(2, m.getAutor());
            ps.setString(3, m.getTipo());
            ps.setInt(4, m.getAno());
            if (m.getNota() == null) ps.setNull(5, java.sql.Types.DECIMAL);
            else ps.setDouble(5, m.getNota());
            ps.setString(6, m.getSinopse());
            ps.setString(7, m.getPosterUrl());
            ps.setInt(8, m.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Exclui a mídia indicada.
     
     * @param id identificador da mídia
     * @throws Exception em falhas de acesso ao banco
     
     */
    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM midia WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /**
     * Retorna a quantidade total de mídias na tabela (sem filtros).
     
     * @return total de registros
     * @throws Exception em falhas de acesso ao banco
     
     */
    public int contarTotal() throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM midia";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
            return 0;
        }
    }

    /**
     * Lista mídias com filtros opcionais, ordenação e paginação.
     
     * @param q      termo de busca por título (pode ser {@code null} ou vazio)
     * @param tipo   "FILME", "LIVRO" ou {@code null} para ambos
     * @param sort   coluna para ordenação (id, titulo, autor, tipo, ano, nota)
     * @param dir    direção "ASC" ou "DESC"
     * @param offset deslocamento inicial (ex.: (pagina-1)*tamanho)
     * @param limit  quantidade de registros a retornar
     * @return lista de mídias
     * @throws Exception em falhas de acesso ao banco
     
     */
    public List<Midia> listar(String q, String tipo, String sort, String dir, int offset, int limit) throws Exception {
        String sortCol = switch (sort) {
            case "titulo", "autor", "tipo", "ano", "nota", "id" -> sort;
            default -> "id";
        };
        String direction = "DESC".equalsIgnoreCase(dir) ? "DESC" : "ASC";

        StringBuilder sb = new StringBuilder(
            "SELECT id, titulo, autor, tipo, ano, nota, sinopse, poster_url FROM midia WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();

        if (q != null && !q.isBlank()) {
            sb.append("AND LOWER(titulo) LIKE LOWER(?) ");
            params.add("%" + q.trim() + "%");
        }
        if (tipo != null && !tipo.isBlank()) {
            sb.append("AND tipo = ? ");
            params.add(tipo);
        }

        sb.append("ORDER BY ").append(sortCol).append(" ").append(direction).append(" ");
        sb.append("LIMIT ? OFFSET ?");

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sb.toString())) {
            int idx = 1;
            for (Object p : params) ps.setObject(idx++, p);
            ps.setInt(idx++, limit);
            ps.setInt(idx, offset);

            List<Midia> lista = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapper(rs));
            }
            return lista;
        }
    }

    /**
     * Conta o total de registros que atendem aos filtros informados.
     
     * @param q    termo de busca por título (pode ser {@code null})
     * @param tipo "FILME", "LIVRO" ou {@code null}
     * @return total de registros compatíveis
     * @throws Exception em falhas de acesso ao banco
     
     */
    public int contar(String q, String tipo) throws Exception {
        StringBuilder sb = new StringBuilder("SELECT COUNT(*) AS total FROM midia WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (q != null && !q.isBlank()) {
            sb.append("AND LOWER(titulo) LIKE LOWER(?) ");
            params.add("%" + q.trim() + "%");
        }
        if (tipo != null && !tipo.isBlank()) {
            sb.append("AND tipo = ? ");
            params.add(tipo);
        }

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sb.toString())) {
            int idx = 1;
            for (Object p : params) ps.setObject(idx++, p);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
                return 0;
            }
        }
    }

    // mapper comum (privado)
    
    private Midia mapper(ResultSet rs) throws Exception {
        return new Midia(
            rs.getInt("id"),
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getString("tipo"),
            rs.getInt("ano"),
            (rs.getObject("nota") == null ? null : rs.getDouble("nota")),
            rs.getString("sinopse"),
            rs.getString("poster_url")
        );
    }
}
