package com.mastertyres.modelo.services;



import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.repository.ModeloRepository;
import com.mastertyres.vehiculo.model.Vehiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public Modelo guardarModelo(Modelo modelo) {
        return modeloRepository.save(modelo);
    }

    @Override
    public List<Modelo> listarModelos() {
        return modeloRepository.findAll();
    }
}

