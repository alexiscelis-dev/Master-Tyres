package com.mastertyres.vehiculo.service;


import com.mastertyres.categoria.entity.Categoria;
import com.mastertyres.cliente.entity.Cliente;
import com.mastertyres.vehiculo.entity.Vehiculo;
import com.mastertyres.vehiculo.DTOs.VehiculoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface IVehiculoService {

    List<VehiculoDTO> listarVehiculos(String vehiculoStatus);

    int eliminarVehiculo(String eliminar, Integer idVehiculo);

    List<VehiculoDTO> buscarVehiculoPorPropietario(String active, String nombre);

    List<VehiculoDTO> buscarVehiculoPorMarca(String active, String marca);

    List<VehiculoDTO> buscarVehiculoPorModelo(String active, String modelo);

    List<VehiculoDTO> buscarVehiculoPorCategoria(String active, String categoria);

    List<VehiculoDTO> buscarVehiculoPorColor(String active, String color);

    List<VehiculoDTO> buscarVehiculoPorAnio(String active, Integer anio);

    List<VehiculoDTO> buscarVehiculoPorAnio(String active, Integer fechaInicio, Integer fechaFin);

    List<VehiculoDTO> buscarVehiculoPorPlacas(String active, String placas);

    List<VehiculoDTO> buscarVehiculoPorNumSerie(String active, String numSerie);

    List<VehiculoDTO> buscarVehiculoPorKilometros(String active, Integer kilometros);

    List<VehiculoDTO> buscarVehiculoPorKilometros(String active, Integer kilometroInicio, Integer kilometroFin);

    List<VehiculoDTO> buscarVehiculoPorUltimoServicio(String active, String ultimoServicio);

    List<VehiculoDTO> buscarVehiculoPorUltimoServicio(String active, String servicioInicio, String servicioFin);

    List<VehiculoDTO> buscarVehiculoPorRegistro(String activo, LocalDate ultimoServicio);

    List<VehiculoDTO> buscarVehiculoPorRegistro(String activo, LocalDate fechaInicio, LocalDate fechaFin);

    List<VehiculoDTO> buscadorVehiculo(String status, String busqueda);

    Vehiculo buscarVehiculoPorId(Integer vehiculoId, String status);

    Vehiculo VehiculoSinDTO(Integer id);

    List<Categoria> listarCategoriasPorMarcaYModelo(String active, Integer marcaId, Integer modeloId);


//Metodos editar

    List<VehiculoDTO> obtenerVehiculosConServicioVencido();

    Page<VehiculoDTO> obtenerVehiculosConServicioVencidoPaginado(Pageable pageable);

    long contarVehiculosActivos(String active);

    List<VehiculoDTO> BuscarVehiculosConServicioVencido(String busqueda);

    Page<VehiculoDTO> BuscarVehiculosConServicioVencidoPaginado(String busqueda, Pageable pageable);

    boolean actualizarUltimoServicio(Integer idVehiculo);

    void guardarVehiculos(Cliente cliente, List<Vehiculo> vehiculos);

    Vehiculo actualizarVehiculo(Integer id, Vehiculo datosActualizados);

    boolean actualizarContador(Integer idVehiculo, Integer contador);

    boolean existeVehiculoPorPlacas(String placas);

    boolean existeVehiculoPlacasEditar(String placas, Integer id);

    boolean existeVehiculoPorNumeroSerie(String numSerie);

    boolean existeVehiculoNumeroSerieEditar(String numSerie, Integer id);

    boolean existeVehiculoPorPlacasONumeroSerie(String placas, String numSerie);

    long contarVehiculosPorBusquedaGeneral(String status, String termino);

    Page<VehiculoDTO> listarVehiculosPaginado(String active, int pagina, int tamanoPagina);

    Page<VehiculoDTO> buscadorVehiculosPaginado(String active, String busqueda, int pagina, int tamanoPagina);

    Page<VehiculoDTO> buscarPorPropietario(String active, String nombre, int pagina, int tamanoPagina);

    Page<VehiculoDTO> buscarVehiculoPorNumSeriePaginado(String active, String numSerie, int pagina, int tamanoPagina);

    Page<VehiculoDTO> buscarPorMarca(String active, String marca, int pagina, int tamanoPagina);

    Page<VehiculoDTO> buscarPorModelo(String active, String modelo, int pagina, int tamanoPagina);

    Page<VehiculoDTO> buscarPorPlacas(String active, String placas, int pagina, int tamanoPagina);

    Page<VehiculoDTO> buscarPorColor(String active, String color, int pagina, int tamanoPagina);

    Page<VehiculoDTO> buscarPorCategoria(String active, String categoria, int pagina, int tamanoPagina);

    Page<VehiculoDTO> buscarPorAnio(String active, Integer anio, int pagina, int tamanoPagina);

    Page<VehiculoDTO> buscarPorAnioRango(String active, Integer inicio, Integer fin, int pagina, int tamanoPagina);

    Page<VehiculoDTO> buscarPorKilometros(String active, Integer kms, int pagina, int tamanoPagina);

    Page<VehiculoDTO> buscarPorKilometrosRango(String active, Integer inicio, Integer fin, int pagina, int tamanoPagina);

    Page<VehiculoDTO> buscarPorFechaRegistro(String active, LocalDate fecha, int pagina, int tamanoPagina);

    Page<VehiculoDTO> buscarVehiculoPorUltimoServicioPaginado(String active, LocalDate fecha, int pagina, int tamanoPagina);

    Page<VehiculoDTO> buscarPorFechaRegistroRango(String active, LocalDate inicio, LocalDate fin, int pagina, int tamanoPagina);

    Page<VehiculoDTO> buscarVehiculoPorUltimoServicioPaginadoRango(String active, LocalDate fechaInicio, LocalDate fechaFin , int pagina, int tamanoPagina);

    int reasignarMarcaPorId(Integer marcaId);

    int reasignarModeloYCategoriaPorMarca(Integer marcaId);

    int reasignarModeloPorId(Integer modeloId);

    int reasignarCategoriaPorId(Integer categoriaId);


}//interface
