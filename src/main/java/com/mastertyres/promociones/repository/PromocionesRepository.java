package com.mastertyres.promociones.repository;

import com.mastertyres.promociones.model.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


public interface PromocionesRepository extends JpaRepository<Promocion, Integer> {

    @Query("SELECT p FROM Promocion p " +
            "WHERE p.active = 'ACTIVE' " +
            "AND (p.fechaInicio IS NULL OR p.fechaInicio <= :hoy) " +
            "AND (p.fechaFin IS NULL OR p.fechaFin >= :hoy)")
    List<Promocion> findPromocionesActivas(@Param("hoy") String hoy);

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
            " CAST(p.valorDescuento AS string) LIKE CONCAT('%', :texto, '%') OR " +
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
}
