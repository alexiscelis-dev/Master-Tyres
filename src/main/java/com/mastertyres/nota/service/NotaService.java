package com.mastertyres.nota.service;

import com.mastertyres.common.exeptions.NotaException;
import com.mastertyres.nota.model.Nota;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.model.StatusNota;
import com.mastertyres.nota.repository.NotaRepository;
import com.mastertyres.notaDetalle.model.NotaDetalle;
import com.mastertyres.notaDetalle.repository.NotaDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotaService implements INotaService {

    private NotaRepository notaRepository;
    private NotaDetalleRepository notaDetalleRepository;

    @Autowired
    public NotaService(NotaRepository notaRepository, NotaDetalleRepository notaDetalleRepository) {
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
        Nota porNumNota = notaRepository.findByNumNota(nota.getNumNota());

        //si no es null encontro coincidencia y ya existe
        if (porNumNota != null) {
            if (porNumNota.getActive().equals(StatusNota.ACTIVE.toString()))
                throw new NotaException("Ya existe una nota registrada con el número: " + porNumNota.getNumNota());
            else if (porNumNota.getActive().equals(StatusNota.INACTIVE.toString())) {
                System.out.println("Numero de nota: "+ porNumNota.getNotaId());
                notaDetalleRepository.deleteById(porNumNota.getNotaId());
                notaRepository.deleteById(porNumNota.getNotaId());
                notaRepository.flush();

            }

        }
        notaRepository.save(nota);
        notaDetalleRepository.save(notaDetalle);

    }

    @Transactional(readOnly = true)
    @Override
    public NotaDTO buscarPorNumNota(String active, String numNota) {

       return notaRepository.buscarPorNumNota(active,numNota);

    }

    @Transactional(readOnly = true)
    @Override
    public Nota findByNumNota(String numNota) {
        return notaRepository.findByNumNota(numNota);
    }


    @Modifying
    @Transactional
    @Override
    public void actualizarAdeudo(float adeudo, String fechaVencimiento, Integer notaId) {
        notaRepository.actualizarAdeudo(adeudo,fechaVencimiento,notaId);

    }

    @Modifying
    @Transactional
    @Override
    public void actualizarUpdatedAtNota(Integer notaId, String updatedAt) {
        notaRepository.actualizarUpdatedAtNota(notaId,updatedAt);
    }

    @Modifying
    @Transactional
    @Override
    public void actualizarSaldo(float saldo, Integer notaId) {
        notaRepository.actualizarSaldo(saldo,notaId);
    }


    @Transactional
    @Override
    public void actualizarNota(Nota nota, NotaDetalle notaDetalle) {
        notaRepository.save(nota);
        notaDetalleRepository.save(notaDetalle);
    }

    @Transactional(readOnly = true)
    @Override
    public Nota buscarPorId(Integer notaId) {
        return notaRepository.findById(notaId).orElseThrow(()-> new NotaException("Error al guardar nota"));
    }

    @Transactional
    @Override
    public void actualizarNumFactura(String numNota, Integer notaId) {
        notaRepository.actualizarNumFactura(numNota,notaId);
    }


}//clase
