package dao;

import model.UsuarioLink;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelas operações de CRUD na tabela 'usuario_link'.
 * Cada registro representa um link associado a um usuário.
 */
public class UsuarioLinkDAO {

    /**
     * Adiciona um novo link para o usuário.
     * @param link Objeto UsuarioLink com os dados a inserir.
     * @return true se inserido com sucesso, false caso contrário.
     */
    public boolean inserirLink(UsuarioLink link) {
        String sql = "INSERT INTO usuario_link (id_usuario, tipo, url) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, link.getIdUsuario());
            stmt.setString(2, link.getTipo());
            stmt.setString(3, link.getUrl());

            int linhas = stmt.executeUpdate();
            if (linhas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    link.setIdLink(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir link: " + e.getMessage());
        }

        return false;
    }

    /**
     * Atualiza um link existente.
     * @param link Objeto com dados atualizados.
     * @return true se atualizado com sucesso, false caso contrário.
     */
    public boolean atualizarLink(UsuarioLink link) {
        String sql = "UPDATE usuario_link SET tipo = ?, url = ? WHERE id_link = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, link.getTipo());
            stmt.setString(2, link.getUrl());
            stmt.setInt(3, link.getIdLink());

            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar link: " + e.getMessage());
        }

        return false;
    }

    /**
     * Deleta um link específico.
     * @param idLink ID do link a ser deletado.
     * @return true se deletado com sucesso, false caso contrário.
     */
    public boolean deletarLink(int idLink) {
        String sql = "DELETE FROM usuario_link WHERE id_link = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idLink);
            int linhas = stmt.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar link: " + e.getMessage());
        }

        return false;
    }

    /**
     * Lista todos os links de um usuário específico.
     * @param idUsuario ID do usuário.
     * @return Lista de objetos UsuarioLink.
     */
    public List<UsuarioLink> listarPorUsuario(int idUsuario) {
        String sql = "SELECT * FROM usuario_link WHERE id_usuario = ?";
        List<UsuarioLink> links = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                links.add(criarUsuarioLinkDoResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar links: " + e.getMessage());
        }

        return links;
    }

    /**
     * Retorna um objeto UsuarioLink a partir de uma linha do ResultSet.
     */
    private UsuarioLink criarUsuarioLinkDoResultSet(ResultSet rs) throws SQLException {
        UsuarioLink link = new UsuarioLink();
        link.setIdLink(rs.getInt("id_link"));
        link.setIdUsuario(rs.getInt("id_usuario"));
        link.setTipo(rs.getString("tipo"));
        link.setUrl(rs.getString("url"));
        return link;
    }
}