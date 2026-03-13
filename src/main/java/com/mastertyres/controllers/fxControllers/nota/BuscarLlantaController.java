package com.mastertyres.controllers.fxControllers.nota;

import com.mastertyres.common.interfaces.ICleanable;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.inventario.model.StatusInventario;
import com.mastertyres.inventario.service.InventarioService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BuscarLlantaController implements IFxController {
    @FXML
    private AnchorPane root;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnAceptar;
    @FXML
    private TextField txtBuscador;
    @FXML
    private TextField txtLlanta;
    @FXML
    private TextField txtStock;
    @FXML
    private TableView<Inventario> tablaLlantas;
    @FXML
    private TableColumn<Inventario, String> colMarca;
    @FXML
    private TableColumn<Inventario, String> colModelo;
    @FXML
    private TableColumn<Inventario, String> colMedida;
    @FXML
    private TableColumn<Inventario, String> colIndiceVelocidad;
    @FXML
    private TableColumn<Inventario, String> colIndiceCarga;
    @FXML
    private TableColumn<Inventario, Integer> colStock;

    private Inventario llantaSeleccionada;


    public Inventario getLlantaSeleccionada() {
        return llantaSeleccionada;
    }

    @Autowired
    InventarioService inventarioService;


    @FXML
    private void initialize() {

        configuraciones();
        listeners();
        cargarLlantasInicio();

    }//initialize

    @Override
    public void configuraciones(){

        MenuContextSetting.disableMenu(root);

    }//configuraciones

    @Override
    public void listeners() {

        btnBuscar.setOnAction(event -> cargarLlantas(txtBuscador.getText()));

        tablaLlantas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtLlanta.setText(newValue.getMarca() + " " + newValue.getModelo() + " " + newValue.getMedida());
                txtStock.setText("1");
                txtStock.setDisable(false);
                btnAceptar.setDisable(false);
            }
        });

        txtBuscador.setOnKeyPressed(event -> {

            if (txtBuscador.getText() != null && !txtBuscador.getText().isEmpty()) {
                if (event.getCode() == KeyCode.ENTER)
                    cargarLlantas(txtBuscador.getText());
            }

        });

    }//listeners

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtLlanta.getScene().getWindow();
        stage.close();

    }//cancelar

    private void cargarLlantas(String busqueda) {
        cargarLlantas();

        cargarDatosLlantas(busqueda);

    }//cargarLlantas

    private void cargarLlantas(){
        colMarca.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getMarca()
        ));
        colModelo.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getModelo()
        ));
        colMedida.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getMedida()
        ));
        colIndiceCarga.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getIndiceCarga()
        ));
        colIndiceVelocidad.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getIndiceVelocidad()
        ));
        colStock.setCellValueFactory(data -> new SimpleIntegerProperty(
                data.getValue().getStock()).asObject());
    }

    private void cargarDatosLlantas(String busqueda) {
        try {
            if (busqueda != null && !busqueda.isEmpty() ) {
                List<Inventario> inventarios = inventarioService.buscadorInventario(StatusInventario.ACTIVE.toString(), busqueda);
                tablaLlantas.getItems().setAll(FXCollections.observableList(inventarios));


            } else {
                List<Inventario> inventarioVacio = new ArrayList<>();
                tablaLlantas.getItems().setAll(FXCollections.observableList(inventarioVacio));
            }

        } catch (Exception e) {
            e.printStackTrace();
            MensajesAlert.mostrarExcepcion(
                    "Error de visualización",
                    "Fallo al cargar registros",
                    "No se pudieron cargar los datos en la vista. Por favor, inténtelo de nuevo más tarde.",
                    e
            );
        }

    }//cargarDatosLlantas

    //Este metodo carga las llantas en la tabla cuando inicia la ventana mientras que cargarLlantas lo hace en la busqueda
    private void cargarLlantasInicio(){
        cargarLlantas();

        try {
            List<Inventario> inventarios = inventarioService.first100Inventario(StatusInventario.ACTIVE.toString());
            tablaLlantas.getItems().setAll(FXCollections.observableList(inventarios));
        }catch (Exception e){
            MensajesAlert.mostrarExcepcion(
                    "Error de visualización",
                    "Fallo al cargar registros",
                    "No se pudieron cargar los datos en la tabla. Por favor, inténtelo de nuevo más tarde.",
                    e
            );
        }

    }//cargarLlantasInicio

    @FXML
    private void aceptar(ActionEvent event) {

        llantaSeleccionada = tablaLlantas.getSelectionModel().getSelectedItem();

        int solicitado = Integer.parseInt(txtStock.getText());
        int disponible = llantaSeleccionada.getStock();

        if (solicitado > disponible) {
            String unidad = (disponible == 1) ? "unidad disponible" : "unidades disponibles";

            if (Integer.parseInt(txtStock.getText()) > llantaSeleccionada.getStock()) {
                MensajesAlert.mostrarWarning(
                        "Advertencia",
                        "Stock insuficiente",
                        "La llanta seleccionada (" + llantaSeleccionada.getMarca() + " " + llantaSeleccionada.getModelo() + ") " +
                                "solo cuenta con " + disponible + " " + unidad + ". Cantidad solicitada: " + solicitado + "."
                );
            }


        }else{

            llantaSeleccionada.setStock(Integer.parseInt(txtStock.getText())); // se actualiza el stock con lo que se selecciona en el campo de texto
            btnAceptar.getScene().getWindow().hide();
        }



    }//aceptar

}//class
