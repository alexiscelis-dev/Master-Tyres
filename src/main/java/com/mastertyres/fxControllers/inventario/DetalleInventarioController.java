package com.mastertyres.fxControllers.inventario;

import com.mastertyres.inventario.model.Inventario;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DetalleInventarioController {
    @FXML
    private Label txtCodBarras;
    @FXML
    private Label txtDot;
    @FXML
    private Label txtMarca;
    @FXML
    private Label txtModelo;
    @FXML
    private Label txtMedida;
    @FXML
    private Label txtIdCarga;
    @FXML
    private Label txtIdVelocidad;
    @FXML
    private Label txtStock;
    @FXML
    private Label txtPrecioC;
    @FXML
    private Label txtPrecioV;
    @FXML
    private Label txtObservaciones;
    @FXML
    private Label txtFechaRegistro;
    @FXML
    private Button btnCerrar;
    @FXML
    private ImageView imgInventario;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            scrollPane.setVvalue(0);
            scrollPane.setHvalue(0);
        });

    }//initialize

    public void InformacionInventario(Inventario inventario) {

        txtCodBarras.setText(txtCodBarras.getText() + " " + (inventario.getCodigoBarras() != null ? inventario.getCodigoBarras() : ""));
        txtDot.setText(txtDot.getText() + " " + (inventario.getDot() != null ? inventario.getDot() : ""));
        txtMarca.setText(txtMarca.getText() + " " + (inventario.getMarca() != null ? inventario.getMarca() : ""));
        txtModelo.setText(txtModelo.getText() + " " + (inventario.getModelo() != null ? inventario.getModelo() : ""));
        txtMedida.setText(txtMedida.getText() + " " + (inventario.getMedida() != null ? inventario.getMedida() : ""));
        txtIdCarga.setText(txtIdCarga.getText() + " " + (inventario.getIndiceCarga() != null ? inventario.getIndiceCarga() : ""));
        txtIdVelocidad.setText(txtIdVelocidad.getText() + " " + (inventario.getIndiceVelocidad() != null ? inventario.getIndiceVelocidad() : ""));
        txtStock.setText(txtStock.getText() + " " + inventario.getStock());
        txtPrecioC.setText(txtPrecioC.getText() + " $" + inventario.getPrecioCompra());
        txtPrecioV.setText(txtPrecioV.getText() + " $" + inventario.getPrecioVenta());
        txtObservaciones.setText(txtObservaciones.getText() + " " + (inventario.getObservaciones() != null ? inventario.getObservaciones() : ""));
        txtFechaRegistro.setText(txtFechaRegistro.getText() + " " + (inventario.getCreated_at() != null ? inventario.getCreated_at() : ""));

        File file = new File(inventario.getImagen());
        Image image;
        if (file != null && file.exists())
            image = new Image(file.toURI().toString());
        else
            image = new Image(getClass().getResource("/icons/imagenPorDefecto.jpg").toExternalForm());


        imgInventario.setImage(image);


    }//detalleInventario

    @FXML
    private void cerrarVentana() {
        txtCodBarras.setText("Codigo de barras:");
        txtDot.setText("Dot:");
        txtMarca.setText("Marca:");
        txtModelo.setText("Modelo:");
        txtMedida.setText("Medida:");
        txtIdCarga.setText("Indice de carga:");
        txtIdVelocidad.setText("Indice de velocidad:");
        txtStock.setText("Stock:");
        txtPrecioC.setText("Precio de compra:");
        txtPrecioV.setText("Precio de venta:");
        txtObservaciones.setText("Observaciones:");
        txtFechaRegistro.setText("Fecha de registro:");

        Stage stage = (Stage) txtCodBarras.getScene().getWindow(); //se puede utilizar cualquier campo el caso es optener la ventana en la que se esta
        //para poder cerrarla
        stage.close();

    }//cerrarVentana

}//clase
