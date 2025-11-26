package dao;

import util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipanteEventoDAO {

    /**
     * Solicita participação em um evento.
     */
    public boolean solicitarParticipacao(int idEvento, int idUsuario) {
        String sql = "INSERT INTO participante_evento (id_evento, id_usuario, status) VALUES (?, ?, 'PENDENTE')";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEvento);
            stmt.setInt(2, idUsuario);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Aceita a participação de um usuário em um evento.
     * Deve ser chamado apenas pelo criador do evento.
     */
    public boolean aceitarParticipacao(int idEvento, int idUsuario) {
        String sql = "UPDATE participante_evento SET status = 'ACEITO' WHERE id_evento = ? AND id_usuario = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEvento);
            stmt.setInt(2, idUsuario);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Recusa a participação de um usuário em um evento.
     */
    public boolean recusarParticipacao(int idEvento, int idUsuario) {
        String sql = "UPDATE participante_evento SET status = 'RECUSADO' WHERE id_evento = ? AND id_usuario = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEvento);
            stmt.setInt(2, idUsuario);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lista todos os participantes de um evento.
     */
    public List<Integer> listarParticipantes(int idEvento) {
        String sql = "SELECT id_usuario FROM participante_evento WHERE id_evento = ? AND status = 'ACEITO'";
        List<Integer> participantes = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEvento);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                participantes.add(rs.getInt("id_usuario"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return participantes;
    }

    /**
     * Conta o número de participantes aceitos em um evento.
     */
    public int contarParticipantesAceitos(int idEvento) {
        String sql = "SELECT COUNT(*) FROM participante_evento WHERE id_evento = ? AND status = 'ACEITO'";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEvento);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Verifica se um usuário já solicitou participação.
     */
    public boolean jaSolicitou(int idEvento, int idUsuario) {
        String sql = "SELECT COUNT(*) FROM participante_evento WHERE id_evento = ? AND id_usuario = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEvento);
            stmt.setInt(2, idUsuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean adicionarParticipante(int idUsuario, int idEvento) {

        String sql = "INSERT INTO participante_evento (id_usuario, id_evento, data_inscricao) VALUES (?, ?, NOW())";

        try (Connection conn = Conexao.getConexao(); // Obtém a conexão
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepara o SQL


            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idEvento);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0; // Retorna true se a inserção ocorreu

        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("ParticipanteEventoDAO: O usuário (ID " + idUsuario +
                    ") já está inscrito no evento (ID " + idEvento + ").");
            return false;

        } catch (SQLException e) {
            System.err.println("Erro inesperado ao registrar participação no evento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
