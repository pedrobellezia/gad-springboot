package com.example.gad.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.gad.models.Comentario;
import com.example.gad.models.Post;
import com.example.gad.models.PostMedia;
import com.example.gad.models.PostStatus;
import com.example.gad.services.ComentarioService;
import com.example.gad.services.PostMediaService;
import com.example.gad.services.PostService;

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
    public ResponseEntity<List<Post>> findAll() {
        return ResponseEntity.ok(postService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @GetMapping("/{id}/medias")
    public ResponseEntity<List<PostMedia>> findAllMediasByPostId(@PathVariable UUID id) {
        return ResponseEntity.ok(postMediaService.findAllByPostId(id));
    }

    @GetMapping("/{id}/comentarios")
    public ResponseEntity<List<Comentario>> findAllComentariosByPostId(@PathVariable UUID id) {
        return ResponseEntity.ok(comentarioService.findAllByPostId(id));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Post obj) {

        postService.create(obj);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody Post obj, @PathVariable UUID id) {

        obj.setId(id);
        postService.update(obj);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID id,
            @RequestBody PostStatusRequest body
    ) {
        postService.updateStatus(id, body.status());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    public record PostStatusRequest(PostStatus status) {}
}