package controller;

import dao.AmizadeDAO;
import model.Amizade;
import java.util.List;

/**
 * Controller responsável pela lógica de negócios de amizades e solicitações.
 */
public class AmizadeController {

    private final AmizadeDAO amizadeDAO;

    public AmizadeController() {
        this.amizadeDAO = new AmizadeDAO();
    }

    /**
     * Tenta enviar uma solicitação de amizade, aplicando validações.
     * * @param idSolicitante ID do usuário que envia a solicitação.
     * @param idReceptor ID do usuário que recebe a solicitação.
     * @return true se a solicitação foi enviada com sucesso, false caso contrário.
     */
    public boolean enviarSolicitacao(int idSolicitante, int idReceptor) {
        // 1. Evita que o usuário envie solicitação para si mesmo
        if (idSolicitante == idReceptor) {
            System.err.println("Erro: O usuário não pode enviar solicitação para si mesmo.");
            return false;
        }

        // 2. Verifica se já existe uma relação (pendente ou aceita) em qualquer direção
        if (amizadeDAO.jaExisteSolicitacao(idSolicitante, idReceptor)) {
            System.err.println("Erro: Já existe uma solicitação ou amizade entre estes usuários.");
            return false;
        }

        // 3. Envia a solicitação
        return amizadeDAO.enviarSolicitacao(idSolicitante, idReceptor);
    }

    /**
     * Aceita uma solicitação de amizade.
     */
    public boolean aceitarSolicitacao(int idSolicitante, int idReceptor) {
        return amizadeDAO.aceitarSolicitacao(idSolicitante, idReceptor);
    }

    /**
     * Recusa uma solicitação de amizade.
     */
    public boolean recusarSolicitacao(int idSolicitante, int idReceptor) {
        return amizadeDAO.recusarSolicitacao(idSolicitante, idReceptor);
    }

    /**
     * Lista todas as solicitações pendentes que um usuário recebeu.
     */
    public List<Amizade> listarPendentes(int idUsuario) {
        return amizadeDAO.listarPendentes(idUsuario);
    }

    /**
     * Lista todas as amizades aceitas de um usuário.
     */
    public List<Amizade> listarAmigos(int idUsuario) {
        return amizadeDAO.listarAmigos(idUsuario);
    }
}