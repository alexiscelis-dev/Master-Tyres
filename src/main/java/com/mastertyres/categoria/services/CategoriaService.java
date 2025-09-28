package com.mastertyres.categoria.services;



import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.categoria.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService implements ICategoriaServices {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public List<String> listarNombresCategorias() {
        return categoriaRepository.listarNombresCategorias();
    }



}

