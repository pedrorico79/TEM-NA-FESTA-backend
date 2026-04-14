package com.temnafesta.service;

import com.temnafesta.dto.endereco.EnderecoRequestDto;
import com.temnafesta.exception.endereco.EnderecoNaoEncontrado;
import com.temnafesta.mapper.EnderecoMapper;
import com.temnafesta.model.Endereco;
import com.temnafesta.repository.EnderecoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public Endereco criar(EnderecoRequestDto enderecoRequestDto){
        if (enderecoRequestDto == null) throw new IllegalArgumentException("Enreço não pode ser nulo");
        Endereco entity = EnderecoMapper.toEntity(enderecoRequestDto);
        return enderecoRepository.save(entity);
    }

    public List<Endereco> listar(){
        return enderecoRepository.findAll();
    }

    public Endereco buscarPorId(Integer id){
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo.");
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new EnderecoNaoEncontrado(id));
    }

    public Endereco atualizar(Integer id, EnderecoRequestDto endereco){
        if (id == null || endereco == null)
            throw new IllegalArgumentException("Endereço ou ID não pode ser nulo");

        Endereco enderecoParaAtualizar = enderecoRepository.findById(id)
                .orElseThrow(() -> new EnderecoNaoEncontrado(id));

        enderecoParaAtualizar.setCep(endereco.getCep());
        enderecoParaAtualizar.setLogradouro(endereco.getLogradouro());
        enderecoParaAtualizar.setNumero(endereco.getNumero());
        enderecoParaAtualizar.setComplemento(endereco.getComplemento());
        enderecoParaAtualizar.setBairro(endereco.getBairro());
        enderecoParaAtualizar.setCidade(endereco.getCidade());
        enderecoParaAtualizar.setEstado(endereco.getEstado());

        return enderecoRepository.save(enderecoParaAtualizar);
    }

    public void deletar(Integer id){
        if (id == null) throw new IllegalArgumentException("Id não pode nulo.");
        Endereco enderecoParaDeletar = enderecoRepository.findById(id)
                .orElseThrow(() -> new EnderecoNaoEncontrado(id));

        enderecoRepository.delete(enderecoParaDeletar);
    }
}
