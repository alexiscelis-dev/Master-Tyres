package com.mastertyres.cliente.service;

import com.mastertyres.cliente.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface IClienteService {

    List<Cliente> obtenerClientesAplicables(Integer promocionId);

    Page<Cliente> listarClientesConVehiculosPaginado(String active, int pagina, int limite);

    long contarClientesActivos(String active);

    List<Cliente> buscarClientes(String filtro);

    Cliente guardarCliente(Cliente cliente, boolean registrar);


    boolean existeClientePorRFC(String rfc);

    boolean existeClienteRFC_Editar(String rfc, Integer id);

    List<Cliente> buscarClientePorNombre(String active, String nombre);

    Page<Cliente> buscarClientePorNombrePaginado(String active, String nombre, int page, int size);

    Page<Cliente> buscarClientePornombreEmpresaPaginado(String active, String nombreEmpresa, int page, int size);

    List<Cliente> buscarClientePorNumTelefono(String active, String numTelefono);

    Page<Cliente> buscarClientePorNumTelefonoPaginado(String active, String numTelefono, int page, int size);

    List<Cliente> buscarClientePorEstado(String active, String estado);

    Page<Cliente> buscarClientePorEstadoPaginado(String active, String estado, int page, int size);

    List<Cliente> buscarClientePorCiudad(String active, String ciudad);

    Page<Cliente> buscarClientePorCiudadPaginado(String active, String ciudad, int page, int size);

    List<Cliente> buscarClientePorHobbie(String active, String hobbie);

    Page<Cliente> buscarClientePorHobbiePaginado(String active, String hobbie, int page, int size);

    List<Cliente> buscarClientePorDomicilio(String active, String domicilio);

    Page<Cliente> buscarClientePorDomicilioPaginado(String active, String domicilio, int page, int size);

    List<Cliente> buscarClientePorRfc(String active, String rfc);

    Page<Cliente> buscarClientePorRfcPaginado(String active, String rfc, int page, int size);

    Page<Cliente> buscarClientePorCumpleanosPaginado(String active, String cumpleanos, int page, int size);

    Page<Cliente> buscarClientePorCumpleanosRangoPaginado(String active, String cumpleanos, String cumpleanos2, int page, int size);

    Page<Cliente> buscarClientePorRegistroPaginado(String active, String registro, int page, int size);

    Page<Cliente> buscarClientePorRegistroRangoPaginado(String active, LocalDate registro, LocalDate registro2, int page, int size);

    Page<Cliente> buscarClientePorCorreoPaginado(String active, String registro, int page, int size);

    Page<Cliente> buscadorClientesPaginado(String status, String busqueda, int pagina, int limite);

    List<Cliente> buscadorClientes(String status, String busqueda);


    //List<Cliente> listarCliente(String active);

    // Método para listar clientes activos con vehículos cargados (marca, modelo, año)
    List<Cliente> listarClientesConVehiculos(String active);

    int eliminarCliente(String eliminar,Integer idCliente);

    Cliente buscarClientePorId(Integer clienteId, String status);

    List<Cliente> firs100Buscador(String active);

    long contarClientesPorBusquedaGeneral(String status, String termino);



}