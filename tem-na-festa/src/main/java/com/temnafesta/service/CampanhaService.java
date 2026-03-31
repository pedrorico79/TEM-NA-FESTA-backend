package com.temnafesta.service;

import com.temnafesta.repository.CampanhaRepository;
import org.springframework.stereotype.Service;

@Service
public class CampanhaService {

    private final CampanhaRepository campanhaRepository;

    public CampanhaService(CampanhaRepository campanhaRepository) {
        this.campanhaRepository = campanhaRepository;
    }
}
