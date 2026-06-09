package com.example.gad.models.projection;

import java.util.UUID;

import com.example.gad.models.UsuarioRole;

/**
 * Projection used to return a lightweight view of Usuario for list endpoints.
 */
public interface UsuarioProjection {

    UUID getId();

    String getNome();

    String getEmail();

    String getAvatar();

    UsuarioRole getRole();

}

