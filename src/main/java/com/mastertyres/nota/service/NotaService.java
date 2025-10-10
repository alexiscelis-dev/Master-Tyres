package com.mastertyres.nota.service;

import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.repository.NotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class  NotaService implements INotaService{

    private NotaRepository notaRepository;

    @Autowired
    public NotaService(NotaRepository notaRepository){
        this.notaRepository = notaRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<NotaDTO> listarNotas(String active) {
        return notaRepository.listarNotas(active);
    }

}//clase
