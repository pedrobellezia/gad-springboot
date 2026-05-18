package com.example.gad.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.gad.models.Post;
import com.example.gad.models.Redator;
import com.example.gad.services.PostService;
import com.example.gad.services.RedatorService;

@RestController
@RequestMapping("/redatores")
public class RedatorController {

    private final RedatorService redatorService;
    private final PostService postService;

    public RedatorController(
            RedatorService redatorService,
            PostService postService
    ) {
        this.redatorService = redatorService;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Redator>> findAll() {
        return ResponseEntity.ok(redatorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Redator> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(redatorService.findById(id));
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<Post>> findAllPostsByRedatorId(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.findAllByRedator_Id(id));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Redator obj) {

        redatorService.create(obj);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getUsuarioId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody Redator obj, @PathVariable UUID id) {

        obj.setUsuarioId(id);
        redatorService.update(obj);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        redatorService.delete(id);

        return ResponseEntity.noContent().build();
    }
}