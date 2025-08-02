package com.mastertyres.vehiculo.service;


import com.mastertyres.vehiculo.model.VehiculoDTO;


import java.util.List;

public interface IVehiculoService {

    List<VehiculoDTO> listarVehiculos(String vehiculoStatus);

}//interface

