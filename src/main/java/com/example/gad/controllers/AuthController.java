package com.example.gad.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.gad.models.Cliente;
import com.example.gad.models.Redator;
import com.example.gad.models.Usuario;
import com.example.gad.models.UsuarioRole;
import com.example.gad.repositories.UsuarioRepository;
import com.example.gad.security.JWTUtil;
import com.example.gad.services.ClienteService;
import com.example.gad.services.RedatorService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClienteService clienteService;
    private final RedatorService redatorService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email ja cadastrado");
        }

        UsuarioRole role = request.role();
        if (role == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role é obrigatória");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setRole(role);
        usuario.setAvatar(request.avatar());

        usuario = usuarioRepository.save(usuario);

        // Criar Cliente ou Redator baseado no role
        if (role == UsuarioRole.CLIENTE) {
            if (request.empresaId() == null || request.empresaId().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "empresaId é obrigatório para clientes");
            }
            Cliente cliente = new Cliente();
            cliente.setUsuario(usuario);
            cliente.setEmpresaId(request.empresaId());
            clienteService.create(cliente);
        } else if (role == UsuarioRole.REDATOR) {
            if (request.empresaId() == null || request.empresaId().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "empresaId é obrigatório para redators");
            }
            Redator redator = new Redator();
            redator.setUsuario(usuario);
            redator.setEmpresaId(request.empresaId());
            redatorService.create(redator);
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.senha())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(jwtUtil.generateToken(userDetails));
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou senha invalidos");
        }
    }

    public record RegisterRequest(
            @NotBlank @Size(min = 2, max = 100) String nome,
            @NotBlank @Email String email,
            @NotBlank @Size(min = 6, max = 100) String senha,
            @NotNull UsuarioRole role,
            String avatar,
            String empresaId
    ) {
    }

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String senha
    ) {
    }
}

