package com.mastertyres.fxControllers.cliente;


import com.mastertyres.cliente.model.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;


@Component
public class detalleCliente {

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

    public void setCliente(Cliente clienteSeleccionado){

        txtNombre.setText(txtNombre.getText() + " " + clienteSeleccionado.getNombre());
        txtApellido.setText(txtApellido.getText() + " " + clienteSeleccionado.getApellido());
        txtSegundoApellido.setText(txtSegundoApellido.getText() + " " + clienteSeleccionado.getSegundoApellido());
        txtTelefono.setText(txtTelefono.getText() + " " + clienteSeleccionado.getNumTelefono());
        txtHobbie.setText(txtHobbie.getText() + " " + clienteSeleccionado.getHobbie());
        txtRFC.setText(txtRFC.getText() + " " + clienteSeleccionado.getRfc());
        txtDomicilio.setText(txtDomicilio.getText() + " " + clienteSeleccionado.getDomicilio());
        txtEstado.setText(txtEstado.getText() + " " + clienteSeleccionado.getEstado());
        txtCiudad.setText(txtCiudad.getText() + " " + clienteSeleccionado.getCiudad());
        txtGenero.setText(txtGenero.getText() + " " + clienteSeleccionado.getGenero());
        txtTipoCliente.setText(txtTipoCliente.getText() + " " + clienteSeleccionado.getTipoCliente());
        txtFechaCumple.setText(txtFechaCumple.getText() + " " + clienteSeleccionado.getFechaCumple());
        txtFechaRegistro.setText(txtFechaRegistro.getText() + " " + clienteSeleccionado.getCreated_at());
        txtFechaActualizacion.setText(txtFechaActualizacion.getText() + " " + clienteSeleccionado.getUpdated_at());

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
