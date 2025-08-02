package com.mastertyres.cliente.repository;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.vehiculo.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

   /* @Query("SELECT c FROM Cliente c JOIN FETCH c.marca JOIN FETCH c.modelo")
    List<Cliente> listarCliente();*/

    @Query("SELECT DISTINCT c FROM Cliente c LEFT JOIN FETCH c.vehiculos v LEFT JOIN FETCH v.marca LEFT JOIN FETCH v.modelo")
    List<Cliente> listarCliente();


}

