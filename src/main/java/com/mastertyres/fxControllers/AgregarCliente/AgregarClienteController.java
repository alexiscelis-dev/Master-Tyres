package com.mastertyres.fxControllers.AgregarCliente;

import com.mastertyres.vehiculo.model.Vehiculo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;
import javafx.fxml.FXML;

@Component
public class AgregarClienteController {

    @FXML private TableView<Vehiculo> tablaVehiculos;
    @FXML private TableColumn<Vehiculo, String> colMarca;
    @FXML private TableColumn<Vehiculo, String> colCategoria;
    @FXML private TableColumn<Vehiculo, String> colColor;
    @FXML private TableColumn<Vehiculo, String> colPlacas;
    @FXML private TableColumn<Vehiculo, String> colAnnio;
    @FXML private TableColumn<Vehiculo, String> colModelo;
    @FXML private TableColumn<Vehiculo, String> colNumSerie;
    @FXML private TableColumn<Vehiculo, String> colKilometros;
    @FXML private TableColumn<Vehiculo, String> colultimoServicio;
    @FXML private TableColumn<Vehiculo, String> colObservaciones;
    @FXML private TableColumn<Vehiculo, Void> colEliminar;


    @FXML private TextField txtMarca;
    @FXML private TextField txtCategoria;
    @FXML private TextField txtColor;
    @FXML private TextField txtPlacas;
    @FXML private TextField txtModelo;
    @FXML private TextField txtAnio;
    @FXML private TextField txtSerie;
    @FXML private TextField txtKilometros;
    @FXML private TextField txtUltimoServicio;
    @FXML private TextField txtObservaciones;


    private ObservableList<Vehiculo> listaVehiculos = FXCollections.observableArrayList();


    @FXML
    private void initialize() {
        tablaVehiculos.setItems(listaVehiculos);

        colMarca.setCellValueFactory(data -> new SimpleStringProperty(txtOrNull(data.getValue().getMarca())));
        colModelo.setCellValueFactory(data -> new SimpleStringProperty(txtOrNull(data.getValue().getModelo())));
        colAnnio.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getAnio())));
        colCategoria.setCellValueFactory(data -> new SimpleStringProperty(txtOrNull(data.getValue().getCategoria())));
        colColor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getColor()));
        colPlacas.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlacas()));
        colNumSerie.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumSerie()));
        colKilometros.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getKilometros())));
        colultimoServicio.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUltimoServicio()));
        colObservaciones.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getObservaciones()));

        // Botón eliminar
        colEliminar.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Eliminar");

            {
                btn.setOnAction(e -> {
                    Vehiculo v = getTableView().getItems().get(getIndex());
                    listaVehiculos.remove(v);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    // Método auxiliar para evitar null pointer
    private String txtOrNull(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }



    @FXML
    private void agregarVehiculo(ActionEvent event) {
        Vehiculo v = new Vehiculo();

        v.setColor(txtColor.getText());
        v.setPlacas(txtPlacas.getText());
        v.setNumSerie(txtSerie.getText());
        v.setUltimoServicio(txtUltimoServicio.getText());
        v.setObservaciones(txtObservaciones.getText());

        try {
            v.setAnio(Integer.parseInt(txtAnio.getText()));
        } catch (NumberFormatException e) {
            v.setAnio(0); // o mostrar alerta
        }

        try {
            v.setKilometros(Integer.parseInt(txtKilometros.getText()));
        } catch (NumberFormatException e) {
            v.setKilometros(0);
        }

        
        v.setMarca(null);    // o algún objeto dummy
        v.setModelo(null);   // idem
        v.setCategoria(null); // idem

        listaVehiculos.add(v);

        limpiarCamposVehiculo();
    }

    private void limpiarCamposVehiculo() {
        txtMarca.clear();
        txtModelo.clear();
        txtAnio.clear();
        txtColor.clear();
        txtPlacas.clear();
        txtSerie.clear();
        txtCategoria.clear();
        txtKilometros.clear();
        txtUltimoServicio.clear();
        txtObservaciones.clear();
    }


}
