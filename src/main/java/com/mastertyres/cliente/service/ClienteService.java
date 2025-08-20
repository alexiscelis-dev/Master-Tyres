package com.mastertyres.cliente.service;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.repository.ClienteRepository;
import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.repository.VehiculoRepository;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /*public void eliminar(Integer id) {
        clienteRepository.deleteById(id);
    }*/




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


    /*@Override
    @Transactional(readOnly = true)
    public List<Cliente> listarCliente(String active) {
        return clienteRepository.listarCliente(active);
    }*/

    @Transactional
    @Override
    public int eliminarCliente(String eliminar, Integer idCliente) {

        int filasEliminadas = clienteRepository.eliminarCliente(eliminar,idCliente);

        if (filasEliminadas > 0){

            Alert ventana = new  Alert(Alert.AlertType.INFORMATION);
            ventana.setTitle("Cliente eliminado");
            ventana.setHeaderText("Cliente eliminado");
            ventana.setContentText("Cliente eliminado exitosamente.");

            ventana.showAndWait();

        }else {

            Alert ventana = new Alert(Alert.AlertType.ERROR);
            ventana.setTitle("Error al eliminar cliente");
            ventana.setHeaderText("Algo salio mal");
            ventana.setContentText("No se pudo eliminar el cliente seleccionado.");

        }
        return filasEliminadas;

    }//eliminarCliente


    public List<Cliente> buscarClientes(String filtro) {
        return clienteRepository.buscarClientes(filtro);
    }

    public Cliente guardarCliente(Cliente cliente) {

        return clienteRepository.save(cliente);

    }

    public boolean existeClientePorRFC(String rfc) {
        return clienteRepository.existeClientePorRFC(rfc);
    }




}//clase
