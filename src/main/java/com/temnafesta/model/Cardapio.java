package com.temnafesta.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "cardapio")
public class Cardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    private String observacoes;

    @Column(nullable = false)
    private Boolean isAtivo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "campanha_id")
    private Campanha campanha;

    //Getters e Setters
    public Campanha getCampanha() {return campanha;}
    public void setCampanha(Campanha campanha) {this.campanha = campanha;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getObservacoes() {return observacoes;}
    public void setObservacoes(String observacoes) {this.observacoes = observacoes;}

    public Boolean getAtivo() {return isAtivo;}
    public void setAtivo(Boolean ativo) {isAtivo = ativo;}

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
}
