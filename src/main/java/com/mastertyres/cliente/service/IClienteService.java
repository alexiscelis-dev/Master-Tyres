package com.mastertyres.cliente.service;

import com.mastertyres.cliente.model.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IClienteService {

    //List<Cliente> listarCliente(String active);

    // Método para listar clientes activos con vehículos cargados (marca, modelo, año)
    List<Cliente> listarClientesConVehiculos(String active);

    int eliminarCliente(String eliminar,Integer idCliente);

    Cliente buscarClientePorId(Integer clienteId, String status);



}
