package com.mastertyres.promociones.service;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.promociones.model.Promocion;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPromocionService{

    List<Marca> listarMarcas();

    List<Modelo> listarModelos();

    List<Categoria> listarCategorias();

    void guardarPromocion(Promocion promocion);

    Promocion buscarPromocionId(Integer id);



}//IPromocionService