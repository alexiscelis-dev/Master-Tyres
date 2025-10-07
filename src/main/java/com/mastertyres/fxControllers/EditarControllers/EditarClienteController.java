package com.mastertyres.fxControllers.EditarControllers;


import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.MensajesAlert;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EditarClienteController {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtSegundoApellido;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtHobbie;
    @FXML
    private TextField txtRfc;
    @FXML
    private TextField txtDomicilio;
    @FXML
    private TextField txtEstado;
    @FXML
    private TextField txtCiudad;
    @FXML
    private ChoiceBox<String> choiceGenero;
    @FXML
    private ChoiceBox<String> choiceTipoCliente;
    @FXML
    private DatePicker dateCumpleanos;
    @FXML
    private Button btnCambiar;

    private Cliente cliente;

    @Autowired
    private ClienteService clienteService;

    //Validaciones:
    private BooleanProperty rfcValido = new SimpleBooleanProperty(true);
    private BooleanProperty telefonoValido = new SimpleBooleanProperty(true);
    private BooleanProperty nombreValido = new SimpleBooleanProperty(true);
    private BooleanProperty apellidoValido = new SimpleBooleanProperty(true);

    public void initialize() {

        configurarValidaciones();
    }

    private void configurarValidaciones() {

        // NOMBRE
        txtNombre.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isBlank()) {
                nombreValido.set(false);
                txtNombre.setStyle("-fx-border-color: red;");
            } else {
                nombreValido.set(true);
                txtNombre.setStyle("");
            }
        });
        txtNombre.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // pierde foco
                txtNombre.setText(formatoOracion(txtNombre.getText()));
            }
        });

// APELLIDO
        txtApellido.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.isBlank()) {
                apellidoValido.set(false);
                txtApellido.setStyle("-fx-border-color: red;");
            } else {
                apellidoValido.set(true);
                txtApellido.setStyle("");
            }
        });
        txtApellido.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                txtApellido.setText(formatoOracion(txtApellido.getText()));
            }
        });

