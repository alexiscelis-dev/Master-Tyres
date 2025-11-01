package com.mastertyres.fxControllers.nota;

import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.inventario.model.StatusInventario;
import com.mastertyres.inventario.service.InventarioService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.mastertyres.common.MensajesAlert.mostrarError;

@Component
public class BuscarLlantaController {
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnAceptar;
    @FXML
    private TextField txtBuscador;
    @FXML
    private TextField txtMarca;
    @FXML
    private TextField txtModelo;
    @FXML
    private TextField txtMedida;
    @FXML
    private TableView<Inventario> tablaLlantas;
    @FXML
    private TableColumn<Inventario,String> colMarca;
    @FXML
    private TableColumn<Inventario,String> colModelo;
    @FXML
    private TableColumn<Inventario,String> colMedida;
    @FXML
    private TableColumn<Inventario,String> colIndiceVelocidad;
    @FXML
    private TableColumn<Inventario,String> colIndiceCarga;

    private Inventario llantaSeleccionada;

    public Inventario getLlantaSeleccionada(){
        return llantaSeleccionada;
    }

    @Autowired
    InventarioService inventarioService;


    @FXML
    private void initialize(){
        btnBuscar.setOnAction(event -> cargarLlantas(txtBuscador.getText()));

        tablaLlantas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null){
                txtMarca.setText(newValue.getMarca());
                txtModelo.setText(newValue.getModelo());
                txtMedida.setText(newValue.getMedida());
                btnAceptar.setDisable(false);
            }

        });

    }//initialize

    @FXML
    private void cancelar(ActionEvent event){
        Stage stage = (Stage) txtMarca.getScene().getWindow();
        stage.close();

    }//cancelar

    private void cargarLlantas(String busqueda){
        colMarca.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getMarca()
        ));
        colModelo.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getModelo()
        ));
        colMedida.setCellValueFactory( data -> new SimpleStringProperty(
                data.getValue().getMedida()
        ));
        colIndiceCarga.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getIndiceCarga()
        ));
        colIndiceVelocidad.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getIndiceVelocidad()
        ));

        cargarDatosLlantas(busqueda);

    }//cargarLlantas

    private void cargarDatosLlantas(String busqueda){
        try {
            if (busqueda != null && !busqueda.isEmpty()){
                List<Inventario> inventarios = inventarioService.buscadorInventario(StatusInventario.ACTIVE.toString(),busqueda);
                tablaLlantas.getItems().setAll(FXCollections.observableList(inventarios));

            }else {
                List<Inventario> inventarioVacio = new ArrayList<>();
                tablaLlantas.getItems().setAll(FXCollections.observableList(inventarioVacio));
            }

        }catch (Exception e){
            e.printStackTrace();
            mostrarError("Error al mostrar datos", "", "No se pudieron cargar los datos. Por favor, inténtalo de nuevo más tarde.");
        }

    }//cargarDatosLlantas

    @FXML
    private void aceptar(ActionEvent event){
        llantaSeleccionada = tablaLlantas.getSelectionModel().getSelectedItem();
        btnAceptar.getScene().getWindow().hide();

    }//aceptar

}//class
