package teste;

import dao.EventoDAO;
import model.Evento;
// ✅ CORREÇÃO: Altera o import de Date para Timestamp
import java.sql.Timestamp;

public class TesteEvento {
    public static void main(String[] args) {
        EventoDAO dao = new EventoDAO();

        Evento evento = new Evento();
        evento.setIdCriador(1); // id_usuario válido
        evento.setTitulo("Teste de DAO");
        evento.setDescricao("Evento de teste para verificar DAO");

        // ✅ CORREÇÃO: Usa Timestamp.valueOf() com o formato yyyy-MM-dd HH:mm:ss
        evento.setDataEvento(Timestamp.valueOf("2025-11-30 08:00:00"));

        evento.setLocal("Local de teste");
        evento.setLimiteParticipantes(10);
        evento.setVisibilidade("PUBLICO");

        // Testar inserção
        if (dao.inserirEvento(evento)) {
            System.out.println("Evento inserido com ID: " + evento.getIdEvento());
        } else {
            System.out.println("Erro ao inserir evento");
        }

        // Buscar e imprimir
        Evento e = dao.buscarPorId(evento.getIdEvento());
        System.out.println("Buscado: " + (e != null ? e.getTitulo() : "Evento não encontrado"));

        // Atualizar
        if (e != null) {
            e.setTitulo("Evento Atualizado");
            System.out.println("Atualização: " + (dao.atualizarEvento(e) ? "OK" : "Falhou"));
        }

        // Listar todos
        System.out.println("\n Lista de eventos:");
        dao.listarTodos().forEach(ev -> System.out.println(ev.getIdEvento() + " - " + ev.getTitulo()));

        // Deletar
        System.out.println("\n Exclusão: " + (dao.excluirEvento(evento.getIdEvento()) ? "OK" : "Falhou"));
    }
}