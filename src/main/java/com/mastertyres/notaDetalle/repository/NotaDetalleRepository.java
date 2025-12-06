package com.mastertyres.notaDetalle.repository;

import com.mastertyres.nota.model.Nota;
import com.mastertyres.notaDetalle.model.NotaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaDetalleRepository extends JpaRepository<NotaDetalle, Integer> {

    @Query("SELECT nd FROM NotaDetalle nd WHERE nd.nota = :nota")
    NotaDetalle BuscarNotaDetalle(@Param("nota") Nota nota);


}//interface
