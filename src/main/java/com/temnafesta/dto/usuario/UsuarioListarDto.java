package com.temnafesta.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de listagem de usuário")
public class UsuarioListarDto {

    @Schema(description = "ID do usuário", example = "1")
    private Integer id;

    @Schema(description = "Nome do usuário", example = "João Silva")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "joao.silva@email.com")
    private String email;

    @Schema(description = "Status do usuário", example = "true")
    private Boolean ativo;

    @Schema(description = "Perfil do usuário")
    private PerfilUsuarioDto perfilUsuarioDto;

    @Schema(description = "Dados resumidos do perfil do usuário")
    public static class PerfilUsuarioDto{
        @Schema(description = "ID do perfil", example = "1")
        private Integer id;

        @Schema(description = "Nome do perfil", example = "ADMIN")
        private String nome;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public PerfilUsuarioDto getPerfilUsuarioDto() {
        return perfilUsuarioDto;
    }

    public void setPerfilUsuarioDto(PerfilUsuarioDto perfilUsuarioDto) {
        this.perfilUsuarioDto = perfilUsuarioDto;
    }
}
