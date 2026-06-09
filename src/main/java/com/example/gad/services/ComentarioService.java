package com.example.gad.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.gad.models.Comentario;
import com.example.gad.models.Post;
import com.example.gad.models.Usuario;
import com.example.gad.models.dto.ComentarioCreateDTO;
import com.example.gad.models.dto.ComentarioUpdateDTO;
import com.example.gad.models.projection.ComentarioProjection;
import com.example.gad.repositories.ComentarioRepository;
import com.example.gad.services.exceptions.DataBindingViolationException;
import com.example.gad.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;

    private final PostService postService;

    private final UsuarioService usuarioService;

    public List<ComentarioProjection> findAll() {
        return comentarioRepository.findAllProjectedBy();
    }

    public Comentario findById(UUID id) {
        return comentarioRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Comentario nao encontrado! Id: " + id
                ));
    }

    public List<ComentarioProjection> findAllByPostId(UUID postId) {
        return comentarioRepository.findProjectedByPost_Id(postId);
    }

    public Comentario fromCreateDTO(ComentarioCreateDTO dto) {
        Comentario comentario = new Comentario();
        comentario.setTexto(dto.getTexto());

        Post post = new Post();
        post.setId(dto.getPostId());
        comentario.setPost(post);

        Usuario usuario = new Usuario();
        usuario.setId(dto.getUsuarioId());
        comentario.setUsuario(usuario);

        return comentario;
    }

    @Transactional
    public Comentario create(Comentario obj) {

        if (obj.getPost() == null || obj.getPost().getId() == null) {
            throw new DataBindingViolationException(
                    "Post deve ser informado para criar comentario!"
            );
        }

        if (obj.getUsuario() == null || obj.getUsuario().getId() == null) {
            throw new DataBindingViolationException(
                    "Usuario deve ser informado para criar comentario!"
            );
        }

        Post post = postService.findById(obj.getPost().getId());

        Usuario usuario = usuarioService.findById(obj.getUsuario().getId());

        obj.setId(null);
        obj.setPost(post);
        obj.setUsuario(usuario);

        return comentarioRepository.save(obj);
    }

    @Transactional
    public Comentario update(UUID id, ComentarioUpdateDTO objDto) {

        Comentario newObj = findById(id);

        if (objDto.getTexto() != null) {
            newObj.setTexto(objDto.getTexto());
        }

        return comentarioRepository.save(newObj);
    }

    @Transactional
    public void delete(UUID id) {

        findById(id);

        try {
            comentarioRepository.deleteById(id);

        } catch (Exception e) {

            throw new DataBindingViolationException(
                    "Nao e possivel excluir comentario pois ha entidades relacionadas!"
            );
        }
    }
}
