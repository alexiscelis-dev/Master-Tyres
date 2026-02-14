package com.mastertyres.categoria.repository;

import com.mastertyres.categoria.model.Categoria;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    final String SELECT_FROM_CATEGORIA = "SELECT c FROM Categoria c";


    //  Listar nombres sin incluir la categoría genérica
    @Query("SELECT c.nombreCategoria FROM Categoria c WHERE c.categoriaId <> 1")
    List<String> listarNombresCategorias();


    //  Buscar categorías por nombre (excluyendo la genérica)
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_FROM_CATEGORIA + " "+ """
            WHERE LOWER(c.nombreCategoria) LIKE LOWER(CONCAT('%', :filtro, '%')) AND c.categoriaId <> 1""")
    Page<Categoria> buscarCategoriasPorNombre(@Param("filtro") String filtro, Pageable pageable);
}
