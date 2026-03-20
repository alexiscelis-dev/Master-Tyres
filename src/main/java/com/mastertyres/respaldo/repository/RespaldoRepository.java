package com.mastertyres.respaldo.repository;

import com.mastertyres.respaldo.model.Respaldo;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespaldoRepository extends JpaRepository<Respaldo, Integer> {

    final String UPDATE_RESPALDO = "UPDATE Respaldo r SET";
    final String SELECT_RESPALDO = "SELECT r FROM Respaldo r";

    @Modifying
    @Query(UPDATE_RESPALDO + " " + "r.estado = :estado WHERE r.respaldoId = :id")
    void actualizarEstado(@Param("id") Integer id, @Param("estado")String estado);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_RESPALDO + " WHERE r.estado = 'COMPLETO' ORDER BY r.createdAt DESC LIMIT 1 ")
    Respaldo obtenerUltimoRespaldo();

    @Modifying
    @Query(UPDATE_RESPALDO + " " + "r.tipoRespaldo = :tipoRespaldo WHERE r.respaldoId = :respaldoId")
    void actualizarTipoRespaldo(@Param("respaldoId") Integer respaldoId, @Param("tipoRespaldo") String tipoRespaldo);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_RESPALDO + " WHERE r.createdAt BETWEEN :fechaInicio AND :fechaFin ORDER BY r.createdAt DESC")
    List<Respaldo> listarRespaldos(@Param("fechaInicio") String fechaInicio, @Param("fechaFin")String fechaFin);

}//interface
