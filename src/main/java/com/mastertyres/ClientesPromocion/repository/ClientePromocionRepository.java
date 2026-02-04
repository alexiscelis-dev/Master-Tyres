package com.mastertyres.ClientesPromocion.repository;

import com.mastertyres.ClientesPromocion.model.ClientesPromocion;
import com.mastertyres.cliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClientePromocionRepository extends JpaRepository<ClientesPromocion, Integer> {

    // 1. Eliminar relaciones por ID de promoción
    @Modifying
    @Transactional
    @Query("DELETE FROM ClientesPromocion cp WHERE cp.promocion.promocionId = :promocionId")
    void deleteByPromocionId(Integer promocionId);

    @Query("""
    SELECT cp
    FROM ClientesPromocion cp
    JOIN FETCH cp.cliente c
    WHERE cp.promocion.promocionId = :promocionId
""")
    List<ClientesPromocion> findByPromocionIdConCliente(Integer promocionId);


    // 3. Obtener relaciones por promoción (útil internamente)
    List<ClientesPromocion> findByPromocion_PromocionId(Integer promocionId);

    // En ClientePromocionRepository
    @Modifying
    @Transactional
    void deleteByPromocionPromocionId(Integer promocionId);

}
