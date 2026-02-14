package com.mastertyres.vehiculoPromocion.repository;

import com.mastertyres.vehiculoPromocion.model.VehiculoPromocion;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehiculoPromocionRepository extends JpaRepository<VehiculoPromocion, Integer> {

    final String UPDATE_VEHICULO_PROMOCION = "UPDATE VehiculoPromocion v SET";
    final String DELETE_VEHICULO_PROMOCION = "DELETE FROM VehiculoPromocion vp";


    // Opción simple (sin fetch join):
    List<VehiculoPromocion> findByPromocion_PromocionId(Integer promocionId);

    // Opción con fetch join (recomendada para poder leer marca/modelo fuera de la transacción):
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("""
           SELECT vp
           FROM VehiculoPromocion vp
           JOIN fetch vp.marca m
           JOIN fetch vp.modelo mo
           WHERE vp.promocion.promocionId = :promocionId ORDER BY m.nombreMarca, mo.nombreModelo 
           """)
    List<VehiculoPromocion> findAllByPromocionIdWithMarcaModelo(@Param("promocionId") Integer promocionId);


    @Modifying
    @Query(DELETE_VEHICULO_PROMOCION + " " + "WHERE vp.promocion.promocionId = :id")
    void eliminarPorPromocionId(@Param("id") Integer id);


    // Reasignar marca por ID
    @Modifying
    @Query(UPDATE_VEHICULO_PROMOCION + " " + "v.marca.marcaId = 1 WHERE v.marca.marcaId = :marcaId")
    int reasignarMarcaPorId(Integer marcaId);

    // Reasignar modelo y categoría a genéricos por marca
    @Modifying
    @Query(UPDATE_VEHICULO_PROMOCION + " " + "v.modelo.modeloId = 1 WHERE v.marca.marcaId = :marcaId")
    int reasignarModeloYCategoriaPorMarca(Integer marcaId);

    // Reasignar modelo específico a modelo genérico
    @Modifying
    @Query(UPDATE_VEHICULO_PROMOCION + " " + "v.modelo.modeloId = 1 WHERE v.modelo.modeloId = :modeloId")
    int reasignarModeloPorId(Integer modeloId);

}//interface
