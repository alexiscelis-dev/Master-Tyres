package com.mastertyres.fxControllers.EditarControllers;

import com.mastertyres.common.MenuContextSetting;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.inventario.model.StatusInventario;
import com.mastertyres.inventario.service.InventarioService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mastertyres.common.MensajesAlert.*;

@Component
public class EditarInventarioController {
    @FXML private AnchorPane rootPane;
    @FXML private TextField txtCodBarras;
    @FXML private TextField txtDot;
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private TextField txtMedida;
    @FXML private ChoiceBox<String> cbIndiceCarga;
    @FXML private ChoiceBox<String> cbIndiceVelocidad;
    @FXML private TextField txtObservaciones;
    @FXML private TextField txtPrecioV;
    @FXML private TextField txtPrecioC;
    @FXML private TextField txtStock;
    @FXML private TextField txtImg;
    @FXML private VBox vBoxImg;
    @FXML private Button btnActualizar;
    @FXML private Button btnCancelar;
    @FXML private Button btnImagen;

    @Autowired
    InventarioService inventarioService;

    private Inventario inventario;
    private BooleanProperty marcaValido = new SimpleBooleanProperty(true);
    private BooleanProperty medidaValido = new SimpleBooleanProperty(true);
    private BooleanProperty stockValido = new SimpleBooleanProperty(true);
    private BooleanProperty precioCompraValido = new SimpleBooleanProperty(true);
    private BooleanProperty precioVentaValido = new SimpleBooleanProperty(true);
    private BooleanProperty codBarrasValido = new SimpleBooleanProperty(true);
    private BooleanProperty dotValido = new SimpleBooleanProperty(true);


    @FXML
    private void initialize() {

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


        MenuContextSetting.disableMenu(rootPane); //Desabilita el menu en los componentes
        indicesChoiceBox(); //Agrega la lista de indices de carga y velocidad
        configurarValidaciones(); //Verifica validaciones de sintaxis

        //Evitan que la lista del choiceBox se muestre en otro lado que no se abajo del mismo ChoiceBox
        cbIndiceVelocidad.setOnMousePressed(event -> {
            if (!cbIndiceVelocidad.isShowing()) {
                cbIndiceVelocidad.show();
                cbIndiceVelocidad.hide();
            }

        });
        cbIndiceCarga.setOnMousePressed(event -> {
            if (!cbIndiceCarga.isShowing()) {
                cbIndiceCarga.show();
                cbIndiceCarga.hide();
            }
        });
    }//initialize


