package com.mastertyres.fxControllers.cliente;


import com.mastertyres.cliente.model.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import static com.mastertyres.common.FechaUtils.formatearFecha;
import static com.mastertyres.common.FechaUtils.formatearFechaHora;


@Component
public class DetalleCliente {

    @FXML private Label txtNombre;
    @FXML private Label txtApellido;
    @FXML private Label txtSegundoApellido;
    @FXML private Label txtTelefono;
    @FXML private Label txtCorreo;
    @FXML private Label txtHobbie;
    @FXML private Label txtRFC;
    @FXML private Label txtDomicilio;
    @FXML private Label txtEstado;
    @FXML private Label txtCiudad;
    @FXML private Label txtGenero;
    @FXML private Label txtTipoCliente;
    @FXML private Label txtFechaCumple;
    @FXML private Label txtFechaRegistro;
    @FXML private Label txtFechaActualizacion;

    public void informacionCliente(Cliente cliente) {

        txtNombre.setText("Nombre: " + valorONull(cliente.getNombre()));
        txtApellido.setText("Primer apellido: " + valorONull(cliente.getApellido()));
        txtSegundoApellido.setText("Segundo apellido: " + valorONull(cliente.getSegundoApellido()));
        txtTelefono.setText("Teléfono: " + valorONull(cliente.getNumTelefono()));
        txtCorreo.setText("Correo: " + valorONull(cliente.getCorreo()));
        txtHobbie.setText("Hobbie: " + valorONull(cliente.getHobbie()));
        txtRFC.setText("RFC: " + valorONull(cliente.getRfc()));
        txtDomicilio.setText("Domicilio: " + valorONull(cliente.getDomicilio()));
        txtEstado.setText("Estado: " + valorONull(cliente.getEstado()));
        txtCiudad.setText("Ciudad: " + valorONull(cliente.getCiudad()));
        txtGenero.setText("Género: " + formatearGenero(cliente.getGenero()));
        txtTipoCliente.setText("Tipo de cliente: " + valorONull(cliente.getTipoCliente()));

        // FECHAS formateadas
        txtFechaCumple.setText("Fecha de cumpleaños: " + formatearFecha(cliente.getFechaCumple()));
        txtFechaRegistro.setText("Fecha de registro: " + formatearFechaHora(cliente.getCreated_at()));
        txtFechaActualizacion.setText("Fecha de última actualización: " + formatearFechaHora(cliente.getUpdated_at()));
    }



    private String valorONull(String valor) {
        return (valor == null || valor.isBlank()) ? "No especificado" : valor;
    }

    private String formatearGenero(String genero) {
        if (genero == null || genero.isBlank()) return "No especificado";

        genero = genero.trim().toUpperCase();

        switch (genero) {
            case "M":
                return "Masculino";
            case "F":
                return "Femenino";
            case "O":
                return "Otro";
            default:
                return "No especificado";
        }
    }



    @FXML
    private void cerrarVentana() {
        txtNombre.setText("Nombre:");
        txtApellido.setText("Primer apellido:");
        txtSegundoApellido.setText("Segundo Apellido:");
        txtTelefono.setText("Telefono:");
        txtHobbie.setText("Hobbie:");
        txtRFC.setText("RFC:");
        txtDomicilio.setText("Domicilio:");
        txtEstado.setText("Estado:");
        txtCiudad.setText("Ciudad:");
        txtGenero.setText("Genero:");
        txtTipoCliente.setText("Tipo de cliente:");
        txtFechaCumple.setText("Fecha de cumpleaños:");
        txtFechaRegistro.setText("Fecha de registro:");
        txtFechaActualizacion.setText("Fecha de ultima actualizacion:");
        txtCorreo.setText("Correo:");

        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

}
