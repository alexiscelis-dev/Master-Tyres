package com.mastertyres.nota.model;


import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.notaDetalle.model.NotaDetalle;
import com.mastertyres.vehiculo.model.Vehiculo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "nota")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
@Builder
public class Nota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nota_id")
    private Integer notaId;

    //Relacion cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    //Relacion vehiculo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;

    //Relacion nota detalle
    //1 se mappea por nota, 2 se elimina en nota detalle, 3 se hace la misma accion en nota detalle 4 tipo de carga LAZY
    @OneToMany(mappedBy = "nota", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NotaDetalle> notaDetalles = new ArrayList<>();

    //atibutos
    private String numNota;
    private String numFactura;
    private Float total;
    @Column(name = "fecha_hora")
    private LocalDate fechayHora;
    private String observaciones;
    private String createdAt;
    private String updatedAt;
    private String active;
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


}//class
