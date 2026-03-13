package com.mastertyres.controllers.fxControllers.nota;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.common.interfaces.ICleanable;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.utils.ApplicationContextProvider;
import com.mastertyres.common.utils.MensajesAlert;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.common.utils.RegexTools;
import com.mastertyres.components.fxComponents.loader.LoadingComponentController;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.nota.model.BaseNota;
import com.mastertyres.nota.model.CampoNota;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.mastertyres.common.utils.FechaUtils.mostrarFechayHora;

@Component
public class AgregarNotaController extends BaseNota implements IFxController, ILoader {
    @FXML
    private Button btnBuscarCliente;
    @FXML
    private Button btnBuscarLlanta;
    @FXML
    private Button btnBorrarCliente;
    @FXML
    private Button btnBorrarInventario;

    //caracteres que caben en cada campo
    @Value("${mastertyres.ui.nota.direccion.campoNombre.maxlength}")
    private int campoNombre;
    @Value("${mastertyres.ui.nota.direccion.campoDireccion1.maxlength}")
    private int campoDireccion1;
    @Value("${mastertyres.ui.nota.direccion.campoDireccion2.maxlength}")
    private int campoDireccion2;
    @Value("${mastertyres.ui.nota.direccion.campoRfc.maxlength}")
    private int campoRfc;
    @Value("${mastertyres.ui.nota.direccion.campoCorreo.maxlength}")
    private int campoCorreo;
    @Value("${mastertyres.ui.nota.direccion.campoMarca.maxlength}")
    private int campoMarca;
    @Value("${mastertyres.ui.nota.direccion.campoModelo.maxlength}")
    private int campoModelo;
    @Value("${mastertyres.ui.nota.direccion.campoKilometros.maxlength}")
    private int campoKilometros;


    private BooleanProperty boolTotal = new SimpleBooleanProperty(true);
    private LoadingComponentController loadingOverlayController;
    private final Duration TIEMPO = Duration.millis(500);
    private final int FROM = -50;
    private final int TO = 0;
    private final double[] SLIDE_FROM_TO = {FROM, TO};

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

        dibujarGasolina(50);

        notaUtils.mostrarPopupHora(txtHoraEntrega);

        //Iniciliza la fecha y hora del dia y sistema
        mostrarFechayHora(txtDia, txtMes, txtAnio, txtHoraEntrega);

        //deshabilita los menu del clic derecho en los campos de texto
        MenuContextSetting.disableMenu(rootPane);

