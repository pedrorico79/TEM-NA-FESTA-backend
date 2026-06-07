package com.temnafesta.service;

import com.temnafesta.dto.metodoPagamento.MetodoPagamentoResponseDto;
import com.temnafesta.mapper.MetodoPagamentoMapper;
import com.temnafesta.repository.MetodoPagamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetodoPagamentoService {

    private final MetodoPagamentoRepository repository;

    public MetodoPagamentoService(
            MetodoPagamentoRepository repository
    ) {
        this.repository = repository;
    }

    public List<MetodoPagamentoResponseDto> listarTodos() {

        return repository.findAll()
                .stream()
                .map(MetodoPagamentoMapper::toResponseDto)
                .toList();
    }
}