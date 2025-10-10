package com.mastertyres.vehiculo.model;

import lombok.Data;

//Tiene las relaciones de las tablas marcas modelo y categoria y atributo nombre cliente

@Data
public class VehiculoDTO {

private Integer id;
private String nombreCliente;
private String apellido;
private String segundoApellido;
private String numTelefono;
private String nombreMarca;
private String nombreModelo;
private String nombreCategoria;
private Integer anio;
private Integer kilometros;
private String color;
private String placas;
private String numSerie;
private String observaciones;
private String fechaRegistro;
private String ultimoServicio;
private String created_at;
private String updated_at;
private String active;
private Integer contador_mensaje;


//contructor para consulta listar vehiculos


    public VehiculoDTO(Integer id, String nombreCliente, String apellido, String segundoApellido, String numTelefono,
                       String nombreMarca, String nombreModelo, String nombreCategoria, Integer anio,
                       String numSerie, String observaciones, Integer kilometros, String color, String placas,
                       String ultimoServicio, String fechaRegistro, Integer contador_mensaje) {

        this.id = id;
        this.nombreCliente = nombreCliente;
        this.apellido = apellido;
        this.segundoApellido = segundoApellido;
        this.numTelefono = numTelefono;
        this.nombreMarca = nombreMarca;
        this.nombreModelo = nombreModelo;
        this.nombreCategoria = nombreCategoria;
        this.anio = anio;
        this.kilometros = kilometros;
        this.color = color;
        this.placas = placas;
        this.numSerie = numSerie;
        this.observaciones = observaciones;
        this.fechaRegistro = fechaRegistro;
        this.ultimoServicio = ultimoServicio;
        this.contador_mensaje = contador_mensaje;
    }


}//clase
