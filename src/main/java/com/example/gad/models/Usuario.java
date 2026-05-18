package com.example.gad.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(
        name = Usuario.TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_redator_usuario_email",
                        columnNames = "email"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Usuario extends AuditableEntity {

    public interface CreateUsuario {
    }

    public interface UpdateUsuario {
    }

    public static final String TABLE_NAME = "usuario";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "nome", length = 100, nullable = false)
    @NotBlank(groups = CreateUsuario.class)
    @Size(groups = CreateUsuario.class, min = 2, max = 100)
    private String nome;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    @NotBlank(groups = CreateUsuario.class)
    @Email(groups = CreateUsuario.class)
    private String email;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "senha", length = 120, nullable = false)
    @NotBlank(groups = CreateUsuario.class)
    private String senha;

    @Column(name = "avatar")
    private String avatar;

    @JsonIgnore
    @OneToOne(mappedBy = "usuario")
    private Cliente cliente;

    @JsonIgnore
    @OneToOne(mappedBy = "usuario")
    private Redator redator;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Comentario> comentarios = new ArrayList<>();
}