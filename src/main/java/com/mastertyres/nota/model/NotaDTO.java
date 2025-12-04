package com.mastertyres.nota.model;

import lombok.*;


@Builder
@Data
public class NotaDTO {


    public NotaDTO(final Integer notaId, final String numNota, final String numFactura, final String fechaYHora,
                   final String fechaVencimiento, final String statusNota, final String createdAt, final String active,
                   final float total, final Integer inventarioId, final String observaciones, final String observaciones2,
                   final int porcentajeGas, final String rayones, final String golpes, final String tapones, final String tapetes,
                   final String radio, final String gato, final String llave, final String llanta, final String alineacion,
                   final int alineacionCantidad, final float alineacionUnitario, final float alineacionTotal, final String balanceo,
                   final int balanceoCantidad, final float balanceoUnitario, final float balanceoTotal, final String amorDelanteros,
                   final int amorDelCantidad, final float amorDelUnitario, final float amorDelTotal, final String amorTraseros,
                   final int amorTrasCantidad, final float amorTrasUnitario, final float amorTrasTotal, final String suspension,
                   final int suspensionCantidad, final float suspensionUnitario, final float suspensionTotal, final String suspension2,
                   final int suspensionCantidad2, final float suspensionUnitario2, final float suspensionTotal2, final String mecanica,
                   final int mecanicaCantidad, final float mecanicaUnitario, final float mecanicaTotal, final String mecanica2,
                   final int mecanicaCantidad2, final float mecanicaUnitario2, final float mecanicaTotal2, final String frenos,
                   final int frenosCantidad, final float frenosUnitario, final float frenosTotal, final String frenos2,
                   final int frenosCantidad2, final float frenosUnitario2, final float frenosTotal2, final String otros,
                   final int otrosCantidad, final float otrosUnitario, final float otrosTotal, final String otros2,
                   final int otrosCantidad2, final float otrosUnitario2, final float otrosTotal2, final float subTotalMecanica,
                   final float subTotalFrenos, final float subTotalOtros,final String llantaCampo, final int llantaCantidad, final float llantaUnitario,
                   final float llantaTotal, final float adeudo, final float saldoFavor, final Integer clienteId,
                   final String nombreCliente, final String apellido, final String segundoApellido, final String domicilio,
                   final String rfc, final String correo,final String genero, final String tipoCliente, final Integer vehiculoId, final String marca, final String modelo,
                   final String categoria, final Integer anio, final Integer kilometros, final String color, final String placas) {
        this.notaId = notaId;
        this.numNota = numNota;
        this.numFactura = numFactura;
        this.fechaYHora = fechaYHora;
        this.fechaVencimiento = fechaVencimiento;
        this.statusNota = statusNota;
        this.createdAt = createdAt;
        this.active = active;
        this.total = total;
        this.inventarioId = inventarioId;
        this.observaciones = observaciones;
        this.observaciones2 = observaciones2;
        this.porcentajeGas = porcentajeGas;
        this.rayones = rayones;
        this.golpes = golpes;
        this.tapones = tapones;
        this.tapetes = tapetes;
        this.radio = radio;
        this.gato = gato;
        this.llave = llave;
        this.llanta = llanta;
        this.alineacion = alineacion;
        this.alineacionCantidad = alineacionCantidad;
        this.alineacionUnitario = alineacionUnitario;
        this.alineacionTotal = alineacionTotal;
        this.balanceo = balanceo;
        this.balanceoCantidad = balanceoCantidad;
        this.balanceoUnitario = balanceoUnitario;
        this.balanceoTotal = balanceoTotal;
        this.amorDelanteros = amorDelanteros;
        this.amorDelCantidad = amorDelCantidad;
        this.amorDelUnitario = amorDelUnitario;
        this.amorDelTotal = amorDelTotal;
        this.amorTraseros = amorTraseros;
        this.amorTrasCantidad = amorTrasCantidad;
        this.amorTrasUnitario = amorTrasUnitario;
        this.amorTrasTotal = amorTrasTotal;
        this.suspension = suspension;
        this.suspensionCantidad = suspensionCantidad;
        this.suspensionUnitario = suspensionUnitario;
        this.suspensionTotal = suspensionTotal;
        this.suspension2 = suspension2;
        this.suspensionCantidad2 = suspensionCantidad2;
        this.suspensionUnitario2 = suspensionUnitario2;
        this.suspensionTotal2 = suspensionTotal2;
        this.mecanica = mecanica;
        this.mecanicaCantidad = mecanicaCantidad;
        this.mecanicaUnitario = mecanicaUnitario;
        this.mecanicaTotal = mecanicaTotal;
        this.mecanica2 = mecanica2;
        this.mecanicaCantidad2 = mecanicaCantidad2;
        this.mecanicaUnitario2 = mecanicaUnitario2;
        this.mecanicaTotal2 = mecanicaTotal2;
        this.frenos = frenos;
        this.frenosCantidad = frenosCantidad;
        this.frenosUnitario = frenosUnitario;
        this.frenosTotal = frenosTotal;
        this.frenos2 = frenos2;
        this.frenosCantidad2 = frenosCantidad2;
        this.frenosUnitario2 = frenosUnitario2;
        this.frenosTotal2 = frenosTotal2;
        this.otros = otros;
        this.otrosCantidad = otrosCantidad;
        this.otrosUnitario = otrosUnitario;
        this.otrosTotal = otrosTotal;
        this.otros2 = otros2;
        this.otrosCantidad2 = otrosCantidad2;
        this.otrosUnitario2 = otrosUnitario2;
        this.otrosTotal2 = otrosTotal2;
        this.subTotalMecanica = subTotalMecanica;
        this.subTotalFrenos = subTotalFrenos;
        this.subTotalOtros = subTotalOtros;
        this.llantaCampo = llantaCampo;
        this.llantaCantidad = llantaCantidad;
        this.llantaUnitario = llantaUnitario;
        this.llantaTotal = llantaTotal;
        this.adeudo = adeudo;
        this.saldoFavor = saldoFavor;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.apellido = apellido;
        this.segundoApellido = segundoApellido;
        this.domicilio = domicilio;
        this.rfc = rfc;
        this.correo = correo;
        this.genero = genero;
        this.tipoCliente = tipoCliente;
        this.vehiculoId = vehiculoId;
        this.marca = marca;
        this.modelo = modelo;
        this.categoria = categoria;
        this.anio = anio;
        this.kilometros = kilometros;
        this.color = color;
        this.placas = placas;
    }

