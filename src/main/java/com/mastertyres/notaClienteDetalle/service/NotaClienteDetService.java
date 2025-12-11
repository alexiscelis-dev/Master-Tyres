package com.mastertyres.notaClienteDetalle.service;

import com.mastertyres.notaClienteDetalle.repository.NotaClienteDetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotaClienteDetService implements INotaClienteDetService {

    private NotaClienteDetRepository notaClienteDetRepository;

    @Autowired
    public NotaClienteDetService(NotaClienteDetRepository notaClienteDetRepository) {
        this.notaClienteDetRepository = notaClienteDetRepository;
    }
}//class
