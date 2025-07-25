package com.mastertyres.cliente.model;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.List;


public class Cliente {
    private int clienteId;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String numeroTelefono;
    private String estado;
    private String ciudad;
    private String domicilio;
    private String curp;
    private String fechaHora;
    private String tipoCliente;
    private String createdAt;
    private String updatedAt;


}

