package com.mastertyres.notaClienteDetalle.model;

import com.mastertyres.nota.model.Nota;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "nota_cliente_detalle")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
@Builder
public class NotaClienteDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nota_cliente_det_id")
    private Integer nombreClienteDetalleId;
    //relacion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nota_id")
    private Nota nota;

    @Column(name = "nombre_cliente")
    private String nombreClienteNota;
    @Column(name = "direccion1")
    private String direccion1Nota;
    @Column(name = "direccion2")
    private String direccion2Nota;
    @Column(name = "rfc")
    private String rfcNota;
    @Column(name = "correo")
    private String correoNota;
    @Column(name = "marca")
    private String marcaNota;
    @Column(name = "modelo")
    private String modeloNota;
    @Column(name = "anio")
    private Integer anioNota;
    @Column(name = "kilometros")
    private Integer kilometrosNota;
    @Column(name = "placas")
    private String placasNota;


}//class
