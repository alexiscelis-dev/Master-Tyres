package com.mastertyres.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SupabaseUserResponse {

    @JsonProperty("usuario_id")
    private String usuarioId;
    @JsonProperty("username")
    private String username;
    @JsonProperty("contrasena")
    private String password;
    @JsonProperty("role")
    private String role;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("apellido")
    private String apellido;
    @JsonProperty("foto_perfil")
    private String fotoPerfil;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("active")
    private String active;
    @JsonProperty("correo")
    private String correo;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("status_licencia")
    private String statusLicencia;

}//class
