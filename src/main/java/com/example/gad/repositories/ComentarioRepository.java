package com.example.gad.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gad.models.Comentario;
import com.example.gad.models.projection.ComentarioProjection;

public interface ComentarioRepository extends JpaRepository<Comentario, UUID> {

    List<ComentarioProjection> findProjectedByPost_Id(UUID postId);

    List<ComentarioProjection> findAllProjectedBy();
}
