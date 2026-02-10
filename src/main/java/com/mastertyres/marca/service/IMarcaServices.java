package com.mastertyres.marca.service;

import com.mastertyres.marca.model.Marca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IMarcaServices {

    List<Marca> listarMarcas();

    Optional<Marca> findByNombreMarca(String nombreMarca);

    Marca guardarMarca(Marca marca);

    List<String> listarNombresMarcas();

    Page<String> listarNombresMarcas(Pageable pageable);

    Page<Marca> buscarMarcasPorNombre(String filtro, Pageable pageable);

    Page<Marca> listarMarcasPaginado(Pageable pageable);

    Page<Marca> buscarMarcasPorNombreMarca(String filtro, Pageable pageable);

    Page<Marca> listarMarcasPaginado(int numeroPagina, int tamañoPagina);

    Page<Marca> buscarMarcasPorNombre(String filtro, int numeroPagina, int tamañoPagina);

   int eliminarMarcaPorId(Integer marcaId);


}