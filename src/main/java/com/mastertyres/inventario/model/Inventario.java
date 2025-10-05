package com.mastertyres.inventario.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "inventario_llantas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventario_id")
    private Integer inventarioId;
    private String marca;
    private String modelo;
    private String medida;
    @Column(name = "indice_carga")
    private String indiceCarga;
    @Column(name = "indice_velocidad")
    private String indiceVelocidad;
    private int stock;
    @Column(name = "precio_compra")
    private float precioCompra;
    @Column(name = "precio_venta")
    private float precioVenta;
    private String observaciones;
    private String imagen;
    @Column(name = "active",insertable = false, updatable = true)
    private String active;
    @Column(name = "created_at",updatable = false)
    @CreationTimestamp
    private String created_at;
    @Column(name = "updated_at",updatable = true)
    @UpdateTimestamp
    private String updated_at;
    @Column(name = "codigo_barras")
    private String codigoBarras;
    private String dot;




}//clase
