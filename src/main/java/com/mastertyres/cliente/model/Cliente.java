package com.mastertyres.cliente.model;

import jakarta.persistence.*;
import lombok.*;

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
    private TipoCliente tipoCliente;
    @Column(name = "fecha_cumpleanios")
    private Date fechaCumple;
    private String createdAt;
    private String updatedAt;
    private String active;

}

