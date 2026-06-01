package com.example.gad.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.gad.models.Cliente;
import com.example.gad.models.Post;
import com.example.gad.services.ClienteService;
import com.example.gad.services.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cliente")
@Validated
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    private final PostService postService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Cliente>> findAll() {

        return ResponseEntity.ok(
                this.clienteService.findAll()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clienteService.findById(#id).getUsuario().getEmail() == authentication.name")
    public ResponseEntity<Cliente> findById(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                this.clienteService.findById(id)
        );
    }

    @GetMapping("/{id}/posts")
    @PreAuthorize("hasRole('ADMIN') or @clienteService.findById(#id).getUsuario().getEmail() == authentication.name")
    public ResponseEntity<List<Post>> findAllPostsByClienteId(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                this.postService.findAllByCliente_Id(id)
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cliente> create(
            @Valid @RequestBody Cliente obj
    ) {

        Cliente cliente = this.clienteService.create(obj);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cliente.getUsuarioId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(cliente);
    }
    // ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clienteService.findById(#id).getUsuario().getEmail() == authentication.name")
    public ResponseEntity<Cliente> update(
            @Valid @RequestBody Cliente obj,
            @PathVariable UUID id
    ) {

        obj.setUsuarioId(id);

        return ResponseEntity.ok(
                this.clienteService.update(obj)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {

        this.clienteService.delete(id);

        return ResponseEntity.noContent().build();
    }
}