package com.mastertyres.cliente.service;

import com.mastertyres.cliente.domain.ClienteValidator;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.repository.ClienteRepository;
import com.mastertyres.common.exeptions.ClienteException;
import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClienteService implements IClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private VehiculoRepository vehiculoRepository;
    @Autowired
    private ClienteValidator clienteValidator;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository,
                          VehiculoRepository vehiculoRepository) {
        this.clienteRepository = clienteRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Cliente> obtenerClientesAplicables(Integer promocionId) {
        List<Cliente> clientes = clienteRepository.findClientesAplicables(promocionId);
        return clientes;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Cliente> listarClientesConVehiculosPaginado(String active, int pagina, int limite) {
        Pageable pageable = PageRequest.of(pagina, limite, Sort.by("clienteId").ascending());

        // Paso 1: obtener página de clientes activos
        Page<Cliente> paginaClientes = clienteRepository.listarClientesActivos(active, pageable);

        if (paginaClientes.isEmpty()) {
            return paginaClientes;
        }

        // Paso 2: inicializar listas para evitar LazyInitializationException
        for (Cliente cliente : paginaClientes) {
            cliente.setVehiculos(new ArrayList<>());
        }

        // Paso 3: obtener los IDs de los clientes en esta página
        List<Integer> clienteIds = paginaClientes.stream()
                .map(Cliente::getClienteId)
                .toList();

        // Paso 4: obtener vehículos asociados
        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        // Paso 5: asociar los vehículos a sus clientes
        Map<Integer, Cliente> mapaClientes = paginaClientes.stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaClientes.get(v.getCliente().getClienteId());
            if (cliente != null) {
                cliente.getVehiculos().add(v);
            }
        }

        return paginaClientes;
    }

    @Transactional(readOnly = true)
    @Override
    public long contarClientesActivos(String active) {
        return clienteRepository.contarClientesActivos(active);
    }


    @Transactional(readOnly = true)
    @Override
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

        vehiculoRepository.eliminarVehiculosPorCliente(eliminar, idCliente);

        int filasEliminadas = clienteRepository.eliminarCliente(eliminar, idCliente);

        if (filasEliminadas == 0)
            throw new ClienteException("Error interno. No se pudo eliminar el cliente seleccionado.");

        return filasEliminadas;

    }//eliminarCliente

    @Transactional(readOnly = true)
    @Override
    public List<Cliente> buscarClientes(String filtro) {
        return clienteRepository.buscarClientes(filtro);
    }

    @Transactional
    @Override
    public Cliente guardarCliente(Cliente cliente, boolean registrar) {
        clienteValidator.validarGuardar(cliente, registrar);
        return clienteRepository.save(cliente);

    }

    @Transactional(readOnly = true)
    @Override
    public boolean existeClientePorRFC(String rfc) {
        return clienteRepository.existeClientePorRFC(rfc);
    }

    @Transactional
    @Override
    public boolean existeClienteRFC_Editar(String rfc, Integer id) {
        return clienteRepository.existeClienteRFC_Editar(rfc, id);
    }

    @Transactional(readOnly = true)
    @Override
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
    @Override
    public Page<Cliente> buscarClientePorNombrePaginado(String active, String nombre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> clientes = clienteRepository.buscarClientePorNombrePaginado(active, nombre, pageable);

        if (clientes.isEmpty()) return clientes;

        // Inicializamos la lista de vehículos
        for (Cliente cliente : clientes.getContent()) {
            cliente.setVehiculos(new ArrayList<>());
        }

        List<Integer> clienteIds = clientes.getContent().stream()
                .map(Cliente::getClienteId)
                .toList();

        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        Map<Integer, Cliente> mapaCliente = clientes.getContent().stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaCliente.get(v.getCliente().getClienteId());
            cliente.getVehiculos().add(v);
        }

        return clientes;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Cliente> buscarClientePornombreEmpresaPaginado(String active, String nombreEmpresa, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> clientes = clienteRepository.buscarClientePorNombreEmpresaPaginado(active, nombreEmpresa, pageable);

        if (clientes.isEmpty()) return clientes;

        // Inicializamos la lista de vehículos
        for (Cliente cliente : clientes.getContent()) {
            cliente.setVehiculos(new ArrayList<>());
        }

        List<Integer> clienteIds = clientes.getContent().stream()
                .map(Cliente::getClienteId)
                .toList();

        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        Map<Integer, Cliente> mapaCliente = clientes.getContent().stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaCliente.get(v.getCliente().getClienteId());
            cliente.getVehiculos().add(v);
        }

        return clientes;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Cliente> buscarClientePorNumTelefono(String active, String numTelefono) {
        List<Cliente> clientes = clienteRepository.buscarClientePorNumTelefono(active, numTelefono);

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
    @Override
    public Page<Cliente> buscarClientePorNumTelefonoPaginado(String active, String numTelefono, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> clientes = clienteRepository.buscarClientePorNumTelefonoPaginado(active, numTelefono, pageable);

        if (clientes.isEmpty()) return clientes;

        // Inicializamos la lista de vehículos
        for (Cliente cliente : clientes.getContent()) {
            cliente.setVehiculos(new ArrayList<>());
        }

        List<Integer> clienteIds = clientes.getContent().stream()
                .map(Cliente::getClienteId)
                .toList();

        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        Map<Integer, Cliente> mapaCliente = clientes.getContent().stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaCliente.get(v.getCliente().getClienteId());
            cliente.getVehiculos().add(v);
        }

        return clientes;

    }//buscarClientePorNumTelefono

    @Transactional(readOnly = true)
    @Override
    public List<Cliente> buscarClientePorEstado(String active, String estado) {
        List<Cliente> clientes = clienteRepository.buscarClientePorEstado(active, estado);

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
    @Override
    public Page<Cliente> buscarClientePorEstadoPaginado(String active, String estado, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> clientes = clienteRepository.buscarClientePorEstadoPaginado(active, estado, pageable);

        if (clientes.isEmpty()) return clientes;

        // Inicializamos la lista de vehículos
        for (Cliente cliente : clientes.getContent()) {
            cliente.setVehiculos(new ArrayList<>());
        }

        List<Integer> clienteIds = clientes.getContent().stream()
                .map(Cliente::getClienteId)
                .toList();

        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        Map<Integer, Cliente> mapaCliente = clientes.getContent().stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaCliente.get(v.getCliente().getClienteId());
            cliente.getVehiculos().add(v);
        }

        return clientes;

    }//buscarClientePorNumTelefono

    @Transactional(readOnly = true)
    @Override
    public List<Cliente> buscarClientePorCiudad(String active, String ciudad) {
        List<Cliente> clientes = clienteRepository.buscarClientePorCiudad(active, ciudad);

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
    @Override
    public Page<Cliente> buscarClientePorCiudadPaginado(String active, String ciudad, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> clientes = clienteRepository.buscarClientePorCiudadPaginado(active, ciudad, pageable);

        if (clientes.isEmpty()) return clientes;

        // Inicializamos la lista de vehículos
        for (Cliente cliente : clientes.getContent()) {
            cliente.setVehiculos(new ArrayList<>());
        }

        List<Integer> clienteIds = clientes.getContent().stream()
                .map(Cliente::getClienteId)
                .toList();

        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        Map<Integer, Cliente> mapaCliente = clientes.getContent().stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaCliente.get(v.getCliente().getClienteId());
            cliente.getVehiculos().add(v);
        }

        return clientes;

    }//buscarClientePorNumTelefono

    @Transactional(readOnly = true)
    @Override
    public List<Cliente> buscarClientePorHobbie(String active, String hobbie) {
        List<Cliente> clientes = clienteRepository.buscarClientePorHobbie(active, hobbie);

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
    @Override
    public Page<Cliente> buscarClientePorHobbiePaginado(String active, String hobbie, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> clientes = clienteRepository.buscarClientePorHobbiePaginado(active, hobbie, pageable);

        if (clientes.isEmpty()) return clientes;

        // Inicializamos la lista de vehículos
        for (Cliente cliente : clientes.getContent()) {
            cliente.setVehiculos(new ArrayList<>());
        }

        List<Integer> clienteIds = clientes.getContent().stream()
                .map(Cliente::getClienteId)
                .toList();

        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        Map<Integer, Cliente> mapaCliente = clientes.getContent().stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaCliente.get(v.getCliente().getClienteId());
            cliente.getVehiculos().add(v);
        }

        return clientes;

    }//buscarClientePorNumTelefono

    @Transactional(readOnly = true)
    @Override
    public List<Cliente> buscarClientePorDomicilio(String active, String domicilio) {
        List<Cliente> clientes = clienteRepository.buscarClientePorDomicilio(active, domicilio);

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
    @Override
    public Page<Cliente> buscarClientePorDomicilioPaginado(String active, String domicilio, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> clientes = clienteRepository.buscarClientePorDomicilioPaginado(active, domicilio, pageable);

        if (clientes.isEmpty()) return clientes;

        // Inicializamos la lista de vehículos
        for (Cliente cliente : clientes.getContent()) {
            cliente.setVehiculos(new ArrayList<>());
        }

        List<Integer> clienteIds = clientes.getContent().stream()
                .map(Cliente::getClienteId)
                .toList();

        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        Map<Integer, Cliente> mapaCliente = clientes.getContent().stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaCliente.get(v.getCliente().getClienteId());
            cliente.getVehiculos().add(v);
        }

        return clientes;

    }//buscarClientePorNumTelefono

    @Transactional(readOnly = true)
    @Override
    public List<Cliente> buscarClientePorRfc(String active, String rfc) {
        List<Cliente> clientes = clienteRepository.buscarClientePorRfc(active, rfc);

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
    @Override
    public Page<Cliente> buscarClientePorRfcPaginado(String active, String rfc, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> clientes = clienteRepository.buscarClientePorRfcPaginado(active, rfc, pageable);

        if (clientes.isEmpty()) return clientes;

        // Inicializamos la lista de vehículos
        for (Cliente cliente : clientes.getContent()) {
            cliente.setVehiculos(new ArrayList<>());
        }

        List<Integer> clienteIds = clientes.getContent().stream()
                .map(Cliente::getClienteId)
                .toList();

        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        Map<Integer, Cliente> mapaCliente = clientes.getContent().stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaCliente.get(v.getCliente().getClienteId());
            cliente.getVehiculos().add(v);
        }

        return clientes;

    }//buscarClientePorNumTelefono

    @Transactional(readOnly = true)
    @Override
    public Page<Cliente> buscarClientePorCumpleanosPaginado(String active, String cumpleanos, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> clientes = clienteRepository.buscarClientePorCumpleanosPaginado(active, cumpleanos, pageable);

        if (clientes.isEmpty()) return clientes;

        // Inicializamos la lista de vehículos
        for (Cliente cliente : clientes.getContent()) {
            cliente.setVehiculos(new ArrayList<>());
        }

        List<Integer> clienteIds = clientes.getContent().stream()
                .map(Cliente::getClienteId)
                .toList();

        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        Map<Integer, Cliente> mapaCliente = clientes.getContent().stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaCliente.get(v.getCliente().getClienteId());
            cliente.getVehiculos().add(v);
        }

        return clientes;

    }//buscarClientePorCumpleanos

    @Transactional(readOnly = true)
    @Override
    public Page<Cliente> buscarClientePorCumpleanosRangoPaginado(String active, String cumpleanos, String cumpleanos2, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> clientes = clienteRepository.buscarClientePorCumpleanosRangoPaginado(active, cumpleanos, cumpleanos2, pageable);

        if (clientes.isEmpty()) return clientes;

        // Inicializamos la lista de vehículos
        for (Cliente cliente : clientes.getContent()) {
            cliente.setVehiculos(new ArrayList<>());
        }

        List<Integer> clienteIds = clientes.getContent().stream()
                .map(Cliente::getClienteId)
                .toList();

        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        Map<Integer, Cliente> mapaCliente = clientes.getContent().stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaCliente.get(v.getCliente().getClienteId());
            cliente.getVehiculos().add(v);
        }

        return clientes;

    }//buscarClientePorCumpleanos

    @Transactional(readOnly = true)
    @Override
    public Page<Cliente> buscarClientePorRegistroPaginado(String active, String registro, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> clientes = clienteRepository.buscarClientePorRegistroPaginado(active, registro, pageable);

        if (clientes.isEmpty()) return clientes;

        // Inicializamos la lista de vehículos
        for (Cliente cliente : clientes.getContent()) {
            cliente.setVehiculos(new ArrayList<>());
        }

        List<Integer> clienteIds = clientes.getContent().stream()
                .map(Cliente::getClienteId)
                .toList();

        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        Map<Integer, Cliente> mapaCliente = clientes.getContent().stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaCliente.get(v.getCliente().getClienteId());
            cliente.getVehiculos().add(v);
        }

        return clientes;

    }//buscarClientePorCumpleanos

    @Transactional(readOnly = true)
    @Override
    public Page<Cliente> buscarClientePorRegistroRangoPaginado(String active, LocalDate registro, LocalDate registro2, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> clientes = clienteRepository.buscarClientePorRegistroRangoPaginado(active, registro, registro2, pageable);

        if (clientes.isEmpty()) return clientes;

        // Inicializamos la lista de vehículos
        for (Cliente cliente : clientes.getContent()) {
            cliente.setVehiculos(new ArrayList<>());
        }

        List<Integer> clienteIds = clientes.getContent().stream()
                .map(Cliente::getClienteId)
                .toList();

        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        Map<Integer, Cliente> mapaCliente = clientes.getContent().stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaCliente.get(v.getCliente().getClienteId());
            cliente.getVehiculos().add(v);
        }

        return clientes;

    }//buscarClientePorCumpleanos

    @Transactional(readOnly = true)
    @Override
    public Page<Cliente> buscarClientePorCorreoPaginado(String active, String registro, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Cliente> clientes = clienteRepository.buscarClientePorCorreoPaginado(active, registro, pageable);

        if (clientes.isEmpty()) return clientes;

        // Inicializamos la lista de vehículos
        for (Cliente cliente : clientes.getContent()) {
            cliente.setVehiculos(new ArrayList<>());
        }

        List<Integer> clienteIds = clientes.getContent().stream()
                .map(Cliente::getClienteId)
                .toList();

        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        Map<Integer, Cliente> mapaCliente = clientes.getContent().stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaCliente.get(v.getCliente().getClienteId());
            cliente.getVehiculos().add(v);
        }

        return clientes;

    }//buscarClientePorCumpleanos

    @Transactional(readOnly = true)
    @Override
    public Page<Cliente> buscadorClientesPaginado(String status, String busqueda, int pagina, int limite) {
        Pageable pageable = PageRequest.of(pagina, limite, Sort.by("clienteId").ascending());

        // Paso 1: obtener clientes filtrados y paginados
        Page<Cliente> paginaClientes = clienteRepository.buscadorClientes(status, busqueda, pageable);

        if (paginaClientes.isEmpty()) {
            return paginaClientes;
        }

        // Paso 2: inicializar listas para evitar LazyInitializationException
        for (Cliente cliente : paginaClientes) {
            cliente.setVehiculos(new ArrayList<>());
        }

        // Paso 3: obtener IDs de los clientes
        List<Integer> clienteIds = paginaClientes.stream()
                .map(Cliente::getClienteId)
                .toList();

        // Paso 4: obtener vehículos de esos clientes
        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorClientes(clienteIds);

        // Paso 5: asignar vehículos
        Map<Integer, Cliente> mapaClientes = paginaClientes.stream()
                .collect(Collectors.toMap(Cliente::getClienteId, c -> c));

        for (Vehiculo v : vehiculos) {
            Cliente cliente = mapaClientes.get(v.getCliente().getClienteId());
            if (cliente != null) {
                cliente.getVehiculos().add(v);
            }
        }

        return paginaClientes;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Cliente> buscadorClientes(String status, String busqueda) {
        List<Cliente> clientes = clienteRepository.buscadorClientes(status, busqueda);

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

    @Transactional(readOnly = true)
    @Override
    public Cliente buscarClientePorId(Integer clienteId, String status) {
        return clienteRepository.buscarClientePorId(clienteId, status);
    }

    @Transactional(readOnly = true)
    @Override
    public long contarClientesPorBusquedaGeneral(String status, String termino) {
        return clienteRepository.contarClientesPorBusquedaGeneral(status, termino);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Cliente> firs100Buscador(String active) {
        return clienteRepository.first100Buscador(active);
    }


}//clase
