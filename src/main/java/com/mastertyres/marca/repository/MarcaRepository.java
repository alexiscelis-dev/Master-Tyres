package com.mastertyres.marca.repository;



import com.mastertyres.marca.model.Marca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface MarcaRepository extends JpaRepository<Marca, Integer> {

    @Query("SELECT m.nombreMarca FROM Marca m WHERE m.marcaId <> 1")
    List<String> listarNombresMarcas();


    @Query("SELECT m.nombreMarca FROM Marca m WHERE m.marcaId <> 1")
    Page<String> listarNombresMarcasPaginado(Pageable pageable);


    @Query("""
    SELECT m FROM Marca m
    WHERE LOWER(m.nombreMarca) LIKE LOWER(CONCAT('%', :filtro, '%')) AND m.marcaId <> 1
""")
    Page<Marca> buscarMarcasPorNombre(@Param("filtro") String filtro, Pageable pageable);


    @Query("SELECT m FROM Marca m WHERE m.marcaId <> 1")
    List<Marca> listarMarcasSinGenerica();

    @Query("SELECT m FROM Marca m WHERE m.marcaId <> 1")
    Page<Marca> listarMarcasPaginadoSinGenerica(Pageable pageable);


    // 🔹 Buscar por nombre ignorando mayúsculas/minúsculas (sin incluir la genérica)
    @Query("""
        SELECT m FROM Marca m
        WHERE LOWER(m.nombreMarca) LIKE LOWER(CONCAT('%', :nombre, '%'))
        AND m.marcaId <> 1
    """)
    Page<Marca> findByNombreMarcaContainingIgnoreCaseExcludingGenerica(@Param("nombre") String nombre, Pageable pageable);


//    @Query("SELECT m.nombreMarca FROM Marca m WHERE LOWER(m.nombreMarca) LIKE LOWER(CONCAT('%', :filtro, '%'))")
//    Page<String> buscarMarcasPorNombre(@Param("filtro") String filtro, Pageable pageable);

    Page<Marca> findByNombreMarcaContainingIgnoreCase(String nombre, Pageable pageable);

    // Eliminar marca (si no es el genérico)
    @Modifying
    @Transactional
    @Query("DELETE FROM Marca m WHERE m.marcaId = :marcaId AND m.marcaId <> 1")
    int eliminarMarcaPorId(Integer marcaId);



}

