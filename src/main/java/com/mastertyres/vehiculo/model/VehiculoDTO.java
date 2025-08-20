package com.mastertyres.vehiculo.model;

import lombok.Data;

//Tiene las relaciones de las tablas marcas modelo y categoria y atributo nombre cliente

@Data
public class VehiculoDTO {

private Integer id;
private String nombreCliente;
private String apellido;
private String segundoApellido;
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


//contructor para consulta listar vehiculos


    public VehiculoDTO(Integer id, String nombreCliente, String apellido, String segundoApellido, String nombreMarca, String nombreModelo, String nombreCategoria, Integer anio, String numSerie,String observaciones, Integer kilometros, String color, String placas, String ultimoServicio, String fechaRegistro) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.apellido = apellido;
        this.segundoApellido = segundoApellido;
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
    }


}//clase
