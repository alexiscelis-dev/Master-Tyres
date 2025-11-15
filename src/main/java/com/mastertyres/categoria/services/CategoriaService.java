package com.mastertyres.categoria.services;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.categoria.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService implements ICategoriaServices {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // 🔹 Listar categorías excluyendo la genérica
    @Override
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .filter(c -> c.getCategoriaId() != 1)
                .toList();
    }

    // 🔹 Listar nombres sin incluir la genérica
    public List<String> listarNombresCategorias() {
        return categoriaRepository.listarNombresCategorias();
    }

    // 🔹 Buscar categorías por nombre (excluyendo la genérica)
    public Page<Categoria> buscarCategoriasPorNombre(String filtro, Pageable pageable) {
        return categoriaRepository.buscarCategoriasPorNombre(filtro, pageable);
    }
}
