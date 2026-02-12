package com.mastertyres.promociones.service;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.promociones.model.Promocion;

import java.util.List;


public interface IPromocionService{

    List<Marca> listarMarcas();

    List<Modelo> listarModelos();

    List<Categoria> listarCategorias();

    void guardarPromocion(Promocion promocion);

    Promocion buscarPromocionId(Integer id);

    List<Promocion> obtenerPromocionesActivas();

    List<Promocion> buscarPromociones(String texto);

    void desactivarPromocion(Integer id);






}//IPromocionService