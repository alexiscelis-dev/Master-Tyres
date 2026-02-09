package com.mastertyres.cliente.repository;

import com.mastertyres.cliente.model.Cliente;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("""
                SELECT DISTINCT c 
                FROM Cliente c
                JOIN c.vehiculos v
                JOIN VehiculoPromocion vp ON vp.marca = v.marca 
                                        AND vp.modelo = v.modelo
                                        AND vp.annio = v.anio
                WHERE vp.promocion.promocionId = :promocionId
                  AND c.active = 'ACTIVE'
                  AND v.active = 'ACTIVE'
            """)
    List<Cliente> findClientesAplicables(@Param("promocionId") Integer promocionId);


    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.active = 'ACTIVE' AND c.rfc = :rfc")
    boolean existeClientePorRFC(@Param("rfc") String rfc);

    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.active = 'ACTIVE' AND c.rfc = :rfc and c.clienteId <> :idCliente")
    boolean existeClienteRFC_Editar(@Param("rfc") String rfc, @Param("idCliente") Integer idCliente);

    // Consulta base: solo clientes activos
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active")
    List<Cliente> listarClientesActivos(@Param("active") String active);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active ORDER BY c.nombre ASC, c.apellido ASC, c.segundoApellido ASC")
    Page<Cliente> listarClientesActivos(@Param("active") String active, Pageable pageable);


    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.active = :active")
    long contarClientesActivos(@Param("active") String active);


    @Transactional
    @Modifying
    @Query("UPDATE Cliente c SET c.active = :inactive WHERE c.clienteId = :idCliente")
    int eliminarCliente(@Param("inactive") String eliminar, @Param("idCliente") Integer idCliente);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = 'ACTIVE' AND (" +
            "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(c.apellido) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(c.segundoApellido) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(c.hobbie) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(c.domicilio) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(c.fechaCumple) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(c.created_at) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(c.updated_at) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(c.rfc) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(c.numTelefono) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(c.tipoCliente) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(c.ciudad) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(c.estado) LIKE LOWER(CONCAT('%', :filtro, '%')))")
    List<Cliente> buscarClientes(@Param("filtro") String filtro);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER( CONCAT( CONCAT(CONCAT(c.nombre, ' '), " +
            "COALESCE(c.apellido, '')), CONCAT(' ', COALESCE(c.segundoApellido, '')) ) ) " +
            "LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Cliente> buscarClientePorNombre(@Param("active") String active, @Param("nombre") String nombre);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER( CONCAT( CONCAT(CONCAT(c.nombre, ' '), " +
            "COALESCE(c.apellido, '')), CONCAT(' ', COALESCE(c.segundoApellido, '')) ) ) " +
            "LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Cliente> buscarClientePorNombrePaginado(@Param("active") String active,
                                                 @Param("nombre") String nombre,
                                                 Pageable pageable);

@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND c.numTelefono = :numTelefono")
    List<Cliente> buscarClientePorNumTelefono(@Param("active") String active, @Param("numTelefono") String numTelefono);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND c.numTelefono = :numTelefono")
    Page<Cliente> buscarClientePorNumTelefonoPaginado(@Param("active") String active, @Param("numTelefono") String numTelefono, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER (COALESCE(c.estado, '')) LIKE LOWER (CONCAT('%', :estado , '%'))")
    List<Cliente> buscarClientePorEstado(@Param("active") String active, @Param("estado") String estado);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER (COALESCE(c.estado, '')) LIKE LOWER (CONCAT('%', :estado , '%'))")
    Page<Cliente> buscarClientePorEstadoPaginado(@Param("active") String active, @Param("estado") String estado, Pageable pageable);

   @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER (COALESCE(c.nombreEmpresa, '')) LIKE LOWER (CONCAT('%', :nombreEmpresa , '%'))")
    Page<Cliente> buscarClientePorNombreEmpresaPaginado(@Param("active") String active, @Param("nombreEmpresa") String nombreEmpresa, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER ( COALESCE(c.ciudad, '')) LIKE LOWER (CONCAT('%', :ciudad , '%'))")
    List<Cliente> buscarClientePorCiudad(@Param("active") String active, @Param("ciudad") String ciudad);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER ( COALESCE(c.ciudad, '')) LIKE LOWER (CONCAT('%', :ciudad , '%'))")
    Page<Cliente> buscarClientePorCiudadPaginado(@Param("active") String active, @Param("ciudad") String ciudad, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER ( COALESCE(c.domicilio, '')) LIKE LOWER (CONCAT('%', :domicilio , '%'))")
    List<Cliente> buscarClientePorDomicilio(@Param("active") String active, @Param("domicilio") String domicilio);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER ( COALESCE(c.domicilio, '')) LIKE LOWER (CONCAT('%', :domicilio , '%'))")
    Page<Cliente> buscarClientePorDomicilioPaginado(@Param("active") String active, @Param("domicilio") String domicilio, Pageable pageable);


    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER ( COALESCE(c.hobbie, '')) LIKE LOWER (CONCAT('%', :hobbie , '%'))")
    List<Cliente> buscarClientePorHobbie(@Param("active") String active, @Param("hobbie") String hobbie);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER ( COALESCE(c.hobbie, '')) LIKE LOWER (CONCAT('%', :hobbie , '%'))")
    Page<Cliente> buscarClientePorHobbiePaginado(@Param("active") String active, @Param("hobbie") String hobbie, Pageable pageable);


    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER ( COALESCE(c.rfc, '')) LIKE LOWER (CONCAT('%', :rfc , '%'))")
    List<Cliente> buscarClientePorRfc(@Param("active") String active, @Param("rfc") String rfc);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER ( COALESCE(c.rfc, '')) LIKE LOWER (CONCAT('%', :rfc , '%'))")
    Page<Cliente> buscarClientePorRfcPaginado(@Param("active") String active, @Param("rfc") String rfc, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER ( COALESCE(c.correo, '')) LIKE LOWER (CONCAT('%', :correo , '%'))")
    Page<Cliente> buscarClientePorCorreoPaginado(@Param("active") String active, @Param("correo") String correo, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER ( COALESCE(c.fechaCumple, '')) LIKE LOWER (CONCAT('%', :cumpleanos , '%'))")
    Page<Cliente> buscarClientePorCumpleanosPaginado(@Param("active") String active, @Param("cumpleanos") String cumpleanos, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER ( COALESCE(c.created_at, '')) LIKE LOWER (CONCAT('%', :registro , '%'))")
    Page<Cliente> buscarClientePorRegistroPaginado(@Param("active") String active, @Param("registro") String registro, Pageable pageable);


    // === BÚSQUEDA POR FECHA DE REGISTRO (created_at es usualmente Timestamp) ===

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND DATE(c.created_at) = :registro")
    Page<Cliente> buscarClientePorRegistroPaginado(@Param("active") String active, @Param("registro") LocalDate registro, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND DATE(c.created_at) BETWEEN :inicio AND :fin")
    Page<Cliente> buscarClientePorRegistroRangoPaginado(@Param("active") String active, @Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin, Pageable pageable);


// === BÚSQUEDA POR FECHA DE NACIMIENTO (fechaCumple) ===

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND c.fechaCumple = :cumple")
    Page<Cliente> buscarClientePorCumpleanosPaginado(@Param("active") String active, @Param("cumple") LocalDate cumple, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND c.fechaCumple BETWEEN :inicio AND :fin")
    Page<Cliente> buscarClientePorCumpleanosRangoPaginado(
            @Param("active") String active,
            @Param("inicio") String inicio, // Cambiado a String
            @Param("fin") String fin,       // Cambiado a String
            Pageable pageable
    );

    @QueryHints({@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true")})
    @Query("SELECT c FROM Cliente c WHERE (c.active = :status) AND ( " +
            "(LOWER (COALESCE(c.nombre, '')) LIKE LOWER (CONCAT('%', :busqueda , '%'))) OR " +
            "(LOWER (COALESCE(c.apellido, '')) LIKE LOWER (CONCAT('%', :busqueda , '%'))) OR " +
            "(LOWER (COALESCE(c.segundoApellido, '')) LIKE LOWER (CONCAT('%', :busqueda , '%'))) OR " +
            "(LOWER (COALESCE(c.hobbie, '')) LIKE LOWER (CONCAT('%', :busqueda , '%'))) OR " +
            "(LOWER (COALESCE(c.rfc, '')) LIKE LOWER (CONCAT('%', :busqueda , '%'))) OR " +
            "(LOWER (COALESCE(c.numTelefono, '')) LIKE LOWER (CONCAT('%', :busqueda , '%'))) OR " +
            "(LOWER (COALESCE(c.estado, '')) LIKE LOWER (CONCAT('%', :busqueda , '%'))) OR " +
            "(LOWER (COALESCE(c.ciudad, '')) LIKE LOWER (CONCAT('%', :busqueda , '%'))) OR " +
            "(LOWER (COALESCE(c.domicilio, '')) LIKE LOWER (CONCAT('%', :busqueda , '%'))) OR " +
            "(LOWER (COALESCE(c.tipoCliente, '')) LIKE LOWER (CONCAT('%', :busqueda , '%')))) ")
    List<Cliente> buscadorClientes(@Param("status") String status, @Param("busqueda") String busqueda);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT c FROM Cliente c WHERE (c.active = :status) AND (" +
            "LOWER(COALESCE(c.nombre, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.nombreEmpresa, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.apellido, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.segundoApellido, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.hobbie, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.rfc, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.numTelefono, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.estado, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.ciudad, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.domicilio, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.tipoCliente, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.created_at, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.updated_at, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.genero, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.correo, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.fechaCumple, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) )")
    Page<Cliente> buscadorClientes(@Param("status") String status,
                                   @Param("busqueda") String busqueda,
                                   Pageable pageable);

    @Query("SELECT c FROM Cliente c WHERE c.clienteId = :clienteId AND c.active = :status")
    Cliente buscarClientePorId(@Param("clienteId") Integer clienteId, @Param("status") String status);


    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.active = :status AND (" +
            "LOWER(COALESCE(c.nombre, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.nombreEmpresa, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.apellido, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.segundoApellido, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.hobbie, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.rfc, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.numTelefono, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.estado, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.ciudad, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.domicilio, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.tipoCliente, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.created_at, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.updated_at, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.genero, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.correo, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(COALESCE(c.fechaCumple, '')) LIKE LOWER(CONCAT('%', :busqueda, '%')) )")
    long contarClientesPorBusquedaGeneral(String status, String busqueda);

    @QueryHints({@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true")})
    @Query("SELECT c FROM Cliente c WHERE c.active = :active ORDER BY c.nombre ASC, c.apellido ASC, c.segundoApellido ASC LIMIT 100")
    List<Cliente> first100Buscador(@Param("active") String active);


}//interface

