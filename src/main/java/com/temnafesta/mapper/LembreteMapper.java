package com.temnafesta.mapper;

import com.temnafesta.dto.cliente.ClienteResponseDto;
import com.temnafesta.dto.lembrete.LembreteRequestDto;
import com.temnafesta.dto.lembrete.LembreteResponseDto;
import com.temnafesta.model.Cliente;
import com.temnafesta.model.Endereco;
import com.temnafesta.model.Lembrete;
import com.temnafesta.model.Usuario;

import java.util.List;

public class LembreteMapper {

    public LembreteMapper() {}

    public static Lembrete toEntity(LembreteRequestDto dto) {
        Lembrete lembrete = new Lembrete();
        lembrete.setDescricao(dto.getDescricao());
        lembrete.setData_criacao(dto.getData_criacao());
        lembrete.setData_limite(dto.getData_limite());
        lembrete.setPrioridade(dto.getPrioridade());
        return lembrete;
    }

    public static LembreteResponseDto toResponseDto(Lembrete lembrete) {

        Usuario usuarioEntidade =
                lembrete.getUsuario();

        LembreteResponseDto.UsuarioLembreteDto usuarioDto = new
                LembreteResponseDto.UsuarioLembreteDto();

        usuarioDto.setId(usuarioEntidade.getId());
        usuarioDto.setNome(usuarioEntidade.getNome());
        usuarioDto.setEmail(usuarioEntidade.getEmail());
        usuarioDto.setPerfil(usuarioEntidade.getPerfil());
        usuarioDto.setAtivo(usuarioEntidade.getAtivo());
        usuarioDto.setDataCriacao(usuarioEntidade.getDataCriacao());

        LembreteResponseDto dto = new LembreteResponseDto();

        dto.setId(lembrete.getId());
        dto.setDescricao(lembrete.getDescricao());
        dto.setData_criacao(lembrete.getData_criacao());
        dto.setData_limite(lembrete.getData_limite());
        dto.setPrioridade(lembrete.getPrioridade());
        dto.setUsuario(usuarioDto);

        return dto;
    }

    public static List<LembreteResponseDto> toResponseDtoList(List<Lembrete> lembretes) {
        return lembretes.stream()
                .map(LembreteMapper::toResponseDto)
                .toList();
    }
}
