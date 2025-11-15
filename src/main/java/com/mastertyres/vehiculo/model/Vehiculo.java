package com.mastertyres.vehiculo.model;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "vehiculo")
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"cliente","marca","modelo","categoria"}) // parte de evitar recursión al imprimir el cliente.
@EqualsAndHashCode
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehiculo_id")
    private Integer vehiculoId;

    //relacion cliente
    //@JoinColumn(name = "cliente_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    //relacion marca
//    @JoinColumn(name = "marca_id", insertable = false,updatable = false)//Indicar FK marca //false para poder guardardesde marcaId sin usar un objeto marca
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")//Indicar FK marca //false para poder guardardesde marcaId sin usar un objeto marca
    private Marca marca;

    //relacion modelo
    //@JoinColumn(name = "modelo_id", insertable = false,updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id")
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
    private String observaciones;
    @Column(name = "fecha_registro")
    private String fechaRegistro;
    @Column(name = "ultimo_servicio")
    private String ultimoServicio;
    private String created_at;
    private String updated_at;
    private String active;
    private Integer contador_mensaje;



}//clase
