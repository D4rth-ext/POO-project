package model;

/**
 * Representa um link externo de um usuário (ex: Spotify, YouTube, Instagram, etc.).
 */
public class UsuarioLink {
    private int idLink;
    private int idUsuario;
    private String tipo; // Ex: "Spotify", "Instagram", etc.
    private String url;

    public int getIdLink() {
        return idLink;
    }

    public void setIdLink(int idLink) {
        this.idLink = idLink;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}