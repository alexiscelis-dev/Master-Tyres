package com.mastertyres.inventario.repository;

import com.mastertyres.inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface InventarioRepository extends JpaRepository<Inventario,Integer> {

    @Query("SELECT i FROM Inventario i WHERE i.active = :active")
    List<Inventario> listarInventario(@Param("active")String active);

    @Query("SELECT i FROM Inventario i WHERE i.active = :active AND " +
            "LOWER(COALESCE(i.codigoBarras,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.dot,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.modelo,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.medida,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.indiceCarga,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.indiceVelocidad,'')) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "CAST(i.stock AS STRING) LIKE LOWER(CONCAT('%',:busqueda,'%')) OR " +
            "LOWER(COALESCE(i.marca,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    List<Inventario>buscadorInventario(@Param("active") String active, @Param("busqueda")String busqueda);

    @Query("SELECT i FROM Inventario i WHERE i.active = :active AND LOWER(COALESCE(i.codigoBarras,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    List<Inventario>buscarPorCodBarras(@Param("active")String active ,@Param("busqueda")String busqueda);

    @Query("SELECT i FROM Inventario i WHERE i.active = :active AND LOWER(COALESCE(i.dot,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    List<Inventario>buscarPorDot(@Param("active")String active, @Param("busqueda")String busqueda);

    @Query("SELECT i FROM Inventario i WHERE i.active = :active AND LOWER(COALESCE(i.marca,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    List<Inventario>buscarPorMarca(@Param("active")String active, @Param("busqueda")String busqueda);

    @Query("SELECT i FROM Inventario i WHERE i.active = :active AND LOWER(COALESCE(i.modelo,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    List<Inventario>buscarPorModelo(@Param("active")String active, @Param("busqueda")String busqueda);

    @Query("SELECT i FROM Inventario i WHERE i.active = :active AND LOWER(COALESCE(i.medida,'')) LIKE LOWER(CONCAT('%',:busqueda,'%'))")
    List<Inventario>buscarPorMedida(@Param("active")String active, @Param("busqueda")String busqueda);

    @Query("SELECT i FROM Inventario i WHERE i.active = :active AND DATE(i.created_at) = :fecha")
    List<Inventario>buscarPorFecha(@Param("active")String active, @Param("fecha")LocalDate fecha);

    @Query("SELECT i FROM Inventario i WHERE i.active = :active AND DATE(i.created_at) BETWEEN :fechaInicio AND :fechaFin")
    List<Inventario>buscarPorFecha(@Param("active")String active, @Param("fechaInicio")LocalDate fechaInicio,@Param("fechaFin")LocalDate fechaFin);

    @Transactional
    @Modifying
    @Query("UPDATE Inventario i SET i.active = :inactive WHERE i.inventarioId = :idInventario")
    int eliminarInventario(@Param("inactive")String inactive,@Param("idInventario")Integer idInventario);




}//interface
