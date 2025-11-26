package model;

import java.util.List;

public class Usuario {
    private int idUsuario;
    private String nome;
    private String senha;
    private String email;
    private String sexo;
    private String genero;
    private String telefone;
    private String sobre_mim;
    private byte[] foto_perfil;
    private byte[] foto_capa;
    private List<String> tags;
    private List<String> links;

    public Usuario() {}

    public Usuario(int idUsuario, String nome, String senha, String email, String sexo, String genero,
                   String telefone, String sobre_mim, byte[] foto_perfil, byte[] foto_capa,
                   List<String> tags, List<String> links) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.sexo = sexo;
        this.genero = genero;
        this.telefone = telefone;
        this.sobre_mim = sobre_mim;
        this.foto_perfil = foto_perfil;
        this.foto_capa = foto_capa;
        this.tags = tags;
        this.links = links;
    }

    // Getters e setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getSobre_mim() { return sobre_mim; }
    public void setSobre_mim(String sobre_mim) { this.sobre_mim = sobre_mim; }

    public byte[] getFoto_perfil() { return foto_perfil; }
    public void setFoto_perfil(byte[] foto_perfil) { this.foto_perfil = foto_perfil; }

    public byte[] getFoto_capa() { return foto_capa; }
    public void setFoto_capa(byte[] foto_capa) { this.foto_capa = foto_capa; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public List<String> getLinks() { return links; }
    public void setLinks(List<String> links) { this.links = links; }
}