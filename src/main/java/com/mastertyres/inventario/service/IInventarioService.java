package com.mastertyres.inventario.service;

import com.mastertyres.inventario.model.Inventario;

import java.time.LocalDate;
import java.util.List;

public interface IInventarioService {

    List<Inventario>listarInventario(String active);

    List<Inventario>buscadorInventario(String active, String busqueda);

    List<Inventario>buscarPorCodBarras(String active, String busqueda);

    List<Inventario>buscarPorDot(String active, String busqueda);

    List<Inventario>buscarPorMarca(String active, String busqueda);

    List<Inventario>buscarPorModelo(String active, String busqueda);

    List<Inventario>buscarPorMedida(String active, String busqueda);

    List<Inventario>buscarPorFecha(String active, LocalDate fecha);

    List<Inventario>buscarPorFecha(String active, LocalDate fechaInicio, LocalDate fechaFin);

    int eliminarInventario(String inactive, Integer idInventario);

    void guardarInventario(Inventario inventario);

    void uptatedAt(String now, Integer idInventario);

}//interface
