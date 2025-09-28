package com.mastertyres.marca.services;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MarcaService implements IMarcaServices {

    private final MarcaRepository marcaRepository;

    @Autowired
    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }

    public Marca guardarMarca(Marca marca) {
        return marcaRepository.save(marca);
    }

    @Override
    public List<Marca> listarMarcas() {
        return marcaRepository.findAll();
    }
    public List<String> listarNombresMarcas() {
        return marcaRepository.listarNombresMarcas();
    }

}
