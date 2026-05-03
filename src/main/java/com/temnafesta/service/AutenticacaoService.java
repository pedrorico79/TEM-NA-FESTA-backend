package com.temnafesta.service;

import com.temnafesta.dto.usuario.UsuarioDetalhesDto;import com.temnafesta.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.temnafesta.model.Usuario;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  // Método da interface implementada
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(username);

    if (usuarioOpt.isEmpty()) {

      throw new UsernameNotFoundException(String.format("usuário: %s não encontrado", username));
    }

    return new UsuarioDetalhesDto(usuarioOpt.get());
  }
}
