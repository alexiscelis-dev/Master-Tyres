package com.mastertyres.respaldo.service;

import com.mastertyres.respaldo.entity.Respaldo;

import java.util.List;

public interface IRespaldoService {

    boolean generarRespaldo();

    void guardarRespaldo(Respaldo archivoRespaldo);

    void actualizarEstado(Integer id, String estado);

    Respaldo ObtenerUltimoRespaldo();

    void actualizarTipoRespaldo(Integer respaldoId, String tipoRespaldo);

    List<Respaldo> listarRespaldos(String fechaInicio, String fechaFin);



}//interface
