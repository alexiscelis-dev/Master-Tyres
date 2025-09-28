package com.mastertyres.detalleCategoria.repository;

import com.mastertyres.detalleCategoria.model.DetalleCategoria;
import com.mastertyres.marca.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DetalleCategoriaRepository extends JpaRepository<DetalleCategoria, Integer> {

    // Obtener todos los modelos + categorias por marca
    @Query("""
       SELECT d FROM DetalleCategoria d
       JOIN FETCH d.modelo
       JOIN FETCH d.categoria
       WHERE d.marca.marcaId = :marcaId
       """)
    List<DetalleCategoria> findByMarcaId(@Param("marcaId") Integer marcaId);


    // Eliminar todos los modelos + categorias relacionados a una marca
    @Modifying
    @Transactional
    @Query("DELETE FROM DetalleCategoria d WHERE d.marca.marcaId = :marcaId")
    void deleteByMarcaId(@Param("marcaId") Integer marcaId);


    @Query("SELECT d FROM DetalleCategoria d " +
            "JOIN FETCH d.marca " +
            "JOIN FETCH d.modelo " +
            "JOIN FETCH d.categoria " +
            "WHERE d.marca = :marca")
    List<DetalleCategoria> findByMarcaWithRelations(@Param("marca") Marca marca);

    // Buscar todos los registros por marca
    List<DetalleCategoria> findByMarca(Marca marca);



}
