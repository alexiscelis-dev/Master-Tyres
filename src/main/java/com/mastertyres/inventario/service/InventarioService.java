package com.mastertyres.inventario.service;

import com.mastertyres.common.exeptions.InventarioException;
import com.mastertyres.inventario.domain.InventarioValidator;
import com.mastertyres.inventario.entity.Inventario;
import com.mastertyres.inventario.entity.StatusInventario;
import com.mastertyres.inventario.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InventarioService implements IInventarioService {
    private InventarioRepository inventarioRepository;

    @Autowired
    private InventarioValidator inventarioValidator;

    @Autowired
    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public long contarInventariosActivos(String active) {
        return inventarioRepository.contarInventarioActivos(active);
    }

    @Transactional(readOnly = true)
    @Override
    public long contarInventarioPorBusquedaGeneral(String status, String termino) {
        return inventarioRepository.contarInventarioPorBusquedaGeneral(status, termino);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> listarInventario(String active) {
        return inventarioRepository.listarInventario(active);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Inventario> listarInventarioPaginado(String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("inventarioId").descending());
        return inventarioRepository.listarInventarioPaginada(active, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscadorInventario(String active, String busqueda) {
        return inventarioRepository.buscadorInventario(active, busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Inventario> buscadorInventarioPaginado(String active, String busqueda, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("inventarioId").descending());
        return inventarioRepository.buscadorInventarioPaginado(active, busqueda, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorCodBarras(String active, String busqueda) {
        return inventarioRepository.buscarPorCodBarras(active, busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Inventario> buscarPorCodBarrasPaginado(String active, String busqueda, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("inventarioId").descending());
        return inventarioRepository.buscarPorCodBarrasPaginado(active, busqueda, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorDot(String active, String busqueda) {
        return inventarioRepository.buscarPorDot(active, busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Inventario> buscarPorDotPaginado(String active, String busqueda, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("inventarioId").descending());
        return inventarioRepository.buscarPorDotPaginado(active, busqueda, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorMarca(String active, String busqueda) {
        return inventarioRepository.buscarPorMarca(active, busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Inventario> buscarPorMarcaPaginado(String active, String busqueda, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("inventarioId").descending());
        return inventarioRepository.buscarPorMarcaPaginado(active, busqueda, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorModelo(String active, String busqueda) {
        return inventarioRepository.buscarPorModelo(active, busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Inventario> buscarPorModeloPaginado(String active, String busqueda, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("inventarioId").descending());
        return inventarioRepository.buscarPorModeloPaginado(active, busqueda, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorMedida(String active, String busqueda) {
        return inventarioRepository.buscarPorMedida(active, busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Inventario> buscarPorMedidaPaginado(String active, String busqueda, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("inventarioId").descending());
        return inventarioRepository.buscarPorMedidaPaginado(active, busqueda, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorFecha(String active, LocalDate fecha) {
        return inventarioRepository.buscarPorFecha(active, fecha);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Inventario> buscarPorFechaPaginado(String active, LocalDate fecha, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("inventarioId").descending());
        return inventarioRepository.buscarPorFechaPaginado(active, fecha, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorFecha(String active, LocalDate fechaInicio, LocalDate fechaFin) {
        return inventarioRepository.buscarPorFecha(active, fechaInicio, fechaFin);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Inventario> buscarPorFechaPaginadoRangos(String active, LocalDate fechaInicio, LocalDate fechaFin, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("inventarioId").descending());
        return inventarioRepository.buscarPorFechaPaginado(active, fechaInicio, fechaFin, pageable);
    }

    @Transactional
    @Override
    public int eliminarInventario(String inactive, Integer idInventario) {

        int filasEliminadas = inventarioRepository.eliminarInventario(inactive, idInventario);

        if (filasEliminadas == 0)
            throw new InventarioException("No se pudo eliminar el elemento seleccionado");

        return filasEliminadas;

    }

    @Transactional
    @Override
    public void guardarInventario(Inventario inventario) {

        inventarioValidator.validarGuardar(inventario);

        //Buscar una llanta por medio del identificador llanta
        Optional<Inventario> porIdentificador = inventarioRepository.findByIdentificadorLlanta(inventario.getIdentificadorLlanta());

        if (porIdentificador.isPresent()) {
            Inventario inv = porIdentificador.get();

            if (inv.getActive().equals(StatusInventario.ACTIVE.toString())) {
                throw new InventarioException("La llanta ya esta registrada en el inventario");

            } else if (inv.getActive().equals(StatusInventario.SIN_STOCK.toString())) {
                throw new InventarioException("Llanta registrada, no tiene stock");

            } else if (inv.getActive().equals(StatusInventario.INACTIVE.toString())) {

                inv.setIdentificadorLlanta(inventario.getIdentificadorLlanta());
                inv.setMarca(inventario.getMarca());
                inv.setModelo(inventario.getModelo());
                inv.setMedida(inventario.getMedida());
                inv.setIndiceCarga(inventario.getIndiceCarga());
                inv.setIndiceVelocidad(inventario.getIndiceVelocidad());
                inv.setStock(inventario.getStock());
                inv.setPrecioCompra(inventario.getPrecioCompra());
                inv.setPrecioVenta(inventario.getPrecioVenta());
                inv.setObservaciones(inventario.getObservaciones());
                inv.setImagen(inventario.getImagen());
                inv.setActive(StatusInventario.ACTIVE.toString());
                inv.setUpdated_at(LocalDateTime.now().toString());
                inv.setCreated_at(LocalDateTime.now().toString());

                inv.setCodigoBarras(inventario.getCodigoBarras());
                inv.setDot(inventario.getDot());

                inventarioRepository.save(inv);
                return;
            }

        }//por identificador


        if (inventario.getCodigoBarras() != null && !inventario.getCodigoBarras().trim().isBlank()) {
            Optional<Inventario> porBarras = inventarioRepository.findByCodigoBarras(inventario.getCodigoBarras());

            if (porBarras.isPresent()) {
                Inventario inv = porBarras.get();
                if (inv.getActive().equals(StatusInventario.ACTIVE.toString())) {
                    throw new InventarioException("Codigo de barras ya existe en un producto activo"); //Lanza excepcion para que en el front sepa que no se inserta

                } else if (inv.getActive().equals(StatusInventario.SIN_STOCK.toString())) {
                    throw new InventarioException("Codigo de barras ya existe en un producto sin stock");
                } else if (inv.getActive().equals(StatusInventario.INACTIVE.toString())) {

                    inv.setMarca(inventario.getMarca());
                    inv.setModelo(inventario.getModelo());
                    inv.setMedida(inventario.getMedida());
                    inv.setIndiceCarga(inventario.getIndiceCarga());
                    inv.setIndiceVelocidad(inventario.getIndiceVelocidad());
                    inv.setStock(inventario.getStock());
                    inv.setPrecioCompra(inventario.getPrecioCompra());
                    inv.setPrecioVenta(inventario.getPrecioVenta());
                    inv.setObservaciones(inventario.getObservaciones());
                    inv.setImagen(inventario.getImagen());
                    inv.setActive(StatusInventario.ACTIVE.toString());
                    inv.setUpdated_at(LocalDateTime.now().toString());
                    inv.setCreated_at(LocalDateTime.now().toString());


                    inv.setCodigoBarras(inventario.getCodigoBarras());
                    inv.setDot(inventario.getDot());

                    inventarioRepository.save(inv);
                    return;

                }

            }//por codigo de barras
        }

        if (inventario.getDot() != null && !inventario.getDot().trim().isBlank()) {
            Optional<Inventario> porDot = inventarioRepository.findByDot(inventario.getDot());
            if (porDot.isPresent()) {
                Inventario inv = porDot.get();
                if (inv.getActive().equals(StatusInventario.ACTIVE.toString())) {
                    throw new InventarioException("Codigo dot ya existe en un producto activo");
                } else if (inv.getActive().equals(StatusInventario.SIN_STOCK.toString())) {
                    throw new InventarioException("Codigo dot ya existe en un producto sin stock");
                } else if (inv.getActive().equals(StatusInventario.INACTIVE.toString())) {
                    inv.setMarca(inventario.getMarca());
                    inv.setModelo(inventario.getModelo());
                    inv.setMedida(inventario.getMedida());
                    inv.setIndiceCarga(inventario.getIndiceCarga());
                    inv.setIndiceVelocidad(inventario.getIndiceVelocidad());
                    inv.setStock(inventario.getStock());
                    inv.setPrecioCompra(inventario.getPrecioCompra());
                    inv.setPrecioVenta(inventario.getPrecioVenta());
                    inv.setObservaciones(inventario.getObservaciones());
                    inv.setImagen(inventario.getImagen());
                    inv.setActive(StatusInventario.ACTIVE.toString());
                    inv.setCodigoBarras(inventario.getCodigoBarras());
                    inv.setDot(inventario.getDot());
                    inv.setUpdated_at(LocalDateTime.now().toString());
                    inv.setCreated_at(LocalDateTime.now().toString());

                    inventarioRepository.save(inv);
                    return;
                }
            }


        } //por dot


        inventarioRepository.save(inventario);
    }//guardarInventario

    @Transactional
    @Override
    public void actualizarInventario(Inventario inventario) {

        inventarioValidator.validarGuardar(inventario);

        Optional<Inventario> porIdentificador = inventarioRepository.findByIdentificadorLlanta(inventario.getIdentificadorLlanta());

        if (porIdentificador.isPresent() && !porIdentificador.get().getInventarioId().equals(inventario.getInventarioId())) {
            Inventario inv = porIdentificador.get();

            if (inv.getActive().equals(StatusInventario.ACTIVE.toString())) {
                throw new InventarioException("No es posible actualizar una llanta con el mismo nombre y caracteristicas de una existente. \n Verifique la informacion y vuelva a intentar.");
            } else if (inv.getActive().equals(StatusInventario.SIN_STOCK.toString())) {
                throw new InventarioException("No es posible actualizar una llanta con el mismo nombre y caracteristicas de una existente sin stock.\n Verifique la informacion y vuelva a intentar.");
            } else if (inv.getActive().equals(StatusInventario.INACTIVE.toString())) {

                inv.setIdentificadorLlanta(inventario.getIdentificadorLlanta());
                inv.setMarca(inventario.getMarca());
                inv.setModelo(inventario.getModelo());
                inv.setMedida(inventario.getMedida());
                inv.setIndiceCarga(inventario.getIndiceCarga());
                inv.setIndiceVelocidad(inventario.getIndiceVelocidad());
                inv.setStock(inventario.getStock());
                inv.setPrecioCompra(inventario.getPrecioCompra());
                inv.setPrecioVenta(inventario.getPrecioVenta());
                inv.setObservaciones(inventario.getObservaciones());
                inv.setImagen(inventario.getImagen());
                inv.setActive(StatusInventario.ACTIVE.toString());
                inv.setCodigoBarras(inventario.getCodigoBarras());
                inv.setDot(inventario.getDot());
                inv.setUpdated_at(LocalDateTime.now().toString());
                inv.setCreated_at(LocalDateTime.now().toString());

                return;

            }

            inventarioRepository.save(inv);
            return;

        }


        inventarioRepository.save(inventario);

    }//actualizarInventario

    //Metodo actualiza cuando se crea un elemento nuevo en el inventario
    @Transactional
    @Override
    public void actualizarUptatedAt(String now, Integer idInventario) {
        //    Optional<Inventario> invExistente = inventarioRepository.fin
        inventarioRepository.actualizarUpdatedAt(now, idInventario);

    }

    //Verifica si existe una llanta con el mismo codigo de barras para no volverla a registrar
    @Transactional(readOnly = true)
    @Override
    public Optional<Inventario> findByCodigoBarras(String codBarras) {
        return inventarioRepository.findByCodigoBarras(codBarras);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Inventario> findByDot(String dot) {
        return inventarioRepository.findByDot(dot);
    }


    @Transactional
    @Override
    public void actualizarCreatedAt(String now, Integer id) {
        inventarioRepository.actualizaCreatedAt(now, id);
    }

    @Transactional(readOnly = true)
    @Override
    public Inventario buscarLlantaPorId(Integer idLlanta) {
        return inventarioRepository.findById(idLlanta).orElseThrow(() -> new InventarioException("No se encontro la llanta en el inventario."));
    }

    @Transactional
    @Override
    public void actualizarStock(Integer inventarioId, Integer stock, String active) {
        Inventario inventario = inventarioRepository.findById(inventarioId)
                .orElseThrow(() -> new InventarioException("No se encontro en el inventario"));

        if (stock < 0) {
            throw new InventarioException("Cantidad invalida");
        }

        inventario.setStock(inventario.getStock() - stock);

        inventarioRepository.actualizarStock(inventarioId, stock, active);

    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> first100Inventario(String active) {
        return inventarioRepository.first100Inventario(active);
    }


}//clase
