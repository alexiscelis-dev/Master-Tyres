package com.mastertyres.notaDetalle.service;

import com.mastertyres.notaDetalle.repository.NotaDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotaDetalleService implements INotaDetalleService{

    private NotaDetalleRepository notaDetalleRepository;

    @Autowired
    public NotaDetalleService(NotaDetalleRepository notaDetalleRepository){
        this.notaDetalleRepository = notaDetalleRepository;

    }


}//class
