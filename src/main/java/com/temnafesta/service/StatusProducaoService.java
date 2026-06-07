package com.temnafesta.service;

import com.temnafesta.dto.statusProducao.StatusProducaoResponseDto;
import com.temnafesta.mapper.StatusProducaoMapper;
import com.temnafesta.repository.StatusProducaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusProducaoService {

    private final StatusProducaoRepository repository;

    public StatusProducaoService(
            StatusProducaoRepository repository
    ) {
        this.repository = repository;
    }

    public List<StatusProducaoResponseDto> listarTodos() {

        return repository.findAll()
                .stream()
                .map(StatusProducaoMapper::toResponseDto)
                .toList();
    }
}