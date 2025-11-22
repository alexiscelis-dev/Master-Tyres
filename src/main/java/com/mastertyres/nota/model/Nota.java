package com.mastertyres.nota.model;


import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.notaDetalle.model.NotaDetalle;
import com.mastertyres.vehiculo.model.Vehiculo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id")
    private Vehiculo vehiculo;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "inventario_id")
    private Inventario inventario;

    @Column(name = "num_nota")
    private String numNota;
    @Column(name = "num_factura")
    private String numFactura;
    @Column(name = "fecha_hora")
    private String fechaYhora;
    @Column(name = "fecha_vencimiento")
    private String fechaVencimiento;
    @Column(name = "status_nota")
    private String statusNota;
    @Column(name = "created_at",updatable = false)
    @CreationTimestamp
    private String createdAt;
    @Column(name = "updated_at",updatable = true)
    @UpdateTimestamp
    private String updatedAt;

    @Column(name = "active")
    private String active;

    private float adeudo;
    @Column(name = "saldo_favor")
    private float saldoFavor;

    private float total;


    @OneToMany(mappedBy = "nota",cascade = CascadeType.ALL)
    private List<NotaDetalle>detalles;





}//class
