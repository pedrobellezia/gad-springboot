package com.example.gad.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gad.models.Post;

public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findByCliente_UsuarioId(UUID clienteId);

    List<Post> findByRedator_UsuarioId(UUID redatorId);
}