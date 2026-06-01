package com.example.gad.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gad.models.Usuario;
import com.example.gad.services.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Usuario> me(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        Usuario usuario = usuarioService.findByEmail(email);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping
    public ResponseEntity<Void> updateMe(@AuthenticationPrincipal UserDetails userDetails,
                                         @RequestBody Usuario update) {
        String email = userDetails.getUsername();
        Usuario current = usuarioService.findByEmail(email);
        update.setId(current.getId());
        usuarioService.update(update);
        return ResponseEntity.noContent().build();
    }
}

