package com.mastertyres.categoria.service;

import com.mastertyres.categoria.entity.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoriaService {

    List<Categoria> listarCategorias();

    List<String> listarNombresCategorias();

    Page<Categoria> buscarCategoriasPorNombre(String filtro, Pageable pageable);
}
