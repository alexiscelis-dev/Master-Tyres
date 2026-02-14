package com.mastertyres.promociones.repository;

import com.mastertyres.promociones.model.Promocion;
import com.mastertyres.promociones.model.StatusPromocion;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface PromocionesRepository extends JpaRepository<Promocion, Integer> {

    final String UPDATE_PROMOCION = "UPDATE Promocion p SET";
    final String SELECT_FROM_PROMOCION = "SELECT p FROM Promocion p";

    @Modifying
    @Query(UPDATE_PROMOCION + " " + "p.active = 'INACTIVE' WHERE p.promocionId = :id")
    int desactivarPromocion(@Param("id") Integer id);

    @QueryHints(@QueryHint(name = "org.hibernate.annotations.QueryHints.READ_ONLY", value = "true"))
    @Query(SELECT_FROM_PROMOCION + " " +
            "WHERE p.active = 'ACTIVE' " +
            "AND (p.fechaInicio IS NULL OR p.fechaInicio <= :hoy) " +
            "AND (p.fechaFin IS NULL OR p.fechaFin >= :hoy)")
    List<Promocion> findPromocionesActivas(@Param("hoy") String hoy);

    @QueryHints(@QueryHint(name = "org.hibernate.annotations.QueryHints.READ_ONLY", value = "true"))
    @Query("SELECT DISTINCT p FROM Promocion p " +
            "LEFT JOIN VehiculoPromocion vp ON vp.promocion = p " +
            "LEFT JOIN Marca m ON vp.marca = m " +
            "LEFT JOIN Modelo mo ON vp.modelo = mo " +
            "WHERE p.active = 'ACTIVE' " +
            "AND (p.fechaInicio IS NULL OR p.fechaInicio <= :hoy) " +
            "AND (p.fechaFin IS NULL OR p.fechaFin >= :hoy) " +
            "AND (" +
            " LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            " LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            " LOWER(p.tipoDescuento) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            " CAST(p.porcentaje AS string) LIKE CONCAT('%', :texto, '%') OR " +
            " CAST(p.fechaInicio AS string) LIKE CONCAT('%', :texto, '%') OR " +
            " CAST(p.fechaFin AS string) LIKE CONCAT('%', :texto, '%') OR " +
            " CAST(p.precio AS string) LIKE CONCAT('%', :texto, '%') OR " +
            " LOWER(m.nombreMarca) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            " LOWER(mo.nombreModelo) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            " CAST(vp.annio AS string) LIKE CONCAT('%', :texto, '%')" +
            ")")
    List<Promocion> buscarPromocionesActivas(
            @Param("hoy") String hoy,
            @Param("texto") String texto);


    @Modifying
    @Transactional
    @Query("""
    UPDATE Promocion p
    SET p.active = :estadoDelete
    WHERE p.fechaFin < :fechaActual
    AND p.active = :estadoActive
""")
    int marcarPromocionesVencidas(
            @Param("fechaActual") String fechaActual,
            @Param("estadoDelete") String estadoDelete,
            @Param("estadoActive") String estadoActive
    );

}//interface
