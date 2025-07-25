package com.mastertyres.vehiculo.model;


import jakarta.persistence.Entity;
import lombok.*;


public class Vehiculo {
    private int vehiculoId;
    private String marca;
    private String modelo;
    private String placas;
    private String color;
    private String numeroSerie;
    private String año;
    private String created_at;
    private String update_at;
    private String observaciones;
    private String ultimoServicio;
    private String kilometros;
    private String fechaRegistro;
    private VehiculoStatus status;

}
