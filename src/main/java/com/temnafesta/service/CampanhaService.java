package com.temnafesta.service;

import com.temnafesta.exception.campanha.CampanhaDuplicadaException;
import com.temnafesta.exception.campanha.CampanhaNaoEncontrada;
import com.temnafesta.model.Campanha;
import com.temnafesta.repository.CampanhaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampanhaService {

    private final CampanhaRepository campanhaRepository;

    public CampanhaService(CampanhaRepository campanhaRepository) {
        this.campanhaRepository = campanhaRepository;
    }

    public Campanha create(Campanha campanha) {
        campanhaRepository.findByNomeIgnoreCase(campanha.getNome())
                .ifPresent(c -> { throw new CampanhaDuplicadaException(campanha.getNome()); });
        return campanhaRepository.save(campanha);
    }

    public Campanha update(Integer id, Campanha campanha) {
        if (!campanhaRepository.existsById(id)) {
            throw new CampanhaNaoEncontrada(id);
        }
        if (campanhaRepository.existsByNomeIgnoreCaseAndIdNot(campanha.getNome(), id)) {
            throw new CampanhaDuplicadaException(campanha.getNome());
        }

        Campanha existente = campanhaRepository.findById(id).get();
        existente.setNome(campanha.getNome());
        existente.setDataInicio(campanha.getDataInicio());
        existente.setDataFim(campanha.getDataFim());
        existente.setAtiva(campanha.getAtiva());

        return campanhaRepository.save(existente);
    }

    public void desativar(Integer id) {
        Campanha campanha = campanhaRepository.findById(id)
                        .orElseThrow(() -> new CampanhaNaoEncontrada(id));
        campanha.setAtiva(false);
        campanhaRepository.save(campanha);
    }

    public void reativar(Integer id) {
        Campanha campanha = campanhaRepository.findById(id)
                .orElseThrow(() -> new CampanhaNaoEncontrada(id));
        campanha.setAtiva(true);
        campanhaRepository.save(campanha);
    }

    public List<Campanha> listarAtivas() {
        return campanhaRepository.findByAtiva(true);
    }

    public List<Campanha> listarTodas() {
        return campanhaRepository.findAll();
    }

    public Campanha findById(Integer id) {
        return campanhaRepository.findById(id)
                .orElseThrow(() -> new CampanhaNaoEncontrada(id));
    }

    public List<Campanha> listarInativas(){
        return campanhaRepository.findByAtiva(false);
    }
}