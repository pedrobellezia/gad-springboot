package com.example.gad.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = Redator.TABLE_NAME)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Redator extends AuditableEntity {

    public interface CreateRedator {
    }

    public interface UpdateRedator {
    }

    public static final String TABLE_NAME = "redator";

    @Id
    @Column(name = "usuario_id", nullable = false, updatable = false)
    @JsonIgnore
    @EqualsAndHashCode.Include
    private UUID usuarioId;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "empresa_id", nullable = false, length = 36)
    @NotBlank(groups = CreateRedator.class)
    private String empresaId;

    @JsonIgnore
    @OneToMany(mappedBy = "redator")
    private List<Post> posts = new ArrayList<>();

}