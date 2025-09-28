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

    public List<Cliente> obtenerClientesAplicables(Integer promocionId) {
        List<Cliente> clientes = clienteRepository.findClientesAplicables(promocionId);
        return clientes;
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

    @Transactional(readOnly = true)
    public List<Cliente> buscarClientes( String filtro) {
        return clienteRepository.buscarClientes(filtro);
    }

    public Cliente guardarCliente(Cliente cliente) {

        return clienteRepository.save(cliente);

    }

    public boolean existeClientePorRFC(String rfc) {
        return clienteRepository.existeClientePorRFC(rfc);
    }

    public boolean existeClienteRFC_Editar(String rfc, Integer id) {
        return clienteRepository.existeClienteRFC_Editar(rfc,id);
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
    public List<Cliente> buscarClientePorNumTelefono(String active, String numTelefono) {
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

    @Transactional(readOnly = true)
    public List<Cliente> buscarClientePorEstado(String active, String estado) {
        List<Cliente> clientes = clienteRepository.buscarClientePorEstado(active,estado);

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

    }//buscarClientePorEstado

    @Transactional(readOnly = true)
    public List<Cliente> buscarClientePorCiudad(String active, String ciudad) {
        List<Cliente> clientes = clienteRepository.buscarClientePorCiudad(active,ciudad);

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

    }//buscarClientePorCiudad

    @Transactional(readOnly = true)
    public List<Cliente> buscarClientePorHobbie(String active, String hobbie) {
        List<Cliente> clientes = clienteRepository.buscarClientePorHobbie(active,hobbie);

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

    }//buscarClientePorHobbie

    @Transactional(readOnly = true)
    public List<Cliente> buscarClientePorDomicilio(String active, String domicilio) {
        List<Cliente> clientes = clienteRepository.buscarClientePorDomicilio(active,domicilio);

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

    }//buscarClientePorDomicilio

    @Transactional(readOnly = true)
    public List<Cliente> buscarClientePorRfc(String active, String rfc) {
        List<Cliente> clientes = clienteRepository.buscarClientePorRfc(active,rfc);

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

    }//buscarClientePorRfc

    @Transactional(readOnly = true)
    public List<Cliente> buscadorClientes(String status, String busqueda) {
        List<Cliente> clientes = clienteRepository.buscadorClientes(status,busqueda);

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

    }//buscadorClientes

}//clase
