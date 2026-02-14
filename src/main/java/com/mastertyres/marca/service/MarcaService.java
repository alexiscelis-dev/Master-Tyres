package com.mastertyres.marca.service;

import com.mastertyres.marca.domain.MarcaValidator;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaService implements IMarcaServices {

    private final MarcaRepository marcaRepository;
    @Autowired
    private MarcaValidator marcaValidator;

    @Autowired
    public MarcaService(MarcaRepository marcaRepository) {
        this.marcaRepository = marcaRepository;
    }



    @Transactional
    @Override
    public Marca guardarMarca(Marca marca) {
        //validar exepciones al guardar
        marcaValidator.validarGuardar(marca);

        return marcaRepository.save(marca);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Marca> listarMarcas() {
        return marcaRepository.listarMarcasSinGenerica();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Marca> findByNombreMarca(String nombreMarca) {
        return marcaRepository.findByNombreMarca(nombreMarca);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> listarNombresMarcas() {
        return marcaRepository.listarNombresMarcas();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<String> listarNombresMarcas( Pageable pageable) {
        return marcaRepository.listarNombresMarcasPaginado(pageable);
    }

    // Buscar marcas por nombre (paginado)
    @Transactional(readOnly = true)
    @Override
    public Page<Marca> buscarMarcasPorNombre(String filtro, Pageable pageable) {
        return marcaRepository.buscarMarcasPorNombre(filtro, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Marca> listarMarcasPaginado(Pageable pageable) {
        return marcaRepository.listarMarcasPaginadoSinGenerica(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Marca> buscarMarcasPorNombreMarca(String filtro, Pageable pageable) {
        return marcaRepository.findByNombreMarcaContainingIgnoreCaseExcludingGenerica(filtro, pageable);
    }


    //  Listar paginado
    @Transactional(readOnly = true)
    @Override
    public Page<Marca> listarMarcasPaginado(int numeroPagina, int tamañoPagina) {
        Pageable pageable = PageRequest.of(numeroPagina, tamañoPagina);
        return marcaRepository.listarMarcasPaginadoSinGenerica(pageable);
    }

    //  Buscar marcas paginado
    @Transactional(readOnly = true)
    @Override
    public Page<Marca> buscarMarcasPorNombre(String filtro, int numeroPagina, int tamañoPagina) {
        Pageable pageable = PageRequest.of(numeroPagina, tamañoPagina);
        return marcaRepository.findByNombreMarcaContainingIgnoreCaseExcludingGenerica(filtro, pageable);
    }


    @Transactional
    @Override
    public int eliminarMarcaPorId(Integer marcaId) {

        marcaValidator.validarEliminacion(marcaId);

        return marcaRepository.eliminarMarcaPorId(marcaId);

    }

}//class
