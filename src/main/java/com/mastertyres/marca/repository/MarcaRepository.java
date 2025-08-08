package com.mastertyres.marca.repository;


import com.mastertyres.marca.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    @Query("SELECT m.nombreMarca FROM Marca m")
    List<String> listarNombresMarcas();


}

