package com.example.gad.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.gad.models.Comentario;
import com.example.gad.models.Post;
import com.example.gad.models.PostMedia;
import com.example.gad.models.PostStatus;
import com.example.gad.models.dto.PostCreateDTO;
import com.example.gad.models.dto.PostUpdateDTO;
import com.example.gad.models.projection.PostProjection;
import com.example.gad.services.ComentarioService;
import com.example.gad.services.PostMediaService;
import com.example.gad.services.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final PostMediaService postMediaService;
    private final ComentarioService comentarioService;

    public PostController(
            PostService postService,
            PostMediaService postMediaService,
            ComentarioService comentarioService
    ) {
        this.postService = postService;
        this.postMediaService = postMediaService;
        this.comentarioService = comentarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('REDATOR')")
    public ResponseEntity<List<PostProjection>> findAll() {
        return ResponseEntity.ok(postService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REDATOR') or @postService.findById(#id).getCliente().getUsuario().getEmail() == authentication.name")
    public ResponseEntity<Post> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @GetMapping("/{id}/medias")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REDATOR') or @postService.findById(#id).getCliente().getUsuario().getEmail() == authentication.name")
    public ResponseEntity<List<PostMedia>> findAllMediasByPostId(@PathVariable UUID id) {
        return ResponseEntity.ok(postMediaService.findAllByPostId(id));
    }

    @GetMapping("/{id}/comentarios")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REDATOR') or @postService.findById(#id).getCliente().getUsuario().getEmail() == authentication.name")
    public ResponseEntity<List<Comentario>> findAllComentariosByPostId(@PathVariable UUID id) {
        return ResponseEntity.ok(comentarioService.findAllByPostId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('REDATOR')")
    public ResponseEntity<Void> create(@Valid @RequestBody PostCreateDTO dto) {

        Post created = postService.create(postService.fromCreateDTO(dto));

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REDATOR')")
    public ResponseEntity<Void> update(@Valid @RequestBody PostUpdateDTO dto, @PathVariable UUID id) {

        dto.setId(id);
        postService.update(postService.fromUpdateDTO(dto));

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REDATOR') or @postService.findById(#id).getCliente().getUsuario().getEmail() == authentication.name")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID id,
            @RequestBody PostStatusRequest body
    ) {
        postService.updateStatus(id, body.status());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REDATOR')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    public record PostStatusRequest(PostStatus status) {}
}