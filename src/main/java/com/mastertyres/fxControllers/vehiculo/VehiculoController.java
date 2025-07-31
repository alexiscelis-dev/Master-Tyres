package com.mastertyres.fxControllers.vehiculo;

import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.repository.VehiculoRepository;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class VehiculoController {
    @FXML
    private TableView<Vehiculo> tablaVehiculos;
    @FXML
    private TableColumn<Vehiculo, String> colMarca;
    @FXML
    private TableColumn<Vehiculo, String> colModelo;
    @FXML
    private TableColumn<Vehiculo, Integer> colAnio;
    @FXML
    private TableColumn<Vehiculo, String> colPlacas;
    @FXML
    private TableColumn<Vehiculo, String> colNumeroSerie;
    @FXML
    private TableColumn<Vehiculo, Integer> colKilometraje;
    @FXML
    private TableColumn<Vehiculo, String> colUltimoServicio;
    @FXML
    private TableColumn<Vehiculo, String> colFechaRegistro;

    @Autowired
    private  VehiculoService vehiculoService;






    @FXML
    public void initialize() {
        colMarca.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getMarca().getNombre())

        );
        colModelo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getModelo().getNombre())
        );
        colAnio.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getAnio()).asObject()
        );
        colPlacas.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPlacas())
        );
        colNumeroSerie.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNumSerie())
        );
        colKilometraje.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getKilometros()).asObject()
        );
        colUltimoServicio.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getUltimoServicio())
        );
        colFechaRegistro.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFechaRegistro())
        );

        //Llenar tabla con registros
        tablaVehiculos.getItems().setAll(vehiculoService.listarVehiculos());


    }//initialize

}//NotasController
