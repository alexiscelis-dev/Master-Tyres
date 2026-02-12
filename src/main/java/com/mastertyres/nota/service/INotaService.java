package com.mastertyres.nota.service;

import com.mastertyres.nota.model.Nota;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.notaClienteDetalle.model.NotaClienteDetalle;
import com.mastertyres.notaDetalle.model.NotaDetalle;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface INotaService {


    List<NotaDTO> listarNotas(String active);

    void guardarNota(Nota nota, NotaDetalle notaDetalle, NotaClienteDetalle clienteDetalle);

    NotaDTO buscarPorNumNota(String active, String numNota);

    Nota findByNumNota(String numNota);

    void actualizarAdeudo(float adeudo, String fechaVencimiento, Integer notaId);

    void actualizarUpdatedAtNota(Integer notaId, String updatedAt);

    void actualizarSaldo(float saldo, Integer notaId);

    void actualizarNota(Nota nota, NotaDetalle notaDetalle, NotaClienteDetalle clienteDetalle);

    Nota buscarPorId(Integer notaId);

    void actualizarNumFactura(String numNota, Integer notaId);

    void actualizarStatus(String status, Integer notaId);

    void eliminarNota(String active, Integer notaId);

    void actualilzarFechaVencimiento(String fechaVencimiento, Integer notaId, String active);

    //consultas de historial


    List<NotaDTO> buscarHistorial(int cantidadResultados,String nombreCliente);

    Page<NotaDTO> buscador (String filtro, String busqueda, int IndicePagina, int tamañoPagina);

    Page<NotaDTO> buscadorRangos (String filtro, String busqueda, String busqueda2, int IndicePagina, int tamañoPagina);

    Page<NotaDTO> listarNotasPaginado(String active, int pagina, int tamanoPagina);

    Page<NotaDTO> buscarNotas(String filtro, int pagina, int tamanio);

    Page<NotaDTO> buscarPorNumeroFactura(String numFactura, String active, int pagina, int tamanoPagina);

    Page<NotaDTO> buscarPorNombreCliente(String nombreCliente, String active, int pagina, int tamanoPagina);

    Page<NotaDTO> buscarPorFechaNota(LocalDate fecha, String active, int pagina, int tamanoPagina);

    Page<NotaDTO> buscarPorFechaNotaRango(String fecha, String fecha2, String active, int pagina, int tamanoPagina);

    Page<NotaDTO> buscarPorVehiculo(String filtro, String active, int pagina, int tamanoPagina);

    Page<NotaDTO> buscarPorFechaVencimiento(String filtro, String active, int pagina, int tamanoPagina);

    Page<NotaDTO> buscarPorFechaVencimientoRango(String filtro, String filtro2, String active, int pagina, int tamanoPagina);

    Page<NotaDTO> buscarPorDireccion(String filtro, String active, int pagina, int tamanoPagina);

    Page<NotaDTO> buscarPorPlacas(String filtro, String active, int pagina, int tamanoPagina);

    Page<NotaDTO> buscarPorRfc(String filtro, String active, int pagina, int tamanoPagina);

    Page<NotaDTO> buscarPorAdeudo(String active, int pagina, int tamanoPagina);

    Page<NotaDTO> buscarPorTotal(Double filtro, String active, int pagina, int tamanoPagina);

    Page<NotaDTO> buscarPorSaldoFavor(String active, int pagina, int tamanoPagina);

    Page<NotaDTO> BucarPorNumNota(String filtro, String active, int pagina, int tamanoPagina);











}//interface