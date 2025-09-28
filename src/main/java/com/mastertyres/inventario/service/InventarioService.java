package com.mastertyres.inventario.service;

import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.inventario.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.mastertyres.common.MensajesAlert.mostrarError;
import static com.mastertyres.common.MensajesAlert.mostrarInformacion;

@Service
public class InventarioService implements IInventarioService{
    private InventarioRepository inventarioRepository;

    @Autowired
    public InventarioService(InventarioRepository inventarioRepository){
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
        return inventarioRepository.buscadorInventario(active,busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorCodBarras(String active, String busqueda) {
        return inventarioRepository.buscarPorCodBarras(active,busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorDot(String active, String busqueda) {
        return inventarioRepository.buscarPorDot(active,busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorMarca(String active, String busqueda) {
        return inventarioRepository.buscarPorMarca(active,busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorModelo(String active, String busqueda) {
        return inventarioRepository.buscarPorModelo(active,busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorMedida(String active, String busqueda) {
        return inventarioRepository.buscarPorMedida(active,busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorFecha(String active, LocalDate fecha) {
        return inventarioRepository.buscarPorFecha(active,fecha);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Inventario> buscarPorFecha(String active, LocalDate fechaInicio, LocalDate fechaFin) {
        return inventarioRepository.buscarPorFecha(active,fechaInicio,fechaFin);
    }

    @Transactional
    @Override
    public int eliminarInventario(String inactive, Integer idInventario) {

        int filasEliminadas = inventarioRepository.eliminarInventario(inactive,idInventario);

        if (filasEliminadas > 0)
            mostrarInformacion("Elemento eliminado","Elemento eliminado","Elemento eliminado exitosamente");
        else
            mostrarError("Error al eliminar elemento","Algo salio mal","No se pudo eliminar el elemento seleccionado.");

        return filasEliminadas;

    }

    @Transactional
    @Override
    public void guardarInventario(Inventario inventario) {
        inventarioRepository.save(inventario);
    }

}//clase
