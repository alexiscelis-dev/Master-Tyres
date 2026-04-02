package com.mastertyres.clientesPromocion.repository;

import com.mastertyres.clientesPromocion.entity.ClientesPromocion;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClientePromocionRepository extends JpaRepository<ClientesPromocion, Integer> {

    final String DELETE_CLIENTESPROMOCION = "DELETE FROM ClientesPromocion cp";

    // 1. Eliminar relaciones por ID de promoción
    @Modifying
    @Transactional
    @Query(DELETE_CLIENTESPROMOCION + " " + "WHERE cp.promocion.promocionId = :promocionId")
    void deleteByPromocionId(Integer promocionId);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("""
                SELECT cp
                FROM ClientesPromocion cp
                JOIN FETCH cp.cliente c
                WHERE cp.promocion.promocionId = :promocionId
            """)
    List<ClientesPromocion> findByPromocionIdConCliente(Integer promocionId);


    // 3. Obtener relaciones por promoción (útil internamente)
    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    List<ClientesPromocion> findByPromocion_PromocionId(Integer promocionId);

    // En ClientePromocionRepository
    @Modifying
    @Transactional
    void deleteByPromocionPromocionId(Integer promocionId);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT p.img FROM ClientesPromocion cp JOIN cp.promocion p " +
            "WHERE cp.cliente.clienteId = :clienteId AND p.promocionId = :promocionId AND p.active = 'ACTIVE'")
    List<String> buscarImgPromocion(@Param("clienteId") Integer clienteId, @Param("promocionId") Integer promocionId);


}//class
