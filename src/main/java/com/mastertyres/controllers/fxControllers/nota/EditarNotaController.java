package com.mastertyres.controllers.fxControllers.nota;

import com.mastertyres.cliente.entity.Cliente;
import com.mastertyres.cliente.entity.StatusCliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.exeptions.NotaException;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.common.utils.ApplicationContextProvider;
import com.mastertyres.common.utils.RegexTools;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.inventario.entity.Inventario;
import com.mastertyres.inventario.service.InventarioService;
import com.mastertyres.nota.DTOs.NotaDTO;
import com.mastertyres.nota.entity.*;
import com.mastertyres.nota.service.NotaService;
import com.mastertyres.notaClienteDetalle.entity.NotaClienteDetalle;
import com.mastertyres.notaClienteDetalle.service.NotaClienteDetService;
import com.mastertyres.notaDetalle.entity.NotaDetalle;
import com.mastertyres.notaDetalle.service.NotaDetalleService;
import com.mastertyres.vehiculo.entity.StatusVehiculo;
import com.mastertyres.vehiculo.entity.Vehiculo;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PopupControl;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.mastertyres.common.utils.MensajesAlert.*;
import static com.mastertyres.common.utils.MenuContextSetting.disableMenu;

@Component
public class EditarNotaController extends BaseNota implements IFxController, ILoader{

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnActualizarDatos;
    @FXML
    private Button btnActualizarAdeudo;
    @FXML
    private Button btnClienteDetalles;
    @FXML
    private Button btnActualizarSaldoFavor;
    @FXML
    private Button btnNotaDetalles;
    @FXML
    private Button btnAgregarNumFactura;


    private PopupControl popup;
    private ComboBox<Integer> cmbHora;
    private ComboBox<Integer> cmbMinuto;
    private int porcentajeGasNota;

    private NotaDTO notaEditar;
    private String numNota;
    private LoadingComponentController loadingOverlayController;

    private final Duration TIEMPO = Duration.millis(500);
    private final int FROM = -50;
    private final int TO = 0;
    private final double[] SLIDE_FROM_TO = {FROM, TO};