        //validaciones Regex
        //RegexTools.aplicarNumerosDecimalNota(txtTotal);
        RegexTools.aplicar24Horas(txtHoraEntrega);
        RegexTools.aplicar6Enteros(txtNumNota);
        RegexTools.aplicar2Enteros(txtDia);
        RegexTools.aplicar2Enteros(txtMes);
        RegexTools.aplicar4Enteros(txtAnio);
        RegexTools.aplicar4Enteros(txtAnioVehiculo);
        RegexTools.aplicarNumeroEntero(txtKms);

//        RegexTools.aplicarNumeroEntero(txtAlineacionCantidad);
//        RegexTools.aplicarNumeroEntero(txtBalanceoCantidad);
//        RegexTools.aplicarNumeroEntero(txtLlantasCantidad);
//        RegexTools.aplicarNumeroEntero(txtAmorDelCantidad);
//        RegexTools.aplicarNumeroEntero(txtAmorTrasCantidad);
//        RegexTools.aplicarNumeroEntero(txtSuspensionCantidad);
//        RegexTools.aplicarNumeroEntero(txtSuspensionCantidad2);
//        RegexTools.aplicarNumeroEntero(txtMecanicaCantidad);
//        RegexTools.aplicarNumeroEntero(txtMecanicaCantidad2);
//        RegexTools.aplicarNumeroEntero(txtFrenosCantidad);
//        RegexTools.aplicarNumeroEntero(txtFrenosCantidad2);
//        RegexTools.aplicarNumeroEntero(txtOtrosCantidad);
//        RegexTools.aplicarNumeroEntero(txtOtrosCantidad2);

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

//        RegexTools.aplicarNumerosDecimalNota(txtAlineacionUnitario);
//        RegexTools.aplicarNumerosDecimalNota(txtBalanceoUnitario);
//        RegexTools.aplicarNumerosDecimalNota(txtLlantasUnitario);
//        RegexTools.aplicarNumerosDecimalNota(txtAmorDelUnitario);
//        RegexTools.aplicarNumerosDecimalNota(txtAmorTrasUnitario);
//        RegexTools.aplicarNumerosDecimalNota(txtSuspensionUnitario);
//        RegexTools.aplicarNumerosDecimalNota(txtSuspensionUnitario2);
//        RegexTools.aplicarNumerosDecimalNota(txtMecanicaUnitario);
//        RegexTools.aplicarNumerosDecimalNota(txtMecanicaUnitario);
//        RegexTools.aplicarNumerosDecimalNota(txtMecanicaUnitario2);
//        RegexTools.aplicarNumerosDecimalNota(txtFrenosUnitario);
//        RegexTools.aplicarNumerosDecimalNota(txtFrenosUnitario2);
//        RegexTools.aplicarNumerosDecimalNota(txtOtrosUnitario);
//        RegexTools.aplicarNumerosDecimalNota(txtOtrosUnitario2);
//        RegexTools.aplicarNumerosDecimalNota(txtAlineacionTotal);
//        RegexTools.aplicarNumerosDecimalNota(txtBalanceoTotal);
//        RegexTools.aplicarNumerosDecimalNota(txtLlantasTotal);
//        RegexTools.aplicarNumerosDecimalNota(txtAmorDelTotal);
//        RegexTools.aplicarNumerosDecimalNota(txtAmorTrasTotal);
//        RegexTools.aplicarNumerosDecimalNota(txtSuspensionTotal);
//        RegexTools.aplicarNumerosDecimalNota(txtSuspensionTotal2);
//        RegexTools.aplicarNumerosDecimalNota(txtSubTotalFrenos);
//        RegexTools.aplicarNumerosDecimalNota(txtSubTotalMecanica);
//        RegexTools.aplicarNumerosDecimalNota(txtSubTotalOtros);
//        RegexTools.aplicarNumerosDecimalNota(txtMecanicaTotal);
//        RegexTools.aplicarNumerosDecimalNota(txtMecanicaTotal2);
//        RegexTools.aplicarNumerosDecimalNota(txtFrenosTotal);
//        RegexTools.aplicarNumerosDecimalNota(txtFrenosTotal2);
//        RegexTools.aplicarNumerosDecimalNota(txtOtrosTotal);
//        RegexTools.aplicarNumerosDecimalNota(txtOtrosTotal2);

        // Decimales con límite de BD
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

