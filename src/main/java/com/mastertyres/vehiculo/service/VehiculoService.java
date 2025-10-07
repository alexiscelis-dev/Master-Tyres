package com.mastertyres.vehiculo.service;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import com.mastertyres.vehiculo.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mastertyres.common.MensajesAlert.mostrarError;
import static com.mastertyres.common.MensajesAlert.mostrarInformacion;

@Service
public class VehiculoService implements IVehiculoService {


    private VehiculoRepository vehiculoRepository;


    @Autowired
    public VehiculoService(VehiculoRepository vehiculoRepository) {

        this.vehiculoRepository = vehiculoRepository;

    }

    public List<VehiculoDTO> obtenerVehiculosConServicioVencido() {

        LocalDate fechaLimite = LocalDate.now().minusMonths(6);


        return vehiculoRepository.listarVehiculosConServicioVencido(fechaLimite);
    }

    public boolean actualizarUltimoServicio(Integer idVehiculo) {
        return vehiculoRepository.findById(idVehiculo)
                .map(vehiculo -> {
                    vehiculo.setUltimoServicio(LocalDate.now()+""); // actualiza a la fecha actual
                    vehiculoRepository.save(vehiculo); // persiste el cambio
                    return true;
                })
                .orElse(false); // si no se encuentra el vehículo
    }


    public void guardarVehiculos(Cliente cliente, List<Vehiculo> vehiculos) {
        if (cliente == null || vehiculos == null || vehiculos.isEmpty()) {
            throw new IllegalArgumentException("Cliente o vehículos inválidos");
        }

        for (Vehiculo v : vehiculos) {
            v.setCliente(cliente);
            v.setActive("ACTIVE");
            if (v.getCreated_at() == null) {
                v.setCreated_at(LocalDate.now().toString());
            }
            if (v.getUpdated_at() == null){
                v.setUpdated_at(LocalDate.now().toString());
            }
            if (v.getFechaRegistro() == null){
                v.setFechaRegistro(LocalDate.now().toString());
            }

            vehiculoRepository.save(v);
        }
    }

    @Transactional
    public Vehiculo actualizarVehiculo(Integer id, Vehiculo datosActualizados) {
        Vehiculo existente = vehiculoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado con id " + id));

        // 🔹 Actualizar campos
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

    public boolean existeVehiculoPorPlacas(String placas) {
        return vehiculoRepository.existeVehiculoPorPlacas(placas);
    }

    public boolean existeVehiculoPlacas_Editar(String placas, Integer id) {
        return vehiculoRepository.existeVehiculoPlacas_Editar(placas,id);
    }

    public boolean existeVehiculoPorNumeroSerie(String numSerie) {
        return vehiculoRepository.existeVehiculoPorNumeroSerie(numSerie);
    }

    public boolean existeVehiculoNumeroSerie_Editar(String numSerie, Integer id) {
        return vehiculoRepository.existeVehiculoNumeroSerie_Editar(numSerie, id);
    }

    public boolean existeVehiculoPorPlacasONumeroSerie(String placas, String numSerie) {
        return vehiculoRepository.existeVehiculoPorPlacasONumeroSerie(placas, numSerie);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoDTO> listarVehiculos(String vehiculoStatus) {
        return vehiculoRepository.listarVehiculos(vehiculoStatus);
    }

    @Transactional(readOnly = true)
    public Vehiculo Vehiculo_SinDTO(Integer id) {
        return vehiculoRepository.Vehiculo_SinDTO(id);
    }

    @Transactional
    @Override
    public int eliminarVehiculo(String eliminar, Integer idVehiculo) {

        int filasEliminadas = vehiculoRepository.eliminarVehiculo(eliminar, idVehiculo);


        if (filasEliminadas > 0)
            mostrarInformacion("Vehiculo eliminado","Vehiculo eliminado","Vehiculo eliminado exitosamente.");

        else
            mostrarError("Error al eliminar vehiculo","Algo salio mal","No se pudo eliminar el vehiculo seleccionado.");

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

}//clase
