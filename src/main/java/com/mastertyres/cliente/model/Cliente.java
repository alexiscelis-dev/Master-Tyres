package com.mastertyres.cliente.model;

import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.vehiculo.model.Vehiculo;
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
    private int clienteId;


    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehiculo> vehiculos = new ArrayList<>();

/*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marca_id")
    private Marca marca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modelo_id")
    private Modelo modelo;
*/

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

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente")
    private TipoCliente tipoCliente;

    @Column(name = "fecha_cumpleanios")
    private Date fechaCumple;
    private String createdAt;
    private String updatedAt;
    private String active;


}

