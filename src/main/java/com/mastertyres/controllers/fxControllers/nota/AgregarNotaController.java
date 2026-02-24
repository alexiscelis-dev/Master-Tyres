package com.mastertyres.controllers.fxControllers.nota;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoader;
import com.mastertyres.common.utils.ApplicationContextProvider;
import com.mastertyres.common.utils.MenuContextSetting;
import com.mastertyres.common.utils.RegexTools;
import com.mastertyres.components.fxComponents.LoadingComponentController;
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
import org.springframework.stereotype.Component;

import static com.mastertyres.common.utils.FechaUtils.mostrarFechayHora;
import static com.mastertyres.common.utils.MensajesAlert.*;
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
        RegexTools.aplicarNumerosDecimalNota(txtTotal);
        RegexTools.aplicar24Horas(txtHoraEntrega);
        RegexTools.aplicar6Enteros(txtNumNota);
        RegexTools.aplicar2Enteros(txtDia);
        RegexTools.aplicar2Enteros(txtMes);
        RegexTools.aplicar4Enteros(txtAnio);
        RegexTools.aplicar4Enteros(txtAnioVehiculo);
        RegexTools.aplicarNumeroEntero(txtKms);

        RegexTools.aplicarNumeroEntero(txtAlineacionCantidad);
        RegexTools.aplicarNumeroEntero(txtBalanceoCantidad);
        RegexTools.aplicarNumeroEntero(txtLlantasCantidad);
        RegexTools.aplicarNumeroEntero(txtAmorDelCantidad);
        RegexTools.aplicarNumeroEntero(txtAmorTrasCantidad);
        RegexTools.aplicarNumeroEntero(txtSuspensionCantidad);
        RegexTools.aplicarNumeroEntero(txtSuspensionCantidad2);
        RegexTools.aplicarNumeroEntero(txtMecanicaCantidad);
        RegexTools.aplicarNumeroEntero(txtMecanicaCantidad2);
        RegexTools.aplicarNumeroEntero(txtFrenosCantidad);
        RegexTools.aplicarNumeroEntero(txtFrenosCantidad2);
        RegexTools.aplicarNumeroEntero(txtOtrosCantidad);
        RegexTools.aplicarNumeroEntero(txtOtrosCantidad2);

        RegexTools.aplicarNumerosDecimalNota(txtAlineacionUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtBalanceoUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtLlantasUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtAmorDelUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtAmorTrasUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtSuspensionUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtSuspensionUnitario2);
        RegexTools.aplicarNumerosDecimalNota(txtMecanicaUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtMecanicaUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtMecanicaUnitario2);
        RegexTools.aplicarNumerosDecimalNota(txtFrenosUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtFrenosUnitario2);
        RegexTools.aplicarNumerosDecimalNota(txtOtrosUnitario);
        RegexTools.aplicarNumerosDecimalNota(txtOtrosUnitario2);
        RegexTools.aplicarNumerosDecimalNota(txtAlineacionTotal);
        RegexTools.aplicarNumerosDecimalNota(txtBalanceoTotal);
        RegexTools.aplicarNumerosDecimalNota(txtLlantasTotal);
        RegexTools.aplicarNumerosDecimalNota(txtAmorDelTotal);
        RegexTools.aplicarNumerosDecimalNota(txtAmorTrasTotal);
        RegexTools.aplicarNumerosDecimalNota(txtSuspensionTotal);
        RegexTools.aplicarNumerosDecimalNota(txtSuspensionTotal2);
        RegexTools.aplicarNumerosDecimalNota(txtSubTotalFrenos);
        RegexTools.aplicarNumerosDecimalNota(txtSubTotalMecanica);
        RegexTools.aplicarNumerosDecimalNota(txtSubTotalOtros);
        RegexTools.aplicarNumerosDecimalNota(txtMecanicaTotal);
        RegexTools.aplicarNumerosDecimalNota(txtMecanicaTotal2);
        RegexTools.aplicarNumerosDecimalNota(txtFrenosTotal);
        RegexTools.aplicarNumerosDecimalNota(txtFrenosTotal2);
        RegexTools.aplicarNumerosDecimalNota(txtOtrosTotal);
        RegexTools.aplicarNumerosDecimalNota(txtOtrosTotal2);



        notaUtils.descripcionComponent(btnBuscarCliente, "Buscar cliente");
        notaUtils.descripcionComponent(btnBuscarLlanta, "Buscar llantas");
        notaUtils.descripcionComponent(btnBorrarCliente,"Eliminar Cliente de la nota");
        notaUtils.descripcionComponent(btnBorrarInventario,"Eliminar llanta de la nota");
        notaUtils.descripcionComponent(btnGuardar,"Guardar");
        notaUtils.descripcionComponent(btnRefrescar,"Refrescar");
        notaUtils.descripcionComponent(txtHoraEntrega,"Haz doble clic para modificar la Hora de Entrega");
        notaUtils.descripcionComponent(spPorcentajeGas,"Haz clic para modificar el porcentaje de Gasolina");
        notaUtils.descripcionComponent(hboxFecha,"Haz clic derecho para modificar la fecha");




    }//configuraciones

    @Override
    public void listeners() {


        operacionesCampos();

        hboxFecha.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY){
                notaUtils.mostrarPopupHora(txtNombre);
            }
        });

        spPorcentajeGas.setOnMouseClicked(event -> mostrarSlider(spPorcentajeGas.getScene().getWindow()));

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

        btnShowIcons.setOnMouseClicked(event -> notaUtils.showIcons(gridPaneIcons,SLIDE_FROM_TO,TIEMPO));

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
            mostrarError("Error de carga", "", "Ocurrio un problema al cargar la vista. Vuelva a intentarlo mas tarde.");
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
            mostrarError("Error de carga", "", "Ocurrió un problema al cargar la vista. Vuelva a intentarlo mas tarde.");

            e.printStackTrace();
        }

    }//buscarCliente

    protected void llenarNota(Cliente cliente) {
        if (cliente != null) {

            notaUtils.campoFormatter(cliente.getNombre() + " " +
                            (cliente.getApellido() != null ? cliente.getApellido() : "") + " " +
                            (cliente.getSegundoApellido() != null ? cliente.getSegundoApellido() : ""),
                    txtNombre,
                    CampoNota.NOMBRE

            );

            String domicilio = (cliente.getDomicilio() != null ? cliente.getDomicilio() : "") + " " +
                    (cliente.getCiudad() != null ? cliente.getCiudad() : "") + " " +
                    (cliente.getEstado() != null ? cliente.getEstado() : "");

            notaUtils.campoFormatter(domicilio,txtDireccion, txtDireccion2);

            notaUtils.campoFormatter(
                    cliente.getRfc() != null ? cliente.getRfc() : "",
                    txtRfc,
                    CampoNota.RFC

            );

            notaUtils.campoFormatter(
                    cliente.getCorreo() != null ? cliente.getCorreo() : "",
                    txtCorreo
            );


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
            habilitar(false, CampoNota.CLIENTE.toString());

            setVehiculoNota(vehiculos);

        }


    }//llenarNota

    private void llenarNota(Inventario inventario) {
        if (inventario != null) {
            txtLlantas.setText(inventario.getMarca() + " " + inventario.getModelo() + " " + " " + inventario.getMedida());

            if (inventario.getStock() == 0) {
                txtLlantasCantidad.setText("1");
                txtLlantasUnitario.setText(inventario.getPrecioVenta() + "");
            } else {
                txtLlantasCantidad.setText(inventario.getStock() + "");
                txtLlantasUnitario.setText(inventario.getPrecioVenta() + "");

            }


            habilitar(false, CampoNota.INVENTARIO.toString());

        }

        setInventarioNota(inventario);
        btnBorrarInventario.setDisable(false);


    }//llenarNota

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


    private void registrar(Cliente cRegistro, VehiculoDTO vRegistro, Inventario iRegistro) {
        boolean error = false;

        if (cRegistro == null) {
            mostrarWarning(
                    "Campos obligatorio",
                    "Cliente y Vehículo Faltantes ",
                    "Asocie un cliente y un vehículo antes de guardar la nota.");

            error = true;
            return;
        }

        if (txtNumNota.getText().trim().isEmpty()) {
            mostrarWarning("Campo obligatorio",
                    "Numero de nota ",
                    "El numero de nota es obligatorio. Por favor, ingrese un valor antes de continuar.");

            error = true;
            return;

        }

        if (txtTotal.getText().isBlank()) {
            mostrarWarning("Campo obligatorio",
                    "Total de la nota ",
                    "El total de la nota es obligatorio. Por favor, ingrese un valor antes de continuar.");

            error = true;
            return;
        }

        if (notaUtils.toFloatSafe(txtTotal.getText()) == 0) {
            mostrarWarning("Monto inválido ",
                    "",
                    "El total de la nota no puede ser cero. Agregue al menos un concepto o monto válido.");

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
                mostrarError("Error de carga",
                        "",
                        "Ocurrió un problema al cargar la vista. Vuelve a intentarlo mas tarde");

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


        habilitar(true, CampoNota.INVENTARIO.toString());
        habilitar(true, CampoNota.CLIENTE.toString());

    }//refrescar

    //pone en null stock de inventario para desasociar la relacion Entidad nota-inventario
    @FXML
    private void eliminarInventario() {


        boolean confirmar = mostrarConfirmacion(
                "Eliminar llanta de Nota",
                "La llanta seleccionada dejará de estar asociada a la nota.\n",
                "Esto no eliminará la llanta del inventario, solo la relación con la nota.\n" +
                        "¿Deseas continuar?",
                "Continuar",
                "Cancelar"
        );

        if (confirmar) {
            setInventarioNota(null);
            txtLlantas.setText("");
            txtLlantasCantidad.setText("");
            txtLlantasUnitario.setText("");
            txtLlantasTotal.setText("");
            btnBorrarInventario.setDisable(true);

        }

    }//eliminarInventario

    //Pone en null cliente y vehiculo para desasocial las relaciones de entidades Nota - vehiculo- Cliente
    @FXML
    private void eliminarCliente() {


        boolean confirmar = mostrarConfirmacion(
                "Eliminar cliente de Nota",
                "El cliente y el vehículo dejarán de estar asociados a la nota.\n",
                "Esto no eliminará al cliente ni el vehículo del sistema, solo la relación con la nota.\n" +
                        "¿Deseas continuar?\n\n",
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
