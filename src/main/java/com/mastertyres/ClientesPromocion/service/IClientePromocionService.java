package com.mastertyres.ClientesPromocion.service;

import java.util.List;

public interface IClientePromocionService {

    void guardarClientesPromocion(Integer promocionId, List<Integer> clientesIds);

    void eliminarClientesPorPromocion(Integer promocionId);

}
