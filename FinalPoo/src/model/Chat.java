package model;

import java.sql.Timestamp;

/**
 * Representa uma mensagem enviada entre usuários ou em um evento.
 */

public class Chat {
    private int idMensagem;
    private int idRemetente;
    private Integer idReceptor;
    private Integer idEvento;
    private String conteudo;
    private Timestamp dataEnvio;

    public int getIdMensagem() { return idMensagem; }
    public void setIdMensagem(int idMensagem) { this.idMensagem = idMensagem; }

    public int getIdRemetente() { return idRemetente; }
    public void setIdRemetente(int idRemetente) { this.idRemetente = idRemetente; }

    public Integer getIdReceptor() { return idReceptor; }
    public void setIdReceptor(Integer idReceptor) { this.idReceptor = idReceptor; }

    public Integer getIdEvento() { return idEvento; }
    public void setIdEvento(Integer idEvento) { this.idEvento = idEvento; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public Timestamp getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(Timestamp dataEnvio) { this.dataEnvio = dataEnvio; }
}