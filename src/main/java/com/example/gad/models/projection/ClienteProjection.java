package com.example.gad.models.projection;

import java.util.UUID;

/**
 * Lightweight Cliente view for listing endpoints.
 */
public interface ClienteProjection {

    UUID getUsuarioId();

    String getEmpresaId();
}

