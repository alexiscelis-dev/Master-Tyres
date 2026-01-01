package com.mastertyres.fxControllers.nota;

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
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.mastertyres.common.MensajesAlert.mostrarError;
import static com.mastertyres.common.MensajesAlert.mostrarWarning;

@Component
public class BuscarLlantaController {
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
        txtBuscador.setOnKeyPressed(event -> {

            if (txtBuscador.getText() != null && !txtBuscador.getText().isEmpty()) {
                if (event.getCode() == KeyCode.ENTER)
                    cargarLlantas(txtBuscador.getText());
            }

        });

        btnBuscar.setOnAction(event -> cargarLlantas(txtBuscador.getText()));

        tablaLlantas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtLlanta.setText(newValue.getMarca() + " " + newValue.getModelo() + " " + newValue.getMedida());
                txtStock.setText("1");
                txtStock.setDisable(false);
                btnAceptar.setDisable(false);
            }
        });

    }//initialize

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtLlanta.getScene().getWindow();
        stage.close();

    }//cancelar

    private void cargarLlantas(String busqueda) {
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


        cargarDatosLlantas(busqueda);

    }//cargarLlantas

    private void cargarDatosLlantas(String busqueda) {
        try {
            if (busqueda != null && !busqueda.isEmpty()) {
                List<Inventario> inventarios = inventarioService.buscadorInventario(StatusInventario.ACTIVE.toString(), busqueda);
                tablaLlantas.getItems().setAll(FXCollections.observableList(inventarios));

            } else {
                List<Inventario> inventarioVacio = new ArrayList<>();
                tablaLlantas.getItems().setAll(FXCollections.observableList(inventarioVacio));
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al mostrar datos", "", "No se pudieron cargar los datos. Por favor, inténtalo de nuevo más tarde.");
        }

    }//cargarDatosLlantas

    @FXML
    private void aceptar(ActionEvent event) {

        llantaSeleccionada = tablaLlantas.getSelectionModel().getSelectedItem();

        int solicitado = Integer.parseInt(txtStock.getText());
        int disponible = llantaSeleccionada.getStock();

        if (solicitado > disponible) {
            String unidad = (disponible == 1) ? "unidad disponible" : "unidades disponibles";

            if (Integer.parseInt(txtStock.getText()) > llantaSeleccionada.getStock()) {
                mostrarWarning(
                        "Stock insuficiente",
                        "No hay suficiente stock disponible",
                        "La llanta seleccionada: " + llantaSeleccionada.getMarca() + " " +
                                llantaSeleccionada.getModelo() + " " + llantaSeleccionada.getMedida() + "\n" +
                                "solo cuenta con " + disponible + " " + unidad + ".\n" +
                                "Cantidad solicitada: " + solicitado + "."
                );
            }


        }else{

            llantaSeleccionada.setStock(Integer.parseInt(txtStock.getText())); // se actualiza el stock con lo que se selecciona en el campo de texto
            btnAceptar.getScene().getWindow().hide();
        }



    }//aceptar

}//class
