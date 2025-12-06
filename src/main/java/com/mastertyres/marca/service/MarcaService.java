package com.mastertyres.marca.service;

import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return marcaRepository.listarMarcasSinGenerica();
    }


    public List<String> listarNombresMarcas() {
        return marcaRepository.listarNombresMarcas();
    }

    public Page<String> listarNombresMarcas( Pageable pageable) {
        return marcaRepository.listarNombresMarcasPaginado(pageable);
    }

    // Buscar marcas por nombre (paginado)
    public Page<Marca> buscarMarcasPorNombre(String filtro, Pageable pageable) {
        return marcaRepository.buscarMarcasPorNombre(filtro, pageable);
    }

    public Page<Marca> listarMarcasPaginado(Pageable pageable) {
        return marcaRepository.listarMarcasPaginadoSinGenerica(pageable);
    }

    public Page<Marca> buscarMarcasPorNombreMarca(String filtro, Pageable pageable) {
        return marcaRepository.findByNombreMarcaContainingIgnoreCaseExcludingGenerica(filtro, pageable);
    }


    //  Listar paginado
    public Page<Marca> listarMarcasPaginado(int numeroPagina, int tamañoPagina) {
        Pageable pageable = PageRequest.of(numeroPagina, tamañoPagina);
        return marcaRepository.listarMarcasPaginadoSinGenerica(pageable);
    }

    //  Buscar marcas paginado
    public Page<Marca> buscarMarcasPorNombre(String filtro, int numeroPagina, int tamañoPagina) {
        Pageable pageable = PageRequest.of(numeroPagina, tamañoPagina);
        return marcaRepository.findByNombreMarcaContainingIgnoreCaseExcludingGenerica(filtro, pageable);
    }


    @Transactional
    public int eliminarMarcaPorId(Integer marcaId) {
        return marcaRepository.eliminarMarcaPorId(marcaId);
    }




}
