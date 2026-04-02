package com.mastertyres.notaDetalle.repository;

import com.mastertyres.nota.entity.Nota;
import com.mastertyres.notaDetalle.entity.NotaDetalle;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaDetalleRepository extends JpaRepository<NotaDetalle, Integer> {

    final String SELECT_FROM_NOTA_DET = "SELECT nd FROM NotaDetalle nd";

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_FROM_NOTA_DET + " " + "WHERE nd.nota = :nota")
    NotaDetalle BuscarNotaDetalle(@Param("nota") Nota nota);


}//interface
