package com.temnafesta.infrastructure.persistence.repository;

import com.temnafesta.infrastructure.persistence.entity.ProdutoJpaEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class SpringDataProdutoRepositoryTest {

    @Autowired
    private SpringDataProdutoRepository repository;

    @Test
    void deveBuscarProdutoNaoDeletadoPorId() {
        ProdutoJpaEntity produto = repository.save(new ProdutoJpaEntity(
                null,
                "Bolo de Chocolate",
                null,
                new BigDecimal("49.90"),
                true,
                false));

        assertTrue(repository.findByIdAndDeletadoFalse(produto.getId()).isPresent());
    }

    @Test
    void naoDeveBuscarProdutoDeletadoPorId() {
        ProdutoJpaEntity produto = repository.save(new ProdutoJpaEntity(
                null,
                "Bolo Deletado",
                null,
                new BigDecimal("49.90"),
                false,
                true));

        assertFalse(repository.findByIdAndDeletadoFalse(produto.getId()).isPresent());
    }
}
