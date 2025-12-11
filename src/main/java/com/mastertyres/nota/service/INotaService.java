package com.mastertyres.nota.service;

import com.mastertyres.nota.model.Nota;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.notaClienteDetalle.model.NotaClienteDetalle;
import com.mastertyres.notaDetalle.model.NotaDetalle;

import java.util.List;

public interface INotaService {


    List<NotaDTO> listarNotas(String active);

    void guardarNota(Nota nota, NotaDetalle notaDetalle, NotaClienteDetalle clienteDetalle);

    NotaDTO buscarPorNumNota(String active, String numNota);

    Nota findByNumNota(String numNota);

    void actualizarAdeudo(float adeudo, String fechaVencimiento, Integer notaId);

    void actualizarUpdatedAtNota(Integer notaId, String updatedAt);

    void actualizarSaldo(float saldo, Integer notaId);

    void actualizarNota(Nota nota, NotaDetalle notaDetalle);

    Nota buscarPorId(Integer notaId);

    void actualizarNumFactura(String numNota, Integer notaId);

    void actualizarStatus(String status, Integer notaId);




}//interface
