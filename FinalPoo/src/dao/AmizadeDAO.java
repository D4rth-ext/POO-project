package dao;

import model.Amizade;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável por gerenciar solicitações e amizades entre usuários.
 */
public class AmizadeDAO {

    /**
     * Envia uma solicitação de amizade.
     */
    public boolean enviarSolicitacao(int idSolicitante, int idReceptor) {
        String sql = "INSERT INTO amizade (id_solicitante, id_receptor, status, data_solicitacao) VALUES (?, ?, 'PENDENTE', NOW())";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSolicitante);
            stmt.setInt(2, idReceptor);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Verifica se já existe uma solicitação ou amizade (PENDENTE ou ACEITA) entre dois usuários.
     * Verifica em ambas as direções (A -> B ou B -> A).
     */
    public boolean jaExisteSolicitacao(int idSolicitante, int idReceptor) {
        String sql = """
                SELECT COUNT(*) FROM amizade 
                WHERE (id_solicitante = ? AND id_receptor = ?)
                   OR (id_solicitante = ? AND id_receptor = ?)
                """;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSolicitante);
            stmt.setInt(2, idReceptor);
            stmt.setInt(3, idReceptor);
            stmt.setInt(4, idSolicitante);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Aceita uma solicitação de amizade (Apenas se estiver PENDENTE).
     */
    public boolean aceitarSolicitacao(int idSolicitante, int idReceptor) {
        String sql = "UPDATE amizade SET status = 'ACEITA' WHERE id_solicitante = ? AND id_receptor = ? AND status = 'PENDENTE'";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSolicitante);
            stmt.setInt(2, idReceptor);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Recusa uma solicitação de amizade (Deleta a entrada PENDENTE).
     */
    public boolean recusarSolicitacao(int idSolicitante, int idReceptor) {
        String sql = "DELETE FROM amizade WHERE id_solicitante = ? AND id_receptor = ? AND status = 'PENDENTE'";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idSolicitante);
            stmt.setInt(2, idReceptor);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Lista todas as solicitações pendentes que um usuário recebeu (onde ele é o receptor).
     */
    public List<Amizade> listarPendentes(int idUsuario) {
        String sql = "SELECT * FROM amizade WHERE id_receptor = ? AND status = 'PENDENTE'";
        List<Amizade> pendentes = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                pendentes.add(criarAmizadeDoResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pendentes;
    }

    /**
     * Lista todas as amizades aceitas de um usuário (onde ele é solicitante ou receptor).
     */
    public List<Amizade> listarAmigos(int idUsuario) {
        String sql = """
                SELECT * FROM amizade 
                WHERE status = 'ACEITA' 
                  AND (id_solicitante = ? OR id_receptor = ?)
                """;

        List<Amizade> amigos = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                amigos.add(criarAmizadeDoResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return amigos;
    }

    private Amizade criarAmizadeDoResultSet(ResultSet rs) throws SQLException {
        Amizade amizade = new Amizade();

        amizade.setId(rs.getInt("id_amizade"));

        amizade.setIdSolicitante(rs.getInt("id_solicitante"));
        amizade.setIdReceptor(rs.getInt("id_receptor"));
        amizade.setStatus(rs.getString("status"));
        amizade.setDataSolicitacao(rs.getTimestamp("data_solicitacao"));
        return amizade;
    }
}