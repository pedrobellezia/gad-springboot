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
import com.example.gad.models.dto.ClienteCreateDTO;
import com.example.gad.models.dto.ClienteUpdateDTO;
import com.example.gad.models.projection.ClienteProjection;
import com.example.gad.models.projection.PostProjection;
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('REDATOR')")
    public ResponseEntity<List<ClienteProjection>> findAll() {

        return ResponseEntity.ok(
                this.clienteService.findAll()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("@securityService.canAccessCliente(#id, authentication)")
    public ResponseEntity<Cliente> findById(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                this.clienteService.findById(id)
        );
    }

    @GetMapping("/{id}/posts")
    @PreAuthorize("@securityService.canAccessCliente(#id, authentication)")
    public ResponseEntity<List<PostProjection>> findAllPostsByClienteId(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                this.postService.findAllByCliente_Id(id)
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cliente> create(
            @Valid @RequestBody ClienteCreateDTO dto
    ) {

        Cliente cliente = this.clienteService.create(this.clienteService.fromCreateDTO(dto));

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
    @PreAuthorize("@securityService.canAccessCliente(#id, authentication)")
    public ResponseEntity<Cliente> update(
            @Valid @RequestBody ClienteUpdateDTO dto,
            @PathVariable UUID id
    ) {

        dto.setUsuarioId(id);

        return ResponseEntity.ok(
                this.clienteService.update(this.clienteService.fromUpdateDTO(dto))
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