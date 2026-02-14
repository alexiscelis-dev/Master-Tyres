package com.mastertyres.notaClienteDetalle.repository;

import com.mastertyres.nota.model.Nota;
import com.mastertyres.notaClienteDetalle.model.NotaClienteDetalle;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaClienteDetRepository extends JpaRepository<NotaClienteDetalle, Integer> {

    final String SELECT_FROM_NOTA_CD = "SELECT ncd FROM NotaClienteDetalle ncd";

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_FROM_NOTA_CD + " " + "WHERE ncd.nota = :nota")
    NotaClienteDetalle buscarclienteDetalle(@Param("nota") Nota nota);



}//class
