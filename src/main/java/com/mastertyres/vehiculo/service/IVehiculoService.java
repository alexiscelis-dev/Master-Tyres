package com.mastertyres.vehiculo.service;


import com.mastertyres.vehiculo.model.VehiculoDTO;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface IVehiculoService {

    List<VehiculoDTO> listarVehiculos(String vehiculoStatus);

    int eliminarVehiculo(String eliminar,Integer idVehiculo);



}//interface

