package dao;

import model.Evento;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {

    public boolean inserirEvento(Evento evento) {
        String sql = "INSERT INTO evento (id_criador, titulo, descricao, data_evento, local, limite_participantes, visibilidade) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, evento.getIdCriador());
            stmt.setString(2, evento.getTitulo());
            stmt.setString(3, evento.getDescricao());
            stmt.setTimestamp(4, evento.getDataEvento());
            stmt.setString(5, evento.getLocal());
            stmt.setInt(6, evento.getLimiteParticipantes());
            stmt.setString(7, evento.getVisibilidade());

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    evento.setIdEvento(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean atualizarEvento(Evento evento) {
        String sql = "UPDATE evento SET titulo=?, descricao=?, data_evento=?, local=?, limite_participantes=?, visibilidade=? " +
                "WHERE id_evento=?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, evento.getTitulo());
            stmt.setString(2, evento.getDescricao());
            stmt.setTimestamp(3, evento.getDataEvento());
            stmt.setString(4, evento.getLocal());
            stmt.setInt(5, evento.getLimiteParticipantes());
            stmt.setString(6, evento.getVisibilidade());
            stmt.setInt(7, evento.getIdEvento());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public Evento buscarPorId(int idEvento) {
        String sql = "SELECT * FROM evento WHERE id_evento = ?";
        Evento evento = null;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEvento);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                evento = criarEvento(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return evento;
    }


    public List<Evento> listarTodos() {
        String sql = "SELECT * FROM evento";
        List<Evento> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(criarEvento(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Evento> listarPorUsuario(int idCriador) {
        String sql = "SELECT * FROM evento WHERE id_criador = ?";
        List<Evento> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCriador);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(criarEvento(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Evento criarEvento(ResultSet rs) throws SQLException {
        Evento evento = new Evento();
        evento.setIdEvento(rs.getInt("id_evento"));
        evento.setIdCriador(rs.getInt("id_criador"));
        evento.setTitulo(rs.getString("titulo"));
        evento.setDescricao(rs.getString("descricao"));
        evento.setDataEvento(rs.getTimestamp("data_evento"));
        evento.setLocal(rs.getString("local"));
        evento.setLimiteParticipantes(rs.getInt("limite_participantes"));
        evento.setVisibilidade(rs.getString("visibilidade"));
        return evento;
    }

    public List<Evento> buscarEventosPorCriador(int idCriador) {
        String sql = "SELECT * FROM evento WHERE id_criador = ?";
        List<Evento> eventos = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCriador);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                eventos.add(criarEvento(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventos;
    }

    public List<Evento> buscarPorNomeOuDescricao(String termo) {
        String sql = "SELECT * FROM evento WHERE titulo LIKE ? OR descricao LIKE ?";
        List<Evento> eventos = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String busca = "%" + termo + "%";
            stmt.setString(1, busca);
            stmt.setString(2, busca);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                eventos.add(criarEvento(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventos;
    }

    public boolean adicionarParticipante(int idUsuario, int idEvento) {
        String sql = "INSERT INTO participante_evento (id_usuario, id_evento, data_inscricao) VALUES (?, ?, NOW())";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idEvento);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao registrar participação no evento: " + e.getMessage());
            return false;
        }
    }


    /**
     * Exclui um evento, incluindo suas participações, em uma única transação.
     * @param idEvento ID do evento a ser excluído.
     * @return true se a exclusão foi bem-sucedida, false caso contrário.
     */
    public boolean excluirEvento(int idEvento) {
        String sql = "DELETE FROM evento WHERE id_evento = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEvento);
            int linhasAfetadas = stmt.executeUpdate();


            return linhasAfetadas > 0;

        } catch (SQLException e) {


            System.err.println("ERRO FATAL (SQL/CONEXÃO) AO EXCLUIR O EVENTO (ID: " + idEvento + "):");
            e.printStackTrace();
            return false;
        }

    }}