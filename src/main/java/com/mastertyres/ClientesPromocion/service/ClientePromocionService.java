package com.mastertyres.ClientesPromocion.service;

import com.mastertyres.ClientesPromocion.model.ClientesPromocion;
import com.mastertyres.ClientesPromocion.repository.ClientePromocionRepository;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.promociones.model.Promocion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientePromocionService {

    @Autowired
    private ClientePromocionRepository clientePromocionRepository;

    public ClientePromocionService(ClientePromocionRepository clientePromocionRepository) {
        this.clientePromocionRepository = clientePromocionRepository;
    }

    /**
     * Guarda múltiples clientes asociados a una promoción
     */
    @Transactional
    public void guardarClientesPromocion(Integer promocionId, List<Integer> clientesIds) {

        Promocion promocion = Promocion.builder()
                .promocionId(promocionId)
                .build();

        List<ClientesPromocion> relaciones = clientesIds.stream()
                .map(clienteId -> {
                    Cliente cliente = Cliente.builder()
                            .clienteId(clienteId) // ajusta si el ID se llama distinto
                            .build();

                    return ClientesPromocion.builder()
                            .cliente(cliente)
                            .promocion(promocion)
                            .build();
                })
                .toList();

        clientePromocionRepository.saveAll(relaciones);
    }

    /**
     * Elimina todos los clientes asociados a una promoción
     */
    public void eliminarClientesPorPromocion(Integer promocionId) {
        clientePromocionRepository.deleteByPromocionId(promocionId);
    }

    /**
     * Lista los nombres de los clientes relacionados a una promoción
     */
    public List<ClientesPromocion> listarClientesPorPromocion(Integer promocionId) {
        return clientePromocionRepository.findByPromocionIdConCliente(promocionId);
    }


    public void eliminarPorPromocionId(Integer promocionId) {
        clientePromocionRepository.deleteByPromocionPromocionId(promocionId);
    }

    public void guardar(ClientesPromocion clientePromocion) {
        clientePromocionRepository.save(clientePromocion);
    }

    public List<Cliente> obtenerClientesAplicables(Integer promocionId) {
        List<ClientesPromocion> relaciones = clientePromocionRepository.findByPromocionIdConCliente(promocionId);

        return relaciones.stream()
                .map(ClientesPromocion::getCliente)
                .filter(cliente -> cliente != null)
                .distinct()
                .toList();
    }

}
