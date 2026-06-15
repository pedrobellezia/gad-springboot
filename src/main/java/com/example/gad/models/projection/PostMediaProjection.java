package com.example.gad.models.projection;

import com.example.gad.models.MediaTipo;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public interface PostMediaProjection {
    UUID getId();

    @Value("#{target.path}")
    String getUrl();

    MediaTipo getTipo();
}
