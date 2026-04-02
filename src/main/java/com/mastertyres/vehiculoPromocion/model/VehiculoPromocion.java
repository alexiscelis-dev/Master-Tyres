package com.mastertyres.vehiculoPromocion.model;


import com.mastertyres.marca.entity.Marca;
import com.mastertyres.modelo.entity.Modelo;
import com.mastertyres.promociones.entity.Promocion;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehiculo_promocion")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class VehiculoPromocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehiculo_promocion_id")
    private Integer vehiculoPromocionID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    private Marca marca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id")
    private Modelo modelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promocion_id")
    private Promocion promocion;


    @Column(name = "anio")
    private Integer annio;
}
