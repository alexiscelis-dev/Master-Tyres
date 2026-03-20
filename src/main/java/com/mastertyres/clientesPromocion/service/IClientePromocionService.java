package com.mastertyres.clientesPromocion.service;

import com.mastertyres.clientesPromocion.model.ClientesPromocion;
import com.mastertyres.cliente.model.Cliente;

import java.util.List;

public interface IClientePromocionService {

    void guardarClientesPromocion(Integer promocionId, List<Integer> clientesIds);

    void eliminarClientesPorPromocion(Integer promocionId);

    List<ClientesPromocion> listarClientesPorPromocion(Integer promocionId);

    void guardar(ClientesPromocion clientePromocion);

    void eliminarPorPromocionId(Integer promocionId);

    List<Cliente> obtenerClientesAplicables(Integer promocionId);

    List<String> buscarImgPromocion(Integer clienteId, Integer promocionId);

}//interface