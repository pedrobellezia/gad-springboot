package com.example.gad.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gad.models.Usuario;
import com.example.gad.models.projection.UsuarioProjection;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    // Projection-based query for lightweight list responses
    java.util.List<UsuarioProjection> findAllProjectedBy();

    java.util.Optional<UsuarioProjection> findProjectedById(java.util.UUID id);

    Optional<UsuarioProjection> findProjectedByEmail(String email);
}
