package com.mastertyres.notaDetalle.repository;

import com.mastertyres.notaDetalle.model.NotaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaDetalleRepository extends JpaRepository <NotaDetalle, Integer> {



}//interface
