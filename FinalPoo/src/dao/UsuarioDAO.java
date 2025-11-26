package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Usuario;
import util.Conexao;
import util.CriptografiaUtil;

/**
 * Classe responsável por fazer a comunicação entre o Java e o banco de dados
 * especificamente para operações da tabela 'usuario'.
 */
public class UsuarioDAO {

    /**
     * Insere um novo usuário no banco de dados.
     * @param usuario Objeto contendo os dados do usuário.
     * @return true se o usuário foi cadastrado com sucesso, false caso contrário.
     */
    public boolean inserirUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, senha, telefone, sexo, genero, sobre_mim, foto_perfil, foto_capa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Criptografa a senha antes de salvar
            String senhaCriptografada = CriptografiaUtil.hashSenha(usuario.getSenha());

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, senhaCriptografada);
            stmt.setString(4, usuario.getTelefone());
            stmt.setString(5, usuario.getSexo());
            stmt.setString(6, usuario.getGenero());
            stmt.setString(7, usuario.getSobre_mim());
            stmt.setBytes(8, usuario.getFoto_perfil());
            stmt.setBytes(9, usuario.getFoto_capa());

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    usuario.setIdUsuario(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir usuário: " + e.getMessage());
        }
        return false;
    }

    /**
     * Busca um usuário pelo email.
     */
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        Usuario usuario = null;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = criarUsuario(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por email: " + e.getMessage());
        }

        return usuario;
    }

    /**
     * Faz login verificando o hash da senha com BCrypt.
     * Retorna o usuário se as credenciais estiverem corretas, null caso contrário.
     */
    public Usuario autenticarUsuario(String email, String senhaDigitada) {
        Usuario usuario = buscarPorEmail(email);

        if (usuario != null && CriptografiaUtil.verificarSenha(senhaDigitada, usuario.getSenha())) {
            return usuario;
        }

        return null;
    }

    /**
     * Atualiza os dados de um usuário.
     */
    public boolean atualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuario SET nome=?, email=?, senha=?, telefone=?, sexo=?, genero=?, sobre_mim=?, foto_perfil=?, foto_capa=? WHERE id_usuario=?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String senhaCriptografada = CriptografiaUtil.hashSenha(usuario.getSenha());

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, senhaCriptografada);
            stmt.setString(4, usuario.getTelefone());
            stmt.setString(5, usuario.getSexo());
            stmt.setString(6, usuario.getGenero());
            stmt.setString(7, usuario.getSobre_mim());
            stmt.setBytes(8, usuario.getFoto_perfil());
            stmt.setBytes(9, usuario.getFoto_capa());
            stmt.setInt(10, usuario.getIdUsuario());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
        }

        return false;
    }

    /**
     * Atualiza apenas as fotos do usuário.
     */
    public boolean atualizarFotos(int idUsuario, byte[] fotoPerfil, byte[] fotoCapa) {
        String sql = "UPDATE usuario SET foto_perfil=?, foto_capa=? WHERE id_usuario=?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBytes(1, fotoPerfil);
            stmt.setBytes(2, fotoCapa);
            stmt.setInt(3, idUsuario);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar fotos: " + e.getMessage());
        }

        return false;
    }

    /**
     * Busca um usuário pelo seu ID.
     */
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        Usuario usuario = null;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = criarUsuario(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
        }

        return usuario;
    }

    /**
     * Lista todos os usuários cadastrados.
     */
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM usuario";
        List<Usuario> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(criarUsuario(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Deleta um usuário do banco de dados pelo seu ID.
     */
    public boolean deletarUsuario(int id) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar usuário: " + e.getMessage());
        }

        return false;
    }

    /**
     * Busca usuários por nome ou telefone.
     */
    public List<Usuario> buscarPorNomeOuTelefone(String termo) {
        String sql = "SELECT * FROM usuario WHERE nome LIKE ? OR telefone LIKE ?";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String busca = "%" + termo + "%";
            stmt.setString(1, busca);
            stmt.setString(2, busca);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                usuarios.add(criarUsuario(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuários: " + e.getMessage());
        }

        return usuarios;
    }

    /**
     * Busca usuários que possuam uma determinada tag.
     */
    public List<Usuario> buscarPorTag(String nomeTag) {
        String sql = """
        SELECT u.* FROM usuario u
        JOIN usuario_tag ut ON u.id_usuario = ut.id_usuario
        JOIN tag t ON ut.id_tag = t.id_tag
        WHERE t.nome LIKE ?
        """;
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nomeTag + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                usuarios.add(criarUsuario(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuários por tag: " + e.getMessage());
        }

        return usuarios;
    }

    /**
     * Converte uma linha do ResultSet em um objeto Usuario.
     */
    private Usuario criarUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setSexo(rs.getString("sexo"));
        usuario.setGenero(rs.getString("genero"));
        usuario.setSobre_mim(rs.getString("sobre_mim"));
        usuario.setFoto_perfil(rs.getBytes("foto_perfil"));
        usuario.setFoto_capa(rs.getBytes("foto_capa"));
        return usuario;
    }
}