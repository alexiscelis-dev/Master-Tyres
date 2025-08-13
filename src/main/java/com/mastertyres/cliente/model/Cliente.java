package com.mastertyres.cliente.model;

import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "cliente")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Integer clienteId;

    //atributos
    @Column(name = "nombre_cliente")
    private String nombre;
    @Column(name = "apellido_cliente")
    private String apellido;
    @Column(name = "segundo_apellido")
    private String segundoApellido;
    private String curp;
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


    //relaciones

    //relacion vehiculo
    //@OneToMany(mappedBy = "cliente",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    //private  List<Vehiculo> vehiculos = new ArrayList<>();
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Vehiculo> vehiculos = new ArrayList<>();

    // orphanRemoval = true si se elimina de la lista tambien de la base de datos
    //cascade = CascadeType.ALL cambios en cliente afectan - vehiculo


}//clase

