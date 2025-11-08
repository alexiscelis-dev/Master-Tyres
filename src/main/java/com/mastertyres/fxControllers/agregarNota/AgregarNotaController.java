package com.mastertyres.fxControllers.agregarNota;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.common.ApplicationContextProvider;
import com.mastertyres.common.MenuContextSetting;
import com.mastertyres.fxControllers.nota.BuscarClienteController;
import com.mastertyres.fxControllers.nota.BuscarLlantaController;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mastertyres.common.MensajesAlert.mostrarError;
import static com.mastertyres.common.MensajesAlert.mostrarWarning;

@Component
public class AgregarNotaController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private GridPane gridPaneIcons;
    @FXML
    private StackPane btnShowIcons;
    @FXML
    private TextField txtDia;
    @FXML
    private TextField txtMes;
    @FXML
    private TextField txtAnio;
    @FXML
    private StackPane spPorcentajeGas;
    @FXML
    private TextField txtHoraEntrega;
    @FXML
    private TextField txtNumNota;
    private Arc arcoGas;
    @FXML
    private Canvas canvasGas;
    @FXML
    private Label lblGas;

    //Datos Clientes
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtDireccion2;
    @FXML
    private TextField txtRfc;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtMarca;
    @FXML
    private TextField txtModelo;
    @FXML
    private TextField txtCategoria;
    @FXML
    private TextField txtAnioVehiculo;
    @FXML
    private TextField txtKms;
    @FXML
    private TextField txtPlacas;

    @FXML
    private TextField txtObservaciones;
    @FXML
    private TextField txtObservaciones2;

    //seccion cuando se busca a la llanta
    //Datos nota detalle

    @FXML
    private CheckBox cbRayonesSi;
    @FXML
    private CheckBox cbRayonesNo;
    @FXML
    private CheckBox cbGolpesSi;
    @FXML
    private CheckBox cbGolpesNo;
    @FXML
    private CheckBox cbTaponesSi;
    @FXML
    private CheckBox cbTaponesNo;
    @FXML
    private CheckBox cbTapetesSi;
    @FXML
    private CheckBox cbTapetesNo;
    @FXML
    private CheckBox cbRadioSi;
    @FXML
    private CheckBox cbRadioNo;
    @FXML
    private CheckBox cbGatoSi;
    @FXML
    private CheckBox cbGatoNo;
    @FXML
    private CheckBox cbLlaveSi;
    @FXML
    private CheckBox cbLlaveNo;
    @FXML
    private CheckBox cbLlantaSi;
    @FXML
    private CheckBox cbLlantaNo;
    @FXML
    private CheckBox cbCbRadioSi;
    @FXML
    private CheckBox cbCbRadioNo;
    @FXML
    private TextField txtAlineacion;
    @FXML
    private TextField txtAlineacionCantidad;
    @FXML
    private TextField txtAlineacionUnitario;
    @FXML
    private TextField txtAlineacionTotal;
    @FXML
    private TextField txtBalanceo;
    @FXML
    private TextField txtBalanceoCantidad;
    @FXML
    private TextField txtBalanceoUnitario;
    @FXML
    private TextField txtBalanceoTotal;
    @FXML
    private TextField txtLlantas;
    @FXML
    private TextField txtLlantasCantidad;
    @FXML
    private TextField txtLlantasUnitario;
    @FXML
    private TextField txtLlantasTotal;
    @FXML
    private TextField txtAmorDelanteros;
    @FXML
    private TextField txtAmorDelCantidad;
    @FXML
    private TextField txtAmorDelUnitario;
    @FXML
    private TextField txtAmorDelTotal;
    @FXML
    private TextField txtAmorTraseros;
    @FXML
    private TextField txtAmorTrasCantidad;
    @FXML
    private TextField txtAmorTrasUnitario;
    @FXML
    private TextField txtAmorTrasTotal;
    @FXML
    private TextField txtSuspension;
    @FXML
    private TextField txtSuspensionCantidad;
    @FXML
    private TextField txtSuspensionUnitario;
    @FXML
    private TextField txtSuspensionTotal;
    @FXML
    private TextField txtSuspension2;
    @FXML
    private TextField txtSuspensionCantidad2;
    @FXML
    private TextField txtSuspensionUnitario2;
    @FXML
    private TextField txtSuspensionTotal2;
    @FXML
    private TextField txtMecanica;
    @FXML
    private TextField txtMecanicaCantidad;
    @FXML
    private TextField txtMecanicaUnitario;
    @FXML
    private TextField txtMecanicaTotal;
    @FXML
    private TextField txtMecanica2;
    @FXML
    private TextField txtMecanicaCantidad2;
    @FXML
    private TextField txtMecanicaUnitario2;
    @FXML
    private TextField txtMecanicaTotal2;
    @FXML
    private TextField txtFrenos;
    @FXML
    private TextField txtFrenosCantidad;
    @FXML
    private TextField txtFrenosUnitario;
    @FXML
    private TextField txtFrenosTotal;
    @FXML
    private TextField txtFrenos2;
    @FXML
    private TextField txtFrenosCantidad2;
    @FXML
    private TextField txtFrenosUnitario2;
    @FXML
    private TextField txtFrenosTotal2;
    @FXML
    private TextField txtSubTotalMecanica;  //subtotal mecanica
    @FXML
    private TextField txtSubTotalFrenos;  //subtotal frenos
    @FXML
    private TextField txtSubTotalOtros;  //subtotal otros
    @FXML
    private TextField txtOtros;
    @FXML
    private TextField txtOtrosCantidad;
    @FXML
    private TextField txtOtrosUnitario;
    @FXML
    private TextField txtOtros2;
    @FXML
    private TextField txtOtrosCantidad2;
    @FXML
    private TextField txtOtrosUnitario2;
    @FXML
    private TextField txtOtrosTotal2;
    @FXML
    private TextField txtOtrosTotal;
    @FXML
    private TextField txtTotal; //total de la nota


    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnRefrescar;

    private Cliente clienteNota;
    private Inventario inventarioNota;


    @FXML
    private void initialize() {

        txtHoraEntrega.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("^(?:[01]\\d|2[0-3]):[0-5]\\d$")) {
                return change;
            }
            return null;
        }));
        MenuContextSetting.disableMenu(rootPane);
        btnShowIcons.setOnMouseClicked(event -> showIcons());
        mostrarFechayHora();
        // spPorcentajeGas.setOnMouseClicked( );
        txtNumNota.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,6}")) {
                return change;
            }
            return null;
        }));
        txtDia.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,2}")) {
                return change;
            }
            return null;
        }));
        txtMes.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,2}")) {
                return change;
            }
            return null;
        }));
        txtAnio.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,4}")) {
                return change;
            }
            return null;
        }));
        txtAnioVehiculo.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d{0,4}")) {
                return change;
            }
            return null;
        }));
        txtKms.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            }
            return null;
        }));
        spPorcentajeGas.setOnMouseClicked(event -> mostrarSlider(spPorcentajeGas.getScene().getWindow()));
        dibujarGasolina(50);

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

        btnGuardar.setOnAction(event -> registrar(clienteNota));



    }//initialize

    //Seccion precargar en nota
    private void showIcons() {
        final Duration TIEMPO = Duration.millis(500);

        if (!gridPaneIcons.isVisible()) {
            gridPaneIcons.setVisible(true);
            TranslateTransition slideIN = new TranslateTransition(TIEMPO, gridPaneIcons);
            slideIN.setFromY(-50); //Mueve nodo
            slideIN.setToY(0); // Termina en su posición normal

            FadeTransition fadeIn = new FadeTransition(TIEMPO, gridPaneIcons);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            ParallelTransition showTransition = new ParallelTransition(slideIN, fadeIn);
            showTransition.play();

        } else {

            TranslateTransition slideOut = new TranslateTransition(TIEMPO, gridPaneIcons);
            slideOut.setFromY(0); //Mueve nodo
            slideOut.setToY(-50); // Termina en su posición normal

            FadeTransition fadeOut = new FadeTransition(TIEMPO, gridPaneIcons);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            ParallelTransition showTransition = new ParallelTransition(slideOut, fadeOut);
            showTransition.setOnFinished(event -> gridPaneIcons.setVisible(false));
            showTransition.play();


        }


    }//showIcons

    private void mostrarFechayHora() {
        LocalDate hoy = LocalDate.now();
        txtDia.setText(String.format("%02d", hoy.getDayOfMonth()));
        txtMes.setText(String.format("%02d", hoy.getMonthValue()));
        txtAnio.setText(String.valueOf(hoy.getYear()));

        LocalDateTime fechayHora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String hora = fechayHora.format(formatter);
        txtHoraEntrega.setText(hora);

    }//mostrarFecha

    //Seccion dibujar arco gasolina

    private void mostrarSlider(Window owner) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(owner);

        VBox vBox = new VBox(10);
        vBox.setPadding(new javafx.geometry.Insets(15));
        Label label = new Label("Selecciona el porcentaje de gasolina");

        Slider slider = new Slider(0, 100, 50);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(4);

        Button btnAceptar = new Button("Aceptar");

        btnAceptar.setOnAction(event -> {
            double porcentaje = slider.getValue();
            dibujarGasolina(porcentaje);
            dialog.close();
        });
        vBox.getChildren().add(label);
        vBox.getChildren().add(slider);
        vBox.getChildren().add(btnAceptar);
        Scene scene = new Scene(vBox, 300, 150);
        dialog.setScene(scene);
        dialog.setTitle("Porcentaje de gas");
        dialog.showAndWait();

    }//mostrarSlider

    private void dibujarGasolina(double porcentaje) {
        GraphicsContext gc = canvasGas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasGas.getWidth(), canvasGas.getHeight());

        double centerX = canvasGas.getWidth() / 2;
        double centerY = canvasGas.getHeight() / 2;
        double radius = 60;
        double startAngle = 180; // arco empieza desde izquierda
        double length = 180 * porcentaje / 100.0;

        // Fondo gris
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(8);
        gc.strokeArc(centerX - radius, centerY - radius, radius * 2, radius * 2,
                startAngle, 180, ArcType.OPEN);

        lblGas.setText(String.format("%d %%", (int) porcentaje, "%"));

        // Arco de porcentaje
        if (porcentaje > 60)
            gc.setStroke(Color.GREEN);
        else if (porcentaje > 30)
            gc.setStroke(Color.ORANGE);
        else
            gc.setStroke(Color.RED);

        gc.strokeArc(centerX - radius, centerY - radius, radius * 2, radius * 2,
                startAngle, length, ArcType.OPEN);
    }

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
            mostrarError("Error", "", "Error inesperado.");
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
            mostrarError("Error", "", "Error inesperado.");
            e.printStackTrace();
        }

    }//buscarCliente


    private void llenarNota(Cliente cliente) {
        if (cliente != null) {
            System.out.println(cliente.getClienteId());
            txtNombre.setText(cliente.getNombre() + " " +
                    (cliente.getApellido() != null ? cliente.getApellido() : "") + " " +
                    (cliente.getSegundoApellido() != null ? cliente.getSegundoApellido() : "")
            );

            txtDireccion.setText(cliente.getDomicilio() != null ? cliente.getDomicilio() : "");
            txtRfc.setText(cliente.getRfc() != null ? cliente.getRfc() : "");
            txtCorreo.setText(cliente.getCorreo() != null ? cliente.getCorreo() : "");

        }
        setClienteNota(cliente);

    }//llenarNota

    private void llenarNota(VehiculoDTO vehiculos) {
        if (vehiculos != null) {
            txtMarca.setText(vehiculos.getNombreMarca());
            txtModelo.setText(vehiculos.getNombreModelo() + " " + vehiculos.getNombreCategoria());
            txtAnioVehiculo.setText(vehiculos.getAnio() + "");
            txtKms.setText(vehiculos.getKilometros() + "");
            txtPlacas.setText(vehiculos.getPlacas());
            habilitar(false, "cliente");
        }


    }//llenarNota

    private void llenarNota(Inventario inventario) {
        if (inventario != null) {
            txtLlantas.setText(inventario.getMarca() + " " + inventario.getModelo() + " " + " " + inventario.getMedida());

            if (inventario.getStock() == 0)
                txtLlantasCantidad.setText("1");

            else
                txtLlantasCantidad.setText(inventario.getStock() + "");

            habilitar(false, "inventario");

        }

        setInventarioNota(inventario);


    }//llenarNota

    //pone la suma de los totales en el campo total
    private void total() {

    }//total


    private void habilitar(boolean habilitar, String opcion) {

        switch (opcion) {
            case "cliente" -> {
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
            case "inventario" -> {
                txtLlantas.setEditable(habilitar);
                txtLlantasCantidad.setEditable(habilitar);
            }

        }//switch

    }//habilitar

    private void registrar(Cliente cliente) {
        boolean error = false;

        if (cliente == null) {
            mostrarWarning("Cliente y Vehículo Faltantes",
                    "",
                    "Asocie un cliente y un vehículo antes de guardar la nota.");

            error = true;
            return;
        }

        if (txtNumNota.getText().trim().isEmpty()){
            mostrarWarning("Sin Número de Nota",
                    "",
                    "No se ha especificado un número de nota. Ingrese uno para continuar.");

            error = true;
            return;

        }

        if (!error){
         /*   String fechaYhora = txtAnio.getText() + "-" + txtMes.getText() + "-" + txtDia.getText() + " " + txtHoraEntrega.getText() + ":00";
            NotaDTO nuevaNota = NotaDTO.builder()
                    .numNota(txtNumNota.getText())
                    .numFactura("")
                    .fechaYHora(fechaYhora)
                    .fechaVencimiento("")
                    .statusNota()

                    .build(); */
        }


    }//registrar

    @FXML
    private void refrescar(ActionEvent event) {
        clienteNota = null;
        txtNumNota.setText("");
       mostrarFechayHora();
        txtNombre.setText("");
        txtDireccion.setText("");
        txtDireccion2.setText("");
        txtRfc.setText("");
        txtCorreo.setText("");
        txtHoraEntrega.setText("");
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
        txtFrenosCantidad.setText("");
        txtFrenosUnitario2.setText("");
        txtFrenosTotal2.setText("");
        txtOtros.setText("");
        txtOtrosCantidad.setText("");
        txtOtrosUnitario.setText("");
        txtOtrosTotal.setText("");
        txtOtros2.setText("");
        txtOtrosCantidad2.setText("");
        txtOtrosTotal2.setText("");
        txtSubTotalFrenos.setText("");
        txtSubTotalMecanica.setText("");
        txtSubTotalOtros.setText("");
        txtTotal.setText("");


        habilitar(true,"inventario");
        habilitar(true,"cliente");

    }//refrescar

    private void setClienteNota(Cliente clienteNota) {
        this.clienteNota = clienteNota;
    }
    private void setInventarioNota(Inventario inventarioNota){
        this.inventarioNota = inventarioNota;
    }


}//class
