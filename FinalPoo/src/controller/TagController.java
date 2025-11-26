package controller;

import dao.TagDAO;
import model.Tag;

import java.sql.SQLException;
import java.util.List;

public class TagController {

    private TagDAO tagDAO;

    public TagController() {
        this.tagDAO = new TagDAO();
    }

    // Cadastrar nova tag
    public boolean cadastrarTag(String nome) {
        try {
            // Evita duplicadas
            if (tagDAO.buscarPorNome(nome) != null) {
                System.out.println("Tag já existe!");
                return false;
            }
            Tag tag = new Tag();
            tag.setNome(nome);
            tagDAO.inserir(tag);
            System.out.println("Tag cadastrada com sucesso!");
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar tag: " + e.getMessage());
            return false;
        }
    }

    // Buscar tag por ID
    public Tag buscarPorId(int id) {
        try {
            return tagDAO.buscarPorId(id);
        } catch (SQLException e) {
            System.out.println("Erro ao buscar tag: " + e.getMessage());
            return null;
        }
    }

    // Listar todas as tags
    public List<Tag> listarTodas() {
        try {
            return tagDAO.listarTodas();
        } catch (SQLException e) {
            System.out.println("Erro ao listar tags: " + e.getMessage());
            return null;
        }
    }

    // Atualizar tag
    public boolean atualizarTag(int id, String novoNome) {
        try {
            Tag tag = tagDAO.buscarPorId(id);
            if (tag == null) {
                System.out.println("Tag não encontrada!");
                return false;
            }
            tag.setNome(novoNome);
            tagDAO.atualizar(tag);
            System.out.println("Tag atualizada com sucesso!");
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar tag: " + e.getMessage());
            return false;
        }
    }

    // Deletar tag
    public boolean deletarTag(int id) {
        try {
            Tag tag = tagDAO.buscarPorId(id);
            if (tag == null) {
                System.out.println("Tag não encontrada!");
                return false;
            }
            tagDAO.deletar(id);
            System.out.println("Tag deletada com sucesso!");
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao deletar tag: " + e.getMessage());
            return false;
        }
    }
}