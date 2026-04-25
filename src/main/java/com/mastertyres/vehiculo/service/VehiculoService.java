package com.mastertyres.vehiculo.service;

import com.mastertyres.categoria.entity.Categoria;
import com.mastertyres.cliente.domain.ClienteValidator;
import com.mastertyres.cliente.entity.Cliente;
import com.mastertyres.common.exeptions.VehiculoException;
import com.mastertyres.vehiculo.domain.VehiculoValidator;
import com.mastertyres.vehiculo.entity.StatusVehiculo;
import com.mastertyres.vehiculo.entity.Vehiculo;
import com.mastertyres.vehiculo.DTOs.VehiculoDTO;
import com.mastertyres.vehiculo.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VehiculoService implements IVehiculoService {

   @Autowired
    private VehiculoRepository vehiculoRepository;

   @Autowired
   private VehiculoValidator vehiculoValidator;

    @Autowired
    private ClienteValidator clienteValidator;

    @Autowired
    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> obtenerVehiculosConServicioVencido() {
        LocalDate fechaLimite = LocalDate.now().minusMonths(6);
        return vehiculoRepository.listarVehiculosConServicioVencido(fechaLimite);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> obtenerVehiculosConServicioVencidoPaginado(Pageable pageable) {
        LocalDate fechaLimite = LocalDate.now().minusMonths(6);
        return vehiculoRepository.listarVehiculosConServicioVencidoPaginado(fechaLimite, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public long contarVehiculosActivos(String active) {
        return vehiculoRepository.contarVehiculosActivos(active);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> BuscarVehiculosConServicioVencido(String busqueda) {
        LocalDate fechaLimite = LocalDate.now().minusMonths(6);
        return vehiculoRepository.BuscarVehiculosConServicioVencido(fechaLimite, busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> BuscarVehiculosConServicioVencidoPaginado(String busqueda, Pageable pageable) {
        LocalDate fechaLimite = LocalDate.now().minusMonths(6);
        return vehiculoRepository.BuscarVehiculosConServicioVencidoPaginado(fechaLimite, busqueda, pageable);
    }

    @Transactional
    @Override
    public boolean actualizarUltimoServicio(Integer idVehiculo) {
        return vehiculoRepository.findById(idVehiculo)
                .map(vehiculo -> {
                    vehiculo.setUltimoServicio(LocalDate.now()+""); // actualiza a la fecha actual
                    vehiculoRepository.save(vehiculo); // persiste el cambio
                    return true;
                })
                .orElseThrow(() -> new VehiculoException("No se encontro el vehiculo con el identificador " + idVehiculo )); // si no se encuentra el vehículo
    }

    @Transactional
    @Override
    public void guardarVehiculos(Cliente cliente, List<Vehiculo> vehiculos) {

        if (cliente == null || vehiculos == null || vehiculos.isEmpty()) {
            throw new VehiculoException("Cliente o vehículos inválidos");
        }

        vehiculoValidator.validarGuardar(vehiculos);


        for (Vehiculo v : vehiculos) {
            v.setCliente(cliente);
            v.setActive(StatusVehiculo.ACTIVE.toString());
            if (v.getCreated_at() == null) {
                v.setCreated_at(LocalDate.now().toString());
            }
            if (v.getUpdated_at() == null){
                v.setUpdated_at(LocalDate.now().toString());
            }
            if (v.getFechaRegistro() == null){
                v.setFechaRegistro(LocalDate.now().toString());
            }
            v.setContador_mensaje(0);
            vehiculoRepository.save(v);
        }
    }//guardarVehiculos

    @Transactional
    @Override
    public Vehiculo actualizarVehiculo(Integer id, Vehiculo datosActualizados) {
        Vehiculo existente = vehiculoRepository.findById(id)
                .orElseThrow(() -> new VehiculoException("Error interno: No se encontro el vehiculo con el identificador " + id));

        vehiculoValidator.validarActualizar(datosActualizados);

        //  Actualizar campos
        existente.setMarca(datosActualizados.getMarca());
        existente.setModelo(datosActualizados.getModelo());
        existente.setCategoria(datosActualizados.getCategoria());
        existente.setAnio(datosActualizados.getAnio());
        existente.setKilometros(datosActualizados.getKilometros());
        existente.setColor(datosActualizados.getColor());
        existente.setPlacas(datosActualizados.getPlacas());
        existente.setNumSerie(datosActualizados.getNumSerie());
        existente.setObservaciones(datosActualizados.getObservaciones());
        existente.setUltimoServicio(datosActualizados.getUltimoServicio());
        existente.setUpdated_at(LocalDateTime.now().toString());

        return vehiculoRepository.save(existente); // save() hace update si ya existe
    }

    @Transactional
    @Override
    public boolean actualizarContador(Integer idVehiculo, Integer contador){
        return vehiculoRepository.findById(idVehiculo)
                .map(vehiculo -> {
                    vehiculo.setContador_mensaje(contador);
                    vehiculoRepository.save(vehiculo);
                    return true;
                })
                .orElseThrow(() -> new VehiculoException("No se encontro el vehiculo con el identificador " + idVehiculo));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existeVehiculoPorPlacas(String placas) {
        return vehiculoRepository.existeVehiculoPorPlacas(placas);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existeVehiculoPlacasEditar(String placas, Integer id) {
        return vehiculoRepository.existeVehiculoPlacas_Editar(placas,id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existeVehiculoPorNumeroSerie(String numSerie) {
        return vehiculoRepository.existeVehiculoPorNumeroSerie(numSerie);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existeVehiculoNumeroSerieEditar(String numSerie, Integer id) {
        return vehiculoRepository.existeVehiculoNumeroSerie_Editar(numSerie, id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existeVehiculoPorPlacasONumeroSerie(String placas, String numSerie) {
        return vehiculoRepository.existeVehiculoPorPlacasONumeroSerie(placas, numSerie);
    }


    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> listarVehiculos(String vehiculoStatus) {
        return vehiculoRepository.listarVehiculos(vehiculoStatus);
    }


    @Transactional(readOnly = true)
    @Override
    public Vehiculo VehiculoSinDTO(Integer id) {
        return vehiculoRepository.VehiculoSinDTO(id);
    }

    @Transactional
    @Override
    public int eliminarVehiculo(String eliminar, Integer idVehiculo) {

        int filasEliminadas = vehiculoRepository.eliminarVehiculo(eliminar, idVehiculo);

        if (filasEliminadas == 0)
         throw new VehiculoException("Error interno. No se pudo eliminar el vehiculo seleccionado.");

        return filasEliminadas;

    }//eliminar vehiculo

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> buscarVehiculoPorPropietario(String active, String nombre) {
        return vehiculoRepository.buscarVehiculoPorPropietario(active,nombre);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> buscarVehiculoPorMarca(String activo, String marca) {
        return vehiculoRepository.buscarVehiculoPorMarca(activo, marca);
    }//buscarVehiculoPorMarca

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> buscarVehiculoPorModelo(String active, String modelo) {
        return vehiculoRepository.buscarVehiculoPorModelo(active,modelo);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> buscarVehiculoPorCategoria(String active, String categoria) {
        return vehiculoRepository.buscarVehiculoPorCategoria(active,categoria);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> buscarVehiculoPorColor(String active, String color) {
        return vehiculoRepository.buscarVehiculoPorColor(active,color);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> buscarVehiculoPorAnio(String active, Integer anio) {
        return vehiculoRepository.buscarVehiculoPorAnio(active,anio);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> buscarVehiculoPorAnio(String active, Integer fechaInicio, Integer FechaFin) {
        return vehiculoRepository.buscarVehiculoPorAnio(active,fechaInicio,FechaFin);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> buscarVehiculoPorPlacas(String active, String placas) {
        return vehiculoRepository.buscarVehiculoPorPlacas(active,placas);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> buscarVehiculoPorNumSerie(String active, String numSerie) {
        return vehiculoRepository.buscarVehiculoPorNumSerie(active,numSerie);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> buscarVehiculoPorKilometros(String active, Integer kilometros) {
        return vehiculoRepository.buscarVehiculoPorKilometros(active,kilometros);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> buscarVehiculoPorKilometros(String active, Integer kilometroInicio, Integer kilometroFin) {
        return vehiculoRepository.buscarVehiculoPorKilometros(active,kilometroInicio,kilometroFin);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> buscarVehiculoPorUltimoServicio(String active, String ultimoServicio) {
        return vehiculoRepository.buscarVehiculoPorUltimoServicio(active,ultimoServicio);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> buscarVehiculoPorUltimoServicio(String active, String servicioInicio, String servicioFin) {
        return vehiculoRepository.buscarVehiculoPorUltimoServicio(active,servicioInicio,servicioFin);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> buscarVehiculoPorRegistro(String activo, LocalDate ultimoServicio) {
        return vehiculoRepository.buscarVehiculoPorRegistro(activo,ultimoServicio);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> buscarVehiculoPorRegistro(String activo, LocalDate fechaInicio, LocalDate fechaFin) {
        return vehiculoRepository.buscarVehiculoPorRegistro(activo,fechaInicio,fechaFin);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Categoria> listarCategoriasPorMarcaYModelo(String active, Integer marcaId, Integer modeloId) {
        List<Vehiculo> vehiculos = vehiculoRepository.listarVehiculosPorMarcaYModelo(active, marcaId, modeloId);

        // Extraer categorías únicas
        return vehiculos.stream()
                .map(Vehiculo::getCategoria)
                .filter(c -> c != null)
                .distinct() // en memoria
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<VehiculoDTO> buscadorVehiculo(String status, String busqueda) {
        return vehiculoRepository.buscadorVehiculos(status,busqueda);
    }

    @Transactional(readOnly = true)
    @Override
    public long contarVehiculosPorBusquedaGeneral(String status, String termino){
        return vehiculoRepository.contarVehiculosPorBusquedaGeneral(status, termino);
    };

    //  Listar vehículos activos con paginación
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> listarVehiculosPaginado(String active, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("vehiculoId").descending());
        return vehiculoRepository.listarVehiculosPaginado(active, pageable);
    }

    //  Buscador general (multiatributo)
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> buscadorVehiculosPaginado(String active, String busqueda, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("vehiculoId").descending());
        return vehiculoRepository.buscadorVehiculosPaginado(active, busqueda, pageable);
    }

    //  Buscar por propietario
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> buscarPorPropietario(String active, String nombre, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina);
        return vehiculoRepository.buscarVehiculoPorPropietarioPaginado(active, nombre, pageable);
    }

    //  Buscar por propietario
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> buscarVehiculoPorNumSeriePaginado(String active, String numSerie, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina);
        return vehiculoRepository.buscarVehiculoPorNumSeriePaginado(active, numSerie, pageable);
    }

    //  Buscar por marca
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> buscarPorMarca(String active, String marca, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina);
        return vehiculoRepository.buscarVehiculoPorMarcaPaginado(active, marca, pageable);
    }

    //  Buscar por modelo
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> buscarPorModelo(String active, String modelo, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina);
        return vehiculoRepository.buscarVehiculoPorModeloPaginado(active, modelo, pageable);
    }

    //  Buscar por placas
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> buscarPorPlacas(String active, String placas, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina);
        return vehiculoRepository.buscarVehiculoPorPlacasPaginado(active, placas, pageable);
    }

    //  Buscar por color
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> buscarPorColor(String active, String color, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina);
        return vehiculoRepository.buscarVehiculoPorColorPaginado(active, color, pageable);
    }

    //  Buscar por categoría
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> buscarPorCategoria(String active, String categoria, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina);
        return vehiculoRepository.buscarVehiculoPorCategoriaPaginado(active, categoria, pageable);
    }

    //  Buscar por año exacto
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> buscarPorAnio(String active, Integer anio, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina);
        return vehiculoRepository.buscarVehiculoPorAnioPaginado(active, anio, pageable);
    }

    //  Buscar por rango de años
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> buscarPorAnioRango(String active, Integer inicio, Integer fin, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina);
        return vehiculoRepository.buscarVehiculoPorAnioPaginado(active, inicio, fin, pageable);
    }

    //  Buscar por kilometraje exacto
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> buscarPorKilometros(String active, Integer kms, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina);
        return vehiculoRepository.buscarVehiculoPorKilometrosPaginado(active, kms, pageable);
    }

    //  Buscar por rango de kilometraje
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> buscarPorKilometrosRango(String active, Integer inicio, Integer fin, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina);
        return vehiculoRepository.buscarVehiculoPorKilometrosPaginado(active, inicio, fin, pageable);
    }

    //  Buscar por fecha de registro exacta
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> buscarPorFechaRegistro(String active, LocalDate fecha, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina);
        return vehiculoRepository.buscarVehiculoPorRegistroPaginado(active, fecha, pageable);
    }

    //  Buscar por fecha de registro exacta
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> buscarVehiculoPorUltimoServicioPaginado(String active, LocalDate fecha, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina);
        return vehiculoRepository.buscarVehiculoPorUltimoServicioPaginado(active, fecha, pageable);
    }

    //  Buscar por fecha de registro exacta
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> buscarVehiculoPorUltimoServicioPaginadoRango(String active, LocalDate fechaInicio, LocalDate fechaFin , int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina);
        return vehiculoRepository.buscarVehiculoPorUltimoServicioPaginado(active, fechaInicio, fechaFin, pageable);
    }

    //  Buscar por rango de fecha de registro
    @Transactional(readOnly = true)
    @Override
    public Page<VehiculoDTO> buscarPorFechaRegistroRango(String active, LocalDate inicio, LocalDate fin, int pagina, int tamanoPagina) {
        Pageable pageable = PageRequest.of(pagina, tamanoPagina);
        return vehiculoRepository.buscarVehiculoPorRegistroPaginado(active, inicio, fin, pageable);
    }

    // Reasignar marca
    @Transactional
    @Override
    public int reasignarMarcaPorId(Integer marcaId) {
        return vehiculoRepository.reasignarMarcaPorId(marcaId);
    }

    // Reasignar modelo y categoría por marca
    @Transactional
    @Override
    public int reasignarModeloYCategoriaPorMarca(Integer marcaId) {
        return vehiculoRepository.reasignarModeloYCategoriaPorMarca(marcaId);
    }

    // Reasignar modelo específico
    @Transactional
    @Override
    public int reasignarModeloPorId(Integer modeloId) {
        return vehiculoRepository.reasignarModeloPorId(modeloId);
    }

    // Reasignar categoria específico
    @Transactional
    @Override
    public int reasignarCategoriaPorId(Integer categoriaId) {
        return vehiculoRepository.reasignarCategoriaPorId(categoriaId);
    }


    @Transactional(readOnly = true)
    @Override
    public Vehiculo buscarVehiculoPorId(Integer vehiculoId, String status) {
        return vehiculoRepository.buscarVehiculoPorId(vehiculoId,status);
    }

}//clase
