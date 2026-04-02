package com.mastertyres.modelo.repository;

import com.mastertyres.modelo.entity.Modelo;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ModeloRepository extends JpaRepository<Modelo, Integer> {

    final String SELECT_FROM_MODELO = "SELECT mo FROM Modelo mo";
    final String DELETE_MODELO = "DELETE FROM Modelo m";
    final String UPDTE_MODELO = "UPDATE Modelo v SET";

    //  Listar nombres sin incluir el modelo genérico
    @Query("SELECT mo.nombreModelo FROM Modelo mo WHERE mo.modeloId <> 1 ORDER BY mo.nombreModelo ASC")
    List<String> listarNombresModelos();

    //  Buscar modelos por nombre (excluyendo el genérico)
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_FROM_MODELO + " " + "WHERE LOWER(mo.nombreModelo) LIKE LOWER(CONCAT('%', :filtro, '%')) AND mo.modeloId <> 1")
    Page<Modelo> buscarModelosPorNombre(@Param("filtro") String filtro, Pageable pageable);

    //  Reasignar marca (para eliminación segura)
    @Modifying
    @Query(UPDTE_MODELO + " " + "v.marca_id.marcaId = 1 WHERE v.marca_id.marcaId = :marcaId")
    int reasignarMarcaPorId(Integer marcaId);

    //  Eliminar modelo (si no es el genérico)
    @Modifying
    @Query(DELETE_MODELO + " " + "WHERE m.modeloId = :modeloId AND m.modeloId <> 1")
    int eliminarModeloPorId(Integer modeloId);

    @QueryHints(@QueryHint(name = "org.hibernate.annotations.QueryHints.READ_ONLY", value = "true"))
    @Query("SELECT m FROM Modelo m WHERE m.modeloId <> 1 ORDER BY m.nombreModelo ASC")
    List<Modelo> listarModelos();

}//class

