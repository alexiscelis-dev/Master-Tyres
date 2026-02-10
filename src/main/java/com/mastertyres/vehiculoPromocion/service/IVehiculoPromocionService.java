package com.mastertyres.vehiculoPromocion.service;

import com.mastertyres.vehiculoPromocion.model.VehiculoPromocion;

import java.util.List;

public interface IVehiculoPromocionService {


    void guardarVehiculosAplicables(VehiculoPromocion vehiculoPromocion);

    List<VehiculoPromocion> obtenerVehiculosPorPromocion(Integer promocionId);

    void eliminarPorPromocionId(Integer promocionId);

    int reasignarMarcaPorId(Integer marcaId);

    int reasignarModeloYCategoriaPorMarca(Integer marcaId);

    int reasignarModeloPorId(Integer modeloId);
}//interface
