package com.temnafesta.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "metodo_pagamento")
@Getter
@Setter
public class MetodoPagamentoJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;

    // Coluna de teste da E7 da infraestrutura (Q12): criada no startup pelo
    // ddl-auto=update, com o usuário da aplicação sem DROP. Não faça merge.
    @Column(name = "teste_e7", length = 20)
    private String testeE7;
}