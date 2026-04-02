package com.mastertyres.categoria.service;

import com.mastertyres.categoria.entity.Categoria;
import com.mastertyres.categoria.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class CategoriaService implements ICategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    //  Listar categorías excluyendo la genérica
    @Transactional(readOnly = true)
    @Override
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .filter(c -> c.getCategoriaId() != 1)
                .sorted(Comparator.comparing(Categoria::getNombreCategoria))
                .toList();
    }

    //  Listar nombres sin incluir la genérica
    @Transactional(readOnly = true)
    @Override
    public List<String> listarNombresCategorias() {
        return categoriaRepository.listarNombresCategorias();
    }

    //  Buscar categorías por nombre (excluyendo la genérica)
    @Transactional(readOnly = true)
    @Override
    public Page<Categoria> buscarCategoriasPorNombre(String filtro, Pageable pageable) {
        return categoriaRepository.buscarCategoriasPorNombre(filtro, pageable);
    }
}//class
