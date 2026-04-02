package com.mastertyres.clientesPromocion.entity;

import com.mastertyres.cliente.entity.Cliente;
import com.mastertyres.promociones.entity.Promocion;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cliente_promocion")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class ClientesPromocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_promocion_id")
    private Integer ClientePromocionID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promocion_id")
    private Promocion promocion;
}
