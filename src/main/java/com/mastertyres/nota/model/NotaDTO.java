package com.mastertyres.nota.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Data
public class NotaDTO {

    public NotaDTO(final Integer notaId, final String numNota, final String numFactura, final String fechaYHora, final String fechaVencimiento,
                   final String statusNota, final String createdAt, final float total, final String observaciones, final String observaciones2,
                   final int porcentajeGas, final String rayones, final String golpes, final String tapones, final String tapetes, final String radio,
                   final String gato, final String llave, final String llanta, final String alineacion, final int alineacionCantidad, final float alineacionUnitario,
                   final float alineacionTotal, final String balanceo, final String balanceo2, final int balanceoCantidad, final float balanceoUnitario, final float balanceoTotal,
                   final String amorDelanteros, final int amorDelCantidad, final float amorDelUnitario, final float amorDelTotal, final String amorTraseros, final int amorTrasCantidad,
                   final float amorTrasUnitario, final float amorTrasTotal, final String suspension, final String suspension2, final int suspensionCantidad, final float suspensionUnitario,
                   final float suspensionTotal, final String mecanica, final String mecanica2, final int mecanicaCantidad, final float mecanicaUnitario, final float mecanicaTotal,
                   final String frenos, final String frenos2, final int frenosCantidad, final float frenosUnitario, final float frenosTotal, final String otros, final String otros2,
                   final int otrosCantidad, final float otrosUnitario, final float otrosTotal, final float subTotalMecanica, final float subTotalFrenos, final float subTotalOtros,
                   final String nombreCliente, final String apellido, final String segundoApellido, final String domicilio, final String rfc, final String correo, final String marca,
                   final String modelo, final String categoria, final Integer anio, final String active) {
        this.notaId = notaId;
        this.numNota = numNota;
        this.numFactura = numFactura;
        this.fechaYHora = fechaYHora;
        this.fechaVencimiento = fechaVencimiento;
        this.statusNota = statusNota;
        this.createdAt = createdAt;
        this.total = total;
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
        this.balanceo2 = balanceo2;
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
        this.suspension2 = suspension2;
        this.suspensionCantidad = suspensionCantidad;
        this.suspensionUnitario = suspensionUnitario;
        this.suspensionTotal = suspensionTotal;
        this.mecanica = mecanica;
        this.mecanica2 = mecanica2;
        this.mecanicaCantidad = mecanicaCantidad;
        this.mecanicaUnitario = mecanicaUnitario;
        this.mecanicaTotal = mecanicaTotal;
        this.frenos = frenos;
        this.frenos2 = frenos2;
        this.frenosCantidad = frenosCantidad;
        this.frenosUnitario = frenosUnitario;
        this.frenosTotal = frenosTotal;
        this.otros = otros;
        this.otros2 = otros2;
        this.otrosCantidad = otrosCantidad;
        this.otrosUnitario = otrosUnitario;
        this.otrosTotal = otrosTotal;
        this.subTotalMecanica = subTotalMecanica;
        this.subTotalFrenos = subTotalFrenos;
        this.subTotalOtros = subTotalOtros;
        this.nombreCliente = nombreCliente;
        this.apellido = apellido;
        this.segundoApellido = segundoApellido;
        this.domicilio = domicilio;
        this.rfc = rfc;
        this.correo = correo;
        this.marca = marca;
        this.modelo = modelo;
        this.categoria = categoria;
        this.anio = anio;
        this.active = active;
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
    private String balanceo2;
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
    private String suspension2;
    private int suspensionCantidad;
    private float suspensionUnitario;
    private float suspensionTotal;
    private String mecanica;
    private String mecanica2;
    private int mecanicaCantidad;
    private float mecanicaUnitario;
    private float mecanicaTotal;
    private String frenos;
    private String frenos2;
    private int frenosCantidad;
    private float frenosUnitario;
    private float frenosTotal;
    private String otros;
    private String otros2;
    private int otrosCantidad;
    private float otrosUnitario;
    private float otrosTotal;
    private float subTotalMecanica;
    private float subTotalFrenos;
    private float subTotalOtros;


    //cliente
    private String nombreCliente;
    private String apellido;
    private String segundoApellido;
    private String domicilio;
    private String rfc;
    private String correo;

    //vehiculo
    private String marca;
    private String modelo;
    private String categoria;
    private Integer anio;

}//class
