package com.temnafesta.service;

import com.temnafesta.repository.PedidoRepository;
import com.temnafesta.repository.PerfilRepository;
import org.springframework.stereotype.Service;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

}
