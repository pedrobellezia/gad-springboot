package com.example.gad.models.dto;

import com.example.gad.models.MediaTipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PostMediaCreateDTO {

    @NotBlank
    private String url;

    @NotNull
    private MediaTipo tipo;

    @NotNull
    private UUID postId;
}
