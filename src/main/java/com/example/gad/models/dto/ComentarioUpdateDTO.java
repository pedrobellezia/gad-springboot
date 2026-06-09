package com.example.gad.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class ComentarioUpdateDTO {

    private UUID id;

    @NotBlank
    private String texto;
}
