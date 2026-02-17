package com.mastertyres.respaldo.service;

import com.mastertyres.respaldo.model.Respaldo;
import com.mastertyres.respaldo.repository.RespaldoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


//Para poder guardar en la tabla respaldos aun despues de que ocurre una exepcion es necesario guardar desde una transaccion difrente, por eso se necesesita crear la clase


@Service
public class RespaldoProxyService implements IRespaldoService {

    private RespaldoRepository respaldoRepository;


    @Autowired
    public RespaldoProxyService(RespaldoRepository respaldoRepository){
      this.respaldoRepository = respaldoRepository;
    }

    @Override
    public boolean generarRespaldo() {
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void guardarRespaldo(Respaldo archivoRespaldo) {
        respaldoRepository.saveAndFlush(archivoRespaldo);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void actualizarEstado(Integer id, String estado) {
        respaldoRepository.actualizarEstado(id,estado);
    }

}//class
