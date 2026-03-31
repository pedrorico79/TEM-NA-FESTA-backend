package com.temnafesta.repository;

import com.temnafesta.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository <Pagamento, Integer> {
}
