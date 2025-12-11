package com.mastertyres.fxControllers.AgregarInventario;

import com.mastertyres.common.MenuContextSetting;
import com.mastertyres.common.exeptions.InventarioException;
import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.inventario.service.InventarioService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.mastertyres.common.MensajesAlert.*;


@Component
public class AgregarInventarioController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField txtCodBarras;
    @FXML
    private TextField txtDot;
    @FXML
    private TextField txtMarca;
    @FXML
    private TextField txtModelo;
    @FXML
    private TextField txtMedida1;
    @FXML
    private TextField txtMedida2;
    @FXML
    private TextField txtObservaciones;
    @FXML
    private ChoiceBox<String> cbIndiceCarga;
    @FXML
    private ChoiceBox<String> cbIndiceVelocidad;
    @FXML
    private TextField txtStock;
    @FXML
    private TextField txtPrecioC;
    @FXML
    private TextField txtPrecioV;
    @FXML
    private TextField txtImg;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnImagen;
    @FXML
    private Button btnRefrescar;

    @Autowired
    InventarioService inventarioService;

    private BooleanProperty codBarrasValido = new SimpleBooleanProperty(true);
    private BooleanProperty dotValido = new SimpleBooleanProperty(true);
    private BooleanProperty marcaValido = new SimpleBooleanProperty(true);
    private BooleanProperty medida1Valido = new SimpleBooleanProperty(true);
    private BooleanProperty medida2Valido = new SimpleBooleanProperty(true);
    private BooleanProperty indiceCargaValido = new SimpleBooleanProperty(true);
    private BooleanProperty indiceVelocidadValido = new SimpleBooleanProperty(true);
    private BooleanProperty stockValido = new SimpleBooleanProperty(true);
    private BooleanProperty precioCompraValido = new SimpleBooleanProperty(true);
    private BooleanProperty precioventaValido = new SimpleBooleanProperty(true);

    private VentanaPrincipalController ventanaPrincipalController;

    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;
    }

    @FXML
    private void initialize() {
        MenuContextSetting.disableMenu(rootPane); //Quita el menu contextual del clic derecho

        indicesChoiceBox();
        btnLimpiar.setOnAction(event -> clean());

        //Abre y cierra el popup del choiceBox para que aparezca en la posicion correcta y no debajo de la tabla
        cbIndiceCarga.setOnMousePressed(event -> {
            if (!cbIndiceCarga.isShowing()) {
                cbIndiceCarga.show();
                cbIndiceCarga.hide();
            }
        });

        cbIndiceVelocidad.setOnMousePressed(event -> {
            if (!cbIndiceVelocidad.isShowing()) {
                cbIndiceVelocidad.show();
                cbIndiceVelocidad.hide();
            }
        });

        btnRegistrar.setOnAction(event -> registrar());

        btnImagen.setOnAction(event -> seleccionarImg());

        txtStock.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));

        txtPrecioC.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));

        txtPrecioV.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));

        configurarValidaciones();

    }//ininitialize

    private void indicesChoiceBox() {
        String[] indicesVelocidad = {
                "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8",
                "B", "C", "D", "E", "F", "G",
                "J", "K", "L", "M", "N",
                "P", "Q", "R", "S", "T",
                "U", "H", "V", "W", "Y",
                "(Y)", "ZR"
        };
        for (String indice : indicesVelocidad)
            cbIndiceVelocidad.getItems().addAll(indice);


        for (int i = 60; i <= 138; i++)
            cbIndiceCarga.getItems().addAll(i + "");


    }//indicesChoiceBox

    private void clean() {
        txtCodBarras.setText("");
        txtDot.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtMedida1.setText("");
        txtMedida2.setText("");
        cbIndiceCarga.setValue(null);
        cbIndiceVelocidad.setValue(null);
        txtStock.setText("");
        txtPrecioC.setText("");
        txtPrecioV.setText("");
        txtImg.setText("");
        txtObservaciones.setText("");

        txtCodBarras.setStyle("");
        txtDot.setStyle("");
        txtMarca.setStyle("");
        txtMedida1.setStyle("");
        txtMedida2.setStyle("");
        txtPrecioC.setStyle("");
        txtPrecioV.setStyle("");
        txtStock.setStyle("");


    }//clean

    private void registrar() {

        String medida = txtMedida1.getText().replaceAll("\\s", "") + "/" + txtMedida2.getText().replaceAll("\\s", "");


        if (!empty()) {

            Inventario inventario = Inventario.builder()
                    .codigoBarras(txtCodBarras.getText())
                    .dot(txtDot.getText())
                    .marca(txtMarca.getText())
                    .modelo(txtModelo.getText())
                    .medida(medida)
                    .indiceCarga(cbIndiceCarga.getValue() != null ? cbIndiceCarga.getValue() : "")
                    .indiceVelocidad(cbIndiceVelocidad.getValue() != null ? cbIndiceVelocidad.getValue() : "")
                    .stock(Integer.parseInt(txtStock.getText()))
                    .precioCompra(Float.parseFloat(txtPrecioC.getText()))
                    .precioVenta(Float.parseFloat(txtPrecioV.getText()))
                    .observaciones(txtObservaciones.getText())
                    .imagen(txtImg.getText() != null ? txtImg.getText() : "")
                    .build();

            try {
                inventarioService.guardarInventario(inventario);
                mostrarInformacion("Elemento agregado", "", "Elemento agregado al inventario correctamente.");
                clean();
                ventanaPrincipalController.irAtras();
            } catch (InventarioException ie) {
                ie.printStackTrace();
                mostrarError("Error de inventario", "", ie.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                mostrarError("Error inesperado", "", "No se pudo guardar el inventario, vuelva a intentarlo más tarde.");
            }

        } else {
            mostrarWarning("Campos oblicatorios", "", "los campos marcados con '*' son obligatorios");
        }

    }//registrar

    // solo verifica si los campos necesarios estan vacios (marca,medida,stock,precios,codigo de barras y dot)

    private boolean empty() {
        boolean empty = false;

        if (txtCodBarras.getText().isEmpty()) {
            txtCodBarras.setStyle("-fx-border-color:red; -fx-border-width:2px;");
            empty = true;
        }
        if (txtDot.getText().isEmpty()) {
            txtDot.setStyle("-fx-border-color:red; -fx-border-width:2px;");
            empty = true;
        }
        if (txtMarca.getText().isEmpty()) {
            txtMarca.setStyle("-fx-border-color:red; -fx-border-width:2px;");
            empty = true;
        }
        if (txtMarca.getText().isEmpty()) {
            txtMarca.setStyle("-fx-border-color:red; -fx-border-width:2px;");
            empty = true;
        }
        if (txtMedida1.getText().isEmpty()) {
            txtMedida1.setStyle("-fx-border-color:red; -fx-border-width:2px;");
            empty = true;
        }
        if (txtMedida2.getText().isEmpty()) {
            txtMedida2.setStyle("-fx-border-color:red; -fx-border-width:2px;");
            empty = true;
        }
        if (txtPrecioC.getText().isEmpty()) {
            txtPrecioC.setStyle("-fx-border-color:red; -fx-border-width:2px;");
            empty = true;
        }
        if (txtPrecioV.getText().isEmpty()) {
            txtPrecioV.setStyle("-fx-border-color:red; -fx-border-width:2px;");
            empty = true;
        }


        return empty;
    }// empty

    private void seleccionarImg() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "Archivos de imagen", "*.png", "*.jpg", "*.jpeg"
        ));

        Stage stage = (Stage) btnImagen.getScene().getWindow();
        File archivo = fileChooser.showOpenDialog(stage);

        if (archivo != null) {
            String url = archivo.getAbsolutePath();
            txtImg.setText(url);
        }

    } //seleccionarImg


    private void configurarValidaciones() {

        txtCodBarras.textProperty().addListener((observable, oldText, newText) -> {

            if (!txtCodBarras.getText().matches("\\d{8,13}")) {
                codBarrasValido.set(false);
                txtCodBarras.setStyle("-fx-border-color: red;");
            } else {
                codBarrasValido.set(true);
                txtCodBarras.setStyle("");
            }
        });

        txtDot.textProperty().addListener(((observable, oldtext, newText) -> {
            txtDot.setText(txtDot.getText().toUpperCase());
            if (!txtDot.getText().matches("^[A-Za-z0-9]{10,13}$")) {
                dotValido.set(false);
                txtDot.setStyle("-fx-border-color: red;");
            } else {
                txtDot.setStyle("");
                dotValido.set(true);
            }
        }));

        txtMarca.textProperty().addListener(((observable, oldtext, newText) -> {
            if (txtMarca.getText().isBlank()) {
                txtMarca.setStyle("-fx-border-color: red;");
                marcaValido.set(false);
            } else {
                marcaValido.set(true);
                txtMarca.setStyle("");
            }


        }));

        txtStock.textProperty().addListener(((observable, oldtext, newText) -> {
            if (txtStock.getText().isBlank() || !txtStock.getText().matches("\\d*")) {
                txtStock.setStyle("-fx-border-color: red;");
                stockValido.set(false);
                return;
            }

            try {
                long valor = Long.parseLong(newText);
                if (valor > Integer.MAX_VALUE) {
                    txtStock.setStyle("-fx-border-color: red;");
                    stockValido.set(false);
                } else {
                    txtStock.setStyle("");
                    stockValido.set(true);
                }

            } catch (NumberFormatException e) {
                txtStock.setStyle("-fx-border-color: red;");
                stockValido.set(false);
            }

        }));

        txtMedida1.textProperty().addListener(((observable, oldText, newText) -> {
            if (txtMedida1.getText().length() > 10 || txtMedida1.getText().isBlank()) {
                medida1Valido.set(false);
                txtMedida1.setStyle("-fx-border-color: red;");
            } else {
                medida1Valido.set(false);
                txtMedida1.setStyle("");
            }
        }));

        txtMedida2.textProperty().addListener(((observable, oldText, newText) -> {
            if (txtMedida2.getText().length() > 10 || txtMedida2.getText().isBlank()) {
                medida2Valido.set(false);
                txtMedida2.setStyle("-fx-border-color: red;");
            } else {
                medida2Valido.set(false);
                txtMedida2.setStyle("");
            }
        }));


        txtPrecioC.textProperty().addListener(((observable, oldtext, newText) -> {
            boolean valido = true;

            if (txtPrecioC.getText().isBlank() || !txtPrecioC.getText().matches("\\d*(\\.\\d{0,2})?")) {
                valido = false;
            } else {
                try {
                    float valor = Float.parseFloat(newText);
                    if (valor > Float.MAX_VALUE) {
                        valido = false;
                    }
                } catch (NumberFormatException e) {
                    valido = false;
                }
            }
            if (!valido) {
                txtPrecioC.setStyle("-fx-border-color: red;");
                precioCompraValido.set(false);
            } else {
                txtPrecioC.setStyle("");
                precioCompraValido.set(true);
            }
        }));

        txtPrecioV.textProperty().addListener(((observable, oldtext, newText) -> {
            boolean valido = true;

            if (txtPrecioV.getText().isBlank() || !txtPrecioV.getText().matches("\\d*(\\.\\d{0,2})?")) {
                valido = false;
            } else {
                try {
                    float valor = Float.parseFloat(newText);
                    if (valor > Float.MAX_VALUE) {
                        valido = false;
                    }
                } catch (NumberFormatException e) {
                    valido = false;  // si no se puede convertir, también invalido
                }
            }
            if (!valido) {
                txtPrecioV.setStyle("-fx-border-color: red;");
                precioventaValido.set(false);
            } else {
                txtPrecioV.setStyle("");
                precioventaValido.set(true);
            }

        }));

        btnRegistrar.disableProperty().bind(
                (txtCodBarras.textProperty().isEmpty())
                        .or(txtDot.textProperty().isEmpty())
                        .or(txtMarca.textProperty().isEmpty())
                        .or(txtMedida1.textProperty().isEmpty())
                        .or(txtMedida2.textProperty().isEmpty())
                        .or(txtStock.textProperty().isEmpty())
                        .or(txtPrecioC.textProperty().isEmpty())
                        .or(txtPrecioV.textProperty().isEmpty())
                        .or(stockValido.not())
                        .or(precioventaValido.not())
                        .or(precioCompraValido.not())


        );

    }//configurarValidaciones


}//class
