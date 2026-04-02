package com.mastertyres.controllers.fxControllers.nota;

import com.mastertyres.common.exeptions.NotaException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.nota.DTOs.NotaDTO;
import com.mastertyres.nota.entity.StatusNota;
import com.mastertyres.nota.service.NotaService;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mastertyres.common.utils.MensajesAlert.*;
import static com.mastertyres.common.utils.MenuContextSetting.disableMenu;
import static com.mastertyres.common.utils.MenuContextSetting.disableMenuDatePicker;
import static com.mastertyres.common.utils.RegexTools.aplicarNumerosDecimal;

@Component
public class EditarAdeudoController implements IFxController, ILoader {

    @FXML
    private AnchorPane root;
    @FXML
    private TextField txtAdeudo;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private CheckBox cbLiquidar;
    @FXML
    private Label lblAdeudo;
    @FXML
    private Button btnActualizar;

    private LoadingComponentController loadingOverlayController;

    @Autowired
    private NotaService notaService;
    @Autowired
    private TaskService taskService;


    private NotaDTO notaAdeudo;

    private BooleanProperty boolActualizarNota = new SimpleBooleanProperty(true);

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

        disableMenuDatePicker(root);

        dpFecha.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #eeeeee;");

                }

            }
        });

        disableMenu(root);
        disableMenuDatePicker(dpFecha);
        aplicarNumerosDecimal(txtAdeudo);

        BooleanBinding fechaInvalida = dpFecha.valueProperty().isNull().and(cbLiquidar.selectedProperty().not());

        btnActualizar.disableProperty().bind(
                txtAdeudo.textProperty().isNull()
                        .or(txtAdeudo.textProperty().isEmpty())
                        .or(boolActualizarNota.not())
                        .or(fechaInvalida)

        );

    }//configuraciones

    @Override
    public void listeners() {

        cbLiquidar.selectedProperty().addListener((observable, oldValue, newValue) -> {
            revisarCheckBox(notaAdeudo.getAdeudo());
        });

        btnActualizar.setOnAction(event -> actualizar(notaAdeudo));

    }//listeners

    private void revisarCheckBox(float conAdeudo) {
        if (cbLiquidar.isSelected()) {
            txtAdeudo.setText("0");
            txtAdeudo.setEditable(false);
            dpFecha.setValue(null);
            dpFecha.setDisable(true);
        } else {
            txtAdeudo.setText("");
            txtAdeudo.setEditable(true);
            dpFecha.setDisable(false);
        }


    }

    public void setAdeudo(NotaDTO notaAdeudo) {
        this.notaAdeudo = notaAdeudo;

        String strFecha = notaAdeudo.getFechaVencimiento();

        if (strFecha != null && !strFecha.trim().isEmpty())
            dpFecha.setValue(LocalDate.parse(strFecha));
        else
            dpFecha.setValue(null);


        lblAdeudo.setText("Adeudo: $" + notaAdeudo.getAdeudo());
        revisarCheckBox(notaAdeudo.getAdeudo());


    }//setAdeudo


    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtAdeudo.getScene().getWindow();
        stage.close();
    }//cancelar

    private void actualizar(NotaDTO notaAdeudo) {

        taskService.runTask(
                loadingOverlayController,
                () -> {
                    if (Float.parseFloat(txtAdeudo.getText()) == 0) {
                        notaService.actualizarStatus(StatusNota.PAGADO.toString(), notaAdeudo.getNotaId());
                        dpFecha.setValue(null);
                    } else {
                        notaService.actualizarStatus(StatusNota.POR_PAGAR.toString(), notaAdeudo.getNotaId());
                    }

                    String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String fechaStr = null;

                    //if asiga null si el datePicker no tiene nada seleccionado
                    if (dpFecha.getValue() != null)
                        fechaStr = dpFecha.getValue().toString();

                    notaService.actualizarAdeudo(Float.parseFloat(txtAdeudo.getText()), fechaStr, notaAdeudo.getNotaId());
                    notaService.actualizarUpdatedAtNota(notaAdeudo.getNotaId(), fecha);


                    return null;
                }, (resultado) -> {


                    mostrarInformacion(
                            "Operación completada",
                            "Adeudo actualizado",
                            "El monto del adeudo se actualizó correctamente. Puede consultar más detalles en la nota '(+)' de Detalles del cliente."
                    );
                    close();


                }, (ex) -> {

                    if (ex instanceof NotaException) {

                        mostrarError(
                                "Error al actualizar",
                                "Ocurrió un problema técnico al intentar guardar la información:",
                                "" + ex.getMessage());
                        ex.getMessage();
                        close();
                    } else {
                        mostrarExcepcionThrowable(
                                "Error inesperado",
                                "Fallo al guardar cambios",
                                "Ocurrió un error inesperado al intentar guardar los cambios. Por favor, inténtelo de nuevo más tarde.",
                                ex
                        );
                    }


                }, null
        );


    }//actualizar


    private void close() {
        Stage stage = (Stage) txtAdeudo.getScene().getWindow();
        stage.close();
    }//close

}//class
