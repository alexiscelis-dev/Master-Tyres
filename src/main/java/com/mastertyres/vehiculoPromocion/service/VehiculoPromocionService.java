package com.mastertyres.vehiculoPromocion.service;

import com.mastertyres.promociones.domain.PromocionValidator;
import com.mastertyres.vehiculoPromocion.domain.VehiculoPromocionValidator;
import com.mastertyres.vehiculoPromocion.model.VehiculoPromocion;
import com.mastertyres.vehiculoPromocion.repository.VehiculoPromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehiculoPromocionService implements  IVehiculoPromocionService{
    @Autowired
    private VehiculoPromocionValidator vehiculoPromocionValidator;
    @Autowired
    private PromocionValidator promocionValidator;

    private final VehiculoPromocionRepository repo;

    public VehiculoPromocionService(VehiculoPromocionRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<VehiculoPromocion> obtenerVehiculosPorPromocion(Integer promocionId) {
        return repo.findAllByPromocionIdWithMarcaModelo(promocionId);
        // Si prefieres la simple: return repo.findByPromocion_PromocionId(promocionId);
    }

    @Transactional
    @Override
    public void guardarVehiculosAplicables(VehiculoPromocion vehiculoPromocion) {
        vehiculoPromocionValidator.validarGuardar(vehiculoPromocion);
        repo.save(vehiculoPromocion);

    }

    @Transactional
    public void eliminarPorPromocionId(Integer promocionId) {
        repo.eliminarPorPromocionId(promocionId);
    }

    // Reasignar marca
    @Transactional
    public int reasignarMarcaPorId(Integer marcaId) {
        return repo.reasignarMarcaPorId(marcaId);
    }

    // Reasignar modelo y categoría por marca
    @Transactional
    public int reasignarModeloYCategoriaPorMarca(Integer marcaId) {
        return repo.reasignarModeloYCategoriaPorMarca(marcaId);
    }

    // Reasignar modelo específico
    @Transactional
    public int reasignarModeloPorId(Integer modeloId) {
        return repo.reasignarModeloPorId(modeloId);
    }

}//clase


