package com.mastertyres.nota.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class NotaDTO {

    public NotaDTO(final Integer notaId, final String numNota, final String numFactura, final Float total, final LocalDate fechayHora,
                   final String observaciones, final String porcentajeGas, final String rayones, final String golpes, final String tapones,
                   final String tapetes, final String radio, final String gato, final String llave, final String llanta, final LocalDate fechaVencimiento,
                   final String statusNota, final String createdAt,final float precio, final String descripcionServicio, final int cantidad, final float precioUnitario,
                   final float montoPagado, final String nombreCliente,final String apellido,final String segundoApellido, final String marca, final String modelo, final String categoria, final Integer anio) {

        this.notaId = notaId;
        this.numNota = numNota;
        this.numFactura = numFactura;
        this.total = total;
        this.fechayHora = fechayHora;
        this.observaciones = observaciones;

        this.porcentajeGas = porcentajeGas;
        this.rayones = rayones;
        this.golpes = golpes;
        this.tapones = tapones;
        this.tapetes = tapetes;
        this.radio = radio;
        this.gato = gato;
        this.llave = llave;
        this.llanta = llanta;
        this.fechaVencimiento = fechaVencimiento;
        this.statusNota = statusNota;
        this.precio = precio;
        this.descripcionServicio = descripcionServicio;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.montoPagado = montoPagado;
        this.nombreCliente = nombreCliente;
        this.apellido = apellido;
        this.segundoApellido = segundoApellido;
        this.marca = marca;
        this.modelo = modelo;
        this.categoria = categoria;
        this.anio = anio;
        this.createdAt = createdAt;
    }

    //Nota
    private Integer notaId;
    private String numNota;
    private String numFactura;
    private Float total;
    private LocalDate fechayHora;
    private String observaciones;

    private String porcentajeGas;
    private String rayones;
    private String golpes;
    private String tapones;
    private String tapetes;
    private String radio;
    private String gato;
    private String llave;
    private String llanta;
    private LocalDate fechaVencimiento;
    private String statusNota;
    private String createdAt;


    // nota detalle
    private float precio;
    private String descripcionServicio;
    private int cantidad;
    private float precioUnitario;
    private float montoPagado;

    //cliente
    private String nombreCliente;
    private String apellido;
    private String segundoApellido;

    //vehiculo
    private String marca;
    private String modelo;
    private String categoria;
    private Integer anio;

}//class
