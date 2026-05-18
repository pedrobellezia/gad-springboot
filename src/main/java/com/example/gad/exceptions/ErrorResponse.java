package com.example.gad.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final int status;
    private final String message;

    private List<ErroValidacao> erros;

    public void addValidationError(String campo, String mensagem) {

        if (Objects.isNull(erros)) {
            this.erros = new ArrayList<>();
        }

        this.erros.add(
                new ErroValidacao(campo, mensagem)
        );
    }

    public record ErroValidacao(
            String campo,
            String mensagem
    ) {
    }
}