package com.mastertyres.promociones.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "promociones")
@Data
@Builder
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

    private int porcentaje;

    private float precio;

    @Column(name = "fecha_inicio")
    private String fechaInicio;

    @Column(name = "fecha_fin")
    private String fechaFin;

    private String active;

    //Es necesario cambiar a local date para poder insertar
   @Column(name = "created_at", updatable = false)
   @CreationTimestamp
    private LocalDate created_at;

   @Column(name = "updated_at")
   @CreationTimestamp
    private LocalDateTime updated_at;

    private String img;

}
