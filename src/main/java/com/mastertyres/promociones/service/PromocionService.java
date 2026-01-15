package com.mastertyres.promociones.service;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.promociones.model.Promocion;
import com.mastertyres.promociones.repository.PromocionesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PromocionService implements IPromocionService {
    private String hoy = LocalDate.now().toString();
    private final PromocionesRepository promocionRepository;


    public PromocionService(PromocionesRepository promocionRepository) {
        this.promocionRepository = promocionRepository;
    }

    public List<Promocion> obtenerPromocionesActivas() {
        return promocionRepository.findPromocionesActivas(hoy);
    }

    public List<Promocion> buscarPromociones(String texto) {
        return promocionRepository.buscarPromocionesActivas(hoy, texto);
    }

    @Transactional
    public void desactivarPromocion(Integer id) {
        promocionRepository.desactivarPromocion(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Marca> listarMarcas() {
        return promocionRepository.listarMarcas();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Modelo> listarModelos() {
        return promocionRepository.listarModelos();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Categoria> listarCategorias() {
        return promocionRepository.listarCategorias();
    }


    @Transactional
    @Override
    public void guardarPromocion(Promocion promocion) {
        promocionRepository.save(promocion);
    }


    @Transactional(readOnly = true)
    @Override
    public Promocion buscarPromocionId(Integer id) {
        Promocion promocion = promocionRepository.findById(id).orElse(null);
        return promocion;
    }

}//PromocionService
