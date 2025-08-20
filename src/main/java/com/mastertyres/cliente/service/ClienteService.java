package com.mastertyres.cliente.service;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.repository.ClienteRepository;
import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mastertyres.common.MensajesAlert.mostrarError;
import static com.mastertyres.common.MensajesAlert.mostrarInformacion;

@Service
public class ClienteService implements IClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private VehiculoRepository vehiculoRepository;


    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarClientesConVehiculos(String active) {
        // Paso 1: obtener clientes activos
        List<Cliente> clientes = clienteRepository.listarClientesActivos(active);
        if (clientes.isEmpty()) return clientes;

        // Inicializamos las listas para evitar LazyInitializationException
        for (Cliente cliente : clientes) {
            cliente.setVehiculos(new ArrayList<>());
        }

        // Paso 2: obtener vehículos asociados a esos clientes
        List<Integer> clienteIds = clientes.stream()
                .map(Cliente::getClienteId)
                .toList();

        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        // Paso 3: asignar vehículos a cada cliente
        Map<Integer, Cliente> mapaClientes = clientes.stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaClientes.get(v.getCliente().getClienteId());
            cliente.getVehiculos().add(v);
        }

        return clientes;
    }


    @Transactional
    @Override
    public int eliminarCliente(String eliminar, Integer idCliente) {

        int filasEliminadas = clienteRepository.eliminarCliente(eliminar, idCliente);

        if (filasEliminadas > 0) {

            mostrarInformacion("Cliente eliminado","Cliente eliminado","Cliente eliminado exitosamente.");

        } else {

            mostrarError("Error al eliminar cliente","Algo salio mal","No se pudo eliminar el cliente seleccionado.");

        }
        return filasEliminadas;

    }//eliminarCliente


    public Cliente guardarCliente(Cliente cliente) {

        return clienteRepository.save(cliente);

    }

    @Transactional(readOnly = true)
    public List<Cliente> buscarClientePorNombre(String active, String nombre) {
        List<Cliente> clientes = clienteRepository.buscarClientePorNombre(active, nombre);

        if (clientes.isEmpty()) return clientes;

        for (Cliente cliente : clientes) {
            cliente.setVehiculos(new ArrayList<>());
        }

        List<Integer> clienteIds = clientes.stream()
                .map(Cliente::getClienteId)
                .toList();

        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        Map<Integer, Cliente> mapaCliente = clientes.stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaCliente.get(v.getCliente().getClienteId());
            cliente.getVehiculos().add(v);
        }

        return clientes;

    }//buscarClientePorNombre


    @Transactional(readOnly = true)
    public List<Cliente> buscarClientePorNumTelefono(String active, Integer numTelefono) {
        List<Cliente> clientes = clienteRepository.buscarClientePorNumTelefono(active,numTelefono);

        if (clientes.isEmpty()) return clientes;

        for (Cliente cliente : clientes) {
            cliente.setVehiculos(new ArrayList<>());
        }

        List<Integer> clienteIds = clientes.stream()
                .map(Cliente::getClienteId)
                .toList();

        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        Map<Integer, Cliente> mapaCliente = clientes.stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaCliente.get(v.getCliente().getClienteId());
            cliente.getVehiculos().add(v);
        }

        return clientes;

    }//buscarClientePorNumTelefono



}//clase