package com.mastertyres.vehiculo.model;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "vehiculo")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehiculo_id")
    private Integer vehiculoId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", insertable = false, updatable = false)
    private Cliente cliente;

    //relacion marca
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id", insertable = false,updatable = false)//Indicar FK marca //false para poder guardardesde marcaId sin usar un objeto marca
    private Marca marca;

    //relacion modelo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id", insertable = false,updatable = false)
    private Modelo modelo;

    //relacion categoria

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    private Integer anio;
    private Integer kilometros;
    private String color;
    private String placas;
    @Column(name = "numero_serie")
    private String numSerie;
    @Column(name = "fecha_registro")
    private String fechaRegistro;
    @Column(name = "ultimo_servicio")
    private String ultimoServicio;
    private String created_at;
    private String updated_at;
    private String active;
    private String observaciones;


}//clase
