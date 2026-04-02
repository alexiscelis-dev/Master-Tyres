package com.mastertyres.controllers.fxControllers.AdministrarMarcasModelosCategorias;

import com.mastertyres.common.exeptions.MarcaException;
import com.mastertyres.common.interfaces.ICleanable;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.marca.entity.Marca;
import com.mastertyres.marca.service.MarcaService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.mastertyres.common.utils.MensajesAlert.*;

@Component
public class EditarMarcaController implements IFxController, ILoader, ICleanable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button btnAgregar;
    @FXML
    private TextField txtMarca;

    private Marca marcaSeleccionada;

    private LoadingComponentController loadingOverlayController;

    @Autowired
    private MarcaService marcaService;
    @Autowired
    private TaskService taskService;


    private BooleanProperty MarcaValido = new SimpleBooleanProperty(true);

    @FXML
    private void initialize() {

        configuraciones();
        listeners();

    }//initialize

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }

    @Override
    public void configuraciones() {

    }//configuraciones

    @Override
    public void listeners() {
        configurarValidaciones();
    }//listeners

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


        boolean confirmar = mostrarConfirmacion(
                "Confirmar actualización",
                "Guardar cambios",
                "¿Está seguro de que desea guardar los cambios en esta marca? Se actualizará el nombre de la marca seleccionada.",
                "Sí, guardar",
                "Cancelar"
        );

        if (confirmar) {

            taskService.disable(rootPane);

            taskService.runTask(
                    loadingOverlayController,
                    ()->{
                        marcaSeleccionada.setNombreMarca(txtMarca.getText());
                        marcaService.guardarMarca(marcaSeleccionada);
                        return null;

                    }, (resultado)->{
                        taskService.enable(rootPane);

                        mostrarInformacion(
                                "Operación completada",
                                "Registro actualizado",
                                "La información de la marca ha sido actualizada en el sistema correctamente."
                        );
                        cerrarVentana();

            },(ex) ->{
                        taskService.enable(rootPane);


                        if (ex instanceof MarcaException) {
                            mostrarError(
                                    "Error al actualizar",
                                    "Problema con el registro de la marca",
                                    "" + ex.getMessage());
                        } else {

                            mostrarExcepcionThrowable(
                                    "Error inesperado",
                                    "No se pudo completar la operación",
                                    "Ocurrió un error inesperado al intentar guardar los cambios. Por favor, inténtelo de nuevo más tarde.",
                                    ex
                            );
                        }

                    },null
            );

        }
    }

    @FXML
    private void cerrarVentana() {
        limpiarCamposVehiculo();
        cleanup();
        Stage stage = (Stage) txtMarca.getScene().getWindow();
        stage.close();
    }

    @Override
    public void cleanup() {

        // 2. Nullificar objeto seleccionado
        marcaSeleccionada = null;

        // 3. Limpiar campo de texto
        if (txtMarca != null) {
            txtMarca.clear();
        }
    }

}//class

