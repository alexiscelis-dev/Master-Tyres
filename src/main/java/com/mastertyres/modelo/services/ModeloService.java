package com.mastertyres.modelo.services;

import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModeloService implements IModeloServices {

    private final ModeloRepository modeloRepository;

    @Autowired
    public ModeloService(ModeloRepository modeloRepository) {
        this.modeloRepository = modeloRepository;
    }

    public Modelo guardarModelo(Modelo modelo) {
        return modeloRepository.save(modelo);
    }

    // 🔹 Listar modelos excluyendo el genérico
    @Override
    public List<Modelo> listarModelos() {
        return modeloRepository.findAll()
                .stream()
                .filter(m -> m.getModeloId() != 1)
                .toList();
    }

    // 🔹 Listar nombres sin incluir el genérico
    public List<String> listarNombresModelos() {
        return modeloRepository.listarNombresModelos();
    }

    // 🔹 Buscar modelos por nombre
    public Page<Modelo> buscarModelosPorNombre(String filtro, Pageable pageable) {
        return modeloRepository.buscarModelosPorNombre(filtro, pageable);
    }

    // 🔹 Reasignar marca
    @Transactional
    public int reasignarMarcaPorId(Integer marcaId) {
        return modeloRepository.reasignarMarcaPorId(marcaId);
    }

    // 🔹 Eliminar modelo (si no es el genérico)
    @Transactional
    public int eliminarModeloPorId(Integer modeloId) {
        return modeloRepository.eliminarModeloPorId(modeloId);
    }
}
