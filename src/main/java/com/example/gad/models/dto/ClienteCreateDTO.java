package com.example.gad.models.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClienteCreateDTO {

    @NotNull
    private UUID usuarioId;

    @NotBlank
    private String empresaId;
}

