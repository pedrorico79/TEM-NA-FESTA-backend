package com.temnafesta.mapper;

import com.temnafesta.dto.usuario.UsuarioRequestDto;
import com.temnafesta.dto.usuario.UsuarioResponseDto;
import com.temnafesta.model.Perfil;
import com.temnafesta.model.Usuario;

import java.util.List;

public class UsuarioMapper {
    public static Usuario toEntity(UsuarioRequestDto dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setAtivo(dto.getAtivo());
        usuario.setDataCriacao(dto.getDataCriacao());
        return usuario;
    }

    public static UsuarioResponseDto toResponseDto(Usuario usuario) {

        Perfil perfilEntidade =
                usuario.getPerfil();

        UsuarioResponseDto.PerfilUsuarioDto perfilDto = new
                UsuarioResponseDto.PerfilUsuarioDto();

        perfilDto.setId(perfilEntidade.getId());
        perfilDto.setNome(perfilEntidade.getNome());


        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setSenha(usuario.getSenha());
        dto.setAtivo(usuario.getAtivo());
        dto.setDataCriacao(usuario.getDataCriacao());
        dto.setPerfil(perfilDto);

        return dto;
    }

    public static List<UsuarioResponseDto> toResponseDtoList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(UsuarioMapper::toResponseDto)
                .toList();
    }
}
