package com.mastertyres.controllers.fxControllers.nota;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.common.exeptions.InventarioException;
import com.mastertyres.common.exeptions.NotaException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.service.NotaUtils;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.common.utils.RegexTools;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.inventario.model.StatusInventario;
import com.mastertyres.inventario.service.InventarioService;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.nota.model.Nota;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.model.StatusNota;
import com.mastertyres.nota.service.NotaService;
import com.mastertyres.notaClienteDetalle.model.NotaClienteDetalle;
import com.mastertyres.notaDetalle.model.NotaDetalle;
import com.mastertyres.vehiculo.model.Vehiculo;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.mastertyres.common.utils.MensajesAlert.*;

@Component
public class RegistrarNotaController implements IFxController, ILoader{
    @FXML
    private AnchorPane root;
    @FXML
    private RadioButton rbPagado;
    @FXML
    private RadioButton rbPorPagar;
    @FXML
    private RadioButton rbAfavor;
    @FXML
    private TextField txtAdeudo;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private HBox hbAdeudo;
    @FXML
    private TextField txtSaldoAfavor;
    @FXML
    private TextField txtNumFactura;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnCancelar;

    @Autowired
    private InventarioService inventarioService;
    @Autowired
    private NotaService notaService;
    @Autowired
    private NotaUtils notaUtils;
    @Autowired
    private TaskService taskService;


    private NotaDTO nota; // Se recibe la instancia con la informacion en el constructor para poder registrar

