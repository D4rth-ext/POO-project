package util;

import dao.UsuarioDAO;
import model.Usuario;

public class TesteUsuario {
    public static void main(String[] args) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        // Usuário 1
        Usuario u1 = new Usuario();
        u1.setNome("João Silva");
        u1.setSexo("Masculino");
        u1.setGenero("Heterossexual");
        u1.setTelefone("11999999999");
        u1.setSobre_mim("Gosto de eventos de tecnologia.");
        u1.setEmail("joao@email.com");
        u1.setSenha("123456"); // será criptografada automaticamente

        // Usuário 2
        Usuario u2 = new Usuario();
        u2.setNome("Maria Oliveira");
        u2.setSexo("Feminino");
        u2.setGenero("Heterossexual");
        u2.setTelefone("11988888888");
        u2.setSobre_mim("Adoro shows e eventos musicais.");
        u2.setEmail("maria@email.com");
        u2.setSenha("senhaSegura"); // será criptografada automaticamente eu acho

        // Inserir no banco
        usuarioDAO.inserirUsuario(u1);
        usuarioDAO.inserirUsuario(u2);

        System.out.println("Usuários inseridos!");
    }
}
