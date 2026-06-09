package com.example.gad.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gad.models.Redator;
import com.example.gad.models.projection.RedatorProjection;

public interface RedatorRepository extends JpaRepository<Redator, UUID> {

    List<RedatorProjection> findAllProjectedBy();

    boolean existsByEmpresaId(String empresaId);
}