        notaUtils.descripcionComponent(btnBuscarCliente, "Buscar cliente");
        notaUtils.descripcionComponent(btnBuscarLlanta, "Buscar llantas");
        notaUtils.descripcionComponent(btnBorrarCliente, "Eliminar Cliente de la nota");
        notaUtils.descripcionComponent(btnBorrarInventario, "Eliminar llanta de la nota");
        notaUtils.descripcionComponent(btnGuardar, "Guardar");
        notaUtils.descripcionComponent(btnRefrescar, "Refrescar");
        notaUtils.descripcionComponent(txtHoraEntrega, "Clic derecho para modificar.");
        notaUtils.descripcionComponent(spPorcentajeGas, "Clic derecho para modificar.");

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

    }//configuraciones

    @Override
    public void listeners() {

        operacionesCampos();

        spPorcentajeGas.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY)
                mostrarSlider(spPorcentajeGas.getScene().getWindow());
        });

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

        btnShowIcons.setOnMouseClicked(event -> notaUtils.showIcons(gridPaneIcons, SLIDE_FROM_TO, TIEMPO));

        btnGuardar.setOnAction(event -> registrar(clienteNota, vehiculosNota, inventarioNota));

    }//listeners

    @FXML
    private void buscarCliente(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/BuscarCliente.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            Stage stage = new Stage(StageStyle.UTILITY);
            BuscarClienteController controller = loader.getController();


            stage.setTitle("Buscar cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.showAndWait();

            Cliente cliente = controller.getClienteSeleccionado();
            llenarNota(cliente);

            VehiculoDTO vehiculos = controller.getVehiculoSeleccionado();
            llenarNota(vehiculos);


        } catch (Exception e) {
            MensajesAlert.mostrarExcepcion(
                    "Error de carga",
                    "No se pudo inicializar la interfaz",
                    "Ocurrió un problema al intentar cargar la vista. Por favor, inténtelo de nuevo más tarde.",
                    e
            );
        e.printStackTrace();
        }

    }//buscarCliente

    @FXML
    private void buscarLlanta(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/BuscarLlanta.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            Stage stage = new Stage(StageStyle.UTILITY);
            BuscarLlantaController controller = loader.getController();

            stage.setTitle("Buscar llantas");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.showAndWait();
            Inventario inventario = controller.getLlantaSeleccionada();
            llenarNota(inventario);


        } catch (Exception e) {
            MensajesAlert.mostrarExcepcion(
                    "Error de carga",
                    "No se pudo inicializar la interfaz",
                    "Ocurrió un problema al intentar cargar la vista. Por favor, inténtelo de nuevo más tarde.",
                    e
            );
            e.printStackTrace();
        }

    }//buscarCliente

    protected void llenarNota(Cliente cliente) {
        if (cliente != null) {

            /*

            notaUtils.campoFormatter(cliente.getNombre() + " " +
                            (cliente.getApellido() != null ? cliente.getApellido() : "") + " " +
                            (cliente.getSegundoApellido() != null ? cliente.getSegundoApellido() : ""),
                    txtNombre,
                    CampoNota.NOMBRE

            );

             */

            txtNombre.setText(
                    cliente.getNombre() + " " +
                            (cliente.getApellido() != null ? cliente.getApellido() : "") + " " +
                            (cliente.getSegundoApellido() != null ? cliente.getSegundoApellido() : "")
            );

            String domicilio = (cliente.getDomicilio() != null ? cliente.getDomicilio() : "") + " " +
                    (cliente.getCiudad() != null ? cliente.getCiudad() : "") + " " +
                    (cliente.getEstado() != null ? cliente.getEstado() : "");

            notaUtils.campoFormatter(domicilio, txtDireccion, txtDireccion2);

            txtRfc.setText(cliente.getRfc() != null ? cliente.getRfc() : "");

            txtCorreo.setText(cliente.getCorreo() != null ? cliente.getCorreo() : "");

            /*
            notaUtils.campoFormatter(
                    cliente.getRfc() != null ? cliente.getRfc() : "",
                    txtRfc,
                    CampoNota.RFC

            );

             */

            /*
            notaUtils.campoFormatter(
                    cliente.getCorreo() != null ? cliente.getCorreo() : "",
                    txtCorreo
            );

             */
            habilitarCampo();

        }
        setClienteNota(cliente);
        btnBorrarCliente.setDisable(false);

    }//llenarNota

    private void llenarNota(VehiculoDTO vehiculos) {

        if (vehiculos != null) {
            notaUtils.campoFormatter(vehiculos.getNombreMarca(),
                    txtMarca,
                    CampoNota.MARCA
            );

            notaUtils.campoFormatter(vehiculos.getNombreModelo(),
                    txtModelo,
                    CampoNota.MODELO
            );

            txtAnioVehiculo.setText(vehiculos.getAnio() + "");

            notaUtils.campoFormatter(
                    vehiculos.getKilometros() + "",
                    txtKms,
                    CampoNota.KILOMETROS

            );
            txtPlacas.setText(vehiculos.getPlacas());


            setVehiculoNota(vehiculos);
            habilitarCampo();

        }


    }//llenarNota

    private void llenarNota(Inventario inventario) {
        if (inventario != null) {
            txtLlantas.setText(inventario.getMarca() + " " + inventario.getModelo() + " " + " " + inventario.getMedida());

            if (inventario.getStock() == 0) {
                txtLlantasCantidad.setText("1");
                txtLlantasUnitario.setText(inventario.getPrecioVenta() + "");
                txtLlantasCantidad.setEditable(false);
            } else {
                txtLlantasCantidad.setText(inventario.getStock() + "");
                txtLlantasUnitario.setText(inventario.getPrecioVenta() + "");
                txtLlantasCantidad.setEditable(false);

            }


        }

        setInventarioNota(inventario);
        btnBorrarInventario.setDisable(false);


    }//llenarNota

    /*
    private void habilitar(boolean habilitar, String opcion) {

        switch (opcion) {
            case "CLIENTE" -> {
                txtMarca.setEditable(habilitar);
                txtModelo.setEditable(habilitar);
                txtAnioVehiculo.setEditable(habilitar);
                txtKms.setEditable(habilitar);
                txtPlacas.setEditable(habilitar);
                txtRfc.setEditable(habilitar);
                txtNombre.setEditable(habilitar);
                txtDireccion.setEditable(habilitar);
                txtDireccion2.setEditable(habilitar);
                txtCorreo.setEditable(habilitar);

            }
            case "INVENTARIO" -> {
                txtLlantas.setEditable(habilitar);
                txtLlantasCantidad.setEditable(habilitar);
            }

        }//switch

    }//habilitar

     */

    private void habilitarCampo() {

        if (txtNombre.getText().length() > campoNombre) {
            txtNombre.setEditable(true);
        }
        if (txtDireccion.getText().length() > campoDireccion1) {
            txtDireccion.setEditable(true);
        }
        if (txtDireccion2.getText().length() > campoDireccion2) {
            txtDireccion2.setEditable(true);
        }
        if (txtRfc.getText().length() > campoRfc) {
            txtRfc.setEditable(true);
        }
        if (txtCorreo.getText().length() > campoCorreo) {
            txtCorreo.setEditable(true);
        }
        if (txtMarca.getText().length() > campoMarca) {
            txtMarca.setEditable(true);
        }
        if (txtModelo.getText().length() > campoModelo) {
            txtModelo.setEditable(true);
        }


    }//habilitar


    private void registrar(Cliente cRegistro, VehiculoDTO vRegistro, Inventario iRegistro) {
        boolean error = false;

        if (cRegistro == null) {
            MensajesAlert.mostrarWarning(
                    "Datos incompletos",
                    "Cliente y vehículo no asociados",
                    "Debe asociar un cliente y un vehículo antes de proceder a guardar la nota."
            );

            error = true;
            return;
        }

        if (txtNumNota.getText().trim().isEmpty()) {
            MensajesAlert.mostrarWarning(
                    "Datos incompletos",
                    "Número de nota obligatorio",
                    "El número de nota es obligatorio. Por favor, ingrese un valor válido antes de continuar."
            );

            error = true;
            return;

        }

        if (txtTotal.getText().isBlank()) {
            MensajesAlert.mostrarWarning(
                    "Datos incompletos",
                    "Monto total obligatorio",
                    "El total de la nota es obligatorio. Por favor, ingrese un valor antes de continuar."
            );

            error = true;
            return;
        }

        if (notaUtils.toFloatSafe(txtTotal.getText()) == 0) {
            MensajesAlert.mostrarWarning(
                    "Advertencia",
                    "Monto inválido",
                    "El total de la nota no puede ser cero. Por favor, agregue al menos un concepto o monto válido."
            );

            error = true;
            return;
        }


        if (!error) {

            checkCheckBoxes();


            try {


                String fechaYhora = txtAnio.getText() + "-" + txtMes.getText() + "-" + txtDia.getText() + " " + txtHoraEntrega.getText() + ":00";
                String nombre = cRegistro.getNombre() + " " + (cRegistro.getApellido() != null ? cRegistro.getApellido() : "") + " " +
                        (cRegistro.getSegundoApellido() != null ? cRegistro.getSegundoApellido() : "");

                NotaDTO.NotaDTOBuilder nuevaNotaBuilder = NotaDTO.builder()


                        .clienteId(cRegistro.getClienteId())
                        .vehiculoId(vRegistro.getId())
                        .nombreClienteNota(nombre)
                        .direccion1Nota(txtDireccion.getText())
                        .direccion2Nota(txtDireccion2.getText())
                        .rfcNota(cRegistro.getRfc())
                        .correoNota(cRegistro.getCorreo())
                        .marcaNota(vRegistro.getNombreMarca())
                        .modeloNota(vRegistro.getNombreModelo())
                        .categoriaNota(vRegistro.getNombreCategoria())
                        .anioNota(vRegistro.getAnio())
                        .kilometrosNota(vRegistro.getKilometros())
                        .placasNota(vRegistro.getPlacas())

                        .numNota(txtNumNota.getText())
                        .numFactura("")
                        .fechaYHora(fechaYhora)
                        .fechaVencimiento("")
                        .total(notaUtils.toFloatSafe(txtTotal.getText())) // datos nota

                        //obserbaciones
                        .observaciones(txtObservaciones.getText())
                        .observaciones2(txtObservaciones2.getText())

                        //porcentaje gasolina
                        .porcentajeGas(getPorcentajeGasNota())
                        //checkBox
                        .rayones(getRayones())
                        .golpes(getGolpes())
                        .tapones(getTapones())
                        .tapetes(getTapetes())
                        .radio(getRadio())
                        .gato(getGato())
                        .llave(getLlave())
                        .llanta(getLlanta())
                        //nota
                        .alineacion(txtAlineacion.getText())
                        .alineacionCantidad(notaUtils.toIntSafe(txtAlineacionCantidad.getText()))
                        .alineacionUnitario(notaUtils.toFloatSafe(txtAlineacionUnitario.getText()))
                        .alineacionTotal(notaUtils.toFloatSafe(txtAlineacionTotal.getText()))
                        .balanceo(txtBalanceo.getText())
                        .balanceoCantidad(notaUtils.toIntSafe(txtBalanceoCantidad.getText()))
                        .balanceoUnitario(notaUtils.toFloatSafe(txtBalanceoUnitario.getText()))
                        .balanceoTotal(notaUtils.toFloatSafe(txtBalanceoTotal.getText()))
                        //inventario o llantas
                        .llantaCampo(txtLlantas.getText())
                        .llantaCantidad(notaUtils.toIntSafe(txtLlantasCantidad.getText()))
                        .llantaUnitario(notaUtils.toFloatSafe(txtLlantasUnitario.getText()))
                        .llantaTotal(notaUtils.toFloatSafe(txtLlantasTotal.getText()))
                        //
                        .amorDelanteros(txtAmorDelanteros.getText())
                        .amorDelCantidad(notaUtils.toIntSafe(txtAmorDelCantidad.getText()))
                        .amorDelUnitario(notaUtils.toFloatSafe(txtAmorDelUnitario.getText()))
                        .amorDelTotal(notaUtils.toFloatSafe(txtAmorDelTotal.getText()))

                        .amorTraseros(txtAmorTraseros.getText())
                        .amorTrasCantidad(notaUtils.toIntSafe(txtAmorDelCantidad.getText()))
                        .amorTrasUnitario(notaUtils.toFloatSafe(txtAmorTrasUnitario.getText()))
                        .amorTrasTotal(notaUtils.toFloatSafe(txtAmorTrasTotal.getText()))

                        .suspension(txtSuspension.getText())
                        .suspensionCantidad(notaUtils.toIntSafe(txtSuspensionCantidad.getText()))
                        .suspensionUnitario(notaUtils.toFloatSafe(txtSuspensionUnitario.getText()))
                        .suspensionTotal(notaUtils.toFloatSafe(txtSuspensionTotal.getText()))

                        .suspension2(txtSuspension2.getText())
                        .suspensionCantidad2(notaUtils.toIntSafe(txtSuspensionCantidad2.getText()))
                        .suspensionUnitario2(notaUtils.toFloatSafe(txtSuspensionUnitario2.getText()))
                        .suspensionTotal2(notaUtils.toFloatSafe(txtSuspensionTotal2.getText()))

                        .mecanica(txtMecanica.getText())
                        .mecanicaCantidad(notaUtils.toIntSafe(txtMecanicaCantidad.getText()))
                        .mecanicaUnitario(notaUtils.toFloatSafe(txtMecanicaUnitario.getText()))
                        .mecanicaTotal(notaUtils.toFloatSafe(txtMecanicaTotal.getText()))

                        .mecanica2(txtMecanica2.getText())
                        .mecanicaCantidad2(notaUtils.toIntSafe(txtMecanicaCantidad2.getText()))
                        .mecanicaUnitario2(notaUtils.toFloatSafe(txtMecanicaUnitario2.getText()))
                        .mecanicaTotal2(notaUtils.toFloatSafe(txtMecanicaTotal2.getText()))

                        .frenos(txtFrenos.getText())
                        .frenosCantidad(notaUtils.toIntSafe(txtFrenosCantidad.getText()))
                        .frenosUnitario(notaUtils.toFloatSafe(txtFrenosUnitario.getText()))
                        .frenosTotal(notaUtils.toFloatSafe(txtFrenosTotal.getText()))

                        .frenos2(txtFrenos2.getText())
                        .frenosCantidad2(notaUtils.toIntSafe(txtFrenosCantidad2.getText()))
                        .frenosUnitario2(notaUtils.toFloatSafe(txtFrenosUnitario2.getText()))
                        .frenosTotal2(notaUtils.toFloatSafe(txtFrenosTotal2.getText()))

                        .otros(txtOtros.getText())
                        .otrosCantidad(notaUtils.toIntSafe(txtOtrosCantidad.getText()))
                        .otrosUnitario(notaUtils.toFloatSafe(txtOtrosUnitario.getText()))
                        .otrosTotal(notaUtils.toFloatSafe(txtOtrosTotal.getText()))

                        .otros2(txtOtros2.getText())
                        .otrosCantidad2(notaUtils.toIntSafe(txtOtrosCantidad2.getText()))
                        .otrosUnitario2(notaUtils.toFloatSafe(txtOtrosUnitario2.getText()))
                        .otrosTotal2(notaUtils.toFloatSafe(txtOtrosTotal2.getText()))

                        //subtotales de la parte de la derecha
                        .subTotalFrenos(notaUtils.toFloatSafe(txtSubTotalFrenos.getText()))
                        .subTotalMecanica(notaUtils.toFloatSafe(txtSubTotalMecanica.getText()))
                        .subTotalOtros(notaUtils.toFloatSafe(txtSubTotalOtros.getText()));

                if (iRegistro != null) {
                    nuevaNotaBuilder.inventarioId(iRegistro.getInventarioId());
                } else {
                    nuevaNotaBuilder.inventarioId(null);
                }

                NotaDTO nuevaNota = nuevaNotaBuilder.build();


                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/nota/RegistrarNota.fxml"));
                loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
                Parent root = loader.load();

                RegistrarNotaController controller = loader.getController();
                controller.agregarNota(nuevaNota);
                controller.setInitializeLoading(loadingOverlayController);
                controller.setOnRegistroCompleto(() -> refrescar(null));


                Stage stage = new Stage(StageStyle.UTILITY);


                stage.setTitle("Registrar Nota");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.showAndWait();

            } catch (Exception e) {
                MensajesAlert.mostrarExcepcion(
                        "Error de carga",
                        "No se pudo inicializar la interfaz",
                        "Ocurrió un problema al intentar cargar la vista. Por favor, inténtelo de nuevo más tarde.",
                        e
                );

                e.printStackTrace();
            }

        }


    }//registrar

    @FXML
    private void refrescar(ActionEvent event) {
        clienteNota = null;
        vehiculosNota = null;
        inventarioNota = null;
        btnBorrarInventario.setDisable(true);
        btnBorrarCliente.setDisable(true);

        txtNumNota.setText("");
        mostrarFechayHora(txtDia, txtMes, txtAnio, txtHoraEntrega);
        txtNombre.setText("");
        txtDireccion.setText("");
        txtDireccion2.setText("");
        txtRfc.setText("");
        txtCorreo.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtAnioVehiculo.setText("");
        txtKms.setText("");
        txtPlacas.setText("");
        cbRayonesSi.setSelected(false);
        cbRayonesNo.setSelected(false);
        cbGolpesSi.setSelected(false);
        cbGolpesNo.setSelected(false);
        cbTaponesSi.setSelected(false);
        cbTaponesNo.setSelected(false);
        cbTapetesSi.setSelected(false);
        cbTapetesNo.setSelected(false);
        cbRadioSi.setSelected(false);
        cbRadioNo.setSelected(false);
        cbGatoSi.setSelected(false);
        cbGatoNo.setSelected(false);
        cbLlaveSi.setSelected(false);
        cbLlaveNo.setSelected(false);
        cbLlantaSi.setSelected(false);
        cbLlantaNo.setSelected(false);
        txtObservaciones.setText("");
        txtObservaciones2.setText("");
        dibujarGasolina(50);
        txtAlineacion.setText("");
        txtAlineacionCantidad.setText("");
        txtAlineacionUnitario.setText("");
        txtAlineacionTotal.setText("");
        txtBalanceo.setText("");
        txtBalanceoCantidad.setText("");
        txtBalanceoUnitario.setText("");
        txtBalanceoTotal.setText("");
        txtLlantas.setText("");
        txtLlantasCantidad.setText("");
        txtLlantasCantidad.setEditable(true);
        txtLlantasUnitario.setText("");
        txtLlantasTotal.setText("");
        txtAmorDelanteros.setText("");
        txtAmorDelCantidad.setText("");
        txtAmorDelUnitario.setText("");
        txtAmorDelTotal.setText("");
        txtAmorTraseros.setText("");
        txtAmorTrasCantidad.setText("");
        txtAmorTrasUnitario.setText("");
        txtAmorTrasTotal.setText("");
        txtSuspension.setText("");
        txtSuspensionCantidad.setText("");
        txtSuspensionUnitario.setText("");
        txtSuspensionTotal.setText("");
        txtSuspension2.setText("");
        txtSuspensionCantidad2.setText("");
        txtSuspensionUnitario2.setText("");
        txtSuspensionTotal2.setText("");
        txtMecanica.setText("");
        txtMecanicaCantidad.setText("");
        txtMecanicaUnitario.setText("");
        txtMecanicaTotal.setText("");
        txtMecanica2.setText("");
        txtMecanicaCantidad2.setText("");
        txtMecanicaUnitario2.setText("");
        txtMecanicaTotal2.setText("");
        txtFrenos.setText("");
        txtFrenosCantidad.setText("");
        txtFrenosUnitario.setText("");
        txtFrenosTotal.setText("");
        txtFrenos2.setText("");
        txtFrenosCantidad2.setText("");
        txtFrenosUnitario2.setText("");
        txtFrenosTotal2.setText("");
        txtOtros.setText("");
        txtOtrosCantidad.setText("");
        txtOtrosUnitario.setText("");
        txtOtrosTotal.setText("");
        txtOtros2.setText("");
        txtOtrosCantidad2.setText("");
        txtOtrosUnitario2.setText("");
        txtOtrosTotal2.setText("");
        txtSubTotalFrenos.setText("");
        txtSubTotalMecanica.setText("");
        txtSubTotalOtros.setText("");
        txtTotal.setText("");


    }//refrescar

    //pone en null stock de inventario para desasociar la relacion Entidad nota-inventario
    @FXML
    private void eliminarInventario() {


        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar eliminación",
                "Desvincular llanta",
                "¿Está seguro de que desea desvincular la llanta seleccionada de la nota?\nEsto no eliminará la llanta del inventario, solo su relación con esta nota.",
                "Continuar",
                "Cancelar"
        );

        if (confirmar) {
            setInventarioNota(null);
            txtLlantas.setText("");
            txtLlantasCantidad.setText("");
            txtLlantasCantidad.setEditable(true);
            txtLlantasUnitario.setText("");
            txtLlantasTotal.setText("");
            btnBorrarInventario.setDisable(true);

        }

    }//eliminarInventario

    //Pone en null cliente y vehiculo para desasocial las relaciones de entidades Nota - vehiculo- Cliente
    @FXML
    private void eliminarCliente() {


        boolean confirmar = MensajesAlert.mostrarConfirmacion(
                "Confirmar eliminación",
                "Desvincular cliente y vehículo",
                "¿Está seguro de que desea desvincular al cliente y el vehículo de esta nota?\nEsta acción no eliminará sus registros del sistema.",
                "Continuar",
                "Cancelar"
        );


        if (confirmar) {
            setClienteNota(null);
            setVehiculoNota(null);

            txtNombre.setText("");
            txtDireccion.setText("");
            txtDireccion2.setText("");
            txtRfc.setText("");
            txtCorreo.setText("");
            mostrarFechayHora(txtDia, txtMes, txtAnio, txtHoraEntrega);
            txtMarca.setText("");
            txtModelo.setText("");
            txtAnioVehiculo.setText("");
            txtKms.setText("");
            txtPlacas.setText("");
            btnBorrarCliente.setDisable(true);
        }
    }//eliminarCliente

    //getters y setters

    public void setClienteNota(Cliente clienteNota) {
        this.clienteNota = clienteNota;
    }

    public void setInventarioNota(Inventario inventarioNota) {
        this.inventarioNota = inventarioNota;
    }

    private void setVehiculoNota(VehiculoDTO vehiculos) {
        this.vehiculosNota = vehiculos;
    }


}//class
