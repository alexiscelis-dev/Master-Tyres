package com.mastertyres.nota.model;

import lombok.*;


@Builder
@Data
public class NotaDTO {

    //Nota
    private Integer notaId;
    private String numNota;
    private String numFactura;
    private String fechaYHora;
    private String fechaVencimiento;
    private String statusNota;
    private String createdAt;
    private String active;
    private Float total;

    private Integer inventarioId;
    // nota detalle
    private String observaciones;
    private String observaciones2;
    private Integer porcentajeGas;
    private String rayones;
    private String golpes;
    private String tapones;
    private String tapetes;
    private String radio;
    private String gato;
    private String llave;
    private String llanta;
    private String alineacion;
    private Integer alineacionCantidad;
    private Float alineacionUnitario;
    private Float alineacionTotal;
    private String balanceo;
    private Integer balanceoCantidad;
    private Float balanceoUnitario;
    private Float balanceoTotal;
    private String amorDelanteros;
    private Integer amorDelCantidad;
    private Float amorDelUnitario;
    private Float amorDelTotal;
    private String amorTraseros;
    private Integer amorTrasCantidad;
    private Float amorTrasUnitario;
    private Float amorTrasTotal;
    private String suspension;
    private Integer suspensionCantidad;
    private Float suspensionUnitario;
    private Float suspensionTotal;
    private String suspension2;
    private Integer suspensionCantidad2;
    private Float suspensionUnitario2;
    private Float suspensionTotal2;
    private String mecanica;
    private Integer mecanicaCantidad;
    private Float mecanicaUnitario;
    private Float mecanicaTotal;
    private String mecanica2;
    private Integer mecanicaCantidad2;
    private Float mecanicaUnitario2;
    private Float mecanicaTotal2;
    private String frenos;
    private Integer frenosCantidad;
    private Float frenosUnitario;
    private Float frenosTotal;
    private String frenos2;
    private Integer frenosCantidad2;
    private Float frenosUnitario2;
    private Float frenosTotal2;
    private String otros;
    private Integer otrosCantidad;
    private Float otrosUnitario;
    private Float otrosTotal;
    private String otros2;
    private Integer otrosCantidad2;
    private Float otrosUnitario2;
    private Float otrosTotal2;
    private Float subTotalMecanica;
    private Float subTotalFrenos;
    private Float subTotalOtros;
    private String llantaCampo;
    private Integer llantaCantidad;
    private Float llantaUnitario;
    private Float llantaTotal;
    private Float adeudo;
    private Float saldoFavor;


    //cliente

    private Integer clienteId;
    private String nombreCliente;
    private String apellido;
    private String segundoApellido;
    private String domicilio;
    private String rfc;
    private String correo;

    //vehiculo
    private Integer vehiculoId;
    private String marca;
    private String modelo;
    private String categoria;
    private Integer anio;
    private Integer kilometros;
    private String color;
    private String placas;


    public NotaDTO(final Integer notaId, final String numNota, final String numFactura, final String fechaYHora,
                   final String fechaVencimiento, final String statusNota, final String createdAt, final String active,
                   final Float total, final Integer inventarioId, final String observaciones, final String observaciones2,
                   final Integer porcentajeGas, final String rayones, final String golpes, final String tapones, final String tapetes,
                   final String radio, final String gato, final String llave, final String llanta, final String alineacion,
                   final Integer alineacionCantidad, final Float alineacionUnitario, final Float alineacionTotal, final String balanceo,
                   final Integer balanceoCantidad, final Float balanceoUnitario, final Float balanceoTotal, final String amorDelanteros,
                   final Integer amorDelCantidad, final Float amorDelUnitario, final Float amorDelTotal, final String amorTraseros,
                   final Integer amorTrasCantidad, final Float amorTrasUnitario, final Float amorTrasTotal, final String suspension,
                   final Integer suspensionCantidad, final Float suspensionUnitario, final Float suspensionTotal, final String suspension2,
                   final Integer suspensionCantidad2, final Float suspensionUnitario2, final Float suspensionTotal2, final String mecanica,
                   final Integer mecanicaCantidad, final Float mecanicaUnitario, final Float mecanicaTotal, final String mecanica2,
                   final Integer mecanicaCantidad2, final Float mecanicaUnitario2, final Float mecanicaTotal2, final String frenos,
                   final Integer frenosCantidad, final Float frenosUnitario, final Float frenosTotal, final String frenos2,
                   final Integer frenosCantidad2, final Float frenosUnitario2, final Float frenosTotal2, final String otros,
                   final Integer otrosCantidad, final Float otrosUnitario, final Float otrosTotal, final String otros2,
                   final Integer otrosCantidad2, final Float otrosUnitario2, final Float otrosTotal2, final Float subTotalMecanica,
                   final Float subTotalFrenos, final Float subTotalOtros,final String llantaCampo, final Integer llantaCantidad, final Float llantaUnitario,
                   final Float llantaTotal, final Float adeudo, final Float saldoFavor, final Integer clienteId,
                   final String nombreCliente, final String apellido, final String segundoApellido, final String domicilio,
                   final String rfc, final String correo, final Integer vehiculoId, final String marca, final String modelo,
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
        this.vehiculoId = vehiculoId;
        this.marca = marca;
        this.modelo = modelo;
        this.categoria = categoria;
        this.anio = anio;
        this.kilometros = kilometros;
        this.color = color;
        this.placas = placas;
    }



}//class
