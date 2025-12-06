package com.mastertyres.vehiculo.service;


import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.model.VehiculoDTO;

import java.time.LocalDate;
import java.util.List;

public interface IVehiculoService {

    List<VehiculoDTO> listarVehiculos(String vehiculoStatus);

    int eliminarVehiculo(String eliminar,Integer idVehiculo);

    List<VehiculoDTO> buscarVehiculoPorPropietario(String active,String nombre);

    List<VehiculoDTO> buscarVehiculoPorMarca(String active,String marca);

    List<VehiculoDTO> buscarVehiculoPorModelo(String active, String modelo);

    List<VehiculoDTO> buscarVehiculoPorCategoria(String active, String categoria);

    List<VehiculoDTO> buscarVehiculoPorColor(String active, String color);

    List<VehiculoDTO> buscarVehiculoPorAnio(String active, Integer anio);

    List<VehiculoDTO> buscarVehiculoPorAnio(String active, Integer fechaInicio, Integer fechaFin);

    List<VehiculoDTO> buscarVehiculoPorPlacas(String active, String placas);

    List<VehiculoDTO> buscarVehiculoPorNumSerie(String active, String numSerie);

    List<VehiculoDTO> buscarVehiculoPorKilometros(String active, Integer kilometros);

    List<VehiculoDTO> buscarVehiculoPorKilometros(String active, Integer kilometroInicio, Integer kilometroFin);

    List<VehiculoDTO> buscarVehiculoPorUltimoServicio(String active, String ultimoServicio);

    List<VehiculoDTO> buscarVehiculoPorUltimoServicio(String active, String servicioInicio,String servicioFin);

    List<VehiculoDTO> buscarVehiculoPorRegistro(String activo, LocalDate ultimoServicio);

    List<VehiculoDTO> buscarVehiculoPorRegistro(String activo, LocalDate fechaInicio, LocalDate fechaFin);

    List<VehiculoDTO> buscadorVehiculo(String status, String busqueda);

    public Vehiculo buscarVehiculoPorId(Integer vehiculoId, String status);





}//interface

