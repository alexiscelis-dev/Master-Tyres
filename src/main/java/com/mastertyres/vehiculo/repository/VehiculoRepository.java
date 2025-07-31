package com.mastertyres.vehiculo.repository;

import com.mastertyres.vehiculo.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo,Integer> {

    @Query("SELECT v FROM  Vehiculo v JOIN FETCH v.marca JOIN FETCH v.modelo")
    List<Vehiculo>listarVehiculos();

}
