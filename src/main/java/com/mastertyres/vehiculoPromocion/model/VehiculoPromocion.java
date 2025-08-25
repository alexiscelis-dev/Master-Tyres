package com.mastertyres.vehiculoPromocion.model;


import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.promociones.model.Promocion;
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
    private String annio;
}
