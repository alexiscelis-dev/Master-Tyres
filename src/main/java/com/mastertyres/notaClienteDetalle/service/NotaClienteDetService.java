package com.mastertyres.notaClienteDetalle.service;

import com.mastertyres.nota.entity.Nota;
import com.mastertyres.notaClienteDetalle.entity.NotaClienteDetalle;
import com.mastertyres.notaClienteDetalle.repository.NotaClienteDetRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotaClienteDetService implements INotaClienteDetService {

    private NotaClienteDetRepository notaClienteDetRepository;

    @Autowired
    public NotaClienteDetService(NotaClienteDetRepository notaClienteDetRepository) {
        this.notaClienteDetRepository = notaClienteDetRepository;
    }



    @Transactional(readOnly = true)
    @Override
    public NotaClienteDetalle buscarclienteDetalle(Nota nota) {
        return notaClienteDetRepository.buscarclienteDetalle(nota);
    }
}//class
