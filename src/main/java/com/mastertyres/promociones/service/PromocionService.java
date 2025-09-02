package com.mastertyres.promociones.service;
import com.mastertyres.promociones.model.Promocion;
import com.mastertyres.promociones.repository.PromocionesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PromocionService {

    private final PromocionesRepository promocionRepository;

    public PromocionService(PromocionesRepository promocionRepository) {
        this.promocionRepository = promocionRepository;
    }

    private String hoy= LocalDate.now().toString();

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

}
