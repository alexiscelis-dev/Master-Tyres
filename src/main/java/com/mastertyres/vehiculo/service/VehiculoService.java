package com.mastertyres.vehiculo.service;

import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.repository.VehiculoRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehiculoService implements IVehiculoService {

    private VehiculoRepository vehiculoRepository;



    @Autowired
    public VehiculoService(VehiculoRepository vehiculoRepository){
        this.vehiculoRepository = vehiculoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehiculo> listarVehiculos() {
        return vehiculoRepository.listarVehiculos();
    }




}
