package com.example.gad.services;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.gad.models.Post;
import com.example.gad.models.PostMedia;
import com.example.gad.models.dto.PostMediaCreateDTO;
import com.example.gad.models.dto.PostMediaUpdateDTO;
import com.example.gad.models.projection.PostMediaProjection;
import com.example.gad.repositories.PostMediaRepository;
import com.example.gad.services.exceptions.DataBindingViolationException;
import com.example.gad.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostMediaService {

    private final PostMediaRepository postMediaRepository;

    private final PostService postService;

    public List<PostMediaProjection> findAll() {
        return postMediaRepository.findAllProjectedBy();
    }

    public PostMedia findById(UUID id) {

        return postMediaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "PostMedia nao encontrado! Id: " + id
                ));
    }

    public List<PostMediaProjection> findAllByPostId(UUID postId) {
        return postMediaRepository.findProjectedByPost_Id(postId);
    }

    public PostMedia fromCreateDTO(PostMediaCreateDTO dto) {
        PostMedia media = new PostMedia();
        media.setPath(dto.getUrl());
        media.setTipo(dto.getTipo());

        Post post = new Post();
        post.setId(dto.getPostId());
        media.setPost(post);

        return media;
    }

    @Transactional
    public PostMedia create(PostMedia obj) {

        if (obj.getPost() == null || obj.getPost().getId() == null) {

            throw new DataBindingViolationException(
                    "Post deve ser informado para criar media!"
            );
        }

        Post post = postService.findById(obj.getPost().getId());

        obj.setId(null);
        obj.setPost(post);

        return postMediaRepository.save(obj);
    }

    @Transactional
    public PostMedia update(UUID id, PostMediaUpdateDTO objDto) {

        PostMedia newObj = findById(id);

        if (objDto.getUrl() != null) {
            newObj.setPath(objDto.getUrl());
        }

        if (objDto.getTipo() != null) {
            newObj.setTipo(objDto.getTipo());
        }

        return postMediaRepository.save(newObj);
    }

    @Transactional
    public void delete(UUID id) {

        findById(id);

        try {

            postMediaRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {

            throw new DataBindingViolationException(
                    "Nao e possivel excluir pois ha entidades relacionadas!"
            );
        }
    }
}
