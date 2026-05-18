package com.example.gad.services;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.gad.models.Post;
import com.example.gad.models.PostMedia;
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

    public List<PostMedia> findAll() {
        return postMediaRepository.findAll();
    }

    public PostMedia findById(UUID id) {

        return postMediaRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "PostMedia nao encontrado! Id: " + id
                ));
    }

    public List<PostMedia> findAllByPostId(UUID postId) {
        return postMediaRepository.findByPost_Id(postId);
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
    public PostMedia update(PostMedia obj) {

        PostMedia newObj = findById(obj.getId());

        if (obj.getPath() != null) {
            newObj.setPath(obj.getPath());
        }

        if (obj.getTipo() != null) {
            newObj.setTipo(obj.getTipo());
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