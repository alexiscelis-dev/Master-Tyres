package com.mastertyres.modelo.repository;

import com.mastertyres.modelo.model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ModeloRepository extends JpaRepository<Modelo, Integer> {
    @Query("SELECT mo.nombreModelo FROM Modelo mo")
    List<String> listarNombresModelos();


}

