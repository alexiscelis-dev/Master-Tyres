package com.mastertyres.vehiculo.repository;

import com.mastertyres.vehiculo.model.Vehiculo;

import com.mastertyres.vehiculo.model.VehiculoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active ")
    List<VehiculoDTO> listarVehiculos(@Param("active") String activo);

    @Modifying
    @Query("UPDATE Vehiculo v SET v.active = :inactive WHERE v.vehiculoId = :idVehiculo")
    int eliminarVehiculo(@Param("inactive") String eliminar, @Param("idVehiculo") Integer idVehiculo);

    // Trae vehículos de los clientes, junto con marca y modelo
    @Query("SELECT v FROM Vehiculo v " +
            "JOIN FETCH v.marca " +
            "JOIN FETCH v.modelo " +
            "WHERE v.cliente.id IN :clienteIds")
    List<Vehiculo> listarVehiculosPorClientes(@Param("clienteIds") List<Integer> clienteIds);

    //Metodos buscador

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND CONCAT(CONCAT(CONCAT(c.nombre, ' '), CONCAT(c.apellido, ' ')), c.segundoApellido) = :nombre ")
    List<VehiculoDTO> buscarVehiculoPorPropietario(@Param("active") String active, @Param("nombre") String nombre);
//usar el operador OR

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND m.nombreMarca = :marca ")
    List<VehiculoDTO> buscarVehiculoPorMarca(@Param("active") String active, @Param("marca") String marca);


    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND mo.nombreModelo = :modelo ")
    List<VehiculoDTO> buscarVehiculoPorModelo(@Param("active") String active, @Param("modelo") String modelo);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND ca.nombreCategoria = :categoria ")
    List<VehiculoDTO> buscarVehiculoPorCategoria(@Param("active") String active, @Param("categoria") String categoria);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND v.color = :color ")
    List<VehiculoDTO> buscarVehiculoPorColor(@Param("active") String active, @Param("color") String color);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND v.anio = :anio ")
    List<VehiculoDTO> buscarVehiculoPorAnio(@Param("active") String active, @Param("anio") Integer anio);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND v.anio BETWEEN :fechaInicio AND :fechaFin")
    List<VehiculoDTO> buscarVehiculoPorAnio(@Param("active") String active, @Param("fechaInicio") Integer fechaInicio, @Param("fechaFin") Integer fechaFin);


    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND v.placas = :placas")
    List<VehiculoDTO> buscarVehiculoPorPlacas(@Param("active") String active, @Param("placas") String placas);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND v.numSerie = :numSerie")
    List<VehiculoDTO> buscarVehiculoPorNumSerie(@Param("active") String active, @Param("numSerie") String numSerie);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND v.kilometros = :kilometros")
    List<VehiculoDTO> buscarVehiculoPorKilometros(@Param("active") String active, @Param("kilometros") Integer kilometros);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND v.kilometros BETWEEN :kilometroInicio AND :kilometroFin")
    List<VehiculoDTO> buscarVehiculoPorKilometros(@Param("active") String active, @Param("kilometroInicio") Integer fechaInicio, @Param("kilometroFin") Integer fechaFin);


    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND v.created_at = :ultimoServicio")
    List<VehiculoDTO> buscarVehiculoPorUltimoServicio(@Param("active") String active, @Param("ultimoServicio") String ultimoServicio);


    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND v.ultimoServicio BETWEEN :servicioInicio AND :servicioFin")
    List<VehiculoDTO> buscarVehiculoPorUltimoServicio(@Param("active") String active, @Param("servicioInicio") String servicioInicio, @Param("servicioFin") String fechaFin);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND DATE(v.created_at) = :fechaRegistro")
    List<VehiculoDTO> buscarVehiculoPorRegistro(@Param("active") String active, @Param("fechaRegistro") LocalDate fechaRegistro);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND DATE(v.created_at) BETWEEN :fechaInicio AND :fechaFin ")
    List<VehiculoDTO> buscarVehiculoPorRegistro(@Param("active") String active, @Param("fechaInicio") LocalDate fechaRegistro, @Param("fechaFin") LocalDate fechaFin);


}//interface