    public void editarInventario(Inventario inventario) {
        this.inventario = inventario;

        txtCodBarras.setText(inventario.getCodigoBarras());
        txtDot.setText(inventario.getDot());
        txtMarca.setText(inventario.getMarca());
        txtModelo.setText(inventario.getModelo());
        txtMedida.setText(inventario.getMedida());
        cbIndiceCarga.setValue(inventario.getIndiceCarga());
        cbIndiceVelocidad.setValue(inventario.getIndiceVelocidad());
        txtStock.setText(inventario.getStock() + "");
        txtPrecioC.setText(inventario.getPrecioCompra() + "");
        txtPrecioV.setText(inventario.getPrecioVenta() + "");
        txtObservaciones.setText(inventario.getObservaciones());

        vBoxImg.setVisible(true);
        txtImg.setText(inventario.getImagen());

        File file = new File(txtImg.getText());

        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(200);
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);
            vBoxImg.getChildren().clear();
            vBoxImg.getChildren().add(imageView);
        } else {
            Image image = new Image(getClass().getResource("/icons/imagenPorDefecto.jpg").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(200);
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);
            vBoxImg.getChildren().clear();
            vBoxImg.getChildren().add(imageView);
        }

    }//editarInventario

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

    private void configurarValidaciones() {

        txtCodBarras.textProperty().addListener(((observable, oldText, newText) -> {
            if (!txtCodBarras.getText().matches("\\d{8,13}")) {
                codBarrasValido.set(false);
                txtCodBarras.setStyle("-fx-border-color: red;");
            } else {
                codBarrasValido.set(true);
                txtCodBarras.setStyle("");
            }

        }));

        txtDot.textProperty().addListener(((observable, oldtext, newTex) -> {
            txtDot.setText(txtDot.getText().toUpperCase());
            if (!txtDot.getText().matches("^[A-Za-z0-9]{10,13}$")) {
                dotValido.set(false);
                txtDot.setStyle("-fx-border-color: red;");
            } else {
                txtDot.setStyle("");
                dotValido.set(true);
            }

        }));

        txtMarca.textProperty().addListener(((observable, oldtext, newTex) -> {
            if (txtMarca.getText().isBlank()) {
                txtMarca.setStyle("-fx-border-color: red;");
                marcaValido.set(false);
            } else {
                marcaValido.set(true);
                txtMarca.setStyle("");
            }

        }));


        txtMedida.textProperty().addListener(((observable, oldtext, newTex) -> {
            if (txtMedida.getText().isBlank() || !txtMedida.getText().matches("^[A-Za-z0-9,/ ]{0,20}$") ||
                    txtMedida.getText().length() > 20) {

                txtMedida.setStyle("-fx-border-color: red;");
                medidaValido.set(false);
            } else {
                txtMedida.setStyle("");
                medidaValido.set(true);
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


        txtPrecioC.textProperty().addListener(((observable, oldtext, newTex) -> {
            if (txtPrecioC.getText().isBlank() || !txtPrecioC.getText().matches("\\d*(\\.\\d{0,2})?")) {
                txtPrecioC.setStyle("-fx-border-color: red;");
                precioCompraValido.set(false);
            } else {
                precioCompraValido.set(true);
                txtPrecioC.setStyle("");
            }

        }));

        txtPrecioV.textProperty().addListener(((observable, oldtext, newTex) -> {
            if (txtPrecioV.getText().isBlank() || !txtPrecioV.getText().matches("\\d*(\\.\\d{0,2})?")) {
                txtPrecioV.setStyle("-fx-border-color: red;");
                precioVentaValido.set(false);
            } else {
                txtPrecioV.setStyle("");
                precioVentaValido.set(true);
            }
        }));


        btnActualizar.disableProperty().bind(
                (txtCodBarras.textProperty().isEmpty())
                        .or(codBarrasValido.not())
                        .or(txtDot.textProperty().isEmpty())
                        .or(dotValido.not())
                        .or(txtMarca.textProperty().isEmpty())
                        .or(txtMedida.textProperty().isEmpty())
                        .or(medidaValido.not())
                        .or(cbIndiceCarga.valueProperty().isNull())
                        .or(cbIndiceVelocidad.valueProperty().isNull())
                        .or(txtStock.textProperty().isEmpty())
                        .or(stockValido.not())
                        .or(txtPrecioC.textProperty().isEmpty())
                        .or(precioCompraValido.not())
                        .or(txtPrecioV.textProperty().isEmpty())
                        .or(precioVentaValido.not())

        );

    }//configurarValidaciones

    @FXML
    private void actualizarInventario() {

        boolean confirmar = mostrarConfirmacion("Confirmar actualización",
                "¿Desea guardar los cambios seleccionados?",
                "Se actualizarán los datos del inventario seleccionado.",
                "Sí, guardar",
                "Cancelar");

        if (confirmar) {
            inventario.setCodigoBarras(txtCodBarras.getText().trim());
            inventario.setDot(txtDot.getText().trim());
            inventario.setMarca(txtMarca.getText().trim());
            inventario.setModelo(txtModelo.getText().trim());
            inventario.setMedida(txtMedida.getText().replaceAll("\\s", ""));
            inventario.setIndiceCarga(cbIndiceCarga.getValue());
            inventario.setIndiceVelocidad(cbIndiceVelocidad.getValue());
            inventario.setStock(Integer.parseInt(txtStock.getText()));
            inventario.setPrecioCompra(Float.parseFloat(txtPrecioC.getText()));
            inventario.setPrecioVenta(Float.parseFloat(txtPrecioV.getText()));
            inventario.setObservaciones(txtObservaciones.getText());
            inventario.setImagen(txtImg.getText());

            if (Integer.parseInt(txtStock.getText()) == 0)
                inventario.setActive(StatusInventario.SIN_STOCK.toString());

            else if (Integer.parseInt(txtStock.getText()) > 0)
                inventario.setActive(StatusInventario.ACTIVE.toString());


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fechaActualizacion = LocalDateTime.now().format(formatter);

            try {
                inventarioService.actualizarUptatedAt(fechaActualizacion.toString(), inventario.getInventarioId());
                inventarioService.actualizarInventario(inventario);

                mostrarInformacion("Inventario actualizado", "", "Inventario se actualizo correctamente");
                cerrarVentana();

            } catch (Exception e) {
                e.printStackTrace();
                mostrarError("Error inesperado", "", "No se pudo actualizar el lemento seleccionado, vuelva a intentarlo más tarde.");
            }

        }


    }//actualizarInventario

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtCodBarras.getScene().getWindow();
        stage.close();

    }//cerrarVentana

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

            File file = new File(archivo.getAbsolutePath());
            Image image = new Image(file.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
            vBoxImg.getChildren().clear();
            vBoxImg.getChildren().add(imageView);
        }

    } //seleccionarImg

}//EditarInventario
