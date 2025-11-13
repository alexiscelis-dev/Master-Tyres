package com.mastertyres.nota.service;

import com.mastertyres.nota.model.Nota;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.repository.NotaRepository;
import com.mastertyres.notaDetalle.model.NotaDetalle;
import com.mastertyres.notaDetalle.repository.NotaDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class  NotaService implements INotaService{

    private NotaRepository notaRepository;
    private NotaDetalleRepository notaDetalleRepository;

    @Autowired
    public NotaService(NotaRepository notaRepository, NotaDetalleRepository notaDetalleRepository){
        this.notaRepository = notaRepository;
        this.notaDetalleRepository = notaDetalleRepository;
    }


    @Transactional(readOnly = true)
    @Override
    public List<NotaDTO> listarNotas(String active) {
        return notaRepository.listarNotas(active);
    }

    @Transactional
    @Override
    public void guardarNota(Nota nota, NotaDetalle notaDetalle) {
        notaRepository.save(nota);
        notaDetalleRepository.save(notaDetalle);

    }

}//clase
