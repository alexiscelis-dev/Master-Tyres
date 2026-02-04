package com.mastertyres.marca.service;

import com.mastertyres.marca.model.Marca;
import java.util.List;
import java.util.Optional;

public interface IMarcaServices {
    List<Marca> listarMarcas();

    Optional<Marca> findByNombreMarca(String nombreMarca);


}
