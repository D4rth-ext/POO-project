package controller;

import dao.EventoDAO;
import model.Evento;
import java.util.List;

/**
 * Controller responsável pelas operações e lógica de eventos.
 */
public class EventoController {

    private final EventoDAO eventoDAO;

    public EventoController() {
        this.eventoDAO = new EventoDAO();
    }

    /**
     * Cadastra um novo evento após validação básica.
     */
    public boolean cadastrarEvento(Evento evento) {
        if (evento.getTitulo() == null || evento.getTitulo().isEmpty()) {
            System.out.println("Erro: Título do evento não pode ser vazio.");
            return false;
        }

        return eventoDAO.inserirEvento(evento);
    }

    public boolean participarEvento(int idUsuario, int idEvento) {
        return eventoDAO.adicionarParticipante(idUsuario, idEvento);
    }

    /**
     * Exclui um evento pelo ID.
     * Adicionamos try-catch para capturar falhas de conexão ou outras exceções
     * que impedem o DAO de imprimir o erro.
     */
    public boolean excluirEvento(int idEvento) {
        try {
            // Repassa a chamada para o DAO
            return eventoDAO.excluirEvento(idEvento);
        } catch (Exception e) {
            // Isso garante que qualquer erro (incluindo falha de conexão) seja visto.
            System.err.println("-----------------------------------------------------------------");
            System.err.println("ERRO INESPERADO NO CONTROLLER AO EXCLUIR O EVENTO (ID: " + idEvento + "):");
            e.printStackTrace();
            System.err.println("-----------------------------------------------------------------");
            return false;
        }
    }

    /**
     * Atualiza um evento existente.
     */
    public boolean atualizarEvento(Evento evento) {
        return eventoDAO.atualizarEvento(evento);
    }

    /**
     * Busca um evento pelo ID.
     */
    public Evento buscarEventoPorId(int id) {
        return eventoDAO.buscarPorId(id);
    }

    /**
     * Lista todos os eventos disponíveis.
     */
    public List<Evento> listarEventos() {
        return eventoDAO.listarTodos();
    }

    /**
     * Lista eventos criados por um usuário específico.
     */
    public List<Evento> listarEventosPorCriador(int idCriador) {
        return eventoDAO.buscarEventosPorCriador(idCriador);
    }
}