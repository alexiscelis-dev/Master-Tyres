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
    private Integer notaDetalleId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nota_id")
    private Nota nota;

    private String observaciones;
    private String observaciones2;
    @Column(name = "porcentaje_gas")
    private int porcentajeGas;
    private String rayones;
    private String golpes;
    private String tapones;
    private String tapetes;
    private String radio;
    private String gato;
    private String llave;
    private String llanta;

    @Column(name = "llanta_campo")
    private String llantaCampo;
    @Column(name = "llanta_cantidad")
    private int llantaCantidad;
    @Column(name = "llanta_unitario")
    private float llantaUnitario;
    @Column(name = "llanta_total")
    private float llantaTotal;
    private String alineacion;
    @Column(name = "alineacion_cantidad")
    private int alineacionCantidad;
    @Column(name = "alineacion_unitario")
    private float alineacionUnitario;
    @Column(name = "alineacion_total")
    private float alineacionTotal;
    private String balanceo;
    @Column(name = "balanceo_cantidad")
    private int balanceoCantidad;
    @Column(name = "balanceo_unitario")
    private float balanceoUnitario;
    @Column(name = "balanceo_total")
    private float balanceoTotal;
    @Column(name = "amor_delanteros")
    private String amorDelanteros;
    @Column(name = "amor_del_cantidad")
    private int amorDelCantidad;
    @Column(name = "amor_del_unitario")
    private float amorDelUnitario;
    @Column(name = "amor_del_total")
    private float amorDelTotal;
    @Column(name = "amor_traseros")
    private String amorTraseros;
    @Column(name = "amor_tras_cantidad")
    private int amorTrasCantidad;
    @Column(name = "amor_tras_unitario")
    private float amorTrasUnitario;
    @Column(name = "amor_tras_total")
    private float amorTrasTotal;

    private String suspension;
    private String suspension2;
    @Column(name = "suspension_cantidad2")
    private int suspensionCantidad2;
    @Column(name = "suspension_unitario2")
    private float suspensionUnitario2;
    @Column(name = "suspension_total2")
    private float suspensionTotal2;
    @Column(name = "suspension_cantidad")
    private int suspensionCantidad;
    @Column(name = "suspension_unitario")
    private float suspensionUnitario;
    @Column(name = "suspension_total")
    private float suspensionTotal;
    private String mecanica;
    @Column(name = "mecanica_cantidad")
    private int mecanicaCantidad;
    @Column(name = "mecanica_unitario")
    private float mecanicaUnitario;
    @Column(name = "mecanica_total")
    private float mecanicaTotal;
    private String mecanica2;
    @Column(name = "mecanica_cantidad2")
    private int mecanicaCantidad2;
    @Column(name = "mecanica_unitario2")
    private float mecanicaUnitario2;
    @Column(name = "mecanica_total2")
    private float mecanicaTotal2;
    private String frenos;
    @Column(name = "frenos_cantidad")
    private int frenosCantidad;
    @Column(name = "frenos_unitario")
    private float frenosUnitario;
    @Column(name = "frenos_total")
    private float frenosTotal;
    private String frenos2;
    @Column(name = "frenos_cantidad2")
    private int frenosCantidad2;
    @Column(name = "frenos_unitario2")
    private float frenosUnitario2;
    @Column(name = "frenos_total2")
    private float frenosTotal2;
    private String otros;
    @Column(name = "otros_cantidad")
    private int otrosCantidad;
    @Column(name = "otros_unitario")
    private float otrosUnitario;
    @Column(name = "otros_total")
    private float otrosTotal;
    private String otros2;
    @Column(name = "otros_cantidad2")
    private int otrosCantidad2;
    @Column(name = "otros_unitario2")
    private float otrosUnitario2;
    @Column(name = "otros_total2")
    private float otrosTotal2;
    @Column(name = "subtotal_mecanica")
    private float subTotalMecanica;
    @Column(name = "subtotal_frenos")
    private float subTotalFrenos;
    @Column(name = "subtotal_otros")
    private float subTotalOtros;

    private float adeudo;
    @Column(name = "saldo_favor")
    private float saldoFavor;




}//class

