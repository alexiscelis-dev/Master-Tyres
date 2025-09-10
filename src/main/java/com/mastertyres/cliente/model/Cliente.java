package com.mastertyres.cliente.model;

import com.mastertyres.vehiculo.model.Vehiculo;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "cliente")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "vehiculos")//evita recursión al imprimir el cliente.
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include  // Indica que solo clienteId sera usado para generar los metodos EqualsAndHashCode
    @Column(name = "cliente_id")
    private Integer clienteId;

    //atributos
    @Column(name = "nombre_cliente")
    private String nombre;
    @Column(name = "apellido_cliente")
    private String apellido;
    @Column(name = "segundo_apellido")
    private String segundoApellido;
    private String hobbie;
    private String rfc;
    @Column(name = "num_telefono")
    private String numTelefono;
    private String estado;
    private String ciudad;
    private String domicilio;
    @Column(name = "tipo_cliente")
    private String tipoCliente;
    @Column(name = "fecha_cumpleanios")
    private String fechaCumple;
    private String created_at;
    private String updated_at;
    private String active;
    private String genero;


    //relaciones

    //relacion vehiculo
    //@OneToMany(mappedBy = "cliente",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    //private  List<Vehiculo> vehiculos = new ArrayList<>();
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Vehiculo> vehiculos = new ArrayList<>();



}//clase

