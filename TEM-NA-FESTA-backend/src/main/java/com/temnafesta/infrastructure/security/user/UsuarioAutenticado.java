package com.temnafesta.infrastructure.security.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UsuarioAutenticado implements UserDetails {

    private final Long id;
    private final String nome;
    private final String email;
    private final String senha;
    private final boolean isAtivo;
    private final Collection<? extends GrantedAuthority> authorities;

    public UsuarioAutenticado(Long id, String nome, String email, String senha, boolean isAtivo, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.isAtivo = isAtivo;
        this.authorities = authorities;
    }

    // --- MÉTODOS CUSTOMIZADOS ---

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    // --- MÉTODOS OBRIGATÓRIOS DA INTERFACE USERDETAILS ---

    @Override
    public String getUsername() {
        return email; // No nosso sistema, o login é feito pelo email
    }

    @Override
    public String getPassword() {
        return senha; // A senha com o hash BCrypt
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // Contém o Perfil mapeado (ex: ROLE_ADMIN)
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isAtivo; // Mapeia diretamente para a coluna 'is_ativo' do banco
    }
}