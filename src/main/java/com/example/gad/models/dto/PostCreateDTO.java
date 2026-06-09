package com.example.gad.models.dto;

import java.util.UUID;

import com.example.gad.models.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateDTO {

    @NotNull
    private UUID clienteId;

    @NotNull
    private UUID redatorId;

    @NotBlank
    private String conteudo;

    private PostStatus status;

    @NotBlank
    private String hash;
}

