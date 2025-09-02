package com.mastertyres.promociones.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "promociones")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "vehiculo_promocion")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "promocion_id")
    private Integer promocionId;

    //atributos
    @Column(name = "nombre_promocion")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "tipo_descuento")
    private String tipoDescuento;

    @Column(name = "porcentaje")
    private float valorDescuento;

    private float precio;

    @Column(name = "fecha_inicio")
    private String fechaInicio;

    @Column(name = "fecha_fin")
    private String fechaFin;

    private String active;

    private String created_at;

    private String updated_at;

    private String img;
}