    private BooleanProperty boolMontoAdeudo = new SimpleBooleanProperty(true);
    private BooleanProperty boolFechaVencimiento = new SimpleBooleanProperty(true);
    private BooleanProperty boolMontoFavor = new SimpleBooleanProperty(true);
    private BooleanProperty boolActualizarInventario = new SimpleBooleanProperty(false);
    private Runnable onRegistroCompleto;
    private LoadingComponentController loadingOverlayController;

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }

    public void agregarNota(NotaDTO nota) {
        this.nota = nota;
    }

    @FXML
    private void initialize() {

        configuraciones();
        listeners();

    }//initialize

    //agrupa los radioButton  en un ToggleGroup, hace que cuando cambie de opcion se borre lo seleccionado
    private void grupoOpciones() {
        rbPagado.setSelected(true);

        ToggleGroup grupoOpciones = new ToggleGroup();
        rbPagado.setToggleGroup(grupoOpciones);
        rbPorPagar.setToggleGroup(grupoOpciones);
        rbAfavor.setToggleGroup(grupoOpciones);

        grupoOpciones.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                RadioButton seleccionado = (RadioButton) newToggle;


                switch (seleccionado.getText().toLowerCase()) {
                    case "pagado" -> {
                        txtAdeudo.setVisible(false);
                        txtSaldoAfavor.setVisible(false);
                        hbAdeudo.setVisible(false);
                        txtAdeudo.setText("");
                        txtSaldoAfavor.setText("");
                        dpFecha.setValue(null);


                    }
                    case "por pagar" -> {

                        txtSaldoAfavor.setVisible(false);
                        txtAdeudo.setVisible(true);
                        hbAdeudo.setVisible(true);
                        txtSaldoAfavor.setText("");

                        LocalDate fechaHoy = LocalDate.now();
                        LocalDate fechaVencimiento = fechaHoy.plusDays(30);

                        dpFecha.setValue(fechaVencimiento);

                    }
                    case "a favor" -> {
                        txtAdeudo.setVisible(false);
                        txtSaldoAfavor.setVisible(true);
                        hbAdeudo.setVisible(false);
                        txtAdeudo.setText("");

                    }
                }
            }
        });
    }//grupoOpciones

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtNumFactura.getScene().getWindow();
        stage.close();

    }

    private void registrar() {

        if (notaUtils.toFloatSafe(txtAdeudo.getText()) > nota.getTotal()) {
            mostrarWarning(
                    "Advertencia",
                    "Monto excedido",
                    "La cantidad 'POR PAGAR' (" + txtAdeudo.getText() + ") excede el total de la nota (" + nota.getTotal() + "). Por favor, ingrese una cantidad válida."
            );
            return;
        }

        if (notaUtils.toFloatSafe(txtSaldoAfavor.getText()) > nota.getTotal()) {
            mostrarWarning(
                    "Advertencia",
                    "Monto excedido",
                    "La cantidad 'A FAVOR' (" + txtSaldoAfavor.getText() + ") excede el total de la nota (" + nota.getTotal() + "). Por favor, ingrese una cantidad válida."
            );
            return;
        }


        taskService.runTask(
                loadingOverlayController,

                () -> {

                    String strNumFactura = "";

                    if (txtNumFactura.getText() == null || txtNumFactura.getText().trim().isEmpty())
                        strNumFactura = null;
                    else
                        strNumFactura = txtNumFactura.getText();


                    if (dpFecha.getValue() != null)
                        nota.setFechaVencimiento(dpFecha.getValue().toString());
                    else
                        nota.setFechaVencimiento(null);


                    nota.setStatusNota(estadoNota());
                    nota.setAdeudo(notaUtils.toFloatSafe(txtAdeudo.getText()));
                    nota.setSaldoFavor(notaUtils.toFloatSafe(txtSaldoAfavor.getText()));
                    nota.setNumFactura(strNumFactura);

                    Cliente clienteNota = Cliente.builder()
                            .clienteId(nota.getClienteId())
                            .build();


                    Marca marca = Marca.builder()
                            .nombreMarca(nota.getMarcaNota())
                            .build();

                    Modelo modelo = Modelo.builder()
                            .nombreModelo(nota.getModeloNota())
                            .build();
                    Categoria categoria = Categoria.builder()
                            .nombreCategoria(nota.getCategoriaNota())
                            .build();

                    Vehiculo vehiculo = Vehiculo.builder()
                            .vehiculoId(nota.getVehiculoId())
                            .cliente(clienteNota)
                            .marca(marca)
                            .modelo(modelo)
                            .categoria(categoria)
                            .anio(nota.getAnioNota())
                            .kilometros(nota.getKilometrosNota())
                            .placas(nota.getPlacasNota())
                            .build();

                    Inventario llantaRegistrar = null;

                    //se busca llanta antes de crear instancia
                    if (nota.getInventarioId() != null) {
                        llantaRegistrar = inventarioService.buscarLlantaPorId(nota.getInventarioId());
                        boolActualizarInventario.set(true);
                    } else {
                        boolActualizarInventario.set(false);
                    }


                    Nota nuevaNota = Nota.builder()
                            .notaId(nota.getNotaId())
                            .cliente(clienteNota)
                            .vehiculo(vehiculo)
                            .inventario(llantaRegistrar)
                            .numNota(nota.getNumNota())
                            .numFactura(nota.getNumFactura())
                            .fechaYhora(nota.getFechaYHora())
                            .statusNota(nota.getStatusNota())
                            .adeudo(notaUtils.toFloatSafe(txtAdeudo.getText()))
                            .fechaVencimiento(nota.getFechaVencimiento())
                            .saldoFavor(notaUtils.toFloatSafe(txtSaldoAfavor.getText()))
                            .total(nota.getTotal())
                            .build();


                    NotaDetalle notaDetalle = NotaDetalle.builder()
                            .nota(nuevaNota)
                            .observaciones(nota.getObservaciones())
                            .observaciones2(nota.getObservaciones2())
                            .porcentajeGas(nota.getPorcentajeGas())
                            .rayones(nota.getRayones())
                            .golpes(nota.getGolpes())
                            .tapones(nota.getTapones())
                            .tapetes(nota.getTapetes())
                            .radio(nota.getRadio())
                            .gato(nota.getGato())
                            .llave(nota.getLlave())
                            .llanta(nota.getLlanta())//llanta de los checkBox
                            .llantaCampo(nota.getLlantaCampo()) //llanta del inventario
                            .llantaCantidad(nota.getLlantaCantidad())
                            .llantaUnitario(nota.getLlantaUnitario())
                            .llantaTotal(nota.getLlantaTotal())
                            .alineacion(nota.getAlineacion())
                            .alineacionCantidad(nota.getAlineacionCantidad())
                            .alineacionUnitario(nota.getAlineacionUnitario())
                            .alineacionTotal(nota.getAlineacionTotal())
                            .balanceo(nota.getBalanceo())
                            .balanceoCantidad(nota.getBalanceoCantidad())
                            .balanceoUnitario(nota.getBalanceoUnitario())
                            .balanceoTotal(nota.getBalanceoTotal())
                            .amorDelanteros(nota.getAmorDelanteros())
                            .amorDelCantidad(nota.getAmorDelCantidad())
                            .amorDelUnitario(nota.getAmorDelUnitario())
                            .amorDelTotal(nota.getAmorDelTotal())
                            .amorTraseros(nota.getAmorTraseros())
                            .amorTrasCantidad(nota.getAmorTrasCantidad())
                            .amorTrasUnitario(nota.getAmorTrasUnitario())
                            .amorTrasTotal(nota.getAmorTrasTotal())
                            .suspension(nota.getSuspension())
                            .suspensionCantidad(nota.getSuspensionCantidad())
                            .suspensionUnitario(nota.getSuspensionUnitario())
                            .suspensionTotal(nota.getSuspensionTotal())
                            .suspension2(nota.getSuspension2())
                            .suspensionCantidad2(nota.getSuspensionCantidad2())
                            .suspensionUnitario2(nota.getSuspensionUnitario2())
                            .suspensionTotal2(nota.getSuspensionTotal2())
                            .mecanica(nota.getMecanica())
                            .mecanicaCantidad(nota.getMecanicaCantidad())
                            .mecanicaUnitario(nota.getMecanicaUnitario())
                            .mecanicaTotal(nota.getMecanicaTotal())
                            .mecanica2(nota.getMecanica2())
                            .mecanicaCantidad2(nota.getMecanicaCantidad2())
                            .mecanicaUnitario2(nota.getMecanicaUnitario2())
                            .mecanicaTotal2(nota.getMecanicaTotal2())
                            .frenos(nota.getFrenos())
                            .frenosCantidad(nota.getFrenosCantidad())
                            .frenosUnitario(nota.getFrenosUnitario())
                            .frenosTotal(nota.getFrenosTotal())
                            .frenos2(nota.getFrenos2())
                            .frenosCantidad2(nota.getFrenosCantidad2())
                            .frenosUnitario2(nota.getFrenosUnitario2())
                            .frenosTotal2(nota.getFrenosTotal2())
                            .otros(nota.getOtros())
                            .otrosCantidad(nota.getOtrosCantidad())
                            .otrosUnitario(nota.getOtrosUnitario())
                            .otrosTotal(nota.getOtrosTotal())
                            .otros2(nota.getOtros2())
                            .otrosCantidad2(nota.getOtrosCantidad2())
                            .otrosUnitario2(nota.getOtrosUnitario2())
                            .otrosTotal2(nota.getOtrosTotal2())
                            .subTotalFrenos(nota.getSubTotalFrenos())
                            .subTotalMecanica(nota.getSubTotalMecanica())
                            .subTotalOtros(nota.getSubTotalOtros())
                            .build();

                    NotaClienteDetalle clienteDetalle = NotaClienteDetalle.builder()
                            .nota(nuevaNota)
                            .nombreClienteNota(nota.getNombreClienteNota())
                            .direccion1Nota(nota.getDireccion1Nota())
                            .direccion2Nota(nota.getDireccion2Nota())
                            .rfcNota(nota.getRfcNota())
                            .correoNota(nota.getCorreoNota())
                            .marcaNota(nota.getMarcaNota())
                            .modeloNota(nota.getModeloNota())
                            .categoriaNota(nota.getCategoriaNota())
                            .anioNota(nota.getAnioNota())
                            .kilometrosNota(nota.getKilometrosNota())
                            .placasNota(nota.getPlacasNota())
                            .build();



                    notaService.guardarNota(nuevaNota, notaDetalle, clienteDetalle);

//if que verifica si existe la llanta si existe la llanta que tiene la nota realiza la consulta (en teoria siempre debe de realizarse)
                    if (boolActualizarInventario.get()) {


                        inventarioService.actualizarStock(nota.getInventarioId(), llantaRegistrar.getStock() - nota.getLlantaCantidad(), StatusInventario.ACTIVE.toString());
                        inventarioService.actualizarUptatedAt(LocalDateTime.now().toString(), nota.getInventarioId());
                    }
                    Thread.sleep(2000);
                    return null;


                }, (resultado) -> {


                    MensajesAlert.mostrarInformacion(
                            "Operación completada",
                            "Nota registrada",
                            "La nota ha sido registrada en el sistema correctamente."
                    );
                    cancelar(null);
                    if (onRegistroCompleto != null) {
                        onRegistroCompleto.run();
                    }

                }, (ex) -> {

                    if (ex instanceof NotaException) {
                        mostrarError(
                                "Error al registrar",
                                "No fue posible registrar la nota",
                                "" + ex.getMessage());

                    } else if (ex instanceof InventarioException) {
                        mostrarError(
                                "Error de inventario",
                                "Ocurrió un error al intentar actualizar el stock del inventario.",
                                "" + ex.getMessage());

                    } else {

                        mostrarExcepcionThrowable(
                                "Error interno",
                                "Error inesperado en el sistema",
                                "Ocurrió un error inesperado al intentar guardar los cambios. Por favor, inténtelo de nuevo más tarde.",
                                ex
                        );
                    }

                }, null
        );
    }//registrar

    //establece reglas habilitar boton o regex
    @Override
    public void configuraciones() {

        grupoOpciones();

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


        //configuracion de campos
        RegexTools.aplicarNumerosDecimal(txtAdeudo);
        RegexTools.aplicarNumerosDecimal(txtSaldoAfavor);
        MenuContextSetting.disableMenuDatePicker(dpFecha);
        MenuContextSetting.disableMenu(root);

        //configuracion deshabilitar boton
        boolMontoAdeudo.bind(txtAdeudo.textProperty().isNotEmpty());
        boolFechaVencimiento.bind(dpFecha.valueProperty().isNotNull());
        boolMontoFavor.bind(txtSaldoAfavor.textProperty().isNotEmpty());

        btnRegistrar.disableProperty().bind(
                rbPorPagar.selectedProperty().and(boolMontoAdeudo.not().or(boolFechaVencimiento.not()))
                        .or(rbAfavor.selectedProperty().and(boolMontoFavor.not()))
        );


    }//configuraciones

    @Override
    public void listeners() {

        btnRegistrar.setOnAction(event -> registrar());

    }//listeners

    //getters y setters
    public void setOnRegistroCompleto(Runnable onRegistroCompleto) {
        this.onRegistroCompleto = onRegistroCompleto;
    }

    public RadioButton getRbPagado() {
        return this.rbPagado;
    }

    public void setRbPagado(final RadioButton rbPagado) {
        this.rbPagado = rbPagado;
    }

    private String estadoNota() {
        String estado = "";

        if (rbPagado.isSelected())
            estado = StatusNota.PAGADO.toString();
        else if (rbPorPagar.isSelected())
            estado = StatusNota.POR_PAGAR.toString();
        else if (rbAfavor.isSelected()) {
            estado = StatusNota.A_FAVOR.toString();
        }

        return estado;
    }

}//class
