package com.temnafesta.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Dados para criação de usuário")
public class UsuarioCriacaoDto {

  @Schema(description = "Nome do usuário", example = "João Silva", minLength = 3, maxLength = 100)
  @NotBlank
  @Size(min = 3, max = 100)
  private String nome;

  @Schema(description = "E-mail do usuário", example = "joao.silva@email.com")
  @NotBlank
  @Email
  private String email;

  @Schema(description = "Senha do usuário", example = "Senha@123", minLength = 8)
  @NotBlank
  @Size(min = 8)
  @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).*$",
          message = "Senha muito fraca")
  private String senha;

  @Schema(description = "Perfil do usuário", example = "ADMIN")
  @NotNull
  private String perfil;

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

  public String getPerfil() {
    return perfil;
  }

  public void setPerfil(String perfil) {
    this.perfil = perfil;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }
}
