package com.mastertyres.vehiculo.repository;

import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active ")
    List<VehiculoDTO> listarVehiculos(@Param("active") String activo);


    @Transactional
    @Modifying
    @Query("UPDATE Vehiculo v SET v.active = :inactive WHERE v.vehiculoId = :idVehiculo")
    int eliminarVehiculo(@Param("inactive") String eliminar, @Param("idVehiculo") Integer idVehiculo);

    // Trae vehículos de los clientes, junto con marca y modelo
    @Query("SELECT v FROM Vehiculo v " +
            "JOIN FETCH v.marca " +
            "JOIN FETCH v.modelo " +
            "WHERE v.cliente.id IN :clienteIds AND v.active = active")
    List<Vehiculo> listarVehiculosPorClientes(@Param("clienteIds") List<Integer> clienteIds);

    //Metodos buscador

    // el simbolo % (cualquier cosa aqui) se concatena para posteriormente con el like ver si hay coincidencias de palabras
    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND LOWER (CONCAT(c.nombre,' ',COALESCE(c.apellido,''), ' ', COALESCE(c.segundoApellido,'')))" +
            "LIKE  LOWER(CONCAT ('%', :nombre , '%'))   ")
    List<VehiculoDTO> buscarVehiculoPorPropietario(@Param("active") String active, @Param("nombre") String nombre);






    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie,  v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND LOWER(m.nombreMarca) LIKE LOWER(CONCAT('%', :marca, '%')) ")
    List<VehiculoDTO> buscarVehiculoPorMarca(@Param("active") String active, @Param("marca") String marca);


    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND LOWER(mo.nombreModelo) LIKE LOWER(CONCAT('%', :modelo, '%')) ")
    List<VehiculoDTO> buscarVehiculoPorModelo(@Param("active") String active, @Param("modelo") String modelo);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND LOWER(ca.nombreCategoria) LIKE LOWER(CONCAT('%', :categoria, '%')) ")
    List<VehiculoDTO> buscarVehiculoPorCategoria(@Param("active") String active, @Param("categoria") String categoria);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND LOWER(v.color) LIKE LOWER(CONCAT('%', :color, '%')) ")
    List<VehiculoDTO> buscarVehiculoPorColor(@Param("active") String active, @Param("color") String color);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND v.anio = :anio ")
    List<VehiculoDTO> buscarVehiculoPorAnio(@Param("active") String active, @Param("anio") Integer anio);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie,  v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND v.anio BETWEEN :fechaInicio AND :fechaFin")
    List<VehiculoDTO> buscarVehiculoPorAnio(@Param("active") String active, @Param("fechaInicio") Integer fechaInicio, @Param("fechaFin") Integer fechaFin);


    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND LOWER(v.placas) LIKE LOWER(CONCAT('%', :placas, '%'))")
    List<VehiculoDTO> buscarVehiculoPorPlacas(@Param("active") String active, @Param("placas") String placas);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND LOWER(v.numSerie) LIKE LOWER(CONCAT('%', :numSerie, '%'))")
    List<VehiculoDTO> buscarVehiculoPorNumSerie(@Param("active") String active, @Param("numSerie") String numSerie);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND v.kilometros = :kilometros")
    List<VehiculoDTO> buscarVehiculoPorKilometros(@Param("active") String active, @Param("kilometros") Integer kilometros);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie,  v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND v.kilometros BETWEEN :kilometroInicio AND :kilometroFin")
    List<VehiculoDTO> buscarVehiculoPorKilometros(@Param("active") String active, @Param("kilometroInicio") Integer fechaInicio, @Param("kilometroFin") Integer fechaFin);


    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND v.ultimoServicio = :ultimoServicio")
    List<VehiculoDTO> buscarVehiculoPorUltimoServicio(@Param("active") String active, @Param("ultimoServicio") String ultimoServicio);


    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND v.ultimoServicio BETWEEN :servicioInicio AND :servicioFin")
    List<VehiculoDTO> buscarVehiculoPorUltimoServicio(@Param("active") String active, @Param("servicioInicio") String servicioInicio, @Param("servicioFin") String fechaFin);


    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND DATE(v.created_at) = :fechaRegistro")
    List<VehiculoDTO> buscarVehiculoPorRegistro(@Param("active") String active, @Param("fechaRegistro") LocalDate fechaRegistro);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.cliente c " +
            "JOIN v.categoria ca " +
            " WHERE v.active = :active AND DATE(v.created_at) BETWEEN :fechaInicio AND :fechaFin ")
    List<VehiculoDTO> buscarVehiculoPorRegistro(@Param("active") String active, @Param("fechaInicio") LocalDate fechaRegistro, @Param("fechaFin") LocalDate fechaFin);


    // 1. Verificar por ambos (placas o numSerie)
    @Query("SELECT COUNT(v) > 0 FROM Vehiculo v WHERE v.active = 'ACTIVE' AND (v.placas = :placas OR v.numSerie = :numSerie)")
    boolean existeVehiculoPorPlacasONumeroSerie(@Param("placas") String placas, @Param("numSerie") String numSerie);

    // 2. Verificar solo por numSerie
    @Query("SELECT COUNT(v) > 0 FROM Vehiculo v WHERE v.active = 'ACTIVE' AND v.numSerie = :numSerie")
    boolean existeVehiculoPorNumeroSerie(@Param("numSerie") String numSerie);

    // 3. Verificar solo por placas
    @Query("SELECT COUNT(v) > 0 FROM Vehiculo v WHERE v.active = 'ACTIVE' AND v.placas = :placas")
    boolean existeVehiculoPorPlacas(@Param("placas") String placas);


    @Query("SELECT v FROM Vehiculo v " +
            "WHERE v.active = :active " +
            "AND v.marca.marcaId = :marcaId " +
            "AND v.modelo.modeloId = :modeloId")
    List<Vehiculo> listarVehiculosPorMarcaYModelo(@Param("active") String active,
                                                  @Param("marcaId") Integer marcaId,
                                                  @Param("modeloId") Integer modeloId);

    @Query("SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(v.vehiculoId, c.nombre, c.apellido,c.segundoApellido, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.numSerie, v.observaciones, v.kilometros, v.color,v.placas, v.ultimoServicio, " +
            "v.fechaRegistro ) FROM Vehiculo v JOIN v.marca m JOIN v.modelo mo JOIN v.cliente c JOIN v.categoria ca " +
            " WHERE v.active = :status AND " +
            "LOWER (COALESCE(c.nombre, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR " +
            "LOWER (COALESCE(c.apellido, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR " +
            "LOWER (COALESCE(c.segundoApellido, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR " +
            "LOWER (COALESCE(m.nombreMarca, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR " +
            "LOWER (COALESCE(mo.nombreModelo, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR " +
            "LOWER (COALESCE(ca.nombreCategoria, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR " +
            "LOWER (COALESCE(v.color, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR " +
            "CAST (v.anio AS STRING) LIKE CONCAT('%', :busqueda , '%') OR " +
            "LOWER (COALESCE(v.numSerie, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR " +
            "CAST (v.kilometros AS STRING) LIKE CONCAT('%', :busqueda , '%') OR " +
            "LOWER (COALESCE(v.placas, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) ")
    List<VehiculoDTO>buscadorVehiculos(@Param("status")String status,@Param("busqueda")String busqueda);


}//interface
