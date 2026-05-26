package com.temnafesta.service;

import com.temnafesta.exception.campanha.CampanhaDuplicadaException;
import com.temnafesta.exception.campanha.CampanhaNaoEncontrada;
import com.temnafesta.model.Evento;
import com.temnafesta.repository.EventoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Evento criar(Evento evento) {
        eventoRepository.findByNomeIgnoreCase(evento.getNome())
                .ifPresent(c -> { throw new CampanhaDuplicadaException(evento.getNome()); });
        return eventoRepository.save(evento);
    }

    public Evento atualizar(Integer id, Evento evento) {
        if (!eventoRepository.existsById(id)) {
            throw new CampanhaNaoEncontrada(id);
        }
        if (eventoRepository.existsByNomeIgnoreCaseAndIdNot(evento.getNome(), id)) {
            throw new CampanhaDuplicadaException(evento.getNome());
        }

        Evento existente = eventoRepository.findById(id).get();
        existente.setNome(evento.getNome());
        existente.setDataInicio(evento.getDataInicio());
        existente.setDataFim(evento.getDataFim());
        existente.setAtiva(evento.getAtiva());

        return eventoRepository.save(existente);
    }

    public void desativar(Integer id) {
        Evento evento = eventoRepository.findById(id)
                        .orElseThrow(() -> new CampanhaNaoEncontrada(id));
        evento.setAtiva(false);
        eventoRepository.save(evento);
    }

    public void reativar(Integer id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new CampanhaNaoEncontrada(id));
        evento.setAtiva(true);
        eventoRepository.save(evento);
    }

    public List<Evento> listarAtivas() {
        return eventoRepository.findByAtiva(true);
    }

    public List<Evento> listarTodas() {
        return eventoRepository.findAll();
    }

    public Evento buscarPorId(Integer id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new CampanhaNaoEncontrada(id));
    }

    public List<Evento> listarInativas(){
        return eventoRepository.findByAtiva(false);
    }
}