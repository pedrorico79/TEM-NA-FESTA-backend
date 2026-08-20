package com.temnafesta.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "cliente")
@Getter
@Setter
public class ClienteJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 20)
    private String telefone;

    @Column(length = 20)
    private String whatsapp;

    @Column(length = 50)
    private String instagram;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    @Column(columnDefinition = "TEXT")
    private String anotacoes;

    // Relacionamento em cascata: ao salvar o Cliente, o Endereço é salvo/atualizado automaticamente
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id")
    private EnderecoJpaEntity endereco;

    @Column(name = "is_ativo", nullable = false)
    private boolean ativo = true;

    @Column(name = "is_deletado", nullable = false)
    private boolean deletado = false;
}