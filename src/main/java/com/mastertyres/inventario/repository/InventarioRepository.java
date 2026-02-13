package com.mastertyres.inventario.repository;

import com.mastertyres.inventario.model.Inventario;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    final String SELECT_INVENTARIO = "SELECT i FROM Inventario i";
    final String UPDATE_INVENTARIO = "UPDATE Inventario i SET";

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active")
    List<Inventario> listarInventario(@Param("active") String active);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active ORDER BY i.identificadorLlanta ASC")
    Page<Inventario> listarInventarioPaginada(@Param("active") String active, Pageable pageable);

    @Query("SELECT COUNT(v) FROM Inventario v WHERE v.active = :active ORDER BY v.identificadorLlanta ASC")
    long contarInventarioActivos(@Param("active") String active);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active AND " +
            "LOWER(COALESCE(i.codigoBarras,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.dot,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.modelo,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.medida,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.indiceCarga,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.indiceVelocidad,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "CAST(i.stock AS STRING) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.marca,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    List<Inventario> buscadorInventario(@Param("active") String active, @Param("busqueda") String busqueda);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active AND " +
            "LOWER(COALESCE(i.codigoBarras,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.dot,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.modelo,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.medida,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.indiceCarga,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.indiceVelocidad,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "CAST(i.stock AS STRING) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.marca,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    Page<Inventario> buscadorInventarioPaginado(@Param("active") String active, @Param("busqueda") String busqueda, Pageable pageable);

    @Query("SELECT COUNT(i) FROM Inventario i WHERE i.active = :status AND " +
            "LOWER(COALESCE(i.codigoBarras,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.dot,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.modelo,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.medida,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.indiceCarga,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.indiceVelocidad,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "CAST(i.stock AS STRING) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.marca,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    long contarInventarioPorBusquedaGeneral(String status, String busqueda);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active AND LOWER(COALESCE(i.codigoBarras,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    List<Inventario> buscarPorCodBarras(@Param("active") String active, @Param("busqueda") String busqueda);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active AND LOWER(COALESCE(i.codigoBarras,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    Page<Inventario> buscarPorCodBarrasPaginado(@Param("active") String active, @Param("busqueda") String busqueda, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active AND LOWER(COALESCE(i.dot,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    List<Inventario> buscarPorDot(@Param("active") String active, @Param("busqueda") String busqueda);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active AND LOWER(COALESCE(i.dot,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    Page<Inventario> buscarPorDotPaginado(@Param("active") String active, @Param("busqueda") String busqueda, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active AND LOWER(COALESCE(i.marca,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    List<Inventario> buscarPorMarca(@Param("active") String active, @Param("busqueda") String busqueda);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active AND LOWER(COALESCE(i.marca,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    Page<Inventario> buscarPorMarcaPaginado(@Param("active") String active, @Param("busqueda") String busqueda, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active AND LOWER(COALESCE(i.modelo,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    List<Inventario> buscarPorModelo(@Param("active") String active, @Param("busqueda") String busqueda);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active AND LOWER(COALESCE(i.modelo,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    Page<Inventario> buscarPorModeloPaginado(@Param("active") String active, @Param("busqueda") String busqueda, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active AND LOWER(COALESCE(i.medida,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    List<Inventario> buscarPorMedida(@Param("active") String active, @Param("busqueda") String busqueda);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active AND LOWER(COALESCE(i.medida,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    Page<Inventario> buscarPorMedidaPaginado(@Param("active") String active, @Param("busqueda") String busqueda, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active AND DATE(i.created_at) = :fecha")
    List<Inventario> buscarPorFecha(@Param("active") String active, @Param("fecha") LocalDate fecha);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active AND DATE(i.created_at) = :fecha")
    Page<Inventario> buscarPorFechaPaginado(@Param("active") String active, @Param("fecha") LocalDate fecha, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active AND DATE(i.created_at) BETWEEN :fechaInicio AND :fechaFin")
    List<Inventario> buscarPorFecha(@Param("active") String active, @Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active AND DATE(i.created_at) BETWEEN :fechaInicio AND :fechaFin")
    Page<Inventario> buscarPorFechaPaginado(@Param("active") String active, @Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin, Pageable pageable);


    @Modifying
    @Query(UPDATE_INVENTARIO + " " + "i.active = :inactive WHERE i.inventarioId = :idInventario")
    int eliminarInventario(@Param("inactive") String inactive, @Param("idInventario") Integer idInventario);


    @Modifying
    @Query(UPDATE_INVENTARIO + " " + "i.updated_at = :now WHERE i.inventarioId = :idInventario ")
    void actualizarUpdatedAt(@Param("now") String now, @Param("idInventario") Integer idInventario);


    @Modifying
    @Query(UPDATE_INVENTARIO + " " + "i.created_at = :now WHERE i.inventarioId = :idInventario ")
    void actualizaCreatedAt(@Param("now") String now, @Param("idInventario") Integer idInventario);

    Optional<Inventario> findByCodigoBarras(String codBarras);

    Optional<Inventario> findByDot(String dot);

    Optional<Inventario> findByIdentificadorLlanta(String identificador);


    @Modifying
    @Query(UPDATE_INVENTARIO + " " + "i.stock = :stock WHERE i.inventarioId = :inventarioId AND i.active = :active")
    void actualizarStock(@Param("inventarioId") Integer inventarioId, @Param("stock") Integer stock, @Param("active") String active);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_INVENTARIO + " " + "WHERE i.active = :active ORDER BY i.identificadorLlanta ASC LIMIT 100")
    List<Inventario> first100Inventario(@Param("active") String active);


}//interface
