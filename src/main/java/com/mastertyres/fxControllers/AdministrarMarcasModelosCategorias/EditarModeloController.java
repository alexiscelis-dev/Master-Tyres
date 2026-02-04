package com.mastertyres.fxControllers.AdministrarMarcasModelosCategorias;


import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.categoria.service.CategoriaService;
import com.mastertyres.common.exeptions.ModeloException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoading;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.detalleCategoria.model.DetalleCategoria;
import com.mastertyres.detalleCategoria.service.DetalleCategoriaService;
import com.mastertyres.fxComponents.LoadingComponentController;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.modelo.service.ModeloService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;
import static com.mastertyres.common.utils.MensajesAlert.mostrarInformacion;

@Component
public class EditarModeloController implements IFxController, ILoading {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField txtModelo;
    @FXML
    private Button btnGuardar;
    @FXML
    private ChoiceBox<Categoria> choiceCategoria;

    private BooleanProperty ModeloValido = new SimpleBooleanProperty(true);

    private Modelo modeloSeleccionada;
    private DetalleCategoria detalleCategoriaSeleccionado;
    private LoadingComponentController loadingOverlayController;


    @Autowired
    private ModeloService modeloService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private DetalleCategoriaService detalleCategoriaService;
    @Autowired
    private TaskService taskService;

    @FXML
    private void initialize() {

        configuraciones();
        listeners();
        cargarCategorias();

    }//initialize

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }

    @Override
    public void configuraciones() {
        MenuContextSetting.disableMenu(rootPane);
    }

    @Override
    public void listeners() {
        configurarValidaciones();
    }

    private void cargarCategorias() {
        List<Categoria> categorias = categoriaService.listarCategorias();
        choiceCategoria.setItems(FXCollections.observableArrayList(categorias));
        choiceCategoria.setConverter(new StringConverter<>() {
            @Override
            public String toString(Categoria categoria) {
                return categoria != null ? categoria.getNombreCategoria() : "";
            }

            @Override
            public Categoria fromString(String string) {
                return null;
            }
        });
    }

    public void setModelos(DetalleCategoria m) {
        this.modeloSeleccionada = m.getModelo();
        this.detalleCategoriaSeleccionado = m;
        txtModelo.setText(modeloSeleccionada.getNombreModelo());
        choiceCategoria.getItems().stream()
                .filter(c -> c.getNombreCategoria().equals(m.getCategoria().getNombreCategoria()))
                .findFirst()
                .ifPresent(choiceCategoria::setValue);
    }

    private void configurarValidaciones() {

        txtModelo.textProperty().addListener((obs, oldText, newText) -> {
            String texto = newText.toUpperCase();
            txtModelo.setText(texto);
            if (texto.isEmpty()) {
                ModeloValido.set(false);
                txtModelo.setStyle("-fx-border-color: red;");
            } else if (!texto.matches("^[A-Z0-9\\s\\p{Punct}]{1,20}$")) {
                ModeloValido.set(false);
                txtModelo.setStyle("-fx-border-color: red;");
            } else {
                ModeloValido.set(true);
                txtModelo.setStyle("");
            }
        });

        //Deshabilitar botón "Guardar" cuando NO haya cliente o la lista de vehículos esté vacía
        btnGuardar.disableProperty().bind(
                txtModelo.textProperty().isEmpty()
                        .or(ModeloValido.not())
                        .or(choiceCategoria.valueProperty().isNull())
        );
    }

    private void limpiarCamposVehiculo() {
        txtModelo.clear();
    }

    @FXML
    private void cerrarVentana() {
        limpiarCamposVehiculo();
        Stage stage = (Stage) txtModelo.getScene().getWindow();
        stage.close();
    }

    public void GuardarCambios(ActionEvent actionEvent) {


        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar actualización",
                "¿Desea guardar los cambios en este Modelo?",
                "Se actualizarán el nombre del modelo seleccionada.",
                "Sí, guardar",
                "Cancelar"
        );

        if (confirmar) {

            taskService.runTask(
                    loadingOverlayController,
                    () -> {

                        modeloSeleccionada.setNombreModelo(txtModelo.getText());
                        detalleCategoriaSeleccionado.setCategoria(choiceCategoria.getValue());

                        modeloService.guardarModelo(modeloSeleccionada);
                        detalleCategoriaService.guardarDetalleCategoria(detalleCategoriaSeleccionado);
                        return null;

                    }, (resultado) -> {

                        mostrarInformacion("Modelo actualizada", "", "El modelo se actualizó correctamente.");
                        cerrarVentana();
                    }, (ex) -> {

                        if (ex instanceof ModeloException) {
                            mostrarError("Error al actualizar",
                                    "Ocurrio un problema al guardar los cambios",
                                    ""+ex.getMessage());

                        } else {
                            cerrarVentana();
                            mostrarError("Error interno",
                                    "Error inesperado",
                                    "Ocurrio un error al intentar guardar la marca y modelo(s) proporsonados");

                        }

                    }, null
            );

        }

    }//GuardarCambios


}//class
