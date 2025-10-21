package br.projeto.dao;

import br.projeto.infra.ConnectionFactory;
import br.projeto.model.Midia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Midia com CRUD, busca e listagem com filtro/ordenação/paginação.
 * Todas as consultas usam PreparedStatement.
 */
public class MidiaDAO {

    // --- Mapper usando o CONSTRUTOR da sua Midia ---
    private Midia mapper(ResultSet rs) throws Exception {
        Integer ano = (Integer) rs.getObject("ano"); // pode ser nulo
        Double  nota = (rs.getObject("nota") == null ? null : rs.getDouble("nota")); // pode ser nulo
        return new Midia(
            rs.getInt("id"),
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getString("tipo"),      // ENUM('FILME','LIVRO')
            ano,
            nota,
            rs.getString("sinopse"),
            rs.getString("poster_url")
        );
    }

    // ===================== CRUD E BUSCAS SIMPLES =====================

    public List<Midia> listarTodas() throws Exception {
        String sql = "SELECT id, titulo, autor, tipo, ano, nota, sinopse, poster_url " +
                     "FROM midia ORDER BY id";
        List<Midia> lista = new ArrayList<>();
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapper(rs));
        }
        return lista;
    }

    public List<Midia> buscarPorTitulo(String termo) throws Exception {
        String sql = "SELECT id, titulo, autor, tipo, ano, nota, sinopse, poster_url " +
                     "FROM midia WHERE LOWER(titulo) LIKE LOWER(?) ORDER BY id";
        List<Midia> lista = new ArrayList<>();
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + termo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapper(rs));
            }
        }
        return lista;
    }

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

    public void atualizarNota(int id, Double nota) throws Exception {
        String sql = "UPDATE midia SET nota = ? WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (nota == null) ps.setNull(1, Types.DECIMAL);
            else ps.setDouble(1, nota);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void inserir(Midia m) throws Exception {
        String sql = "INSERT INTO midia (titulo, autor, tipo, ano, nota, sinopse, poster_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getTitulo());
            ps.setString(2, m.getAutor());
            ps.setString(3, m.getTipo());
            ps.setInt(4, m.getAno());
            if (m.getNota() == null) ps.setNull(5, Types.DECIMAL);
            else ps.setDouble(5, m.getNota());
            ps.setString(6, m.getSinopse());
            ps.setString(7, m.getPosterUrl());
            ps.executeUpdate();
        }
    }

    public void atualizar(Midia m) throws Exception {
        String sql = "UPDATE midia SET titulo=?, autor=?, tipo=?, ano=?, nota=?, sinopse=?, poster_url=? " +
                     "WHERE id=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getTitulo());
            ps.setString(2, m.getAutor());
            ps.setString(3, m.getTipo());
            ps.setInt(4, m.getAno());
            if (m.getNota() == null) ps.setNull(5, Types.DECIMAL);
            else ps.setDouble(5, m.getNota());
            ps.setString(6, m.getSinopse());
            ps.setString(7, m.getPosterUrl());
            ps.setInt(8, m.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM midia WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int contarTotal() throws Exception {
        String sql = "SELECT COUNT(*) AS total FROM midia";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt("total") : 0;
        }
    }

    // ===================== LISTAGEM COM FILTRO/ORDENAÇÃO/PAGINAÇÃO =====================

    public int contar(String q, String tipo) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS total FROM midia WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (q != null && !q.isBlank()) {
            sql.append(" AND titulo LIKE ?");
            params.add("%" + q.trim() + "%");
        }
        if (tipo != null && !tipo.isBlank()) {
            if ("FILME".equalsIgnoreCase(tipo) || "LIVRO".equalsIgnoreCase(tipo)) {
                sql.append(" AND tipo = ?");
                params.add(tipo.toUpperCase());
            }
        }

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            int idx = 1;
            for (Object p : params) {
                if (p instanceof String s) ps.setString(idx++, s);
                else if (p instanceof Integer i) ps.setInt(idx++, i);
                else if (p instanceof Double d) ps.setDouble(idx++, d);
                else if (p == null) ps.setNull(idx++, Types.NULL);
                else ps.setObject(idx++, p);
            }
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new SQLException("Erro em contar() SQL=[" + sql + "] params=" + params, e);
        }
    }

    public List<Midia> listar(String q, String tipo, String sort, String dir, int offset, int limit) throws Exception {
        String sortCol = switch (sort) {
            case "titulo", "autor", "tipo", "ano", "nota", "id" -> sort;
            default -> "id";
        };
        String direction = "DESC".equalsIgnoreCase(dir) ? "DESC" : "ASC";

        StringBuilder sql = new StringBuilder(
            "SELECT id, titulo, autor, tipo, ano, nota, sinopse, poster_url FROM midia WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (q != null && !q.isBlank()) {
            sql.append(" AND titulo LIKE ?");
            params.add("%" + q.trim() + "%");
        }
        if (tipo != null && !tipo.isBlank()) {
            if ("FILME".equalsIgnoreCase(tipo) || "LIVRO".equalsIgnoreCase(tipo)) {
                sql.append(" AND tipo = ?");
                params.add(tipo.toUpperCase());
            }
        }

        sql.append(" ORDER BY ").append(sortCol).append(' ').append(direction)
           .append(" LIMIT ? OFFSET ?");

        params.add(limit);
        params.add(offset);

        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            int idx = 1;
            for (Object p : params) {
                if (p instanceof String s) ps.setString(idx++, s);
                else if (p instanceof Integer i) ps.setInt(idx++, i);
                else if (p instanceof Double d) ps.setDouble(idx++, d);
                else if (p == null) ps.setNull(idx++, Types.NULL);
                else ps.setObject(idx++, p);
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<Midia> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapper(rs));
                return lista;
            }
        } catch (SQLException e) {
            throw new SQLException("Erro em listar() SQL=[" + sql + "] params=" + params, e);
        }
    }
}
