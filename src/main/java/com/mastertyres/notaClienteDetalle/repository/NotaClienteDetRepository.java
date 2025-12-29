package com.mastertyres.notaClienteDetalle.repository;

import com.mastertyres.nota.model.Nota;
import com.mastertyres.notaClienteDetalle.model.NotaClienteDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaClienteDetRepository extends JpaRepository<NotaClienteDetalle, Integer> {

    @Query("SELECT ncd FROM NotaClienteDetalle ncd WHERE ncd.nota = :nota")
    NotaClienteDetalle buscarclienteDetalle(@Param("nota") Nota nota);



}//class
