package com.example.gad.services;

import java.util.List;
import java.util.Objects;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.gad.models.Usuario;
import com.example.gad.models.UsuarioRole;
import com.example.gad.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username).orElse(null);

        if (Objects.isNull(usuario)) {
            throw new UsernameNotFoundException("Usuario nao encontrado: " + username);
        }

        UsuarioRole role = usuario.getRole() != null ? usuario.getRole() : UsuarioRole.CLIENTE;

        return new User(
                usuario.getEmail(),
                usuario.getSenha(),
                List.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
        );
    }
}

