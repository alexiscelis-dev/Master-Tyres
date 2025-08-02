package com.mastertyres.cliente.service;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.repository.ClienteRepository;
import com.mastertyres.vehiculo.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService implements IClienteService{

    private ClienteRepository clienteRepository;

    public void eliminar(Integer id) {
        clienteRepository.deleteById(id);
    }


    @Autowired
    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional (readOnly = true)
    public List<Cliente> listarCliente() {
        return clienteRepository.listarCliente();
    }
}
