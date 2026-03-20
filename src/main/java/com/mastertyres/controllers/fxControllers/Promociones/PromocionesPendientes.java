package com.mastertyres.controllers.fxControllers.Promociones;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.interfaces.IVentanaPrincipal;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.promociones.model.Promocion;
import com.mastertyres.promociones.service.IPromocionService;
import com.mastertyres.promociones.service.PromocionService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

@Component
public class PromocionesPendientes implements ILoader {

    @Autowired
    private final PromocionService promocionService;
    @Autowired
    private final TaskService taskService;
    private LoadingComponentController loadingOverlayController;

    @FXML
    private TableView <PromocionSeleccionado> tablaPromociones;
    @FXML
    private TableColumn<PromocionSeleccionado, Boolean> colCheckBox;
    @FXML
    private TableColumn <PromocionSeleccionado, String> colNombre;
    @FXML
    private TableColumn <PromocionSeleccionado, String> colDescripcion;
    @FXML
    private TableColumn <PromocionSeleccionado, String> colFechaInicio;
    @FXML
    private TableColumn <PromocionSeleccionado, String> colFechaFin;

    private ObservableList<PromocionSeleccionado> listaPromocionesSeleccionadas = observableArrayList();

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }

    public PromocionesPendientes(PromocionService promocionService, TaskService taskService) {
        this.promocionService = promocionService;
        this.taskService = taskService;
    }

    @FXML
    public void initialize() {
        configurarColumnas();
        cargarPromociones();
    }

    private void configurarColumnas() {

        tablaPromociones.setEditable(true);

        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(
                (cellData.getValue().getPromocion().getNombre() == null ? "" : cellData.getValue().getPromocion().getNombre())
        ));
        colDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(
                (cellData.getValue().getPromocion().getDescripcion() == null ? "" : cellData.getValue().getPromocion().getDescripcion())
        ));
        colFechaInicio.setCellValueFactory(cellData -> new SimpleStringProperty(
                (cellData.getValue().getPromocion().getFechaInicio() == null ? "" : cellData.getValue().getPromocion().getFechaInicio())
        ));
        colFechaFin.setCellValueFactory(cellData -> new SimpleStringProperty(
                (cellData.getValue().getPromocion().getFechaFin() == null ? "" : cellData.getValue().getPromocion().getFechaFin())
        ));

        colCheckBox.setEditable(true);
        colCheckBox.setCellValueFactory(cellData -> cellData.getValue().seleccionadoProperty());
        colCheckBox.setCellFactory(CheckBoxTableCell.forTableColumn(colCheckBox));

        tablaPromociones.setItems(listaPromocionesSeleccionadas);
    }

    private void cargarPromociones() {
        List<Promocion> promociones = promocionService.obtenerPromocionesPendientes();

        listaPromocionesSeleccionadas.clear();

        for (Promocion c : promociones) {
            listaPromocionesSeleccionadas.add(new PromocionSeleccionado(c));
        }
        tablaPromociones.setItems(listaPromocionesSeleccionadas);
    }

    @FXML
    private void cerrarVentana() {
        //cleanup();
        Stage stage = (Stage) tablaPromociones.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void SeleccionarTodos() {
        listaPromocionesSeleccionadas.forEach(cs -> cs.setSeleccionado(true));

    }

    @FXML
    public void DeseleccionarTodos() {
        listaPromocionesSeleccionadas.forEach(cs -> cs.setSeleccionado(false));

    }

    @FXML
    public void Eliminar(ActionEvent actionEvent) {

        List<Promocion> seleccionados = listaPromocionesSeleccionadas.stream()
                .filter(PromocionSeleccionado::isSeleccionado)
                .map(PromocionSeleccionado::getPromocion)
                .toList();

        if (seleccionados.isEmpty()) {
            MensajesAlert.mostrarWarning(
                    "Advertencia",
                    "Sin selección",
                    "Debe seleccionar al menos una promoción de la lista antes de intentar eliminarla."
            );
            return;
        }

        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar eliminación",
                "Eliminar promociones",
                "¿Está seguro de que desea eliminar las promociones seleccionadas? Esta acción no se puede deshacer.",
                "Sí, eliminar",
                "Cancelar"
        );

        if (!confirmar) return;

        // ← toda la lógica de servicio ahora corre en hilo de fondo
        taskService.runTask(
                loadingOverlayController,

                () -> {
                    List<String> errores = new ArrayList<>();

                    for (Promocion p : seleccionados) {
                        try {
                            promocionService.desactivarPromocion(p.getPromocionId());
                        } catch (Exception e) {
                            errores.add("Error en promoción '" + p.getNombre() + "': " + e.getMessage());
                        }
                    }

                    // devolvemos los errores al hilo de UI
                    return errores;
                },

                (resultado) -> {
                    @SuppressWarnings("unchecked")
                    List<String> errores = (List<String>) resultado;
                    int exitosos = seleccionados.size() - errores.size();

                    if (errores.isEmpty()) {
                        MensajesAlert.mostrarInformacion(
                                "Operación completada",
                                "Promociones desactivadas",
                                exitosos + " promoción(es) han sido desactivada(s) del sistema correctamente."
                        );
                    } else {
                        MensajesAlert.mostrarWarning(
                                "Advertencia",
                                "Desactivación parcial",
                                "Algunas promociones no pudieron desactivarse. Resultados: " + exitosos + " exitosa(s) y " + errores.size() + " con error(es)."
                        );
                    }

                    cargarPromociones();  // ← refresca tabla en hilo de UI
                },

                (ex) -> {
                    ex.printStackTrace();
                    MensajesAlert.mostrarExcepcionThrowable(
                            "Error inesperado",
                            "Se produjo una excepción durante la operación",
                            "Ocurrió un error técnico al intentar eliminar las promociones seleccionadas.",
                            ex
                    );
                },

                null
        );
    }

    public static class PromocionSeleccionado {
        private final Promocion Promocion;
        private final BooleanProperty seleccionado = new SimpleBooleanProperty(false);

        public PromocionSeleccionado(Promocion Promocion) {
            this.Promocion = Promocion;
        }

        public Promocion getPromocion() {
            return Promocion;
        }

        public BooleanProperty seleccionadoProperty() {
            return seleccionado;
        }

        public boolean isSeleccionado() {
            return seleccionado.get();
        }

        public void setSeleccionado(boolean seleccionado) {
            this.seleccionado.set(seleccionado);
        }
    }
}
