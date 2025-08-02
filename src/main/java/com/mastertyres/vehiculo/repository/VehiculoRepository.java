package com.mastertyres.vehiculo.repository;

import com.mastertyres.vehiculo.model.Vehiculo;

import com.mastertyres.vehiculo.model.VehiculoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo,Integer> {

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active ")
    List<VehiculoDTO>listarVehiculos(@Param("active") String activo);

}