    @Autowired
    private NotaService notaService;
    @Autowired
    private VehiculoService vehiculoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private NotaDetalleService notaDetalleService;
    @Autowired
    private NotaClienteDetService notaClienteDetService;
    @Autowired
    private InventarioService inventarioService;
    @Autowired
    private TaskService taskService;


    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;

    }

    @FXML
    private void initialize() {

        configuraciones();
        listeners();

    }//initialize

    @Override
    public void configuraciones() {

        operacionesCampos();

        //deshabilita los menu del clic derecho en los campos de texto
        disableMenu(rootPane);

        //validaciones Regex
        RegexTools.aplicar24Horas(txtHoraEntrega);
        RegexTools.aplicar6Enteros(txtNumNota);
        RegexTools.aplicar2Enteros(txtDia);
        RegexTools.aplicar2Enteros(txtMes);
        RegexTools.aplicar4Enteros(txtAnio);
        RegexTools.aplicar4Enteros(txtAnioVehiculo);
        RegexTools.aplicarNumeroEntero(txtKms);



        RegexTools.aplicarNumeroEnteroConLimite(txtAlineacionCantidad,  6);
        RegexTools.aplicarNumeroEnteroConLimite(txtBalanceoCantidad,    6);
        RegexTools.aplicarNumeroEnteroConLimite(txtLlantasCantidad,     6);
        RegexTools.aplicarNumeroEnteroConLimite(txtAmorDelCantidad,     6);
        RegexTools.aplicarNumeroEnteroConLimite(txtAmorTrasCantidad,    6);
        RegexTools.aplicarNumeroEnteroConLimite(txtSuspensionCantidad,  6);
        RegexTools.aplicarNumeroEnteroConLimite(txtSuspensionCantidad2, 6);
        RegexTools.aplicarNumeroEnteroConLimite(txtMecanicaCantidad,    6);
        RegexTools.aplicarNumeroEnteroConLimite(txtMecanicaCantidad2,   6);
        RegexTools.aplicarNumeroEnteroConLimite(txtFrenosCantidad,      6);
        RegexTools.aplicarNumeroEnteroConLimite(txtFrenosCantidad2,     6);
        RegexTools.aplicarNumeroEnteroConLimite(txtOtrosCantidad,       6);
        RegexTools.aplicarNumeroEnteroConLimite(txtOtrosCantidad2,      6);

        // Decimales con límite de BD — decimal(11,2) = máx 14 chars ("99999999999.99")
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtTotal,              14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtAlineacionUnitario, 14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtAlineacionTotal,    14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtBalanceoUnitario,   14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtBalanceoTotal,      14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtLlantasUnitario,    14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtLlantasTotal,       14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtAmorDelUnitario,    14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtAmorDelTotal,       14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtAmorTrasUnitario,   14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtAmorTrasTotal,      14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtSuspensionUnitario, 14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtSuspensionTotal,    14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtSuspensionUnitario2,14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtSuspensionTotal2,   14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtMecanicaUnitario,   14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtMecanicaTotal,      14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtMecanicaUnitario2,  14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtMecanicaTotal2,     14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtFrenosUnitario,     14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtFrenosTotal,        14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtFrenosUnitario2,    14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtFrenosTotal2,       14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtOtrosUnitario,      14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtOtrosTotal,         14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtOtrosUnitario2,     14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtOtrosTotal2,        14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtSubTotalFrenos,     14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtSubTotalMecanica,   14);
        RegexTools.aplicarNumerosDecimalNotaConLimite(txtSubTotalOtros,      14);


        // Campos de texto libre — solo límite de BD
        RegexTools.aplicarMaxLength(txtObservaciones,  106);
        RegexTools.aplicarMaxLength(txtObservaciones2, 120);
        RegexTools.aplicarMaxLength(txtAlineacion,     56);
        RegexTools.aplicarMaxLength(txtBalanceo,       56);
        RegexTools.aplicarMaxLength(txtAmorDelanteros, 90);
        RegexTools.aplicarMaxLength(txtAmorTraseros,   90);
        RegexTools.aplicarMaxLength(txtSuspension,     90);
        RegexTools.aplicarMaxLength(txtSuspension2,    90);
        RegexTools.aplicarMaxLength(txtMecanica,       90);
        RegexTools.aplicarMaxLength(txtMecanica2,      90);
        RegexTools.aplicarMaxLength(txtFrenos,         90);
        RegexTools.aplicarMaxLength(txtFrenos2,        90);
        RegexTools.aplicarMaxLength(txtOtros,          90);
        RegexTools.aplicarMaxLength(txtOtros2,         90);
        RegexTools.aplicarMaxLength(txtLlantas,        90);

        notaUtils.mostrarPopupHora(txtHoraEntrega);
        notaUtils.descripcionComponent(btnNotaDetalles,"Detalles cliente");
        notaUtils.descripcionComponent(btnActualizarAdeudo,"Actualizar adeudo");
        notaUtils.descripcionComponent(btnActualizarSaldoFavor,"Actualizar saldo favor");
        notaUtils.descripcionComponent(btnAgregarNumFactura,"Agregar numero de factura");
        notaUtils.descripcionComponent(btnActualizarDatos,"Actualizar datos cliente");
        notaUtils.descripcionComponent(btnGuardar,"Guardar cambios");
        notaUtils.descripcionComponent(txtHoraEntrega, "Clic derecho para modificar.");
        notaUtils.descripcionComponent(spPorcentajeGas, "Clic derecho para modificar.");


    }//configuraciones

    @Override
    public void listeners() {

        btnShowIcons.setOnMouseClicked(event -> notaUtils.showIcons(gridPaneIcons, SLIDE_FROM_TO, TIEMPO));

        spPorcentajeGas.setOnMouseClicked(event -> mostrarSlider(spPorcentajeGas.getScene().getWindow()));

        btnActualizarDatos.setOnAction(event -> actualizarDatosCliente(notaEditar.getClienteId(), notaEditar.getVehiculoId()));

        btnActualizarAdeudo.setOnAction(event -> actualizarAdeudo(getNotaEditar()));

        btnActualizarSaldoFavor.setOnAction(event -> actualizarSaldo(getNotaEditar()));

        btnNotaDetalles.setOnAction(event -> notaDetalles(getNotaEditar()));

        btnGuardar.setOnAction(event -> actualizarNota(notaEditar));

        btnAgregarNumFactura.setOnAction(event -> agregarNumFactura(notaEditar));


        //configuracion checkBox

        cbRayonesSi.setOnAction(event -> {
            if (cbRayonesSi.isSelected()) {
                cbRayonesNo.setSelected(false);
            }
        });
        cbRayonesNo.setOnAction(event -> {
            if (cbRayonesNo.isSelected()) {
                cbRayonesSi.setSelected(false);
            }
        });
        cbGolpesSi.setOnAction(event -> {
            if (cbGolpesSi.isSelected()) {
                cbGolpesNo.setSelected(false);
            }
        });
        cbGolpesNo.setOnAction(event -> {
            if (cbGolpesNo.isSelected()) {
                cbGolpesSi.setSelected(false);
            }
        });
        cbTaponesSi.setOnAction(event -> {
            if (cbTaponesSi.isSelected()) {
                cbTaponesNo.setSelected(false);
            }
        });
        cbTaponesNo.setOnAction(event -> {
            if (cbTaponesNo.isSelected()) {
                cbTaponesSi.setSelected(false);
            }
        });
        cbTapetesSi.setOnAction(event -> {
            if (cbTapetesSi.isSelected()) {
                cbTapetesNo.setSelected(false);
            }
        });
        cbTapetesNo.setOnAction(event -> {
            if (cbTapetesNo.isSelected()) {
                cbTapetesSi.setSelected(false);
            }
        });
        cbRadioSi.setOnAction(event -> {
            if (cbRadioSi.isSelected()) {
                cbRadioNo.setSelected(false);
            }
        });
        cbRadioNo.setOnAction(event -> {
            if (cbRadioNo.isSelected()) {
                cbRadioSi.setSelected(false);
            }
        });
        cbGatoSi.setOnAction(event -> {
            if (cbGatoSi.isSelected()) {
                cbGatoNo.setSelected(false);
            }
        });
        cbGatoNo.setOnAction(event -> {
            if (cbGatoNo.isSelected()) {
                cbGatoSi.setSelected(false);
            }
        });
        cbLlaveSi.setOnAction(event -> {
            if (cbLlaveSi.isSelected()) {
                cbLlaveNo.setSelected(false);
            }
        });
        cbLlaveNo.setOnAction(event -> {
            if (cbLlaveNo.isSelected()) {
                cbLlaveSi.setSelected(false);
            }
        });
        cbLlantaSi.setOnAction(event -> {
            if (cbLlantaSi.isSelected()) {
                cbLlantaNo.setSelected(false);
            }
        });
        cbLlantaNo.setOnAction(event -> {
            if (cbLlantaNo.isSelected()) {
                cbLlantaSi.setSelected(false);
            }
        });


    }//listeners

    public void agregarNota(String numNota) {
        llenarNota(numNota);
    }

    private void llenarNota(String numNota) {
        notaEditar = notaService.buscarPorNumNota(StatusNota.ACTIVE.toString(), numNota);


        if (notaEditar != null) {

            txtNumNota.setText(notaEditar.getNumNota());

            //codigo para mostrar fecha, la fecha viene en una misma columna en la BD y semuestra en diferentes campos en la Nota

            String strFecha = "", strHoraEntrega = "", strHoraFormateada = "";
            String arrayFecha[];


            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter outPutFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter horaInputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            DateTimeFormatter horaOutputFormatter = DateTimeFormatter.ofPattern("HH:mm");
            strFecha = notaEditar.getFechaYHora();

            String fechaFormateada
                    = LocalDateTime.parse(strFecha, inputFormatter)
                    .format(outPutFormatter);

            arrayFecha = fechaFormateada.split("-");

            txtDia.setText(arrayFecha[0]);
            txtMes.setText(arrayFecha[1]);
            txtAnio.setText(arrayFecha[2]);

            arrayFecha = strFecha.split(" ");

            strHoraEntrega = arrayFecha[1];
            LocalTime hora = LocalTime.parse(strHoraEntrega, horaInputFormatter);
            strHoraFormateada = hora.format(horaOutputFormatter);


            txtHoraEntrega.setText(strHoraFormateada);

            //datos cliente
            txtNombre.setText(notaEditar.getNombreClienteNota());
            txtDireccion.setText(notaEditar.getDireccion1Nota() != null ? notaEditar.getDireccion1Nota() : "");
            txtDireccion2.setText(notaEditar.getDireccion2Nota() != null ? notaEditar.getDireccion2Nota() : "");
            txtRfc.setText(notaEditar.getRfcNota() != null ? notaEditar.getRfcNota() : "");
            txtCorreo.setText(notaEditar.getCorreoNota() != null ? notaEditar.getCorreoNota() : "");

            //datos vehiculo
            txtMarca.setText(notaEditar.getMarcaNota());
            txtModelo.setText(notaEditar.getModeloNota());
            txtAnioVehiculo.setText(notaEditar.getAnioNota() + "");
            txtKms.setText((notaEditar.getKilometrosNota() != null ? notaEditar.getKilometrosNota() : "") + "");
            txtPlacas.setText(notaEditar.getPlacasNota() != null ? notaEditar.getPlacasNota() : "");
            dibujarGasolina(notaEditar.getPorcentajeGas());

            //checkBox
            //este metodo revisa cuales estan seleccionados  y
            revisarCheckBoxes(notaEditar);

            txtObservaciones.setText(notaEditar.getObservaciones() != null ? notaEditar.getObservaciones() : "");
            txtObservaciones2.setText(notaEditar.getObservaciones2() != null ? notaEditar.getObservaciones2() : "");

            //Nota
            txtAlineacion.setText(notaEditar.getAlineacion());
            txtAlineacionCantidad.setText(notaUtils.eliminarCero(notaEditar.getAlineacionCantidad()));
            txtAlineacionUnitario.setText(notaUtils.eliminarCero(notaEditar.getAlineacionUnitario()));
            txtAlineacionTotal.setText(notaUtils.eliminarCero(notaEditar.getAlineacionTotal()));

            txtBalanceo.setText(notaEditar.getBalanceo());
            txtBalanceoCantidad.setText(notaUtils.eliminarCero(notaEditar.getBalanceoCantidad()));
            txtBalanceoUnitario.setText(notaUtils.eliminarCero(notaEditar.getBalanceoUnitario()));
            txtBalanceoTotal.setText(notaUtils.eliminarCero(notaEditar.getBalanceoTotal()));

            txtLlantas.setText(notaEditar.getLlantaCampo());
            txtLlantasCantidad.setText(notaUtils.eliminarCero(notaEditar.getLlantaCantidad()));
            txtLlantasUnitario.setText(notaUtils.eliminarCero(notaEditar.getLlantaUnitario()));
            txtLlantasTotal.setText(notaUtils.eliminarCero(notaEditar.getLlantaTotal()));

            txtAmorDelanteros.setText(notaEditar.getAmorDelanteros());
            txtAmorDelCantidad.setText(notaUtils.eliminarCero(notaEditar.getAmorDelCantidad()));
            txtAmorDelUnitario.setText(notaUtils.eliminarCero(notaEditar.getAmorDelUnitario()));
            txtAmorDelTotal.setText(notaUtils.eliminarCero(notaEditar.getAmorDelTotal()));

            txtAmorTraseros.setText(notaEditar.getAmorTraseros());
            txtAmorTrasCantidad.setText(notaUtils.eliminarCero(notaEditar.getAmorTrasCantidad()));
            txtAmorTrasUnitario.setText(notaUtils.eliminarCero(notaEditar.getAmorTrasUnitario()));
            txtAmorTrasTotal.setText(notaUtils.eliminarCero(notaEditar.getAmorTrasTotal()));

            txtSuspension.setText(notaEditar.getSuspension());
            txtSuspensionCantidad.setText(notaUtils.eliminarCero(notaEditar.getSuspensionCantidad()));
            txtSuspensionUnitario.setText(notaUtils.eliminarCero(notaEditar.getSuspensionUnitario()));
            txtSuspensionTotal.setText(notaUtils.eliminarCero(notaEditar.getSuspensionTotal()));

            txtSuspension2.setText(notaEditar.getSuspension2());
            txtSuspensionCantidad2.setText(notaUtils.eliminarCero(notaEditar.getSuspensionCantidad2()));
            txtSuspensionUnitario2.setText(notaUtils.eliminarCero(notaEditar.getSuspensionUnitario2()));
            txtSuspensionTotal2.setText(notaUtils.eliminarCero(notaEditar.getSuspensionTotal2()));

            txtMecanica.setText(notaEditar.getMecanica());
            txtMecanicaCantidad.setText(notaUtils.eliminarCero(notaEditar.getMecanicaCantidad()));
            txtMecanicaUnitario.setText(notaUtils.eliminarCero(notaEditar.getMecanicaUnitario()));
            txtMecanicaTotal.setText(notaUtils.eliminarCero(notaEditar.getMecanicaTotal()));

            txtMecanica2.setText(notaEditar.getMecanica2());
            txtMecanicaCantidad2.setText(notaUtils.eliminarCero(notaEditar.getMecanicaCantidad2()));
            txtMecanicaUnitario2.setText(notaUtils.eliminarCero(notaEditar.getMecanicaUnitario2()));
            txtMecanicaTotal2.setText(notaUtils.eliminarCero(notaEditar.getMecanicaTotal2()));

            txtFrenos.setText(notaEditar.getFrenos());
            txtFrenosCantidad.setText(notaUtils.eliminarCero(notaEditar.getFrenosCantidad()));
            txtFrenosUnitario.setText(notaUtils.eliminarCero(notaEditar.getFrenosUnitario()));
            txtFrenosTotal.setText(notaUtils.eliminarCero(notaEditar.getFrenosTotal()));

            txtFrenos2.setText(notaEditar.getFrenos2());
            txtFrenosCantidad2.setText(notaUtils.eliminarCero(notaEditar.getFrenosCantidad2()));
            txtFrenosUnitario2.setText(notaUtils.eliminarCero(notaEditar.getFrenosUnitario2()));
            txtFrenosTotal2.setText(notaUtils.eliminarCero(notaEditar.getFrenosTotal2()));

            txtOtros.setText(notaEditar.getOtros());
            txtOtrosCantidad.setText(notaUtils.eliminarCero(notaEditar.getOtrosCantidad()));
            txtOtrosUnitario.setText(notaUtils.eliminarCero(notaEditar.getOtrosUnitario()));
            txtOtrosTotal.setText(notaUtils.eliminarCero(notaEditar.getOtrosTotal()));

            txtOtros2.setText(notaEditar.getOtros2());
            txtOtrosCantidad2.setText(notaUtils.eliminarCero(notaEditar.getOtrosCantidad2()));
            txtOtrosUnitario2.setText(notaUtils.eliminarCero(notaEditar.getOtrosUnitario2()));
            txtOtrosTotal2.setText(notaUtils.eliminarCero(notaEditar.getOtrosTotal2()));

            txtSubTotalFrenos.setText(notaUtils.eliminarCero(notaEditar.getSubTotalFrenos()));
            txtSubTotalMecanica.setText(notaUtils.eliminarCero(notaEditar.getSubTotalMecanica()));
            txtSubTotalOtros.setText(notaUtils.eliminarCero(notaEditar.getSubTotalOtros()));
            txtTotal.setText(notaUtils.eliminarCero(notaEditar.getTotal()));

            if (notaEditar.getStatusNota().equals(StatusNota.PAGADO.toString())) {
                btnActualizarAdeudo.setDisable(true);
                btnActualizarSaldoFavor.setDisable(true);
            } else if (notaEditar.getStatusNota().equals(StatusNota.A_FAVOR.toString())) {
                btnActualizarAdeudo.setDisable(true);
            } else if (notaEditar.getStatusNota().equals(StatusNota.POR_PAGAR.toString()) || notaEditar.getStatusNota().equals(StatusNota.VENCIDO.toString())) {
                btnActualizarSaldoFavor.setDisable(true);
            }

            setNotaEditar(notaEditar);


        }//if nota != null


    }//llenarNota


    @FXML
    private void actualizarAdeudo(NotaDTO notaAdeudo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/EditarAdeudo.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();
            EditarAdeudoController controller = loader.getController();
            controller.setAdeudo(notaAdeudo);
            controller.setInitializeLoading(loadingOverlayController);

            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle("Actualizar Adeudo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            actualizarDatosCliente(notaAdeudo);

        } catch (Exception e) {
            mostrarExcepcion(
                "Error de carga",
                "No se pudo inicializar la vista",
                "Ocurrió un problema al intentar cargar la interfaz. Por favor, inténtelo de nuevo más tarde.",
                e
            );


        }


    }//actualizarAdeudo

    private void actualizarDatosCliente(Integer clienteId, Integer vehiculoId) {


        if (clienteId != null) {


            taskService.runTask(
                    loadingOverlayController,
                    () -> {
                        Cliente cliente = clienteService.buscarClientePorId(clienteId, StatusCliente.ACTIVE.toString());
                        Vehiculo vehiculo = vehiculoService.buscarVehiculoPorId(vehiculoId, StatusVehiculo.ACTIVE.toString());

                        return new Object[]{cliente, vehiculo};

                    }, (resultado) -> {


                        Object[] datos = (Object[]) resultado;
                        Cliente cliente = (Cliente) datos[0];
                        Vehiculo vehiculo = (Vehiculo) datos[1];

                        notaUtils.campoFormatter(
                                cliente.getNombre() + " " + (cliente.getApellido() != null ? cliente.getApellido() : "") + " " +
                                        (cliente.getSegundoApellido() != null ? cliente.getSegundoApellido() : ""),
                                txtNombre,
                                CampoNota.NOMBRE
                        );

                        notaUtils.campoFormatter(cliente.getDomicilio() != null ? cliente.getDomicilio() : "", txtDireccion, txtDireccion2);


                        notaUtils.campoFormatter(
                                cliente.getRfc() != null ? cliente.getRfc() : "",
                                txtRfc,
                                CampoNota.RFC
                        );

                        notaUtils.campoFormatter(
                                cliente.getCorreo() != null ? cliente.getCorreo() : "",
                                txtCorreo
                        );
                        notaUtils.campoFormatter(
                                vehiculo.getMarca().getNombreMarca(),
                                txtMarca,
                                CampoNota.MARCA
                        );
                        notaUtils.campoFormatter(
                                vehiculo.getModelo().getNombreModelo(),
                                txtModelo,
                                CampoNota.MODELO
                        );

                        txtAnioVehiculo.setText(vehiculo.getAnio() + "");

                        notaUtils.campoFormatter(
                                vehiculo.getKilometros() + "",
                                txtKms,
                                CampoNota.KILOMETROS
                        );

                        txtPlacas.setText(vehiculo.getPlacas() != null ? vehiculo.getPlacas() : "");


                        mostrarInformacion(
                                "Operación completada",
                                "Registro actualizado",
                                "Los campos de información se han actualizado correctamente en el sistema."
                        );
                    }, (excepcion) -> {

                            mostrarExcepcionThrowable(
                                    "Error inesperado",
                                    "Fallo en la actualización",
                                    "Ocurrió un problema al intentar actualizar los datos. Por favor, intente nuevamente más tarde.",
                                    excepcion
                            );




                    }, null
            );

        }


    }//actualizarDatosCliente

    private void actualizarDatosCliente(NotaDTO nota) {

        if (nota != null)
            llenarNota(nota.getNumNota());

    }//actualizarDatosCliente

    private void actualizarSaldo(NotaDTO notaClienteDet) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/EditarSaldo.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);

            Parent root = loader.load();
            EditarSaldoController controller = loader.getController();
            controller.setSaldoFavor(notaClienteDet);
            controller.setInitializeLoading(loadingOverlayController);

            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle("Actualizar Saldo");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            actualizarDatosCliente(notaClienteDet);


        } catch (Exception e) {
            e.printStackTrace();
            mostrarExcepcion(
                    "Error de carga",
                    "Fallo al cargar la interfaz",
                    "Ocurrió un problema al cargar la vista. Por favor, inténtelo de nuevo más tarde.",
                    e
            );
        }


    }//actualizarSaldo

    private void notaDetalles(NotaDTO notaDetalles) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/NotaDetalles.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);

            Parent root = loader.load();
            NotaDetallesController controller = loader.getController();
            controller.setNotaDetalles(notaDetalles);

            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle("Nota Detalles");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            actualizarDatosCliente(notaDetalles);

        } catch (Exception e) {
            e.printStackTrace();
            mostrarExcepcion(
                    "Error de carga",
                    "Error al inicializar componentes",
                    "Ocurrió un problema técnico al intentar cargar la vista solicitada.",
                    e
            );
        }

    }//notaDetalles

    private void actualizarNota(NotaDTO notaActualizar) {

        boolean guardar = mostrarConfirmacion(
                "Confirmar actualización",
                "Guardar cambios",
                "¿Está seguro de que desea guardar los cambios realizados en el registro?",
                "Guardar",
                "Cancelar"
        );

        checkCheckBoxes();

        if (guardar) {

            taskService.runTask(
                    loadingOverlayController,
                    () -> {

                        Nota notaRegistrar = notaService.buscarPorId(notaActualizar.getNotaId());
                        NotaDetalle ndRegistrar = notaDetalleService.buscarNotaDetalle(notaRegistrar);
                        NotaClienteDetalle detalleCliente = notaClienteDetService.buscarclienteDetalle(notaRegistrar);


                        Cliente cliente = clienteService.buscarClientePorId(notaActualizar.getClienteId(), StatusNota.ACTIVE.toString());
                        Vehiculo vehiculo = vehiculoService.buscarVehiculoPorId(notaActualizar.getVehiculoId(), StatusVehiculo.ACTIVE.toString());

                        Inventario inventario = null;
                        if (notaActualizar.getInventarioId() != null)
                            inventario = inventarioService.buscarLlantaPorId(notaActualizar.getInventarioId());


                        //actualizar campos nota
                        String fechaYHora = txtAnio.getText() + "-" + "-" + txtMes.getText() + "-" + txtDia.getText() + " " + txtHoraEntrega.getText() + ":00";

                        notaRegistrar.setNumFactura(notaActualizar.getNumFactura());
                        notaRegistrar.setTotal(notaUtils.toFloatSafe(txtTotal.getText()));
                        notaRegistrar.setFechaYhora(fechaYHora); //pendiente
                        notaRegistrar.setFechaVencimiento(notaActualizar.getFechaVencimiento());
                        notaRegistrar.setAdeudo(notaActualizar.getAdeudo());
                        notaRegistrar.setSaldoFavor(notaActualizar.getSaldoFavor());
                        notaRegistrar.setStatusNota(notaActualizar.getStatusNota());
                        notaRegistrar.setCliente(cliente);
                        notaRegistrar.setVehiculo(vehiculo);
                        notaRegistrar.setCreatedAt(LocalDateTime.now().toString());


                        //datos cliente y vehiculo
                        //en el caso de la entidad cliente no se setean ya que en caso de haber cambios el usuario los hace con el UPDATE y
                        //estos se traen directo de la BD no de los campos de texto. El mismo caso con vehiculo


                        //actualizar nota detalle
                        ndRegistrar.setObservaciones(txtObservaciones.getText());
                        ndRegistrar.setObservaciones2(txtObservaciones2.getText());
                        ndRegistrar.setPorcentajeGas(getPorcentajeGasNota());
                        ndRegistrar.setRayones(getRayones());//comienzan checkBox
                        ndRegistrar.setGolpes(getGolpes());
                        ndRegistrar.setTapones(getTapones());
                        ndRegistrar.setTapetes(getTapetes());
                        ndRegistrar.setRadio(getRadio());
                        ndRegistrar.setGato(getGato());
                        ndRegistrar.setLlave(getLlave());
                        ndRegistrar.setLlanta(getLlanta()); //terminan CkeckBox
                        ndRegistrar.setAlineacion(txtAlineacion.getText()); //comienzan campos de la nota
                        ndRegistrar.setAlineacionCantidad(notaUtils.toIntSafe(txtAlineacionCantidad.getText()));
                        ndRegistrar.setAlineacionUnitario(notaUtils.toFloatSafe(txtAlineacionUnitario.getText()));
                        ndRegistrar.setAlineacionTotal(notaUtils.toFloatSafe(txtAlineacionTotal.getText()));
                        ndRegistrar.setBalanceo(txtBalanceo.getText());
                        ndRegistrar.setBalanceoCantidad(notaUtils.toIntSafe(txtBalanceoCantidad.getText()));
                        ndRegistrar.setBalanceoUnitario(notaUtils.toFloatSafe(txtBalanceoUnitario.getText()));
                        ndRegistrar.setBalanceoTotal(notaUtils.toFloatSafe(txtBalanceoTotal.getText()));
                        ndRegistrar.setAmorDelanteros(txtAmorDelanteros.getText());
                        ndRegistrar.setAmorDelCantidad(notaUtils.toIntSafe(txtAmorDelCantidad.getText()));
                        ndRegistrar.setAmorDelUnitario(notaUtils.toFloatSafe(txtAmorDelUnitario.getText()));
                        ndRegistrar.setAmorDelTotal(notaUtils.toFloatSafe(txtAmorDelTotal.getText()));
                        ndRegistrar.setAmorTraseros(txtAmorTraseros.getText());
                        ndRegistrar.setAmorTrasCantidad(notaUtils.toIntSafe(txtAmorTrasCantidad.getText()));
                        ndRegistrar.setAmorTrasUnitario(notaUtils.toFloatSafe(txtAmorTrasUnitario.getText()));
                        ndRegistrar.setAmorTrasTotal(notaUtils.toFloatSafe(txtAmorTrasTotal.getText()));
                        ndRegistrar.setSuspension(txtSuspension.getText());
                        ndRegistrar.setSuspensionCantidad(notaUtils.toIntSafe(txtSuspensionCantidad.getText()));
                        ndRegistrar.setSuspensionUnitario(notaUtils.toFloatSafe(txtSuspensionUnitario.getText()));
                        ndRegistrar.setSuspensionTotal(notaUtils.toFloatSafe(txtSuspensionTotal.getText()));
                        ndRegistrar.setSuspension2(txtSuspension2.getText());
                        ndRegistrar.setSuspensionCantidad2(notaUtils.toIntSafe(txtSuspensionCantidad2.getText()));
                        ndRegistrar.setSuspensionUnitario2(notaUtils.toFloatSafe(txtSuspensionUnitario2.getText()));
                        ndRegistrar.setMecanica(txtMecanica.getText());
                        ndRegistrar.setMecanicaCantidad(notaUtils.toIntSafe(txtMecanicaCantidad.getText()));
                        ndRegistrar.setMecanicaUnitario(notaUtils.toFloatSafe(txtMecanicaUnitario.getText()));
                        ndRegistrar.setMecanicaTotal(notaUtils.toFloatSafe(txtMecanicaTotal.getText()));
                        ndRegistrar.setMecanica2(txtMecanica2.getText());
                        ndRegistrar.setMecanicaCantidad2(notaUtils.toIntSafe(txtMecanicaCantidad2.getText()));
                        ndRegistrar.setMecanicaUnitario2(notaUtils.toFloatSafe(txtMecanicaUnitario2.getText()));
                        ndRegistrar.setMecanicaTotal2(notaUtils.toFloatSafe(txtMecanicaTotal2.getText()));
                        ndRegistrar.setFrenos(txtFrenos.getText());
                        ndRegistrar.setFrenosCantidad(notaUtils.toIntSafe(txtFrenosCantidad.getText()));
                        ndRegistrar.setFrenosUnitario(notaUtils.toFloatSafe(txtFrenosUnitario.getText()));
                        ndRegistrar.setFrenosTotal(notaUtils.toFloatSafe(txtFrenosTotal.getText()));
                        ndRegistrar.setFrenos2(txtFrenos2.getText());
                        ndRegistrar.setFrenosCantidad2(notaUtils.toIntSafe(txtFrenosCantidad2.getText()));
                        ndRegistrar.setFrenosUnitario2(notaUtils.toFloatSafe(txtFrenosUnitario2.getText()));
                        ndRegistrar.setFrenosTotal2(notaUtils.toFloatSafe(txtFrenosTotal2.getText()));
                        ndRegistrar.setOtros(txtOtros.getText());
                        ndRegistrar.setOtrosCantidad(notaUtils.toIntSafe(txtOtrosCantidad.getText()));
                        ndRegistrar.setOtrosUnitario(notaUtils.toFloatSafe(txtOtrosUnitario.getText()));
                        ndRegistrar.setOtrosTotal(notaUtils.toFloatSafe(txtOtrosTotal.getText()));
                        ndRegistrar.setOtros2(txtOtros2.getText());
                        ndRegistrar.setOtrosCantidad2(notaUtils.toIntSafe(txtOtrosCantidad2.getText()));
                        ndRegistrar.setOtrosUnitario2(notaUtils.toFloatSafe(txtOtrosUnitario2.getText()));
                        ndRegistrar.setOtrosTotal2(notaUtils.toFloatSafe(txtOtrosTotal2.getText()));
                        ndRegistrar.setSubTotalFrenos(notaUtils.toFloatSafe(txtSubTotalOtros.getText()));
                        ndRegistrar.setSubTotalMecanica(notaUtils.toFloatSafe(txtSubTotalMecanica.getText()));
                        ndRegistrar.setSubTotalOtros(notaUtils.toFloatSafe(txtSubTotalOtros.getText()));
                        ndRegistrar.setLlantaCampo(txtLlantas.getText());
                        ndRegistrar.setLlantaCantidad(notaUtils.toIntSafe(txtLlantasCantidad.getText()));
                        ndRegistrar.setLlantaUnitario(notaUtils.toFloatSafe(txtLlantasUnitario.getText()));
                        ndRegistrar.setLlantaTotal(notaUtils.toFloatSafe(txtLlantasTotal.getText()));

                        //actuallizar Nota Cliente Detalle
                        detalleCliente.setNombreClienteNota(txtNombre.getText());
                        detalleCliente.setDireccion1Nota(txtDireccion.getText());
                        detalleCliente.setDireccion2Nota(txtDireccion2.getText());
                        detalleCliente.setRfcNota(txtRfc.getText());
                        detalleCliente.setCorreoNota(txtCorreo.getText());
                        detalleCliente.setMarcaNota(txtMarca.getText());
                        detalleCliente.setModeloNota(txtModelo.getText());
                        detalleCliente.setCategoriaNota(detalleCliente.getCategoriaNota());
                        detalleCliente.setAnioNota(notaUtils.toIntSafe(txtAnioVehiculo.getText()));
                        detalleCliente.setKilometrosNota(notaUtils.toIntSafe(txtKms.getText()));
                        detalleCliente.setPlacasNota(txtPlacas.getText());


                        notaService.actualizarNota(notaRegistrar, ndRegistrar, detalleCliente);
                        return null;
                    }, (resultado) -> {
                        mostrarInformacion(
                                "Operación completada",
                                "Nota actualizada",
                                "Los cambios en la nota se han guardado correctamente en el sistema."
                        );
                    }, (excepcion) -> {

                        excepcion.printStackTrace();

                        if (excepcion.getCause() instanceof NotaException) {
                            mostrarError(
                                    "Error al guardar",
                                    "Se produjo un error al intentar guardar la información en la base de datos.",
                                    "" + excepcion.getMessage());

                        } else {
                            mostrarExcepcionThrowable(
                                    "Error inesperado",
                                    "Fallo al actualizar nota",
                                    "Ocurrió un problema interno al intentar actualizar la nota. Por favor, intente nuevamente más tarde.",
                                    excepcion
                            );
                        }

                    }, null
            );


        }//if guardar

    }//actualizarNota

    private void agregarNumFactura(NotaDTO notaEditar) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/AgregarNumFactura.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);

            Parent root = loader.load();
            AgregarNumFacturaController controller = loader.getController();
            controller.setNumFactura(notaEditar.getNotaId(), notaEditar.getNumFactura());

            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle("Agregar Numero de Factura");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

            actualizarDatosCliente(notaEditar);

        } catch (Exception e) {
            mostrarExcepcion(
                    "Error de carga",
                    "No se pudo cargar la vista",
                    "Ocurrió un problema técnico al intentar cargar la interfaz.",
                    e
            );


        }


    }//agregarNumFactura

    public NotaDTO getNotaEditar() {
        return this.notaEditar;
    }

    public void setNotaEditar(final NotaDTO notaEditar) {
        this.notaEditar = notaEditar;
    }

}//class
