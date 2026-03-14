package com.mastertyres.controllers.fxControllers.vehiculo;

import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.mastertyres.common.utils.FechaUtils.formatearFecha;
import static com.mastertyres.common.utils.FechaUtils.formatearFechaHora;

@Component
public class detalleVehiculo {

    @FXML private Label txtMarca;
    @FXML private Label txtModelo;
    @FXML private Label txtannio;
    @FXML private Label txtCategoria;
    @FXML private Label txtKilometros;
    @FXML private Label txtColor;
    @FXML private Label txtPlacas;
    @FXML private Label txtNumeroSerie;
    @FXML private Label txtObservaciones;
    @FXML private Label txtUltimoServicio;
    @FXML private Label txtFechaRegistro;
    @FXML private Label txtFechaActualizacion;

    @Autowired
    private VehiculoService vehiculoService;

    public void setVehiculo(VehiculoDTO vehiculoSeleccionado){

        Vehiculo v = vehiculoService.Vehiculo_SinDTO(vehiculoSeleccionado.getId());

        txtMarca.setText(txtMarca.getText() + " " + valorONull(vehiculoSeleccionado.getNombreMarca()));
        txtModelo.setText(txtModelo.getText() + " " + valorONull(vehiculoSeleccionado.getNombreModelo()));
        txtannio.setText(txtannio.getText() + " " + valorONull(vehiculoSeleccionado.getAnio().toString()));
        txtCategoria.setText(txtCategoria.getText() + " " + valorONull(vehiculoSeleccionado.getNombreCategoria()));
        txtKilometros.setText(txtKilometros.getText() + " " + valorONull(vehiculoSeleccionado.getKilometros().toString()));
        txtColor.setText(txtColor.getText() + " " + valorONull(vehiculoSeleccionado.getColor()));
        txtPlacas.setText(txtPlacas.getText() + " " + valorONull(vehiculoSeleccionado.getPlacas()));
        txtNumeroSerie.setText(txtNumeroSerie.getText() + " " + valorONull(vehiculoSeleccionado.getNumSerie()));
        txtObservaciones.setText(txtObservaciones.getText() + " " + valorONull(vehiculoSeleccionado.getObservaciones()));
        txtUltimoServicio.setText(txtUltimoServicio.getText() + " " + formatearFecha(vehiculoSeleccionado.getUltimoServicio()));

        DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaStr = vehiculoSeleccionado.getFechaRegistro();
        LocalDate fecha = LocalDate.parse(fechaStr, formatterEntrada);
        String texto = fecha.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));  //mostrar solo fecha sin la hora del registro
        txtFechaRegistro.setText(txtFechaRegistro.getText() + " " + texto);

        txtFechaActualizacion.setText(txtFechaActualizacion.getText() + " " + formatearFechaHora(v.getUpdated_at()));

    }

    private String valorONull(String valor) {
        return (valor == null || valor.isBlank()) ? "No especificado" : valor;
    }

    @FXML
    private void cerrarVentana() {
        txtMarca.setText("Marca:");
        txtModelo.setText("Modelo:");
        txtannio.setText("Año:");
        txtCategoria.setText("Categoria:");
        txtKilometros.setText("Kilometros:");
        txtColor.setText("Color:");
        txtPlacas.setText("Placas:");
        txtNumeroSerie.setText("Numero de serie:");
        txtObservaciones.setText("Observaciones:");
        txtUltimoServicio.setText("Fecha del ultimo servicio:");
        txtFechaRegistro.setText("Fecha de registro:");
        txtFechaActualizacion.setText("Fecha de la ultima actualizacion:");

        Stage stage = (Stage) txtMarca.getScene().getWindow();
        stage.close();
    }


}//class
