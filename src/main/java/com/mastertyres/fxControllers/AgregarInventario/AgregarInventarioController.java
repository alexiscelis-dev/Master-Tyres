package com.mastertyres.fxControllers.AgregarInventario;

import com.mastertyres.common.MenuContextSetting;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.inventario.service.InventarioService;
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
    @Autowired
    InventarioService inventarioService;


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

        txtPrecioC.setTextFormatter(new TextFormatter<>(texto -> {
            if (texto.getControlNewText().matches("\\d*(\\.\\d{0,2})?"))
                return texto;
            else
                return null;
        }));
        txtPrecioV.setTextFormatter(new TextFormatter<>(texto -> {
            if (texto.getControlNewText().matches("\\d*(\\.\\d{0,2})?"))
                return texto;
            else
                return null;

        }));
        txtStock.setTextFormatter(new TextFormatter<>(texto -> {
            if (texto.getControlNewText().matches("\\d*"))
                return texto;
            else
                return null;
        }));
        txtCodBarras.setTextFormatter(new TextFormatter<>(texto -> {
            if (texto.getControlNewText().matches("\\d*"))
                return texto;
            else
                return null;
        }));
        btnRegistrar.setOnAction(event -> registrar());
        btnImagen.setOnAction(event -> seleccionarImg());

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

        txtCodBarras.setStyle("-fx-border-color:#444444; -fx-border-width:2px;");
        txtDot.setStyle("-fx-border-color:#444444; -fx-border-width:2px;");
        txtMarca.setStyle("-fx-border-color:#444444; -fx-border-width:2px;");
        txtMedida1.setStyle("-fx-border-color:#444444; -fx-border-width:2px;");
        txtMedida2.setStyle("-fx-border-color:#444444; -fx-border-width:2px;");
        txtPrecioC.setStyle("-fx-border-color:#444444; -fx-border-width:2px;");
        txtPrecioV.setStyle("-fx-border-color:#444444; -fx-border-width:2px;");




    }//clean

    private void registrar() {

        String medida = txtMedida1.getText().replaceAll("\\s", "") + "/" + txtMedida2.getText().replaceAll("\\s", "");
        String indiceCarga = "", indiceVelocidad = "";

        if (cbIndiceCarga.getValue() != null)
            indiceCarga = cbIndiceCarga.getValue();
        else
            indiceCarga = "";

        if (cbIndiceVelocidad.getValue() != null)
            indiceVelocidad = cbIndiceVelocidad.getValue();
        else
            indiceVelocidad = "";


        if (!empty()) {

            Inventario inventario = Inventario.builder()
                    .codigoBarras(txtCodBarras.getText())
                    .dot(txtDot.getText())
                    .marca(txtMarca.getText())
                    .modelo(txtModelo.getText())
                    .medida(medida)
                    .indiceCarga(indiceCarga)
                    .indiceVelocidad(indiceVelocidad)
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
            } catch (Exception e) {
                mostrarError("Error al agregar elemento", "", "Ha ocurrido un error al agregar al inventario,vuelva a intentarlo mas tarde.");
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


}//class
