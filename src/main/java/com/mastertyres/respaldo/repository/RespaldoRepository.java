package com.mastertyres.respaldo.repository;

import com.mastertyres.respaldo.model.Respaldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RespaldoRepository extends JpaRepository<Respaldo, Integer> {

    final String UPDATE_ESTADO = "UPDATE Respaldo r SET";

    @Modifying
    @Query(UPDATE_ESTADO + " " + "r.estado = :estado WHERE r.respaldoId = :id")
    void actualizarEstado(@Param("id") Integer id, @Param("estado")String estado);
}//interface
