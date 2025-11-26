package dao;

import model.Chat;
import util.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável por gerenciar as mensagens entre usuários e nos eventos.
 */
public class ChatDAO {

    /**
     * Envia uma mensagem (entre usuários ou em um evento).
     */
    public boolean enviarMensagem(int idRemetente, Integer idReceptor, Integer idEvento, String conteudo) {
        String sql = "INSERT INTO chat (id_remetente, id_receptor, id_evento, conteudo) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRemetente);
            if (idReceptor != null)
                stmt.setInt(2, idReceptor);
            else
                stmt.setNull(2, Types.INTEGER);

            if (idEvento != null)
                stmt.setInt(3, idEvento);
            else
                stmt.setNull(3, Types.INTEGER);

            stmt.setString(4, conteudo);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao enviar mensagem: " + e.getMessage());
        }

        return false;
    }

    /**
     * Lista todas as mensagens trocadas entre dois usuários.
     */
    public List<Chat> listarMensagensEntreUsuarios(int idUsuario1, int idUsuario2) {
        String sql = """
                SELECT * FROM chat
                WHERE ((id_remetente = ? AND id_receptor = ?)
                    OR (id_remetente = ? AND id_receptor = ?))
                  AND id_evento IS NULL
                ORDER BY data_envio
                """;

        List<Chat> mensagens = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario1);
            stmt.setInt(2, idUsuario2);
            stmt.setInt(3, idUsuario2);
            stmt.setInt(4, idUsuario1);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                mensagens.add(criarMensagem(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar mensagens entre usuários: " + e.getMessage());
        }

        return mensagens;
    }

    /**
     * Lista todas as mensagens enviadas em um evento.
     */
    public List<Chat> listarMensagensDoEvento(int idEvento) {
        String sql = "SELECT * FROM chat WHERE id_evento = ? ORDER BY data_envio";
        List<Chat> mensagens = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEvento);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                mensagens.add(criarMensagem(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar mensagens do evento: " + e.getMessage());
        }

        return mensagens;
    }

    private Chat criarMensagem(ResultSet rs) throws SQLException {
        Chat msg = new Chat();
        msg.setIdMensagem(rs.getInt("id_chat"));
        msg.setIdRemetente(rs.getInt("id_remetente"));
        msg.setIdReceptor((Integer) rs.getObject("id_receptor"));
        msg.setIdEvento((Integer) rs.getObject("id_evento"));
        msg.setConteudo(rs.getString("conteudo"));
        msg.setDataEnvio(rs.getTimestamp("data_envio"));
        return msg;
    }
}