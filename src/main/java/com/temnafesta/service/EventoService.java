package com.temnafesta.service;

import com.temnafesta.exception.evento.EventoDuplicadoException;
import com.temnafesta.exception.evento.EventoNaoEncontradoException;
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
                .ifPresent(c -> { throw new EventoDuplicadoException(evento.getNome()); });
        return eventoRepository.save(evento);
    }

    public Evento atualizar(Integer id, Evento evento) {
        if (!eventoRepository.existsById(id)) {
            throw new EventoNaoEncontradoException(id);
        }
        if (eventoRepository.existsByNomeIgnoreCaseAndIdNot(evento.getNome(), id)) {
            throw new EventoDuplicadoException(evento.getNome());
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
                        .orElseThrow(() -> new EventoNaoEncontradoException(id));
        evento.setAtiva(false);
        eventoRepository.save(evento);
    }

    public void reativar(Integer id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EventoNaoEncontradoException(id));
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
                .orElseThrow(() -> new EventoNaoEncontradoException(id));
    }

    public List<Evento> listarInativas(){
        return eventoRepository.findByAtiva(false);
    }
}