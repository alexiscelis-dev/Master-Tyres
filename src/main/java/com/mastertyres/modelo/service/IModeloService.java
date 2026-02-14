package com.mastertyres.modelo.service;

import com.mastertyres.modelo.model.Modelo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IModeloService {

    List<Modelo> listarModelos();

    Modelo guardarModelo(Modelo modelo);

    List<String> listarNombresModelos();

    Page<Modelo> buscarModelosPorNombre(String filtro, Pageable pageable);

    int reasignarMarcaPorId(Integer marcaId);

    int eliminarModeloPorId(Integer modeloId);
}
