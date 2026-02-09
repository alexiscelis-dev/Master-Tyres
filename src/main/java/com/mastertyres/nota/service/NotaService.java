package com.mastertyres.nota.service;

import com.mastertyres.common.exeptions.NotaException;
import com.mastertyres.nota.domain.NotaValidator;
import com.mastertyres.nota.model.Nota;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.model.StatusNota;
import com.mastertyres.nota.repository.NotaRepository;
import com.mastertyres.notaClienteDetalle.model.NotaClienteDetalle;
import com.mastertyres.notaClienteDetalle.repository.NotaClienteDetRepository;
import com.mastertyres.notaDetalle.model.NotaDetalle;
import com.mastertyres.notaDetalle.repository.NotaDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.mastertyres.common.utils.MensajesAlert.mostrarWarning;

@Service
public class NotaService implements INotaService {

    private NotaRepository notaRepository;
    private NotaDetalleRepository notaDetalleRepository;
    private NotaClienteDetRepository notaClienteDetRepository;
    @Autowired
    private NotaValidator notaValidator;

    @Autowired
    public NotaService(NotaRepository notaRepository, NotaDetalleRepository notaDetalleRepository, NotaClienteDetRepository notaClienteDetRepository) {
        this.notaRepository = notaRepository;
        this.notaDetalleRepository = notaDetalleRepository;
        this.notaClienteDetRepository = notaClienteDetRepository;
    }


