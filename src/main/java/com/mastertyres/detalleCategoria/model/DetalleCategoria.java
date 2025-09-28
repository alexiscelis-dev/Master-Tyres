package com.mastertyres.detalleCategoria.model;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalle_categoria")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class DetalleCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer detalle_categoria_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    private Marca marca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id")
    private Modelo modelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
