package dao;

import model.Tag;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagDAO {

    // Inserir nova tag
    public void inserir(Tag tag) throws SQLException {
        String sql = "INSERT INTO tag (nome_tag) VALUES (?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tag.getNome());
            stmt.executeUpdate();
        }
    }

    // Buscar por ID
    public Tag buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tag WHERE id_tag = ?";
        Tag tag = null;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tag = new Tag();
                tag.setId(rs.getInt("id_tag"));
                tag.setNome(rs.getString("nome_tag"));
            }
        }
        return tag;
    }

    // Buscar por nome
    public Tag buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM tag WHERE nome_tag = ?";
        Tag tag = null;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tag = new Tag();
                tag.setId(rs.getInt("id_tag"));
                tag.setNome(rs.getString("nome_tag"));
            }
        }
        return tag;
    }

    // Listar todas as tags
    public List<Tag> listarTodas() throws SQLException {
        String sql = "SELECT * FROM tag ORDER BY nome_tag ASC";
        List<Tag> tags = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Tag tag = new Tag();
                tag.setId(rs.getInt("id_tag"));
                tag.setNome(rs.getString("nome_tag"));
                tags.add(tag);
            }
        }
        return tags;
    }

    // Atualizar tag
    public void atualizar(Tag tag) throws SQLException {
        String sql = "UPDATE tag SET nome_tag = ? WHERE id_tag = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tag.getNome());
            stmt.setInt(2, tag.getId());
            stmt.executeUpdate();
        }
    }

    // Deletar tag
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM tag WHERE id_tag = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}