package com.mastertyres.notaClienteDetalle.service;

import com.mastertyres.nota.entity.Nota;
import com.mastertyres.notaClienteDetalle.entity.NotaClienteDetalle;

public interface INotaClienteDetService {

    NotaClienteDetalle buscarclienteDetalle(Nota nota);
}//interface
