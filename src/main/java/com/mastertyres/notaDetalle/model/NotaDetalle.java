package com.mastertyres.notaDetalle.model;

import com.mastertyres.nota.model.Nota;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "nota_detalle")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class NotaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nota_detalle_id")
    private Integer notaDetalleId;

    //Relacion con nota
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nota_id")
    private Nota nota;

    private float precio;
    @Column(name = "descripcion_servicio")
    private String descripcionServicio;
    private int cantidad;
    @Column(name = "precio_unitario")
    private float precioUnitario;
    @Column(name = "monto_pagado")
    private float montoPagado;




}//class

