package com.mastertyres.fxControllers.AdministrarMarcasModelosCategorias;

import com.mastertyres.common.exeptions.MarcaException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoading;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.fxComponents.LoadingComponentController;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.service.MarcaService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;

@Component
public class EditarMarcaController implements IFxController, ILoading {

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


        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar actualización",
                "¿Desea guardar los cambios en esta marca?",
                "Se actualizarán el nombre de la marca seleccionada.",
                "Sí, guardar",
                "Cancelar"
        );

        if (confirmar) {

            taskService.runTask(
                    loadingOverlayController,
                    ()->{
                        marcaSeleccionada.setNombreMarca(txtMarca.getText());
                        marcaService.guardarMarca(marcaSeleccionada);
                        return null;

                    }, (resultado)->{
                        MensajesAlert.mostrarInformacion("Marca actualizada", "", "La marca se actualizó correctamente.");
                        cerrarVentana();

            },(ex) ->{

                        if (ex instanceof MarcaException){
                            mostrarError("Error al actualzar", "Ocurrio un problema al guardar los cambios", "" + ex.getMessage());
                        }else {
                            mostrarError("Error interno", "","Ocurrio un error inesperado al intentar guardar los cambios. Vuelva a intentarlo mas tarde.");

                        }

                    },null
            );

        }
    }

    @FXML
    private void cerrarVentana() {
        limpiarCamposVehiculo();
        Stage stage = (Stage) txtMarca.getScene().getWindow();
        stage.close();
    }

}//class

