package com.mastertyres.cliente.service;

import com.mastertyres.cliente.model.Cliente;

import java.util.List;

public interface IClienteService {

    List<Cliente> listarCliente(String active);

    int eliminarCliente(String eliminar,Integer idCliente);




}
