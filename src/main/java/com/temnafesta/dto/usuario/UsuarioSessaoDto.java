package com.temnafesta.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados da sessão do usuário autenticado")
public class UsuarioSessaoDto {
    @Schema(description = "ID do usuário", example = "1")
    private Integer userId;

    @Schema(description = "Nome do usuário", example = "João Silva")
    private String nome;

    @Schema(description = "E-mail do usuário", example = "joao.silva@email.com")
    private String email;

    @Schema(description = "Perfil do usuário")
    private PerfilSessaoDto perfilSessaoDto;

    @Schema(description = "Dados resumidos do perfil do usuário")
    public static class PerfilSessaoDto {
        @Schema(description = "ID do perfil", example = "1")
        private Integer perfilId;

        @Schema(description = "Nome do perfil", example = "ADMIN")
        private String nome;

        public Integer getPerfilId() {
            return perfilId;
        }

        public void setPerfilId(Integer perfilId) {
            this.perfilId = perfilId;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public PerfilSessaoDto getPerfilSessaoDto() {
        return perfilSessaoDto;
    }

    public void setPerfilSessaoDto(PerfilSessaoDto perfilSessaoDto) {
        this.perfilSessaoDto = perfilSessaoDto;
    }
}