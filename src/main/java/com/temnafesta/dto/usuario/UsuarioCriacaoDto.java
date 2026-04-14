package com.temnafesta.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public class UsuarioCriacaoDto {

  @NotBlank
  @Size(min = 3, max = 100)
  @Schema(description = "Nome do usuário", example = "John Doe")
  private String nome;

  @NotBlank
  @Email
  @Schema(description = "Email do usuário", example = "john@doe.com")
  private String email;

  @NotBlank
  @Size(min = 8)
  @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).*$",
          message = "Senha muito fraca")
  @Schema(description = "Senha do usuário", example = "123456")
  private String senha;

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
