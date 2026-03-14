package com.mastertyres.promociones.service;

import com.mastertyres.ClientesPromocion.model.ClientesPromocion;
import com.mastertyres.ClientesPromocion.repository.ClientePromocionRepository;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.common.exeptions.PromocionException;
import com.mastertyres.promociones.domain.PromocionValidator;
import com.mastertyres.promociones.model.Promocion;
import com.mastertyres.promociones.model.StatusPromocion;
import com.mastertyres.promociones.repository.PromocionesRepository;
import com.mastertyres.vehiculoPromocion.domain.VehiculoPromocionValidator;
import com.mastertyres.vehiculoPromocion.model.VehiculoPromocion;
import com.mastertyres.vehiculoPromocion.repository.VehiculoPromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PromocionService implements IPromocionService {
    private String hoy = LocalDate.now().toString();

    private final PromocionesRepository promocionRepository;

    @Autowired
    private PromocionValidator promocionValidator;

    @Autowired
    private VehiculoPromocionValidator vehiculoPromocionValidator;

    @Autowired
    private ClientePromocionRepository clientePromocionRepository;


    private final VehiculoPromocionRepository repoVehiculo;


    public PromocionService(PromocionesRepository promocionRepository, VehiculoPromocionRepository vehiculoPromocionRepository, ClientePromocionRepository clientePromocionRepository) {
        this.promocionRepository = promocionRepository;
        this.repoVehiculo = vehiculoPromocionRepository;
        this.clientePromocionRepository = clientePromocionRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Promocion> obtenerPromocionesActivas() {
        return promocionRepository.findPromocionesActivas(hoy);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Promocion> buscarPromociones(String texto) {
        return promocionRepository.buscarPromocionesActivas(hoy, texto);
    }

    @Transactional
    @Override
    public void desactivarPromocion(Integer id) {
        int filasAfectadas = promocionRepository.desactivarPromocion(id);
        if (filasAfectadas == 0) {
            throw new RuntimeException("La promocion no existe o ya fue eliminada previamente");
        }
    }

/*
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


 */

    @Transactional
    @Override
    public void guardarPromocion(Promocion promocion) {
        promocionValidator.validarGuardar(promocion);

        promocionRepository.save(promocion);

    }

    @Transactional(readOnly = true)
    @Override
    public Promocion buscarPromocionId(Integer id) {
        Promocion promocion = promocionRepository.findById(id).orElse(null);
        return promocion;
    }

    @Transactional
    public void crearPromocionConVehiculos(Promocion promocion, List<VehiculoPromocion> vehiculos) {

        promocionValidator.validarGuardar(promocion);

        promocionRepository.save(promocion);

        if (promocion.getPromocionId() == null) {
            throw new PromocionException("No se pudo generar el ID de la promoción");
        }

        for (VehiculoPromocion vp : vehiculos) {
            vp.setPromocion(promocion);
            vehiculoPromocionValidator.validarGuardar(vp);
            repoVehiculo.save(vp);
        }
    }

    @Transactional
    public void crearPromocionConClientes(Promocion promocion, List<Integer> clientesIds) {
        promocionValidator.validarGuardar(promocion);

        promocionRepository.save(promocion);

        if (clientesIds.isEmpty()) {
            throw new PromocionException("Debe agregar al menos un cliente");
        }

        List<ClientesPromocion> relaciones = clientesIds.stream()
                .map(clienteId -> ClientesPromocion.builder()
                        .cliente(Cliente.builder().clienteId(clienteId).build())
                        .promocion(promocion)
                        .build()
                ).toList();

        clientePromocionRepository.saveAll(relaciones);
    }

    @Transactional
    public void actualizarPromocionConVehiculos(Promocion promocion, List<VehiculoPromocion> nuevosVehiculos) {

        promocionValidator.validarGuardar(promocion);
        promocionRepository.save(promocion);

        repoVehiculo.eliminarPorPromocionId(promocion.getPromocionId());

        for (VehiculoPromocion vp : nuevosVehiculos) {
            vp.setVehiculoPromocionID(null);
            vp.setPromocion(promocion);
            repoVehiculo.save(vp);
        }
    }

    @Transactional
    public void actualizarPromocionConClientes(Promocion promocion, List<Cliente> nuevosClientes) {

        promocionValidator.validarGuardar(promocion);
        // 1️ Guardar / actualizar promoción
        promocionRepository.save(promocion);

        // 2️ Eliminar relaciones actuales
        clientePromocionRepository.deleteByPromocionPromocionId(promocion.getPromocionId());

        // 3️ Insertar nuevas relaciones
        for (Cliente cliente : nuevosClientes) {

            ClientesPromocion relacion = new ClientesPromocion();

            relacion.setCliente(cliente);
            relacion.setPromocion(promocion);

            clientePromocionRepository.save(relacion);
        }
    }//actualizarPromocionConClientes


    //Cada minuto
    //@Scheduled(cron = "0 * * * * ?", zone = "America/Mexico_City")

    //Cada hora
    //@Scheduled(cron = "0 0 * * * ?", zone = "America/Mexico_City")

    @Scheduled(cron = "0 0 0 * * ?", zone = "America/Mexico_City")//Cada dia
    @Transactional
    public void actualizarPromocionesVencidas() {

        String hoy = LocalDate.now().toString();

        int filas = promocionRepository.marcarPromocionesVencidas(
                hoy,
                StatusPromocion.DELETE.toString(),
                StatusPromocion.ACTIVE.toString()
        );

    }


}//class
