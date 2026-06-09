package com.example.gad.models.dto;

import java.util.UUID;

import com.example.gad.models.PostStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostUpdateDTO {

    private UUID id;

    private UUID clienteId;

    private UUID redatorId;

    private String conteudo;

    private PostStatus status;

    private String hash;
}

