package com.example.gad.models.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioUpdateDTO {

    private UUID id;

    private String nome;

    private String email;

    /**
     * Password is optional on update; if provided it will be encoded and saved.
     */
    private String senha;

    private String avatar;
}

