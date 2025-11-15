package com.mastertyres.inventario.service;

import com.mastertyres.common.exeptions.InventarioException;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.inventario.model.StatusInventario;
import com.mastertyres.inventario.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.mastertyres.common.MensajesAlert.mostrarError;
import static com.mastertyres.common.MensajesAlert.mostrarInformacion;

@Service
public class InventarioService implements IInventarioService {
    private InventarioRepository inventarioRepository;

    @Autowired
    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> listarInventario(String active) {
        return inventarioRepository.listarInventario(active);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscadorInventario(String active, String busqueda) {
        return inventarioRepository.buscadorInventario(active, busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorCodBarras(String active, String busqueda) {
        return inventarioRepository.buscarPorCodBarras(active, busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorDot(String active, String busqueda) {
        return inventarioRepository.buscarPorDot(active, busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorMarca(String active, String busqueda) {
        return inventarioRepository.buscarPorMarca(active, busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorModelo(String active, String busqueda) {
        return inventarioRepository.buscarPorModelo(active, busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorMedida(String active, String busqueda) {
        return inventarioRepository.buscarPorMedida(active, busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorFecha(String active, LocalDate fecha) {
        return inventarioRepository.buscarPorFecha(active, fecha);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorFecha(String active, LocalDate fechaInicio, LocalDate fechaFin) {
        return inventarioRepository.buscarPorFecha(active, fechaInicio, fechaFin);
    }

    @Transactional
    @Override
    public int eliminarInventario(String inactive, Integer idInventario) {

        int filasEliminadas = inventarioRepository.eliminarInventario(inactive, idInventario);

        if (filasEliminadas > 0)
            mostrarInformacion("Elemento eliminado", "Elemento eliminado", "Elemento eliminado exitosamente");
        else
            mostrarError("Error al eliminar elemento", "Algo salio mal", "No se pudo eliminar el elemento seleccionado.");

        return filasEliminadas;

    }

    @Transactional
    @Override
    public void guardarInventario(Inventario inventario) {
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
                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                inv.setUpdated_at(now);


                inv.setCodigoBarras(inventario.getCodigoBarras());
                inv.setDot(inventario.getDot());

                inventarioRepository.save(inv);
                return;

            }

        }//por codigo de barras

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
                String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                inv.setUpdated_at(now);

                inventarioRepository.save(inv);
                return;
            }
        } //por codigo de barras

        inventarioRepository.save(inventario);
    }//guardarInventario

    @Transactional
    @Override
    public void actualizarInventario(Inventario inventario) {
        inventarioRepository.save(inventario);
    }

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
        return inventarioRepository.findById(idLlanta).orElse(null);
    }

    @Transactional
    @Override
    public void actualizarStock(Integer inventarioId, Integer stock, String active) {
        Inventario inventario = inventarioRepository.findById(inventarioId)
                .orElseThrow(()-> new InventarioException("No se encontro en el inventario"));

        if (stock < 0){
            throw new InventarioException("Cantidad invalida");
        }

        inventario.setStock(inventario.getStock()-stock);

        inventarioRepository.actualizarStock(inventarioId,stock,active);

    }


}//clase
