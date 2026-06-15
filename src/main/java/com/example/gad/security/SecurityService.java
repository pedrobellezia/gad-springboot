package com.example.gad.security;

import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gad.models.*;
import com.example.gad.repositories.*;

import lombok.RequiredArgsConstructor;

@Service("securityService")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final RedatorRepository redatorRepository;
    private final PostRepository postRepository;
    private final ComentarioRepository comentarioRepository;

    public boolean isAdmin(Authentication auth) {
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isOwnerOfUser(UUID userId, Authentication auth) {
        if (auth == null || userId == null) return false;
        if (isAdmin(auth)) return true;
        return usuarioRepository.findById(userId)
                .map(u -> u.getEmail().equals(auth.getName()))
                .orElse(false);
    }

    public boolean isOwnerOfCliente(UUID clienteId, Authentication auth) {
        if (auth == null || clienteId == null) return false;
        if (isAdmin(auth)) return true;
        return clienteRepository.findById(clienteId)
                .map(c -> c.getUsuario().getEmail().equals(auth.getName()))
                .orElse(false);
    }

    public boolean isOwnerOfRedator(UUID redatorId, Authentication auth) {
        if (auth == null || redatorId == null) return false;
        if (isAdmin(auth)) return true;
        return redatorRepository.findById(redatorId)
                .map(r -> r.getUsuario().getEmail().equals(auth.getName()))
                .orElse(false);
    }

    public boolean canAccessCliente(UUID clienteId, Authentication auth) {
        if (auth == null || clienteId == null) return false;
        if (isAdmin(auth)) return true;

        // Check if client is the logged-in user themselves
        boolean isSelf = clienteRepository.findById(clienteId)
                .map(c -> c.getUsuario().getEmail().equals(auth.getName()))
                .orElse(false);
        if (isSelf) return true;

        // Check if logged-in user is a redactor in the same agency (empresaId)
        String currentEmail = auth.getName();
        return redatorRepository.findByUsuarioEmail(currentEmail)
                .flatMap(redator -> clienteRepository.findById(clienteId)
                        .map(cliente -> redator.getEmpresaId().equals(cliente.getEmpresaId())))
                .orElse(false);
    }

    public boolean canAccessPost(UUID postId, Authentication auth) {
        if (auth == null || postId == null) return false;
        if (isAdmin(auth)) return true;

        String currentEmail = auth.getName();
        return postRepository.findById(postId)
                .map(post -> {
                    // Check if they are the redactor who created it
                    boolean isRedator = post.getRedator().getUsuario().getEmail().equals(currentEmail);
                    if (isRedator) return true;

                    // Check if they are the client of the post
                    boolean isCliente = post.getCliente().getUsuario().getEmail().equals(currentEmail);
                    if (isCliente) return true;

                    // Check if they are a redactor in the same agency as the client
                    return redatorRepository.findByUsuarioEmail(currentEmail)
                            .map(redator -> redator.getEmpresaId().equals(post.getCliente().getEmpresaId()))
                            .orElse(false);
                })
                .orElse(false);
    }

    public boolean canCommentOnPost(UUID postId, Authentication auth) {
        return canAccessPost(postId, auth);
    }

    public boolean isOwnerOfComentario(UUID comentarioId, Authentication auth) {
        if (auth == null || comentarioId == null) return false;
        if (isAdmin(auth)) return true;
        return comentarioRepository.findById(comentarioId)
                .map(c -> c.getUsuario().getEmail().equals(auth.getName()))
                .orElse(false);
    }
}