    //Nota
    private Integer notaId;
    private String numNota;
    private String numFactura;
    private String fechaYHora;
    private String fechaVencimiento;
    private String statusNota;
    private String createdAt;
    private String active;
    private float total;

    private Integer inventarioId;
    // nota detalle
    private String observaciones;
    private String observaciones2;
    private int porcentajeGas;
    private String rayones;
    private String golpes;
    private String tapones;
    private String tapetes;
    private String radio;
    private String gato;
    private String llave;
    private String llanta;
    private String alineacion;
    private int alineacionCantidad;
    private float alineacionUnitario;
    private float alineacionTotal;
    private String balanceo;
    private int balanceoCantidad;
    private float balanceoUnitario;
    private float balanceoTotal;
    private String amorDelanteros;
    private int amorDelCantidad;
    private float amorDelUnitario;
    private float amorDelTotal;
    private String amorTraseros;
    private int amorTrasCantidad;
    private float amorTrasUnitario;
    private float amorTrasTotal;
    private String suspension;
    private int suspensionCantidad;
    private float suspensionUnitario;
    private float suspensionTotal;
    private String suspension2;
    private int suspensionCantidad2;
    private float suspensionUnitario2;
    private float suspensionTotal2;
    private String mecanica;
    private int mecanicaCantidad;
    private float mecanicaUnitario;
    private float mecanicaTotal;
    private String mecanica2;
    private int mecanicaCantidad2;
    private float mecanicaUnitario2;
    private float mecanicaTotal2;
    private String frenos;
    private int frenosCantidad;
    private float frenosUnitario;
    private float frenosTotal;
    private String frenos2;
    private int frenosCantidad2;
    private float frenosUnitario2;
    private float frenosTotal2;
    private String otros;
    private int otrosCantidad;
    private float otrosUnitario;
    private float otrosTotal;
    private String otros2;
    private int otrosCantidad2;
    private float otrosUnitario2;
    private float otrosTotal2;
    private float subTotalMecanica;
    private float subTotalFrenos;
    private float subTotalOtros;
    private String llantaCampo;
    private int llantaCantidad;
    private float llantaUnitario;
    private float llantaTotal;
    private float adeudo;
    private float saldoFavor;


    //cliente

    private Integer clienteId;
    private String nombreCliente;
    private String apellido;
    private String segundoApellido;
    private String domicilio;
    private String rfc;
    private String correo;
    private String genero;
    private String tipoCliente;

    //vehiculo
    private Integer vehiculoId;
    private String marca;
    private String modelo;
    private String categoria;
    private Integer anio;
    private Integer kilometros;
    private String color;
    private String placas;

}//class
