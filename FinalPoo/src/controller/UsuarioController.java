package controller;

import dao.UsuarioDAO;
import model.Usuario;
import java.util.List;

/**
 * Controller responsável pela lógica de negócios relacionada a usuários.
 * Faz a ponte entre a camada de interface (ou testes) e o DAO.
 */
public class UsuarioController {

    private final UsuarioDAO usuarioDAO;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Cadastra um novo usuário, com validações básicas e suporte a fotos.
     */
    public boolean cadastrarUsuario(Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            System.out.println("Erro: email não pode ser vazio.");
            return false;
        }

        if (usuarioDAO.buscarPorEmail(usuario.getEmail()) != null) {
            System.out.println("Erro: já existe um usuário com este email.");
            return false;
        }

        // Valida presença (ou ausência) de imagens
        if (usuario.getFoto_perfil() == null) {
            System.out.println("Aviso: o usuário será cadastrado sem foto de perfil.");
        }

        if (usuario.getFoto_capa() == null) {
            System.out.println("Aviso: o usuário será cadastrado sem foto de capa.");
        }

        return usuarioDAO.inserirUsuario(usuario);
    }

    /**
     * Realiza o login verificando email e senha.
     */
    public Usuario login(String email, String senha) {
        Usuario usuario = usuarioDAO.autenticarUsuario(email, senha);

        if (usuario != null) {
            System.out.println("Login bem-sucedido. Bem-vindo, " + usuario.getNome() + "!");
        } else {
            System.out.println("Email ou senha incorretos.");
        }

        return usuario;
    }

    /**
     * Atualiza os dados de um usuário (com ou sem imagens).
     */
    public boolean atualizarUsuario(Usuario usuario) {
        if (usuario == null) {
            System.out.println("Erro: objeto usuário é nulo.");
            return false;
        }

        return usuarioDAO.atualizarUsuario(usuario);
    }

    /**
     * Atualiza apenas as fotos (perfil e capa) de um usuário.
     */
    public boolean atualizarFotosUsuario(int idUsuario, byte[] novaFotoPerfil, byte[] novaFotoCapa) {
        Usuario usuario = usuarioDAO.buscarPorId(idUsuario);
        if (usuario == null) {
            System.out.println("Erro: usuário não encontrado.");
            return false;
        }

        usuario.setFoto_perfil(novaFotoPerfil);
        usuario.setFoto_capa(novaFotoCapa);
        return usuarioDAO.atualizarUsuario(usuario);
    }

    /**
     * Retorna a foto de perfil de um usuário.
     */
    public byte[] obterFotoPerfil(int idUsuario) {
        Usuario usuario = usuarioDAO.buscarPorId(idUsuario);
        return usuario != null ? usuario.getFoto_perfil() : null;
    }

    /**
     * Retorna a foto de capa de um usuário.
     */
    public byte[] obterFotoCapa(int idUsuario) {
        Usuario usuario = usuarioDAO.buscarPorId(idUsuario);
        return usuario != null ? usuario.getFoto_capa() : null;
    }

    /**
     * Lista todos os usuários cadastrados.
     * (Esta é a única versão do método que deve existir)
     */
    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarTodos();
    }

    /**
     * Exclui um usuário do banco de dados.
     */
    public boolean excluirUsuario(int id) {
        return usuarioDAO.deletarUsuario(id);
    }

    /**
     * Busca usuários por nome ou telefone.
     */
    public List<Usuario> buscarPorNomeOuTelefone(String termo) {
        return usuarioDAO.buscarPorNomeOuTelefone(termo);
    }

    /**
     * Busca usuários por tag.
     */
    public List<Usuario> buscarPorTag(String nomeTag) {
        return usuarioDAO.buscarPorTag(nomeTag);
    }

    /**
     * Busca um usuário pelo ID.
     * @param id O ID do usuário a ser buscado.
     * @return O objeto Usuario com seus dados (incluindo tags), ou null se não encontrado.
     */
    public Usuario buscarUsuarioPorId(int id) {
        // Chamada direta ao DAO.
        return usuarioDAO.buscarPorId(id);
    }
}