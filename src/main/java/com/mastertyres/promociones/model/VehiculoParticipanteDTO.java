package com.mastertyres.promociones.model;

import lombok.Data;

@Data
public class VehiculoParticipanteDTO {

    private String marca;
    private String modelo;
    private String categoria;

    public VehiculoParticipanteDTO(String marca, String modelo, String categoria) {
        this.marca = marca;
        this.modelo = modelo;
        this.categoria = categoria;

    }
}
