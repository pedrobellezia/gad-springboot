package com.example.gad.models.projection;

import com.example.gad.models.MediaTipo;

import java.util.UUID;

public interface PostMediaProjection {
    UUID getId();
    String getUrl();
    MediaTipo getTipo();
}
