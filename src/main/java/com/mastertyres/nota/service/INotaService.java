package com.mastertyres.nota.service;

import com.mastertyres.nota.model.NotaDTO;

import java.util.List;

public interface INotaService {

    List<NotaDTO>listarNotas(String active);

}//interface
