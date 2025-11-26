package controller;

import dao.ParticipanteEventoDAO;
import java.util.List;

/**
 * Controller responsável por gerenciar participações em eventos.
 */
public class ParticipanteEventoController {

    private ParticipanteEventoDAO dao;


    public ParticipanteEventoController() {
        this.dao = new ParticipanteEventoDAO();
    }

    public String solicitarParticipacao(int idEvento, int idUsuario) {
        if (dao.jaSolicitou(idEvento, idUsuario)) {
            return "Você já solicitou participação neste evento.";
        }

        boolean sucesso = dao.solicitarParticipacao(idEvento, idUsuario);
        return sucesso ? "Solicitação enviada com sucesso." : "Falha ao enviar solicitação.";
    }

    public String aceitarParticipacao(int idEvento, int idUsuario) {
        boolean sucesso = dao.aceitarParticipacao(idEvento, idUsuario);
        return sucesso ? "Participação aceita." : "Falha ao aceitar participação.";
    }

    public String recusarParticipacao(int idEvento, int idUsuario) {
        boolean sucesso = dao.recusarParticipacao(idEvento, idUsuario);
        return sucesso ? "Participação recusada." : "Falha ao recusar participação.";
    }

    public List<Integer> listarParticipantes(int idEvento) {
        return dao.listarParticipantes(idEvento);
    }

    public int contarParticipantesAceitos(int idEvento) {
        return dao.contarParticipantesAceitos(idEvento);
    }



    public boolean participarEvento(int idUsuario, int idEvento) {
        // Por enquanto, apenas repassa a chamada para a camada DAO.
        return dao.adicionarParticipante(idUsuario, idEvento);
    }
}