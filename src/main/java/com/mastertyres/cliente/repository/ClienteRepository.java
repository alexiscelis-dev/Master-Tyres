package com.mastertyres.cliente.repository;

import com.mastertyres.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {


    //@Query("SELECT DISTINCT c FROM Cliente c LEFT JOIN FETCH c.vehiculos v LEFT JOIN FETCH v.marca LEFT JOIN FETCH v.modelo WHERE c.active = :active")
    //List<Cliente> listarCliente(@Param("active") String active);

    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.active = 'ACTIVE' AND c.rfc = :rfc")
    boolean existeClientePorRFC(@Param("rfc") String rfc);

    // Consulta base: solo clientes activos
    @Query("SELECT c FROM Cliente c WHERE c.active = :active")
    List<Cliente> listarClientesActivos(@Param("active") String active);

    @Transactional
    @Modifying
    @Query("UPDATE Cliente c SET c.active = :inactive WHERE c.clienteId = :idCliente")
    int eliminarCliente(@Param("inactive")String eliminar, @Param("idCliente")Integer idCliente);

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
    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND LOWER( CONCAT( CONCAT(CONCAT(c.nombre, ' '), " +
            "COALESCE(c.apellido, '')), CONCAT(' ', COALESCE(c.segundoApellido, '')) ) ) " +
            "LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Cliente> buscarClientePorNombre(@Param("active")String active, @Param("nombre")String nombre);

    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND c.numTelefono = :numTelefono")
    List<Cliente> buscarClientePorNumTelefono(@Param("active")String active, @Param("numTelefono")Integer numTelefono);

    @Query("SELECT c FROM Cliente c WHERE c.active = :active AND c.numTelefono = :numTelefono")
    List<Cliente> buscarClientePorEstado(@Param("active")String active, @Param("estado")String estado);







}//interface