// SEGUNDO APELLIDO (no obligatorio, no borde rojo)
        txtSegundoApellido.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                txtSegundoApellido.setText(formatoOracion(txtSegundoApellido.getText()));
            }
        });

        //Ciudad
        txtCiudad.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                txtCiudad.setText(formatoOracion(txtCiudad.getText()));
            }
        });

        //Estado
        txtEstado.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                txtEstado.setText(formatoOracion(txtEstado.getText()));
            }
        });

        //Domicilio
        txtDomicilio.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                txtDomicilio.setText(formatoOracion(txtDomicilio.getText()));
            }
        });

        // Teléfono
        txtTelefono.textProperty().addListener((obs, oldText, newText) -> {
            if (newText != null && !newText.isBlank() && newText.matches("^\\+?[\\d\\s\\-()]{7,20}$")) {
                // Válido: no vacío y cumple formato
                telefonoValido.set(true);
                txtTelefono.setStyle("");
            } else {
                telefonoValido.set(false);
                txtTelefono.setStyle("-fx-border-color: red;");
            }
        });
        txtTelefono.setTextFormatter(new TextFormatter<>(c -> {
            if (c.getControlNewText().matches("[\\d\\s\\-()+]*")) {
                return c;
            }
            return null;
        }));

        // RFC: opcional, acepta minúsculas
        txtRfc.textProperty().addListener((obs, oldText, newText) -> {
            String texto = newText.toUpperCase(); // convertir a mayúsculas para la validación
            txtRfc.setText(texto); // actualiza el campo visualmente en mayúsculas
            if (texto.isEmpty()) {
                rfcValido.set(true);
                txtRfc.setStyle("");
            } else if (!texto.matches("^([A-Z,Ñ,&]{3,4}([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[A-Z\\d]{3})$")) {
                rfcValido.set(false);
                txtRfc.setStyle("-fx-border-color: red;");
            } else {
                rfcValido.set(true);
                txtRfc.setStyle("");
            }
        });

        // Deshabilitar botón "Guardar" hasta que se llenen los campos requeridos y haya al menos un vehículo
        btnCambiar.disableProperty().bind(
                (txtNombre.textProperty().isEmpty())
                        .or(txtApellido.textProperty().isEmpty())
                        .or(txtTelefono.textProperty().isEmpty())
                        .or(choiceTipoCliente.valueProperty().isNull())
                        .or(rfcValido.not())
                        .or(telefonoValido.not())

        );

    }

    private String formatoOracion(String texto) {
        if (texto == null || texto.isBlank()) return "";
        String[] palabras = texto.trim().split("\\s+");
        StringBuilder resultado = new StringBuilder();
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                resultado.append(palabra.substring(0, 1).toUpperCase())
                        .append(palabra.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return resultado.toString().trim();
    }

    public void editarCliente(Cliente cliente) {
        this.cliente = cliente;

        // Prellenar campos con los valores del cliente
        txtNombre.setText(cliente.getNombre());
        txtApellido.setText(cliente.getApellido());
        txtSegundoApellido.setText(cliente.getSegundoApellido());
        txtTelefono.setText(cliente.getNumTelefono());
        txtHobbie.setText(cliente.getHobbie());
        txtRfc.setText(cliente.getRfc());
        txtDomicilio.setText(cliente.getDomicilio());
        txtEstado.setText(cliente.getEstado());
        txtCiudad.setText(cliente.getCiudad());

        choiceGenero.getItems().setAll("Masculino", "Femenino", "Otro");
        choiceGenero.setValue(cliente.getGenero());

        choiceTipoCliente.getItems().setAll("INDIVIDUAL", "EMPRESA");
        if (cliente.getTipoCliente() != null) {
            choiceTipoCliente.setValue(cliente.getTipoCliente().toString());
        }

        if (cliente.getFechaCumple() != null && !cliente.getFechaCumple().isBlank()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fecha = LocalDate.parse(cliente.getFechaCumple(), formatter);
            dateCumpleanos.setValue(fecha);
        }


    }//editarCliente

    @FXML
    private void actualizarCliente() {

        if (cliente == null) {
            MensajesAlert.mostrarError(
                    "Error",
                    "No hay cliente seleccionado",
                    "Debe seleccione un cliente antes de poder modificarlo."
            );
            return;
        }


        if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {
            MensajesAlert.mostrarWarning("Validación", "Campo requerido", "El nombre del cliente no puede estar vacío.");
            return;
        }

        if (txtTelefono.getText() == null || txtTelefono.getText().trim().isEmpty()) {
            MensajesAlert.mostrarWarning("Validación", "Campo requerido", "El numero de telefono del cliente no puede estar vacío.");
            return;
        }

        if (txtApellido.getText() == null || txtApellido.getText().trim().isEmpty()) {
            MensajesAlert.mostrarWarning("Validación", "Campo requerido", "El primer apellido del cliente no puede estar vacío.");
            return;
        }

        if (txtTelefono.getText() == null || txtTelefono.getText().trim().isEmpty()) {
            MensajesAlert.mostrarWarning("Validación", "Campo requerido", "El telefono del cliente no puede estar vacío.");
            return;
        }


        if (choiceTipoCliente.getValue() == null) {
            MensajesAlert.mostrarWarning("Validación", "Campo requerido", "Debe seleccionar un tipo de cliente.");
            return;
        }

        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar actualización",
                "¿Desea guardar los cambios en este cliente?",
                "Se actualizarán los datos del cliente seleccionado.",
                "Sí, guardar",
                "Cancelar"
        );

        if (confirmar) {

            try {
                // 🔹 Actualizar datos generales
                cliente.setNombre(txtNombre.getText().trim());
                cliente.setApellido(txtApellido.getText().trim());
                cliente.setSegundoApellido(txtSegundoApellido.getText().trim());
                cliente.setTipoCliente(choiceTipoCliente.getValue());
                cliente.setHobbie(txtHobbie.getText().trim());
                cliente.setEstado(txtEstado.getText().trim());
                cliente.setCiudad(txtCiudad.getText().trim());
                cliente.setDomicilio(txtDomicilio.getText().trim());
                cliente.setFechaCumple(dateCumpleanos.getValue().toString());
                cliente.setRfc(txtRfc.getText().trim());
                cliente.setNumTelefono(txtTelefono.getText().trim());

                if (choiceGenero.getValue().equals("Masculino")) {
                    cliente.setGenero("M");
                } else if (choiceGenero.getValue().equals("Femenino")) {
                    cliente.setGenero("F");
                } else {
                    cliente.setGenero("O");
                }
                cliente.setUpdated_at(LocalDateTime.now().toString());

                // 🔹 Guardar cambios en la promoción
                clienteService.guardarCliente(cliente);


                MensajesAlert.mostrarInformacion("Éxito", "Cliente actualizado", "El cliente se actualizó correctamente.");
                cerrarVentana();

            } catch (Exception e) {
                // Cualquier error en la BD o lógica cae aquí
                MensajesAlert.mostrarError(
                        "Error al actualizar",
                        "No se pudo actualizar el cliente",
                        "Detalles: " + e.getMessage()
                );
                e.printStackTrace();
            }//try-catch

        }//if

    }//actualizarCliente


    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
}