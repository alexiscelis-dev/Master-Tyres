package com.mastertyres.fxControllers.cliente;


import com.mastertyres.cliente.model.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;


@Component
public class DetalleCliente {

    @FXML private Label txtNombre;
    @FXML private Label txtApellido;
    @FXML private Label txtSegundoApellido;
    @FXML private Label txtTelefono;
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

    public void informacionCliente(Cliente clienteSeleccionado){

        txtNombre.setText(txtNombre.getText() + " " + (clienteSeleccionado.getNombre() != null ? clienteSeleccionado.getNombre()  : ""));
        txtApellido.setText(txtApellido.getText() + " " + (clienteSeleccionado.getApellido() != null ? clienteSeleccionado.getApellido() : ""));
        txtSegundoApellido.setText(txtSegundoApellido.getText() + " " + (clienteSeleccionado.getSegundoApellido() != null ? clienteSeleccionado.getSegundoApellido() : ""));
        txtTelefono.setText(txtTelefono.getText() + " " + (clienteSeleccionado.getNumTelefono() != null ? clienteSeleccionado.getNumTelefono() : ""));
        txtHobbie.setText(txtHobbie.getText() + " " + (clienteSeleccionado.getHobbie() != null ? clienteSeleccionado.getHobbie() : "" ));
        txtRFC.setText(txtRFC.getText() + " " + (clienteSeleccionado.getRfc() != null ? clienteSeleccionado.getRfc() : ""));
        txtDomicilio.setText(txtDomicilio.getText() + " " + (clienteSeleccionado.getDomicilio() != null ? clienteSeleccionado.getDomicilio() : ""));
        txtEstado.setText(txtEstado.getText() + " " + (clienteSeleccionado.getEstado() != null ?  clienteSeleccionado.getEstado() : ""));
        txtCiudad.setText(txtCiudad.getText() + " " + (clienteSeleccionado.getCiudad() != null ?clienteSeleccionado.getCiudad() : ""));
        txtGenero.setText(txtGenero.getText() + " " + (clienteSeleccionado.getGenero() != null ? clienteSeleccionado.getGenero(): ""));
        txtTipoCliente.setText(txtTipoCliente.getText() + " " + (clienteSeleccionado.getTipoCliente() != null ? clienteSeleccionado.getTipoCliente()  : ""));
        txtFechaCumple.setText(txtFechaCumple.getText() + " " + (clienteSeleccionado.getFechaCumple() != null ? clienteSeleccionado.getFechaCumple() : ""));
        txtFechaRegistro.setText(txtFechaRegistro.getText() + " " + (clienteSeleccionado.getCreated_at() != null ? clienteSeleccionado.getCreated_at()  : ""));
        txtFechaActualizacion.setText(txtFechaActualizacion.getText() + " " + (clienteSeleccionado.getUpdated_at() != null ? clienteSeleccionado.getUpdated_at(): ""));

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

        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

}
