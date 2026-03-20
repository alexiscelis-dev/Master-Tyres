package com.mastertyres.detalleCategoria.service;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.detalleCategoria.model.DetalleCategoria;
import com.mastertyres.marca.model.Marca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDetalleCategoriaService {

    List<DetalleCategoria> listarDetalleCategoria();

    List<DetalleCategoria> findByCategoria(Categoria categoria);

    List<DetalleCategoria> getByMarca(Integer marcaId);

    List<Categoria> listarCategoriasPorMarcaYModelo(Integer marcaId, Integer modeloId);

    List<DetalleCategoria> buscarPorTexto(String busqueda);

    Page<DetalleCategoria> buscarPorTextoPaginado(String busqueda, Pageable pageable);

    List<DetalleCategoria> listarTodos();

    List<DetalleCategoria> listarPorMarca(Marca marca);

    void reasignarOEliminarPorModelo(Integer modeloId);

    void eliminarPorMarca(Integer marcaId);

    DetalleCategoria guardarDetalleCategoria(DetalleCategoria detalle);


}