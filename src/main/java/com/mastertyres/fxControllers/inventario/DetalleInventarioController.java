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
public class DetalleInventarioController {
    @FXML private Label lblCodBarras;
    @FXML private Label lblDot;
    @FXML private Label lblMarca;
    @FXML private Label lblModelo;
    @FXML private Label lblMedida;
    @FXML private Label lblIdCarga;
    @FXML private Label lblIdVelocidad;
    @FXML private Label lblStock;
    @FXML private Label lblPrecioC;
    @FXML private Label lblPrecioV;
    @FXML private Label lblObservaciones;
    @FXML private Label lblFechaRegistro;
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

        lblCodBarras.setText(lblCodBarras.getText() + " " + valorONull(inventario.getCodigoBarras()));
        lblDot.setText(lblDot.getText() + " " + valorONull(inventario.getDot()));
        lblMarca.setText(lblMarca.getText() + " " + valorONull(inventario.getMarca()));
        lblModelo.setText(lblModelo.getText() + " " + valorONull(inventario.getModelo()));
        lblMedida.setText(lblMedida.getText() + " " + valorONull(inventario.getMedida()));
        lblIdCarga.setText(lblIdCarga.getText() + " " + valorONull(inventario.getIndiceCarga()));
        lblIdVelocidad.setText(lblIdVelocidad.getText() + " " + valorONull(inventario.getIndiceVelocidad()));
        lblStock.setText(lblStock.getText() + " " + valorONull(inventario.getStock()+""));
        lblPrecioC.setText(lblPrecioC.getText() + " $" + valorONull(inventario.getPrecioCompra()+""));
        lblPrecioV.setText(lblPrecioV.getText() + " $" + valorONull(inventario.getPrecioVenta()+""));
        lblObservaciones.setText(lblObservaciones.getText() + " " + valorONull(inventario.getObservaciones()));
        lblFechaRegistro.setText(lblFechaRegistro.getText() + " " + formatearFechaHora(inventario.getCreated_at()));

        File file = new File(inventario.getImagen());
        Image image;
        if (file != null && file.exists())
            image = new Image(file.toURI().toString());
        else
            image = new Image(getClass().getResource("/icons/imagenPorDefecto.jpg").toExternalForm());


        imgInventario.setImage(image);


    }//detalleInventario


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
        lblCodBarras.setText("Codigo de barras:");
        lblDot.setText("Dot:");
        lblMarca.setText("Marca:");
        lblModelo.setText("Modelo:");
        lblMedida.setText("Medida:");
        lblIdCarga.setText("Indice de carga:");
        lblIdVelocidad.setText("Indice de velocidad:");
        lblStock.setText("Stock:");
        lblPrecioC.setText("Precio de compra:");
        lblPrecioV.setText("Precio de venta:");
        lblObservaciones.setText("Observaciones:");
        lblFechaRegistro.setText("Fecha de registro:");

        Stage stage = (Stage) btnCerrar.getScene().getWindow(); //se puede utilizar cualquier campo el caso es optener la ventana en la que se esta
        //para poder cerrarla
        stage.close();

    }//cerrarVentana

}//clase
