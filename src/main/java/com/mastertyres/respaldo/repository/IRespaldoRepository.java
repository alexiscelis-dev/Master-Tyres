package com.mastertyres.respaldo.repository;

import com.mastertyres.respaldo.model.Respaldo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRespaldoRepository extends JpaRepository<Respaldo, Integer> {
}//interface
