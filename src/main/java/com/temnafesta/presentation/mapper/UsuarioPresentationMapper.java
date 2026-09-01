package com.temnafesta.presentation.mapper;

import com.temnafesta.application.dto.CriarUsuarioCommand;
import com.temnafesta.domain.model.Usuario;
import com.temnafesta.presentation.dto.CriarUsuarioRequestDto;
import com.temnafesta.presentation.dto.UsuarioResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioPresentationMapper {

    CriarUsuarioCommand toCommand(CriarUsuarioRequestDto dto);
    UsuarioResponseDto toResponse(Usuario domain);
}
