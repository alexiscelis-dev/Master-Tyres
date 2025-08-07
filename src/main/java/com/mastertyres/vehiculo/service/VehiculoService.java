package com.mastertyres.vehiculo.service;

import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import com.mastertyres.vehiculo.model.VehiculoStatus;
import com.mastertyres.vehiculo.repository.VehiculoRepository;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehiculoService implements IVehiculoService {


    private VehiculoRepository vehiculoRepository;



    @Autowired
    public VehiculoService(VehiculoRepository vehiculoRepository){

        this.vehiculoRepository = vehiculoRepository;

    }


    @Override
    @Transactional(readOnly = true)
    public List<VehiculoDTO> listarVehiculos(String vehiculoStatus) {
        return vehiculoRepository.listarVehiculos(vehiculoStatus);
    }

    @Transactional
    @Override
    public int eliminarVehiculo(String eliminar, Integer idVehiculo) {

        int filasEliminadas = vehiculoRepository.eliminarVehiculo(eliminar,idVehiculo);


        if (filasEliminadas > 0) {

            Alert ventana = new  Alert(Alert.AlertType.INFORMATION);
            ventana.setTitle("Vehiculo eliminado");
            ventana.setHeaderText("Vehiculo eliminado");
            ventana.setContentText("Vehiculo eliminado exitosamente.");

            ventana.showAndWait();


        }
        else{
              Alert ventana = new Alert(Alert.AlertType.ERROR);
              ventana.setTitle("Error al eliminar vehiculo");
              ventana.setHeaderText("Algo salio mal");
              ventana.setContentText("No se pudo eliminar el vehiculo seleccionado.");
            }

        return  filasEliminadas;


    }//eliminar vehiculo


}
