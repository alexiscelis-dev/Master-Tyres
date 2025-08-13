package com.mastertyres.vehiculo.repository;

import com.mastertyres.vehiculo.model.Vehiculo;

import com.mastertyres.vehiculo.model.VehiculoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo,Integer> {

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active ")
    List<VehiculoDTO>listarVehiculos(@Param("active") String activo);

    @Modifying
    @Query("UPDATE Vehiculo v SET v.active = :inactive WHERE v.vehiculoId = :idVehiculo")
    int eliminarVehiculo(@Param("inactive")String eliminar,@Param("idVehiculo") Integer idVehiculo);

    // Trae vehículos de los clientes, junto con marca y modelo
    @Query("SELECT v FROM Vehiculo v " +
            "JOIN FETCH v.marca " +
            "JOIN FETCH v.modelo " +
            "WHERE v.cliente.id IN :clienteIds")
    List<Vehiculo> listarVehiculosPorClientes(@Param("clienteIds") List<Integer> clienteIds);


}
