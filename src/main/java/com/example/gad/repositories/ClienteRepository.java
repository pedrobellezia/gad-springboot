package com.example.gad.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gad.models.Cliente;
import com.example.gad.models.projection.ClienteProjection;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    boolean existsByEmpresaId(String empresaId);

    List<ClienteProjection> findAllProjectedBy();
}