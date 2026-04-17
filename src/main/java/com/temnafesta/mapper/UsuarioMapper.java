package com.temnafesta.mapper;

import com.temnafesta.dto.usuario.*;
import com.temnafesta.model.Perfil;
import com.temnafesta.model.Usuario;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {

    private UsuarioMapper(){}

    /**
     * Converte o DTO de criação para a Entity.
     * Note que não setamos dataCriacao nem isAtivo, pois a Entity já cuida disso.
     */
    public static Usuario toEntity(UsuarioCriacaoDto dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());

        // Converte a String do DTO para o Enum da sua Entity
        if (dto.getPerfil() != null) {
            usuario.setPerfil(dto.getPerfil());
        }

        return usuario;
    }

    /**
     * Mapeia a Entity para o DTO de listagem simples (usado em buscas gerais).
     */
    public static UsuarioListarDto toListarDto(Usuario entity) {
        if (entity == null) return null;

        UsuarioListarDto dto = new UsuarioListarDto();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());

        return dto;
    }

    /**
     * Converte uma lista de Entities para uma lista de DTOs de listagem.
     */
    public static List<UsuarioListarDto> toListarDtoList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(UsuarioMapper::toListarDto)
                .collect(Collectors.toList());
    }

    /**
     * Mapeia a Entity diretamente para o DTO de Sessão (pós-login).
     * Ideal para quando o login é bem sucedido e queremos retornar os dados básicos.
     */
    public static UsuarioSessaoDto toSessaoDto(Usuario entity) {
        if (entity == null) return null;

        UsuarioSessaoDto dto = new UsuarioSessaoDto();
        dto.setUserId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());

        return dto;
    }

    /**
     * Mapeia para o DTO que carrega o Token JWT.
     */
    public static UsuarioTokenDto toTokenDto(Usuario entity, String token) {
        if (entity == null) return null;

        UsuarioTokenDto dto = new UsuarioTokenDto();
        dto.setUserId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setNome(entity.getNome());
        dto.setToken(token);

        return dto;
    }

    public static Usuario toEntity(UsuarioAtualizacaoDto dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setAtivo(dto.getAtivo());
        usuario.setPerfil(dto.getPerfil());
        return usuario;
    }
}