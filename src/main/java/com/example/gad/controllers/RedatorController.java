package com.example.gad.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.gad.models.Redator;
import com.example.gad.models.dto.RedatorCreateDTO;
import com.example.gad.models.dto.RedatorUpdateDTO;
import com.example.gad.models.projection.PostProjection;
import com.example.gad.models.projection.RedatorProjection;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RedatorProjection>> findAll() {
        return ResponseEntity.ok(redatorService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwnerOfRedator(#id, authentication)")
    public ResponseEntity<Redator> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(redatorService.findById(id));
    }

    @GetMapping("/{id}/posts")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwnerOfRedator(#id, authentication)")
    public ResponseEntity<List<PostProjection>> findAllPostsByRedatorId(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.findAllByRedator_Id(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@Valid @RequestBody RedatorCreateDTO objDto) {

        Redator obj = redatorService.fromDTO(objDto);
        obj = redatorService.create(obj);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getUsuarioId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwnerOfRedator(#id, authentication)")
    public ResponseEntity<Void> update(@Valid @RequestBody RedatorUpdateDTO objDto, @PathVariable UUID id) {

        redatorService.update(id, objDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        redatorService.delete(id);

        return ResponseEntity.noContent().build();
    }
}