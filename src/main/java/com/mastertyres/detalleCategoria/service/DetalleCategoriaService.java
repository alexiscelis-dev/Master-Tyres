package com.mastertyres.detalleCategoria.service;

import com.mastertyres.detalleCategoria.model.DetalleCategoria;
import com.mastertyres.detalleCategoria.repository.DetalleCategoriaRepository;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DetalleCategoriaService implements IDetalleCategoriaService {


    @Autowired
    private DetalleCategoriaRepository detalleCategoriaRepository;

    public List<DetalleCategoria> getByMarca(Integer marcaId) {
        return detalleCategoriaRepository.findByMarcaId(marcaId);
    }

    @Transactional
    public void guardarCambios(Marca marca, List<DetalleCategoria> nuevos) {
        // 1. Eliminar existentes
        detalleCategoriaRepository.deleteByMarcaId(marca.getMarcaId());

        // 2. Guardar nuevos
        for (DetalleCategoria d : nuevos) {
            d.setMarca(marca);
            detalleCategoriaRepository.save(d);
        }
    }

    @Autowired
    public DetalleCategoriaService(DetalleCategoriaRepository detalleCategoriaRepository){
        this.detalleCategoriaRepository = detalleCategoriaRepository;
    }

    public List<DetalleCategoria> listarTodos() {
        return detalleCategoriaRepository.findAll();
    }

    public List<DetalleCategoria> listarPorMarca(Marca marca) {
        return detalleCategoriaRepository.findByMarcaWithRelations(marca);
    }

    @Override
    public List<DetalleCategoria> listarDetalleCategoria() {
        return detalleCategoriaRepository.findAll();
    }

    public DetalleCategoria guardarDetalleCategoria(DetalleCategoria detalle) {
        return detalleCategoriaRepository.save(detalle);
    }
}
