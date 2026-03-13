package com.mastertyres.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@Entity
@Table(name = "usuario")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("usuario_id")
    @Column(name = "usuario_id")
    private Integer usuarioId;

    @JsonProperty("auth_id")
    @Column(name = "auth_id")
    private String authId;

    private String username;
    @Column(name = "contrasena")
    @JsonProperty("contrasena")
    private String password;

    private String correo;
    private String role;
    private String nombre;

    @JsonProperty("apellido")
    @Column(name = "apellidos")
    private String apellidos;

    @JsonProperty("foto_perfil")
    @Column(name = "foto_perfil")
    private String fotoPerfil;

    @JsonProperty("created_at")
    @Column(name = "created_at",updatable = true)
    private String createdAt;

    @JsonProperty("updated_at")
    @Column(name = "updated_at",updatable = true)
    private String updatedAt;

    private String active;

    @JsonProperty("status_licencia")
    @Column(name = "status_licencia")
    private String statusLicencia;

    @Column(name = "next_check")
    private String nextCheck;

}//class
