package com.example.gad.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.gad.models.Cliente;
import com.example.gad.models.Post;
import com.example.gad.models.PostStatus;
import com.example.gad.models.Redator;
import com.example.gad.repositories.PostRepository;
import com.example.gad.services.exceptions.DataBindingViolationException;
import com.example.gad.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final ClienteService clienteService;

    private final RedatorService redatorService;

    public List<Post> findAll() {
        return this.postRepository.findAll();
    }

    public Post findById(UUID id) {
        return this.postRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Post nao encontrado! Id: " + id
                ));
    }

    public List<Post> findAllByCliente_Id(UUID clienteId) {
        return this.postRepository.findByCliente_UsuarioId(clienteId);
    }

    public List<Post> findAllByRedator_Id(UUID redatorId) {
        return this.postRepository.findByRedator_UsuarioId(redatorId);
    }

    private UUID resolveClienteId(Cliente cliente) {

        if (cliente == null) {
            return null;
        }

        if (cliente.getUsuarioId() != null) {
            return cliente.getUsuarioId();
        }

        if (cliente.getUsuario() != null) {
            return cliente.getUsuario().getId();
        }

        return null;
    }

    private UUID resolveRedatorId(Redator redator) {

        if (redator == null) {
            return null;
        }

        if (redator.getUsuarioId() != null) {
            return redator.getUsuarioId();
        }

        if (redator.getUsuario() != null) {
            return redator.getUsuario().getId();
        }

        return null;
    }

    @Transactional
    public Post create(Post obj) {

        UUID clienteId = resolveClienteId(obj.getCliente());
        UUID redatorId = resolveRedatorId(obj.getRedator());

        if (clienteId == null || redatorId == null) {
            throw new IllegalArgumentException(
                    "Cliente e Redator devem ser informados para criar post!"
            );
        }

        Cliente cliente = this.clienteService.findById(clienteId);

        Redator redator = this.redatorService.findById(redatorId);

        obj.setId(null);
        obj.setCliente(cliente);
        obj.setRedator(redator);

        if (obj.getStatus() == null) {
            obj.setStatus(PostStatus.RASCUNHO);
        }

        return this.postRepository.save(obj);
    }

    @Transactional
    public Post update(Post obj) {

        Post newObj = findById(obj.getId());

        UUID clienteId = resolveClienteId(obj.getCliente());

        if (clienteId != null) {
            Cliente cliente = this.clienteService.findById(clienteId);
            newObj.setCliente(cliente);
        }

        UUID redatorId = resolveRedatorId(obj.getRedator());

        if (redatorId != null) {
            Redator redator = this.redatorService.findById(redatorId);
            newObj.setRedator(redator);
        }

        if (obj.getConteudo() != null) {
            newObj.setConteudo(obj.getConteudo());
        }

        if (obj.getStatus() != null) {
            newObj.setStatus(obj.getStatus());
        }

        if (obj.getHash() != null) {
            newObj.setHash(obj.getHash());
        }

        return this.postRepository.save(newObj);
    }

    @Transactional
    public Post updateStatus(UUID id, PostStatus status) {

        Post post = findById(id);

        post.setStatus(status);

        return this.postRepository.save(post);
    }

    @Transactional
    public void delete(UUID id) {

        findById(id);

        try {
            this.postRepository.deleteById(id);

        } catch (Exception e) {

            throw new DataBindingViolationException(
                    "Nao e possivel excluir pois ha entidades relacionadas!"
            );
        }
    }
}