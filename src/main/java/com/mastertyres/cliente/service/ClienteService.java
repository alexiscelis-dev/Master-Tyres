package com.mastertyres.cliente.service;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.repository.ClienteRepository;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService implements IClienteService {

    private ClienteRepository clienteRepository;

    public void eliminar(Integer id) {
        clienteRepository.deleteById(id);
    }


    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarCliente(String active) {
        return clienteRepository.listarCliente(active);
    }

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


}//clase
