package com.temnafesta.dto.usuario;

import com.temnafesta.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UsuarioDetalhesDto implements UserDetails {

  private final String nome;
  private final String email;
  private final String senha;
  private final boolean ativo;
  private final String perfil;

  public UsuarioDetalhesDto(Usuario usuario) {
    this.nome = usuario.getNome();
    this.email = usuario.getEmail();
    this.senha = usuario.getSenha();
    this.ativo = usuario.getAtivo();
    // O valor vem do Enum (ex: ADMIN) e guardamos na String
    this.perfil = usuario.getPerfil().name();
  }

  public String getNome() {
    return nome;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // IMPORTANTE: O Spring Security exige o prefixo ROLE_ para usar hasRole()
    return List.of(new SimpleGrantedAuthority("ROLE_" + this.perfil));
  }

  @Override
  public String getPassword() { return senha; }

  @Override
  public String getUsername() { return email; }

  @Override
  public boolean isAccountNonExpired() { return true; }

  @Override
  public boolean isAccountNonLocked() { return true; }

  @Override
  public boolean isCredentialsNonExpired() { return true; }

  @Override
  public boolean isEnabled() { return ativo; }
}