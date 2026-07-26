package com.temnafesta.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para login de usuário")
public class UsuarioLoginDto {

  @Schema(description = "E-mail do usuário", example = "joao.silva@email.com")
  @NotBlank
  @Email
  private String email;

  @Schema(description = "Senha do usuário", example = "Senha@123")
  @NotBlank
  private String senha;

  @Schema(description = "Manter usuário conectado por 30 dias", example = "false")
  private Boolean rememberMe = false;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public Boolean getRememberMe() {
    return rememberMe != null ? rememberMe : false;
  }

  public void setRememberMe(Boolean rememberMe) {
    this.rememberMe = rememberMe;
  }

}
