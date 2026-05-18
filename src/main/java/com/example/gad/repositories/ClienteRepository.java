package com.example.gad.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gad.models.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    boolean existsByEmpresaId(String empresaId);
}