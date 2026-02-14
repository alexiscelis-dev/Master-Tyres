package com.mastertyres.modelo.service;

import com.mastertyres.marca.domain.MarcaValidator;
import com.mastertyres.modelo.domain.ModeloValidator;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModeloService implements IModeloService {

    private final ModeloRepository modeloRepository;


    @Autowired
    private ModeloValidator modeloValidator;
    @Autowired
    private MarcaValidator marcaValidator;



    @Autowired
    public ModeloService(ModeloRepository modeloRepository) {
        this.modeloRepository = modeloRepository;
    }

    @Transactional
    @Override
    public Modelo guardarModelo(Modelo modelo) {
        //validar exepciones al guardar
        modeloValidator.validarGuardar(modelo);

        return modeloRepository.save(modelo);
    }

    //  Listar modelos excluyendo el genérico
    @Transactional(readOnly = true)
    @Override
    public List<Modelo> listarModelos() {
        return modeloRepository.listarModelos();
    }

    //  Listar nombres sin incluir el genérico
    @Transactional(readOnly = true)
    @Override
    public List<String> listarNombresModelos() {
        return modeloRepository.listarNombresModelos();
    }

    //  Buscar modelos por nombre
    @Transactional(readOnly = true)
    @Override
    public Page<Modelo> buscarModelosPorNombre(String filtro, Pageable pageable) {
        return modeloRepository.buscarModelosPorNombre(filtro, pageable);
    }

    //  Reasignar marca
    @Transactional
    @Override
    public int reasignarMarcaPorId(Integer marcaId) {
        return modeloRepository.reasignarMarcaPorId(marcaId);
    }

    //  Eliminar modelo (si no es el genérico)
    @Transactional
    @Override
    public int eliminarModeloPorId(Integer modeloId) {

        modeloValidator.validarEliminar(modeloId);

        return modeloRepository.eliminarModeloPorId(modeloId);
    }

}//class
