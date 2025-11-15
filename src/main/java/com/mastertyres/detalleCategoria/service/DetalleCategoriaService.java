package com.mastertyres.detalleCategoria.service;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.detalleCategoria.model.DetalleCategoria;
import com.mastertyres.detalleCategoria.repository.DetalleCategoriaRepository;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleCategoriaService implements IDetalleCategoriaService {


    @Autowired
    private DetalleCategoriaRepository detalleCategoriaRepository;

    public List<DetalleCategoria> getByMarca(Integer marcaId) {
        return detalleCategoriaRepository.findByMarcaId(marcaId);
    }

    public List<DetalleCategoria> buscarPorTexto(String busqueda) {
        if (busqueda == null || busqueda.trim().isEmpty()) {
            return detalleCategoriaRepository.findAll();
        }
        return detalleCategoriaRepository.buscarPorMarcaModeloOCategoria(busqueda.trim());
    }

    public Page<DetalleCategoria> buscarPorTextoPaginado(String busqueda, Pageable pageable) {
        if (busqueda == null || busqueda.trim().isEmpty()) {
            return Page.empty();
        }
        return detalleCategoriaRepository.buscarPorMarcaModeloOCategoriaPaginado(busqueda.trim(), pageable);
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

    @Override
    public List<DetalleCategoria> findByCategoria(Categoria categoria) {
        return detalleCategoriaRepository.findByCategoria(categoria);
    }

    @Transactional
    public void reasignarOEliminarPorModelo(Integer modeloId) {
        List<DetalleCategoria> registros = detalleCategoriaRepository.findByModeloId(modeloId);

        for (DetalleCategoria dc : registros) {
            Integer marcaId = dc.getMarca().getMarcaId();
            Integer categoriaId = dc.getCategoria().getCategoriaId();

            // Verificar si ya existe un registro con la misma marca pero modelo/categoría genéricos
            Optional<DetalleCategoria> existente = detalleCategoriaRepository.findByMarcaModeloCategoria(
                    marcaId, 1, 1
            );

            if (existente.isPresent()) {
                // Ya hay un registro genérico para esa marca, eliminar este específico
                detalleCategoriaRepository.eliminarPorId(dc.getDetalle_categoria_id());
            } else {
                // No existe duplicado, solo reasignar el actual a los genéricos
                detalleCategoriaRepository.reasignarAGenerico(dc.getDetalle_categoria_id());
            }
        }
    }

    @Transactional
    public void eliminarPorMarca(Integer marcaId) {
        try {
            detalleCategoriaRepository.eliminarPorMarcaId(marcaId);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar los registros de detalleCategoria para la marca con ID: " + marcaId, e);
        }
    }



}
