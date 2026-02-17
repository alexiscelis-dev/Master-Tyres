package com.mastertyres.respaldo.service;

import com.mastertyres.respaldo.model.Respaldo;

public interface IRespaldoService {

    boolean generarRespaldo();

    void guardarRespaldo(Respaldo archivoRespaldo);

    void actualizarEstado(Integer id, String estado);



}//interface
