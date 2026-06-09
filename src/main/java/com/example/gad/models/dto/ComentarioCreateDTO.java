package com.example.gad.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class ComentarioCreateDTO {

    @NotBlank
    private String texto;

    @NotNull
    private UUID postId;

    @NotNull
    private UUID usuarioId;
}
