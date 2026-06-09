package com.example.gad.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class RedatorCreateDTO {

    @NotNull
    private UUID usuarioId;

    @NotBlank
    private String empresaId;
}
