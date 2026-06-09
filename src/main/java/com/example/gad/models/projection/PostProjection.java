package com.example.gad.models.projection;

import java.util.UUID;

import com.example.gad.models.PostStatus;

/**
 * Lightweight Post view for listing endpoints.
 */
public interface PostProjection {

    UUID getId();

    String getConteudo();

    PostStatus getStatus();

    String getHash();
}

