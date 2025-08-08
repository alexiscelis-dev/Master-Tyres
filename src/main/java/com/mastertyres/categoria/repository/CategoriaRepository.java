package com.mastertyres.categoria.repository;

import com.mastertyres.categoria.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    @Query("SELECT c.nombreCategoria FROM Categoria c")
    List<String> listarNombresCategorias();


}

