package com.mastertyres.modelo.services;



import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.repository.ModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ModeloService implements IModeloServices {

    private final ModeloRepository modeloRepository;

    public List<String> listarNombresModelos() {
        return modeloRepository.listarNombresModelos();
    }

    @Autowired
    public ModeloService(ModeloRepository modeloRepository) {
        this.modeloRepository = modeloRepository;
    }

    @Override
    public List<Modelo> listarModelos() {
        return modeloRepository.findAll();
    }
}

