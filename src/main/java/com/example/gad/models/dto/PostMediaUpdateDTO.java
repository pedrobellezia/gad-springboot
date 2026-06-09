package com.example.gad.models.dto;

import com.example.gad.models.MediaTipo;
import lombok.Data;

import java.util.UUID;

@Data
public class PostMediaUpdateDTO {

    private String url;

    private MediaTipo tipo;
}