    @Transactional(readOnly = true)
    @Override
    public List<NotaDTO> listarNotas(String active) {
        return notaRepository.listarNotas(active);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> buscador(String filtro, String busqueda, int IndicePagina, int tamañoPagina) {
        Page<NotaDTO> paginaFiltrada;

        switch (filtro.toLowerCase()) {
            case "sin filtro" -> paginaFiltrada = buscarNotas(busqueda, IndicePagina, tamañoPagina);

            case "numero de nota" ->
                    paginaFiltrada = BucarPorNumNota(busqueda, StatusNota.ACTIVE.toString(), IndicePagina, tamañoPagina);

            case "fecha de emicion" ->
                    paginaFiltrada = buscarPorFechaNota(LocalDate.parse(busqueda), StatusNota.ACTIVE.toString(), IndicePagina, tamañoPagina);

            case "nombre del cliente" ->
                    paginaFiltrada = buscarPorNombreCliente(busqueda, StatusNota.ACTIVE.toString(), IndicePagina, tamañoPagina);

            case "vehiculo" ->
                    paginaFiltrada = buscarPorVehiculo(busqueda, StatusNota.ACTIVE.toString(), IndicePagina, tamañoPagina);

            case "fecha de vencimiento" ->
                    paginaFiltrada = buscarPorFechaVencimiento(busqueda, StatusNota.ACTIVE.toString(), IndicePagina, tamañoPagina);

            case "direccion" ->
                    paginaFiltrada = buscarPorDireccion(busqueda, StatusNota.ACTIVE.toString(), IndicePagina, tamañoPagina);

            case "placas de vehiculo" ->
                    paginaFiltrada = buscarPorPlacas(busqueda, "ACTIVE", IndicePagina, tamañoPagina);

            case "numero de factura" ->
                    paginaFiltrada = buscarPorNumeroFactura(busqueda, StatusNota.ACTIVE.toString(), IndicePagina, tamañoPagina);

            case "rfc" ->
                    paginaFiltrada = buscarPorRfc(busqueda, StatusNota.ACTIVE.toString(), IndicePagina, tamañoPagina);

            case "adeudo" -> {
                try {
                    BigDecimal adeudo = new BigDecimal(busqueda.trim());
                    paginaFiltrada = buscarPorAdeudo(
                            adeudo, StatusNota.ACTIVE.toString(), IndicePagina, tamañoPagina
                    );
                } catch (NumberFormatException ex) {

                    mostrarWarning(
                            "Valor inválido",
                            "Adeudo incorrecto",
                            "Ingrese un valor numérico válido para el adeudo."
                    );


                    return listarNotasPaginado(StatusNota.ACTIVE.toString(), 0, tamañoPagina);
                }
            }

            case "total" -> {
                try {
                    Double total = Double.parseDouble(busqueda.trim());
                    paginaFiltrada = buscarPorTotal(
                            total, "ACTIVE", IndicePagina, tamañoPagina
                    );
                } catch (NumberFormatException ex) {
                    mostrarWarning(
                            "Valor inválido",
                            "Total incorrecto",
                            "Ingrese un valor numérico válido para el total."
                    );
                    return listarNotasPaginado(StatusNota.ACTIVE.toString(), 0, tamañoPagina);
                }
            }

            case "saldo a favor" -> {
                try {
                    Double saldo = Double.parseDouble(busqueda.trim());
                    paginaFiltrada = buscarPorSaldoFavor(
                            saldo, StatusNota.ACTIVE.toString(), IndicePagina, tamañoPagina
                    );
                } catch (NumberFormatException ex) {
                    mostrarWarning(
                            "Valor inválido",
                            "Saldo incorrecto",
                            "Ingrese un valor numérico válido para el saldo a favor."
                    );
                    return listarNotasPaginado(StatusNota.ACTIVE.toString(), 0, tamañoPagina);
                }
            }

            default -> paginaFiltrada = listarNotasPaginado(StatusNota.ACTIVE.toString(), 0, tamañoPagina);
        }
        return paginaFiltrada;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> buscadorRangos(String filtro, String busqueda, String busqueda2, int IndicePagina, int tamañoPagina) {
        Page<NotaDTO> paginaFiltrada;

        switch (filtro.toLowerCase()) {

            case "fecha de emicion" ->
                    paginaFiltrada = buscarPorFechaNotaRango(busqueda, busqueda2, "ACTIVE", IndicePagina, tamañoPagina);

            case "fecha de vencimiento" ->
                    paginaFiltrada = buscarPorFechaVencimientoRango(busqueda, busqueda2, "ACTIVE", IndicePagina, tamañoPagina);

            default -> paginaFiltrada = listarNotasPaginado(StatusNota.ACTIVE.toString(), 0, tamañoPagina);
        }
        return paginaFiltrada;
    }


    //  Listar vehículos activos con paginación
    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> listarNotasPaginado(String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("notaId").descending());
        return notaRepository.listarNotasPaginado(active, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> buscarNotas(String filtro, int pagina, int tamanio) {
        return notaRepository.buscarNotas("ACTIVE", filtro, PageRequest.of(pagina, tamanio));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> buscarPorNumeroFactura(String numFactura, String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("notaId").descending());
        return notaRepository.buscarPorNumeroFactura(numFactura, active, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> buscarPorNombreCliente(String nombreCliente, String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("notaId").descending());
        return notaRepository.buscarPorNombreCliente(nombreCliente, active, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> buscarPorFechaNota(LocalDate fecha, String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("notaId").descending());
        return notaRepository.buscarPorFechaNota(fecha, active, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> buscarPorFechaNotaRango(String fecha, String fecha2, String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("notaId").descending());
        return notaRepository.buscarPorFechaNotaRango(fecha, fecha2, active, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> buscarPorVehiculo(String filtro, String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("notaId").descending());
        return notaRepository.buscarPorVehiculo(filtro, active, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> buscarPorFechaVencimiento(String filtro, String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("notaId").descending());
        return notaRepository.buscarPorFechaVencimiento(filtro, active, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> buscarPorFechaVencimientoRango(String filtro, String filtro2, String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("notaId").descending());
        return notaRepository.buscarPorFechaVencimientoRango(filtro, filtro2, active, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> buscarPorDireccion(String filtro, String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("notaId").descending());
        return notaRepository.buscarPorDireccion(filtro, active, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> buscarPorPlacas(String filtro, String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("notaId").descending());
        return notaRepository.buscarPorPlacas(filtro, active, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> buscarPorRfc(String filtro, String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("notaId").descending());
        return notaRepository.buscarPorRfc(filtro, active, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> buscarPorAdeudo(BigDecimal filtro, String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("notaId").descending());
        return notaRepository.buscarPorAdeudo(filtro, active, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> buscarPorTotal(Double filtro, String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("notaId").descending());
        return notaRepository.buscarPorTotal(filtro, active, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> buscarPorSaldoFavor(Double filtro, String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("notaId").descending());
        return notaRepository.buscarPorSaldoFavor(filtro, active, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NotaDTO> BucarPorNumNota(String filtro, String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("notaId").descending());
        return notaRepository.buscarPorNumeroNota(filtro, active, pageable);
    }

    @Transactional
    @Override
    public void guardarNota(Nota nota, NotaDetalle notaDetalle, NotaClienteDetalle clienteDetalle) {
        Nota porNumNota = notaRepository.findByNumNota(nota.getNumNota());

        //si no es null encontro coincidencia y ya existe
        if (porNumNota != null) {
            if (porNumNota.getActive().equals(StatusNota.ACTIVE.toString()))
                throw new NotaException("Ya existe una nota registrada con el número: " + porNumNota.getNumNota());
            else if (porNumNota.getActive().equals(StatusNota.INACTIVE.toString())) {
                porNumNota.setActive(StatusNota.ACTIVE.toString());
                notaDetalleRepository.deleteById(porNumNota.getNotaId());
                notaRepository.deleteById(porNumNota.getNotaId());
                notaClienteDetRepository.deleteById(porNumNota.getNotaId());
                notaRepository.flush();

            }

        }
        nota.setActive(StatusNota.ACTIVE.toString());
        notaRepository.save(nota);
        notaDetalleRepository.save(notaDetalle);
        notaClienteDetRepository.save(clienteDetalle);

    }

    @Transactional(readOnly = true)
    @Override
    public NotaDTO buscarPorNumNota(String active, String numNota) {

        return Optional.ofNullable(notaRepository.buscarPorNumNota(active, numNota)).
                orElseThrow(() -> new NotaException("Tuvimos problemas al obtener la nota proporcionada"));

    }

    @Transactional(readOnly = true)
    @Override
    public Nota findByNumNota(String numNota) {
        return Optional.
                ofNullable(notaRepository.findByNumNota(numNota)).
                orElseThrow(() -> new NotaException("Tuvimos problemas al obtener la nota proporcionada"));
    }


    @Modifying
    @Transactional
    @Override
    public void actualizarAdeudo(float adeudo, String fechaVencimiento, Integer notaId) {

        notaValidator.validarAdeudo(adeudo, fechaVencimiento, notaId);

        notaRepository.actualizarAdeudo(adeudo, fechaVencimiento, notaId);

    }

    @Modifying
    @Transactional
    @Override
    public void actualizarUpdatedAtNota(Integer notaId, String updatedAt) {
        notaValidator.validadUpdatedAt(notaId);
        notaRepository.actualizarUpdatedAtNota(notaId, updatedAt);
    }

    @Modifying
    @Transactional
    @Override
    public void actualizarSaldo(float saldo, Integer notaId) {
        notaValidator.validarSaldoFavor(notaId);
        notaRepository.actualizarSaldo(saldo, notaId);
    }


    @Transactional
    @Override
    public void actualizarNota(Nota nota, NotaDetalle notaDetalle, NotaClienteDetalle clienteDetalle) {
        notaRepository.save(nota);
        notaDetalleRepository.save(notaDetalle);
        notaClienteDetRepository.save(clienteDetalle);
    }

    @Transactional(readOnly = true)
    @Override
    public Nota buscarPorId(Integer notaId) {
        return notaRepository.findById(notaId).orElseThrow(() -> new NotaException("Error al guardar nota"));
    }

    @Transactional
    @Override
    public void actualizarNumFactura(String numNota, Integer notaId) {
        notaRepository.actualizarNumFactura(numNota, notaId);
    }

    @Transactional
    @Override
    public void actualizarStatus(String status, Integer notaId) {
        notaValidator.validarStatusPagos(notaId);
        notaRepository.actualizarStatus(status, notaId);
    }

    @Transactional
    @Override
    public void eliminarNota(String active, Integer notaId) {
        notaRepository.eliminarNota(active, notaId);
    }

    @Transactional
    @Override
    public void actualilzarFechaVencimiento(String fechaVencimiento, Integer notaId, String active) {
        notaRepository.actualizarFechaVencimiento(fechaVencimiento, notaId, active);
    }


    @Transactional(readOnly = true)
    @Override
    public List<NotaDTO> buscarHistorial(int cantidadResultados, String nombreCliente) {

        Pageable configuracion = PageRequest.of(0, cantidadResultados);
        return notaRepository.buscarHistorial(StatusNota.ACTIVE.toString(), nombreCliente, configuracion);
    }


}//clase
