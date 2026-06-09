package com.example.gad.controllers;

import com.example.gad.models.dto.UsuarioUpdateDTO;
import com.example.gad.models.projection.UsuarioProjection;
import jakarta.validation.Valid;
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
    public ResponseEntity<UsuarioProjection> me(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        UsuarioProjection usuario = usuarioService.findProjectedByEmail(email);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping
    public ResponseEntity<Void> updateMe(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UsuarioUpdateDTO dto
    ) {
        String email = userDetails.getUsername();
        Usuario current = usuarioService.findByEmail(email);
        dto.setId(current.getId());
        usuarioService.update(usuarioService.fromUpdateDTO(dto));
        return ResponseEntity.noContent().build();
    }
}
