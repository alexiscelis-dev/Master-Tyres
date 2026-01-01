package com.mastertyres.notaDetalle.service;

import com.mastertyres.nota.model.Nota;
import com.mastertyres.notaDetalle.model.NotaDetalle;
import com.mastertyres.notaDetalle.repository.NotaDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotaDetalleService implements INotaDetalleService {

    private NotaDetalleRepository notaDetalleRepository;

    @Autowired
    public NotaDetalleService(NotaDetalleRepository notaDetalleRepository) {
        this.notaDetalleRepository = notaDetalleRepository;

    }

    //busca por el id de la relacion el notaId
    @Transactional(readOnly = true)
    @Override
    public NotaDetalle buscarNotaDetalle(Nota nota) {
        return notaDetalleRepository.BuscarNotaDetalle(nota);
    }

}//class
