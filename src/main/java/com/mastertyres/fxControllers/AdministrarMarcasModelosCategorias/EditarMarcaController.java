package com.mastertyres.fxControllers.AdministrarMarcasModelosCategorias;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.common.MensajesAlert;
import com.mastertyres.detalleCategoria.model.DetalleCategoria;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.services.MarcaService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EditarMarcaController {

    @FXML private  Button btnAgregar;

    @FXML
    private TextField txtMarca;

    private Marca marcaSeleccionada;

    @Autowired private MarcaService marcaService;

    private BooleanProperty MarcaValido = new SimpleBooleanProperty(true);

    @FXML
    private void initialize(){

        configurarValidaciones();
    }

    private void configurarValidaciones() {

        txtMarca.textProperty().addListener((obs, oldText, newText) -> {
            String texto = newText.toUpperCase();
            txtMarca.setText(texto);
            if (texto.isEmpty()) {
                MarcaValido.set(false);
                txtMarca.setStyle("-fx-border-color: red;");
            } else if (!texto.matches("^[A-Z0-9\\s\\p{Punct}]{1,20}$")) {
                MarcaValido.set(false);
                txtMarca.setStyle("-fx-border-color: red;");
            } else {
                MarcaValido.set(true);
                txtMarca.setStyle("");
            }
        });


        //Deshabilitar botón "Guardar" cuando NO haya cliente o la lista de vehículos esté vacía
        btnAgregar.disableProperty().bind(
                txtMarca.textProperty().isEmpty()
                        .or(MarcaValido.not())
        );
    }


    private void limpiarCamposVehiculo() {
        txtMarca.clear();
    }

    public void setMarcaModelos(Marca m) {
        this.marcaSeleccionada = m;
        txtMarca.setText(m.getNombreMarca());

    }

    @FXML
    private void GuardarCambios() {


        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar actualización",
                "¿Desea guardar los cambios en esta marca?",
                "Se actualizarán el nombre de la marca seleccionada.",
                "Sí, guardar",
                "Cancelar"
        );

        if (confirmar){
            try {
                marcaSeleccionada.setNombreMarca(txtMarca.getText());
                marcaService.guardarMarca(marcaSeleccionada);
                MensajesAlert.mostrarInformacion("Éxito", "Marca actualizada", "La marca se actualizó correctamente.");
                cerrarVentana();
            } catch (Exception e) {
                MensajesAlert.mostrarError(
                        "Error al actualizar",
                        "No se pudo actualizar la marca",
                        "Detalles: " + e.getMessage()
                );
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void cerrarVentana() {
        limpiarCamposVehiculo();
        Stage stage = (Stage) txtMarca.getScene().getWindow();
        stage.close();
    }
}

