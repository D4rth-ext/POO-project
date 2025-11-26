package dao;

import model.Tag;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerenciar as relações entre usuários e tags.
 */
public class UsuarioTagDAO {

    /**
     * Adiciona uma tag a um usuário.
     * @param idUsuario ID do usuário.
     * @param idTag ID da tag.
     * @return true se inserido com sucesso.
     */
    public boolean adicionarTagAoUsuario(int idUsuario, int idTag) {
        String sql = "INSERT INTO usuario_tag (id_usuario, id_tag) VALUES (?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idTag);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            // Evita erro de duplicidade (PK repetida)
            if (e.getMessage().contains("Duplicate")) {
                System.out.println("Tag já vinculada a este usuário.");
            } else {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Remove uma tag de um usuário.
     * @param idUsuario ID do usuário.
     * @param idTag ID da tag.
     * @return true se removido com sucesso.
     */
    public boolean removerTagDoUsuario(int idUsuario, int idTag) {
        String sql = "DELETE FROM usuario_tag WHERE id_usuario = ? AND id_tag = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idTag);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lista todas as tags associadas a um usuário.
     * @param idUsuario ID do usuário.
     * @return Lista de objetos Tag.
     */
    public List<Tag> listarTagsPorUsuario(int idUsuario) {
        String sql = "SELECT t.id_tag, t.nome_tag FROM tag t " +
                "JOIN usuario_tag ut ON t.id_tag = ut.id_tag " +
                "WHERE ut.id_usuario = ?";
        List<Tag> tags = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Tag tag = new Tag();
                tag.setId(rs.getInt("id_tag"));
                tag.setNome(rs.getString("nome_tag"));
                tags.add(tag);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tags;
    }

    /**
     * Remove todas as tags associadas a um usuário (ex: ao deletar conta).
     * @param idUsuario ID do usuário.
     * @return true se remoção ocorreu sem erros.
     */
    public boolean removerTodasAsTagsDoUsuario(int idUsuario) {
        String sql = "DELETE FROM usuario_tag WHERE id_usuario = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}