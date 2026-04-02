package com.mastertyres.clientesPromocion.service;

import com.mastertyres.clientesPromocion.entity.ClientesPromocion;
import com.mastertyres.clientesPromocion.repository.ClientePromocionRepository;
import com.mastertyres.cliente.entity.Cliente;
import com.mastertyres.promociones.entity.Promocion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientePromocionService implements IClientePromocionService {

    @Autowired
    private ClientePromocionRepository clientePromocionRepository;

    public ClientePromocionService(ClientePromocionRepository clientePromocionRepository) {
        this.clientePromocionRepository = clientePromocionRepository;
    }

    /**
     * Guarda múltiples clientes asociados a una promoción
     */
    @Transactional
    @Override
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

    @Transactional
    @Override
    public void eliminarClientesPorPromocion(Integer promocionId) {
        clientePromocionRepository.deleteByPromocionId(promocionId);
    }


    /**
     * Lista los nombres de los clientes relacionados a una promoción
     */
    @Transactional(readOnly = true)
    @Override
    public List<ClientesPromocion> listarClientesPorPromocion(Integer promocionId) {
        return clientePromocionRepository.findByPromocionIdConCliente(promocionId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> buscarImgPromocion(Integer clienteId, Integer promocionId) {
        return clientePromocionRepository.buscarImgPromocion(clienteId, promocionId);
    }

    @Transactional
    @Override
    public void eliminarPorPromocionId(Integer promocionId) {
        clientePromocionRepository.deleteByPromocionPromocionId(promocionId);
    }

    @Transactional
    @Override
    public void guardar(ClientesPromocion clientePromocion) {
        clientePromocionRepository.save(clientePromocion);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Cliente> obtenerClientesAplicables(Integer promocionId) {
        List<ClientesPromocion> relaciones = clientePromocionRepository.findByPromocionIdConCliente(promocionId);

        return relaciones.stream()
                .map(ClientesPromocion::getCliente)
                .filter(cliente -> cliente != null)
                .distinct()
                .toList();
    }

}//class