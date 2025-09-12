package com.mastertyres.vehiculoPromocion.service;

import com.mastertyres.vehiculoPromocion.model.VehiculoPromocion;
import com.mastertyres.vehiculoPromocion.repository.VehiculoPromocionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehiculoPromocionService implements   IVehiculoPromocionService{

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
        repo.save(vehiculoPromocion);

    }

    @Transactional
    public void eliminarPorPromocionId(Integer promocionId) {
        repo.eliminarPorPromocionId(promocionId);
    }


}//clase


