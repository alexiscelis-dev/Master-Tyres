package com.mastertyres.vehiculoPromocion.repository;

import com.mastertyres.vehiculoPromocion.model.VehiculoPromocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VehiculoPromocionRepository extends JpaRepository<VehiculoPromocion, Integer> {
    // Opción simple (sin fetch join):
    List<VehiculoPromocion> findByPromocion_PromocionId(Integer promocionId);

    // Opción con fetch join (recomendada para poder leer marca/modelo fuera de la transacción):
    @Query("""
           select vp
           from VehiculoPromocion vp
           join fetch vp.marca m
           join fetch vp.modelo mo
           where vp.promocion.promocionId = :promocionId 
           """)
    List<VehiculoPromocion> findAllByPromocionIdWithMarcaModelo(@Param("promocionId") Integer promocionId);

    @Transactional
    @Modifying
    @Query("DELETE FROM VehiculoPromocion vp WHERE vp.promocion.promocionId = :id")
    void eliminarPorPromocionId(@Param("id") Integer id);

}//interface
