package com.mastertyres.vehiculo.repository;

import com.mastertyres.vehiculo.entity.Vehiculo;
import com.mastertyres.vehiculo.DTOs.VehiculoDTO;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

    final String SELECT_VEHICULO_DTO = """ 
              SELECT new com.mastertyres.vehiculo.model.VehiculoDTO(
                    v.vehiculoId,
                    c.nombre,
                    c.apellido,
                    c.segundoApellido,
                    c.numTelefono,
                    m.nombreMarca,
                    mo.nombreModelo,
                    ca.nombreCategoria,
                    v.anio,
                    v.numSerie,
                    v.observaciones,
                    v.kilometros,
                    v.color,
                    v.placas,
                    v.ultimoServicio,
                    v.fechaRegistro,
                    v.contador_mensaje
                )
                FROM Vehiculo v
                JOIN v.marca m
                JOIN v.modelo mo
                JOIN v.cliente c
                JOIN v.categoria ca
            """;
    final String UPDATE_VEHICULO = "UPDATE Vehiculo v SET";
    final String SELECT_FROM_VEHICULO = "SELECT v FROM Vehiculo v";


    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = 'ACTIVE' AND FUNCTION('DATE', v.ultimoServicio) <= :fechaLimite ")
    List<VehiculoDTO> listarVehiculosConServicioVencido(@Param("fechaLimite") LocalDate fechaLimite);

    @Query(SELECT_VEHICULO_DTO +  " " + "WHERE v.active = 'ACTIVE' AND FUNCTION('DATE', v.ultimoServicio) <= :fechaLimite")
    Page<VehiculoDTO> listarVehiculosConServicioVencidoPaginado(@Param("fechaLimite") LocalDate fechaLimite, Pageable pageable);

    @Query(
            SELECT_VEHICULO_DTO + " " +
                     """
                        WHERE v.active = 'ACTIVE'
                        AND FUNCTION('DATE', v.ultimoServicio) <= :fechaLimite
                        AND 
                        (LOWER (COALESCE(c.nombre, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR 
                        LOWER (COALESCE(c.apellido, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR 
                        LOWER (COALESCE(c.segundoApellido, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR 
                        LOWER (COALESCE(m.nombreMarca, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR 
                        LOWER (COALESCE(mo.nombreModelo, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR 
                        LOWER (COALESCE(ca.nombreCategoria, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR 
                        LOWER (COALESCE(v.color, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR 
                        CAST (v.anio AS STRING) LIKE CONCAT('%', :busqueda , '%') OR 
                        LOWER (COALESCE(v.numSerie, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR 
                        LOWER (COALESCE(v.placas, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) )
                    """)
    List<VehiculoDTO> BuscarVehiculosConServicioVencido(@Param("fechaLimite") LocalDate fechaLimite, @Param("busqueda") String busqueda);

    @Query(SELECT_VEHICULO_DTO + " " +
                """
                WHERE v.active = 'ACTIVE'
                AND FUNCTION('DATE', v.ultimoServicio) <= :fechaLimite
                AND 
                (LOWER (COALESCE(c.nombre, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR 
                LOWER (COALESCE(c.apellido, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR 
                LOWER (COALESCE(c.segundoApellido, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR 
                LOWER (COALESCE(m.nombreMarca, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR 
                LOWER (COALESCE(mo.nombreModelo, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR 
                LOWER (COALESCE(ca.nombreCategoria, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR 
                LOWER (COALESCE(v.color, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR 
                CAST (v.anio AS STRING) LIKE CONCAT('%', :busqueda , '%') OR 
                LOWER (COALESCE(v.numSerie, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) OR 
                LOWER (COALESCE(v.placas, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) )
            """)
    Page<VehiculoDTO> BuscarVehiculosConServicioVencidoPaginado(@Param("fechaLimite") LocalDate fechaLimite, @Param("busqueda") String busqueda, Pageable pageable);


    @Query("SELECT COUNT(v) FROM Vehiculo v WHERE v.active = :active")
    long contarVehiculosActivos(@Param("active") String active);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active ")
    List<VehiculoDTO> listarVehiculos(@Param("active") String activo);


    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active ORDER BY c.nombre ASC, c.apellido ASC, c.segundoApellido ASC ")
    Page<VehiculoDTO> listarVehiculosPaginado(@Param("active") String activo, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_FROM_VEHICULO + " " +
            "WHERE v.active = 'ACTIVE' AND v.vehiculoId = :vehiculoId")
    Vehiculo VehiculoSinDTO(@Param("vehiculoId") Integer vehiculoId);


    @Modifying
    @Query(UPDATE_VEHICULO + " " + "v.active = :inactive WHERE v.cliente.clienteId = :idCliente")
    int eliminarVehiculosPorCliente(@Param("inactive") String eliminar, @Param("idCliente") Integer idCliente);


    @Modifying
    @Query(UPDATE_VEHICULO + " " + "v.active = :inactive WHERE v.vehiculoId = :idVehiculo")
    int eliminarVehiculo(@Param("inactive") String eliminar, @Param("idVehiculo") Integer idVehiculo);

    // Trae vehículos de los clientes, junto con marca y modelo
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_FROM_VEHICULO + " " +
            "JOIN FETCH v.marca " +
            "JOIN FETCH v.modelo " +
            "WHERE v.cliente.id IN :clienteIds AND v.active = active")
    List<Vehiculo> listarVehiculosPorClientes(@Param("clienteIds") List<Integer> clienteIds);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_FROM_VEHICULO + " " +
            "JOIN FETCH v.marca " +
            "JOIN FETCH v.modelo " +
            "WHERE v.cliente.id IN :clienteIds AND v.active = :active")
    Page<Vehiculo> listarVehiculosPorClientesPaginado(@Param("active") String active, @Param("clienteIds") List<Integer> clienteIds, Pageable pageable);

    //Metodos buscador

    // el simbolo % (cualquier cosa aqui) se concatena para posteriormente con el like ver si hay coincidencias de palabras
    @Query(SELECT_VEHICULO_DTO + " " +
            """ 
            WHERE v.active = :active AND
             LOWER (CONCAT(c.nombre,' ',COALESCE(c.apellido,''), ' ', COALESCE(c.segundoApellido,'')))
             LIKE  LOWER(CONCAT ('%', :nombre , '%'))  
            """)
    List<VehiculoDTO> buscarVehiculoPorPropietario(@Param("active") String active, @Param("nombre") String nombre);


    @Query(SELECT_VEHICULO_DTO + " " +
            """
            WHERE v.active = :active AND LOWER 
            (CONCAT(c.nombre,' ',COALESCE(c.apellido,''), ' ', COALESCE(c.segundoApellido,''))) LIKE  LOWER(CONCAT ('%', :nombre , '%'))   
            """)
    Page<VehiculoDTO> buscarVehiculoPorPropietarioPaginado(@Param("active") String active, @Param("nombre") String nombre, Pageable pageable);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND LOWER(m.nombreMarca) LIKE LOWER(CONCAT('%', :marca, '%')) ")
    List<VehiculoDTO> buscarVehiculoPorMarca(@Param("active") String active, @Param("marca") String marca);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND LOWER(m.nombreMarca) LIKE LOWER(CONCAT('%', :marca, '%')) ")
    Page<VehiculoDTO> buscarVehiculoPorMarcaPaginado(@Param("active") String active, @Param("marca") String marca, Pageable pageable);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND LOWER(mo.nombreModelo) LIKE LOWER(CONCAT('%', :modelo, '%')) ")
    List<VehiculoDTO> buscarVehiculoPorModelo(@Param("active") String active, @Param("modelo") String modelo);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND LOWER(mo.nombreModelo) LIKE LOWER(CONCAT('%', :modelo, '%')) ")
    Page<VehiculoDTO> buscarVehiculoPorModeloPaginado(@Param("active") String active, @Param("modelo") String modelo, Pageable pageable);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND LOWER(ca.nombreCategoria) LIKE LOWER(CONCAT('%', :categoria, '%')) ")
    List<VehiculoDTO> buscarVehiculoPorCategoria(@Param("active") String active, @Param("categoria") String categoria);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND LOWER(ca.nombreCategoria) LIKE LOWER(CONCAT('%', :categoria, '%')) ")
    Page<VehiculoDTO> buscarVehiculoPorCategoriaPaginado(@Param("active") String active, @Param("categoria") String categoria, Pageable pageable);


    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND LOWER(v.color) LIKE LOWER(CONCAT('%', :color, '%')) ")
    List<VehiculoDTO> buscarVehiculoPorColor(@Param("active") String active, @Param("color") String color);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND LOWER(v.color) LIKE LOWER(CONCAT('%', :color, '%')) ")
    Page<VehiculoDTO> buscarVehiculoPorColorPaginado(@Param("active") String active, @Param("color") String color, Pageable pageable);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND v.anio = :anio ")
    List<VehiculoDTO> buscarVehiculoPorAnio(@Param("active") String active, @Param("anio") Integer anio);

    @Query(SELECT_VEHICULO_DTO +  " " + "WHERE v.active = :active AND v.anio = :anio ")
    Page<VehiculoDTO> buscarVehiculoPorAnioPaginado(@Param("active") String active, @Param("anio") Integer anio, Pageable pageable);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND v.anio BETWEEN :fechaInicio AND :fechaFin")
    List<VehiculoDTO> buscarVehiculoPorAnio(@Param("active") String active, @Param("fechaInicio") Integer fechaInicio, @Param("fechaFin") Integer fechaFin);


    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND v.anio BETWEEN :fechaInicio AND :fechaFin")
    Page<VehiculoDTO> buscarVehiculoPorAnioPaginado(@Param("active") String active, @Param("fechaInicio") Integer fechaInicio, @Param("fechaFin") Integer fechaFin, Pageable pageable);


    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND LOWER(v.placas) LIKE LOWER(CONCAT('%', :placas, '%'))")
    List<VehiculoDTO> buscarVehiculoPorPlacas(@Param("active") String active, @Param("placas") String placas);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND LOWER(v.placas) LIKE LOWER(CONCAT('%', :placas, '%'))")
    Page<VehiculoDTO> buscarVehiculoPorPlacasPaginado(@Param("active") String active, @Param("placas") String placas, Pageable pageable);

    @Query(SELECT_VEHICULO_DTO +  " " + "WHERE v.active = :active AND LOWER(v.numSerie) LIKE LOWER(CONCAT('%', :numSerie, '%'))")
    List<VehiculoDTO> buscarVehiculoPorNumSerie(@Param("active") String active, @Param("numSerie") String numSerie);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND LOWER(v.numSerie) LIKE LOWER(CONCAT('%', :numSerie, '%'))")
    Page<VehiculoDTO> buscarVehiculoPorNumSeriePaginado(@Param("active") String active, @Param("numSerie") String numSerie, Pageable pageable);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND v.kilometros = :kilometros")
    List<VehiculoDTO> buscarVehiculoPorKilometros(@Param("active") String active, @Param("kilometros") Integer kilometros);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND v.kilometros = :kilometros")
    Page<VehiculoDTO> buscarVehiculoPorKilometrosPaginado(@Param("active") String active, @Param("kilometros") Integer kilometros, Pageable pageable);

    @Query(SELECT_VEHICULO_DTO +  " " +"WHERE v.active = :active AND v.kilometros BETWEEN :kilometroInicio AND :kilometroFin")
    List<VehiculoDTO> buscarVehiculoPorKilometros(@Param("active") String active, @Param("kilometroInicio") Integer fechaInicio, @Param("kilometroFin") Integer fechaFin);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND v.kilometros BETWEEN :kilometroInicio AND :kilometroFin")
    Page<VehiculoDTO> buscarVehiculoPorKilometrosPaginado(@Param("active") String active, @Param("kilometroInicio") Integer fechaInicio, @Param("kilometroFin") Integer fechaFin, Pageable pageable);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND v.ultimoServicio = :ultimoServicio")
    List<VehiculoDTO> buscarVehiculoPorUltimoServicio(@Param("active") String active, @Param("ultimoServicio") String ultimoServicio);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND DATE(v.ultimoServicio) = :ultimoServicio")
    Page<VehiculoDTO> buscarVehiculoPorUltimoServicioPaginado(@Param("active") String active, @Param("ultimoServicio") LocalDate ultimoServicio, Pageable pageable);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND v.ultimoServicio BETWEEN :servicioInicio AND :servicioFin")
    List<VehiculoDTO> buscarVehiculoPorUltimoServicio(@Param("active") String active, @Param("servicioInicio") String servicioInicio, @Param("servicioFin") String fechaFin);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND DATE(v.ultimoServicio) BETWEEN :servicioInicio AND :servicioFin")
    Page<VehiculoDTO> buscarVehiculoPorUltimoServicioPaginado(@Param("active") String active, @Param("servicioInicio") LocalDate servicioInicio, @Param("servicioFin") LocalDate fechaFin, Pageable pageable);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND DATE(v.created_at) = :fechaRegistro")
    List<VehiculoDTO> buscarVehiculoPorRegistro(@Param("active") String active, @Param("fechaRegistro") LocalDate fechaRegistro);

    @Query(SELECT_VEHICULO_DTO + " " + "WHERE v.active = :active AND DATE(v.created_at) = :fechaRegistro")
    Page<VehiculoDTO> buscarVehiculoPorRegistroPaginado(@Param("active") String active, @Param("fechaRegistro") LocalDate fechaRegistro, Pageable pageable);

    @Query(SELECT_VEHICULO_DTO +  " " + "WHERE v.active = :active AND DATE(v.created_at) BETWEEN :fechaInicio AND :fechaFin ")
    List<VehiculoDTO> buscarVehiculoPorRegistro(@Param("active") String active, @Param("fechaInicio") LocalDate fechaRegistro, @Param("fechaFin") LocalDate fechaFin);


    @Query(SELECT_VEHICULO_DTO +  " " + "WHERE v.active = :active AND DATE(v.created_at) BETWEEN :fechaInicio AND :fechaFin ")
    Page<VehiculoDTO> buscarVehiculoPorRegistroPaginado(@Param("active") String active, @Param("fechaInicio") LocalDate fechaRegistro, @Param("fechaFin") LocalDate fechaFin, Pageable pageable);

    // 1. Verificar por ambos (placas o numSerie)
    @Query("SELECT COUNT(v) > 0 FROM Vehiculo v WHERE v.active = 'ACTIVE' AND (v.placas = :placas OR v.numSerie = :numSerie)")
    boolean existeVehiculoPorPlacasONumeroSerie(@Param("placas") String placas, @Param("numSerie") String numSerie);

    // 2. Verificar solo por numSerie
    @Query("SELECT COUNT(v) > 0 FROM Vehiculo v WHERE v.active = 'ACTIVE' AND v.numSerie = :numSerie")
    boolean existeVehiculoPorNumeroSerie(@Param("numSerie") String numSerie);

    //Cambiar nombre de metodo
    @Query("SELECT COUNT(v) > 0 FROM Vehiculo v WHERE v.active = 'ACTIVE' AND v.numSerie = :numSerie AND v.vehiculoId <> :vehiculoId")
    boolean existeVehiculoNumeroSerie_Editar(@Param("numSerie") String numSerie, @Param("vehiculoId") Integer vehiculoId);

    // 3. Verificar solo por placas
    @Query("SELECT COUNT(v) > 0 FROM Vehiculo v WHERE v.active = 'ACTIVE' AND v.placas = :placas")
    boolean existeVehiculoPorPlacas(@Param("placas") String placas);

    //Cambiar nombre de metodo
    @Query("SELECT COUNT(v) > 0 FROM Vehiculo v WHERE v.active = 'ACTIVE' AND v.placas = :placas AND v.vehiculoId <> :vehiculoId")
    boolean existeVehiculoPlacas_Editar(@Param("placas") String placas, @Param("vehiculoId") Integer vehiculoId);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_FROM_VEHICULO + " " +
            "WHERE v.active = :active " +
            "AND v.marca.marcaId = :marcaId " +
            "AND v.modelo.modeloId = :modeloId")
    List<Vehiculo> listarVehiculosPorMarcaYModelo(@Param("active") String active,
                                                  @Param("marcaId") Integer marcaId,
                                                  @Param("modeloId") Integer modeloId);

    @Query(SELECT_VEHICULO_DTO +
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
            "LOWER (COALESCE(v.placas, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) ")
    List<VehiculoDTO> buscadorVehiculos(@Param("status") String status, @Param("busqueda") String busqueda);

    @Query(SELECT_VEHICULO_DTO +
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
            "LOWER (COALESCE(v.placas, '')) LIKE LOWER (CONCAT('%', :busqueda ,'%')) ")
    Page<VehiculoDTO> buscadorVehiculosPaginado(@Param("status") String status, @Param("busqueda") String busqueda, Pageable pageable);

    @Query("""
            SELECT COUNT(v)
            FROM Vehiculo v
            WHERE v.active = :status
              AND (
            
                    LOWER(v.cliente.nombre)          LIKE LOWER(CONCAT('%', :busqueda, '%'))
                 OR LOWER(v.cliente.apellido)        LIKE LOWER(CONCAT('%', :busqueda, '%'))
                 OR LOWER(v.cliente.segundoApellido) LIKE LOWER(CONCAT('%', :busqueda, '%'))
            
            
                 OR LOWER(v.marca.nombreMarca)       LIKE LOWER(CONCAT('%', :busqueda, '%')) 
                 OR LOWER(v.modelo.nombreModelo)     LIKE LOWER(CONCAT('%', :busqueda, '%'))  
                 OR LOWER(v.categoria.nombreCategoria) LIKE LOWER(CONCAT('%', :busqueda, '%'))
            
                 OR CAST(v.anio AS string)             LIKE CONCAT('%', :busqueda, '%')
                 OR CAST(v.kilometros AS string)       LIKE CONCAT('%', :busqueda, '%')
            
                 OR LOWER(v.color)                   LIKE LOWER(CONCAT('%', :busqueda, '%'))
                 OR LOWER(v.placas)                  LIKE LOWER(CONCAT('%', :busqueda, '%'))
                 OR LOWER(v.numSerie)                  LIKE LOWER(CONCAT('%', :busqueda, '%'))
                 OR LOWER(v.observaciones)             LIKE LOWER(CONCAT('%', :busqueda, '%'))
                 OR LOWER(v.fechaRegistro)             LIKE LOWER(CONCAT('%', :busqueda, '%'))
                 OR LOWER(v.ultimoServicio)            LIKE LOWER(CONCAT('%', :busqueda, '%'))
                 OR LOWER(v.created_at)                LIKE LOWER(CONCAT('%', :busqueda, '%'))
                 OR LOWER(v.updated_at)                LIKE LOWER(CONCAT('%', :busqueda, '%'))
              )
            """)
    long contarVehiculosPorBusquedaGeneral(
            @Param("status") String status,
            @Param("busqueda") String busqueda
    );

    // Reasignar marca por ID
    @Modifying
    @Query(UPDATE_VEHICULO + " " + "v.marca.marcaId = 1 WHERE v.marca.marcaId = :marcaId")
    int reasignarMarcaPorId(Integer marcaId);

    // Reasignar modelo y categoría a genéricos por marca
    @Modifying
    @Query(UPDATE_VEHICULO + " " + "v.modelo.modeloId = 1, v.categoria.categoriaId = 1 WHERE v.marca.marcaId = :marcaId")
    int reasignarModeloYCategoriaPorMarca(Integer marcaId);

    // Reasignar modelo específico a modelo genérico
    @Modifying
    @Query(UPDATE_VEHICULO + " " + "v.modelo.modeloId = 1, v.categoria.categoriaId = 1 WHERE v.modelo.modeloId = :modeloId")
    int reasignarModeloPorId(Integer modeloId);

    // Reasignar categoria específico a categoria genérico
    @Modifying
    @Query(UPDATE_VEHICULO + " " + "v.categoria.categoriaId = 1 WHERE v.categoria.categoriaId = :categoriaId")
    int reasignarCategoriaPorId(Integer categoriaId);

    //En esta consulta si se utiliza el Joi Fetch para evitar el error LazyInitializationException y porque no son muchos datos
    //Buscar un cliente y su vehiculo mediante sus respectivas ids, se utiliza en Nota
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_FROM_VEHICULO + " " +
            "JOIN FETCH v.marca m "+
            "JOIN FETCH v.modelo mo " +
            "JOIN FETCH v.categoria ca " +
            "WHERE  v.vehiculoId = :vehiculoId AND v.active = :status")
    Vehiculo buscarVehiculoPorId(@Param("vehiculoId") Integer vehiculoId, @Param("status") String status);


}//interface
