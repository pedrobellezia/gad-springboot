package com.example.gad.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gad.models.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, UUID> {

    List<Comentario> findByPost_Id(UUID postId);
}