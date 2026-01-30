package com.mastertyres.modelo.repository;

import com.mastertyres.modelo.model.Modelo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ModeloRepository extends JpaRepository<Modelo, Integer> {
    //  Listar nombres sin incluir el modelo genérico
    @Query("SELECT mo.nombreModelo FROM Modelo mo WHERE mo.modeloId <> 1 ORDER BY mo.nombreModelo ASC")
    List<String> listarNombresModelos();

    //  Buscar modelos por nombre (excluyendo el genérico)
    @Query("""
        SELECT mo FROM Modelo mo
        WHERE LOWER(mo.nombreModelo) LIKE LOWER(CONCAT('%', :filtro, '%'))
        AND mo.modeloId <> 1
    """)
    Page<Modelo> buscarModelosPorNombre(@Param("filtro") String filtro, Pageable pageable);

    //  Reasignar marca (para eliminación segura)
    @Modifying
    @Transactional
    @Query("UPDATE Modelo v SET v.marca_id.marcaId = 1 WHERE v.marca_id.marcaId = :marcaId")
    int reasignarMarcaPorId(Integer marcaId);

    //  Eliminar modelo (si no es el genérico)
    @Modifying
    @Transactional
    @Query("DELETE FROM Modelo m WHERE m.modeloId = :modeloId AND m.modeloId <> 1")
    int eliminarModeloPorId(Integer modeloId);

}//class

