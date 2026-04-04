package com.temnafesta.dto.cardapio;

public class CardapioResponseDto {

    private Integer id;
    private String nome;
    private String observacoes;
    private Boolean isAtivo;
    private Integer campanhaId;
    private String campanhaNome;

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getObservacoes() {return observacoes;}
    public void setObservacoes(String observacoes) {this.observacoes = observacoes;}

    public Boolean getAtivo() {return isAtivo;}
    public void setAtivo(Boolean ativo) {isAtivo = ativo;}

    public Integer getCampanhaId() {return campanhaId;}
    public void setCampanhaId(Integer campanhaId) {this.campanhaId = campanhaId;}

    public String getCampanhaNome() {return campanhaNome;}
    public void setCampanhaNome(String campanhaNome) {this.campanhaNome = campanhaNome;}
}