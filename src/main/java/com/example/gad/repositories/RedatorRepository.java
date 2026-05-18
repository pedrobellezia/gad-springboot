package com.example.gad.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gad.models.Redator;

public interface RedatorRepository extends JpaRepository<Redator, UUID> {

    boolean existsByEmpresaId(String empresaId);
}