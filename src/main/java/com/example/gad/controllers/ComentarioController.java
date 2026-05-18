package com.example.gad.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.gad.models.Comentario;
import com.example.gad.services.ComentarioService;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @GetMapping
    public ResponseEntity<List<Comentario>> findAll() {
        return ResponseEntity.ok(comentarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comentario> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(comentarioService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @Validated(Comentario.CreateComentario.class)
            @RequestBody Comentario obj
    ) {

        comentarioService.create(obj);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @Validated(Comentario.UpdateComentario.class)
            @RequestBody Comentario obj,
            @PathVariable UUID id
    ) {

        obj.setId(id);

        comentarioService.update(obj);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        comentarioService.delete(id);

        return ResponseEntity.noContent().build();
    }
}