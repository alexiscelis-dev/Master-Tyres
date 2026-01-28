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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DetalleInventarioController  {
    @FXML private Label txtCodBarras;
    @FXML private Label txtDot;
    @FXML private Label txtMarca;
    @FXML private Label txtModelo;
    @FXML private Label txtMedida;
    @FXML private Label txtIdCarga;
    @FXML private Label txtIdVelocidad;
    @FXML private Label txtStock;
    @FXML private Label txtPrecioC;
    @FXML private Label txtPrecioV;
    @FXML private Label txtObservaciones;
    @FXML private Label txtFechaRegistro;
    @FXML private Button btnCerrar;
    @FXML private ImageView imgInventario;
    @FXML private ScrollPane scrollPane;

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            scrollPane.setVvalue(0);
            scrollPane.setHvalue(0);
        });

    }//initialize

    public void InformacionInventario(Inventario inventario) {

        txtCodBarras.setText(txtCodBarras.getText() + " " + valorONull(inventario.getCodigoBarras()));
        txtDot.setText(txtDot.getText() + " " + valorONull(inventario.getDot()));
        txtMarca.setText(txtMarca.getText() + " " + valorONull(inventario.getMarca()));
        txtModelo.setText(txtModelo.getText() + " " + valorONull(inventario.getModelo()));
        txtMedida.setText(txtMedida.getText() + " " + valorONull(inventario.getMedida()));
        txtIdCarga.setText(txtIdCarga.getText() + " " + valorONull(inventario.getIndiceCarga()));
        txtIdVelocidad.setText(txtIdVelocidad.getText() + " " + valorONull(inventario.getIndiceVelocidad()));
        txtStock.setText(txtStock.getText() + " " + valorONull(String.valueOf(inventario.getStock())));
        txtPrecioC.setText(txtPrecioC.getText() + " $" + valorONull(String.valueOf(inventario.getPrecioCompra())));
        txtPrecioV.setText(txtPrecioV.getText() + " $" + valorONull(String.valueOf(inventario.getPrecioVenta())));
        txtObservaciones.setText(txtObservaciones.getText() + " " + valorONull(inventario.getObservaciones()));
        txtFechaRegistro.setText(txtFechaRegistro.getText() + " " + formatearFechaHora(inventario.getCreated_at()));

        String rutaImagen = inventario.getImagen();
        Image image;

        if (rutaImagen != null && !rutaImagen.isBlank()) {
            File file = new File(rutaImagen);
            if (file.exists()) {
                image = new Image(file.toURI().toString());
            } else {
                image = new Image(getClass().getResource("/icons/imagenPorDefecto.jpg").toExternalForm());
            }
        } else {
            image = new Image(getClass().getResource("/icons/imagenPorDefecto.jpg").toExternalForm());
        }

        imgInventario.setImage(image);
    }


    private String formatearFecha(String fecha) {
        if (fecha == null || fecha.isBlank()) return "No especificado";

        try {
            LocalDate f = LocalDate.parse(fecha); // Para formato yyyy-MM-dd
            return f.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (Exception e) {
            return "No especificado";
        }
    }

    private String formatearFechaHora(String fechaHora) {
        if (fechaHora == null || fechaHora.isBlank()) return "No especificado";

        try {
            LocalDateTime f = LocalDateTime.parse(fechaHora.replace(" ", "T"));
            return f.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (Exception e) {
            return "No especificado";
        }
    }

    private String valorONull(String valor) {
        return (valor == null || valor.isBlank()) ? "No especificado" : valor;
    }

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
