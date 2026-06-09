package com.example.gad.models.projection;

import java.util.UUID;

public interface RedatorProjection {
    UUID getUsuarioId();
    String getEmpresaId();
    UsuarioProjection getUsuario();
}
