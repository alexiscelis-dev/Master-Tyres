package com.mastertyres.promociones.service;

import com.mastertyres.promociones.model.Promocion;

import java.util.List;


public interface IPromocionService{

    void guardarPromocion(Promocion promocion);

    Promocion buscarPromocionId(Integer id);

    List<Promocion> obtenerPromocionesActivas();

    List<Promocion> buscarPromociones(String texto);

    void desactivarPromocion(Integer id);






}//IPromocionService