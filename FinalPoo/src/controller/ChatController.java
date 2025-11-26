package controller;

import dao.ChatDAO;
import model.Chat;

import java.util.List;

/**
 * Controlador responsável por gerenciar os chats — tanto entre usuários
 * quanto dentro de eventos.
 */
public class ChatController {
    private ChatDAO chatDAO;

    public ChatController() {
        this.chatDAO = new ChatDAO();
    }

    /**
     * Envia uma mensagem.
     *
     * @param idRemetente ID do usuário que está enviando
     * @param idReceptor  ID do usuário que irá receber (ou null se for mensagem de evento)
     * @param idEvento    ID do evento (ou null se for mensagem privada)
     * @param conteudo    Texto da mensagem
     * @return true se enviada com sucesso, false caso contrário
     */
    public boolean enviarMensagem(int idRemetente, Integer idReceptor, Integer idEvento, String conteudo) {
        if (conteudo == null || conteudo.trim().isEmpty()) {
            System.out.println("Mensagem vazia não pode ser enviada.");
            return false;
        }

        return chatDAO.enviarMensagem(idRemetente, idReceptor, idEvento, conteudo);
    }

    /**
     * Lista o histórico de mensagens entre dois usuários.
     *
     * @param idUsuario1 Primeiro usuário
     * @param idUsuario2 Segundo usuário
     * @return Lista de mensagens trocadas
     */
    public List<Chat> listarMensagensEntreUsuarios(int idUsuario1, int idUsuario2) {
        return chatDAO.listarMensagensEntreUsuarios(idUsuario1, idUsuario2);
    }

    /**
     * Lista todas as mensagens enviadas em um evento.
     *
     * @param idEvento ID do evento
     * @return Lista de mensagens do evento
     */
    public List<Chat> listarMensagensDoEvento(int idEvento) {
        return chatDAO.listarMensagensDoEvento(idEvento);
    }
}