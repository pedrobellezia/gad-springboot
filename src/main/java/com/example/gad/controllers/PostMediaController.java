package com.example.gad.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.gad.models.PostMedia;
import com.example.gad.services.PostMediaService;

@RestController
@RequestMapping({"/post-media", "/postmedia"})
public class PostMediaController {

    private final PostMediaService postMediaService;

    public PostMediaController(PostMediaService postMediaService) {
        this.postMediaService = postMediaService;
    }

    @GetMapping
    public ResponseEntity<List<PostMedia>> findAll() {
        return ResponseEntity.ok(postMediaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostMedia> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(postMediaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody PostMedia obj) {

        postMediaService.create(obj);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody PostMedia obj, @PathVariable UUID id) {

        obj.setId(id);
        postMediaService.update(obj);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        postMediaService.delete(id);

        return ResponseEntity.noContent().build();
    }
}