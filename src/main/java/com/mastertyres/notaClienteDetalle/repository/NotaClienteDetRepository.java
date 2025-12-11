package com.mastertyres.notaClienteDetalle.repository;

import com.mastertyres.notaClienteDetalle.model.NotaClienteDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaClienteDetRepository extends JpaRepository<NotaClienteDetalle, Integer> {

}//class
