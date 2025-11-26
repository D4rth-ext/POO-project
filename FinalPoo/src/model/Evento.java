package model;

import java.sql.Timestamp;

/**
 * Representa um evento criado por um usuário.
 */
public class Evento {

    private int idEvento;
    private int idCriador;
    private String titulo;
    private String descricao;
    private int id;

    private Timestamp dataEvento;

    private String local;
    private int limiteParticipantes;
    private String visibilidade;

    // Getters e Setters
    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public int getIdCriador() {
        return idCriador;
    }

    public void setIdCriador(int idCriador) {
        this.idCriador = idCriador;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public Timestamp getDataEvento() {
        return dataEvento;
    }


    public void setDataEvento(Timestamp dataEvento) {
        this.dataEvento = dataEvento;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getLimiteParticipantes() {
        return limiteParticipantes;
    }

    public void setLimiteParticipantes(int limiteParticipantes) {
        this.limiteParticipantes = limiteParticipantes;
    }

    public String getVisibilidade() {
        return visibilidade;
    }

    public void setVisibilidade(String visibilidade) {
        this.visibilidade = visibilidade;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


}