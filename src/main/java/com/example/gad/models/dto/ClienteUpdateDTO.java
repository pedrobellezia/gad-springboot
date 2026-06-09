package com.example.gad.models.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClienteUpdateDTO {

    private UUID usuarioId;

    private String empresaId;
}

