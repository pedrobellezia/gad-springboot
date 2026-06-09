package com.example.gad.models.projection;

import java.util.UUID;

public interface ComentarioProjection {
    UUID getId();
    String getTexto();
    UsuarioProjection getUsuario();
}
