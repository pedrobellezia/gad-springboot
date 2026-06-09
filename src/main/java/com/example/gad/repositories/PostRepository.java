package com.example.gad.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gad.models.Post;
import com.example.gad.models.projection.PostProjection;

public interface PostRepository extends JpaRepository<Post, UUID> {

    List<PostProjection> findAllProjectedBy();

    List<PostProjection> findProjectedByCliente_UsuarioId(UUID clienteId);

    List<PostProjection> findProjectedByRedator_UsuarioId(UUID redatorId);
}