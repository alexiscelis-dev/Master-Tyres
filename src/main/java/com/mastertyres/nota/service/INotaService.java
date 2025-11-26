package com.mastertyres.nota.service;

import com.mastertyres.nota.model.Nota;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.notaDetalle.model.NotaDetalle;

import java.util.List;

public interface INotaService {


    List<NotaDTO> listarNotas(String active);

    void guardarNota(Nota nota, NotaDetalle notaDetalle);

    NotaDTO buscarPorNumNota(String active, String numNota);

    Nota findByNumNota(String numNota);



}//interface
