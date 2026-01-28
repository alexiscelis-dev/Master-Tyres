package com.mastertyres.nota.model;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.common.service.NotaUtils;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
import javafx.stage.Window;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Setter
@Getter
public abstract class BaseNota {

    @FXML
    protected AnchorPane rootPane;
    @FXML
    protected Button btnBorrarCliente;
    @FXML
    protected Button btnBorrarInventario;
    @FXML
    protected GridPane gridPaneIcons;
    @FXML
    protected StackPane btnShowIcons;
    @FXML
    protected TextField txtDia;
    @FXML
    protected TextField txtMes;
    @FXML
    protected TextField txtAnio;
    @FXML
    protected StackPane spPorcentajeGas;
    @FXML
    protected TextField txtHoraEntrega;
    @FXML
    protected TextField txtNumNota;
    protected Arc arcoGas;
    @FXML
    protected Canvas canvasGas;
    @FXML
    protected Label lblGas;

    //Datos Clientes
    @FXML
    protected TextField txtNombre;
    @FXML
    protected TextField txtDireccion;
    @FXML
    protected TextField txtDireccion2;
    @FXML
    protected TextField txtRfc;
    @FXML
    protected TextField txtCorreo;
    @FXML
    protected TextField txtMarca;
    @FXML
    protected TextField txtModelo;
    @FXML
    protected TextField txtCategoria;
    @FXML
    protected TextField txtAnioVehiculo;
    @FXML
    protected TextField txtKms;
    @FXML
    protected TextField txtPlacas;

    @FXML
    protected TextField txtObservaciones;
    @FXML
    protected TextField txtObservaciones2;

    //seccion cuando se busca a la llanta
    //Datos nota detalle

    @FXML
    protected CheckBox cbRayonesSi;
    @FXML
    protected CheckBox cbRayonesNo;
    @FXML
    protected CheckBox cbGolpesSi;
    @FXML
    protected CheckBox cbGolpesNo;
    @FXML
    protected CheckBox cbTaponesSi;
    @FXML
    protected CheckBox cbTaponesNo;
    @FXML
    protected CheckBox cbTapetesSi;
    @FXML
    protected CheckBox cbTapetesNo;
    @FXML
    protected CheckBox cbRadioSi;
    @FXML
    protected CheckBox cbRadioNo;
    @FXML
    protected CheckBox cbGatoSi;
    @FXML
    protected CheckBox cbGatoNo;
    @FXML
    protected CheckBox cbLlaveSi;
    @FXML
    protected CheckBox cbLlaveNo;
    @FXML
    protected CheckBox cbLlantaSi;
    @FXML
    protected CheckBox cbLlantaNo;
    @FXML
    protected CheckBox cbCbRadioSi;
    @FXML
    protected CheckBox cbCbRadioNo;
    @FXML
    protected TextField txtAlineacion;
    @FXML
    protected TextField txtAlineacionCantidad;
    @FXML
    protected TextField txtAlineacionUnitario;
    @FXML
    protected TextField txtAlineacionTotal;
    @FXML
    protected TextField txtBalanceo;
    @FXML
    protected TextField txtBalanceoCantidad;
    @FXML
    protected TextField txtBalanceoUnitario;
    @FXML
    protected TextField txtBalanceoTotal;
    @FXML
    protected TextField txtLlantas;
    @FXML
    protected TextField txtLlantasCantidad;
    @FXML
    protected TextField txtLlantasUnitario;
    @FXML
    protected TextField txtLlantasTotal;
    @FXML
    protected TextField txtAmorDelanteros;
    @FXML
    protected TextField txtAmorDelCantidad;
    @FXML
    protected TextField txtAmorDelUnitario;
    @FXML
    protected TextField txtAmorDelTotal;
    @FXML
    protected TextField txtAmorTraseros;
    @FXML
    protected TextField txtAmorTrasCantidad;
    @FXML
    protected TextField txtAmorTrasUnitario;
    @FXML
    protected TextField txtAmorTrasTotal;
    @FXML
    protected TextField txtSuspension;
    @FXML
    protected TextField txtSuspensionCantidad;
    @FXML
    protected TextField txtSuspensionUnitario;
    @FXML
    protected TextField txtSuspensionTotal;
    @FXML
    protected TextField txtSuspension2;
    @FXML
    protected TextField txtSuspensionCantidad2;
    @FXML
    protected TextField txtSuspensionUnitario2;
    @FXML
    protected TextField txtSuspensionTotal2;
    @FXML
    protected TextField txtMecanica;
    @FXML
    protected TextField txtMecanicaCantidad;
    @FXML
    protected TextField txtMecanicaUnitario;
    @FXML
    protected TextField txtMecanicaTotal;
    @FXML
    protected TextField txtMecanica2;
    @FXML
    protected TextField txtMecanicaCantidad2;
    @FXML
    protected TextField txtMecanicaUnitario2;
    @FXML
    protected TextField txtMecanicaTotal2;
    @FXML
    protected TextField txtFrenos;
    @FXML
    protected TextField txtFrenosCantidad;
    @FXML
    protected TextField txtFrenosUnitario;
    @FXML
    protected TextField txtFrenosTotal;
    @FXML
    protected TextField txtFrenos2;
    @FXML
    protected TextField txtFrenosCantidad2;
    @FXML
    protected TextField txtFrenosUnitario2;
    @FXML
    protected TextField txtFrenosTotal2;
    @FXML
    protected TextField txtSubTotalMecanica;  //subtotal mecanica
    @FXML
    protected TextField txtSubTotalFrenos;  //subtotal frenos
    @FXML
    protected TextField txtSubTotalOtros;  //subtotal otros
    @FXML
    protected TextField txtOtros;
    @FXML
    protected TextField txtOtrosCantidad;
    @FXML
    protected TextField txtOtrosUnitario;
    @FXML
    protected TextField txtOtros2;
    @FXML
    protected TextField txtOtrosCantidad2;
    @FXML
    protected TextField txtOtrosUnitario2;
    @FXML
    protected TextField txtOtrosTotal2;
    @FXML
    protected TextField txtOtrosTotal;
    @FXML
    protected TextField txtTotal; //total de la nota


    @FXML
    protected Button btnGuardar;
    @FXML
    protected Button btnRefrescar;

    protected Cliente clienteNota;
    protected Inventario inventarioNota;
    protected VehiculoDTO vehiculosNota;
    protected int porcentajeGasNota;
    protected String rayones;
    protected String golpes;
    protected String tapones;
    protected String tapetes;
    protected String radio;
    protected String gato;
    protected String llave;
    protected String llanta;

    @Autowired
    protected NotaUtils notaUtils;

    protected void operacionesCampos() {
        //Alineacion
        txtAlineacionCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtAlineacionCantidad.getText()) * notaUtils.toFloatSafe(txtAlineacionUnitario.getText());

            txtAlineacionTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtAlineacionUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtAlineacionCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());
            txtAlineacionTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //Balanceo

        txtBalanceoCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtBalanceoCantidad.getText()) * notaUtils.toFloatSafe(txtBalanceoUnitario.getText());

            txtBalanceoTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtBalanceoUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtBalanceoCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtBalanceoTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //llantas
        txtLlantasCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtLlantasCantidad.getText()) * notaUtils.toFloatSafe(txtLlantasUnitario.getText());

            txtLlantasTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtLlantasUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtLlantasCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtLlantasTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //amortiguadores delanteros
        txtAmorDelCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtAmorDelCantidad.getText()) * notaUtils.toFloatSafe(txtAmorDelUnitario.getText());

            txtAmorDelTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtAmorDelUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtAmorDelCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtAmorDelTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //amortiguadores traseros

        txtAmorTrasCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtAmorTrasCantidad.getText()) * notaUtils.toFloatSafe(txtAmorTrasUnitario.getText());

            txtAmorTrasTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtAmorTrasUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtAmorTrasCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtAmorTrasTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //suspension

        txtSuspensionCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtSuspensionCantidad.getText()) * notaUtils.toFloatSafe(txtSuspensionUnitario.getText());

            txtSuspensionTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtSuspensionUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtSuspensionCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtSuspensionTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //suspension 2

        txtSuspensionCantidad2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtSuspensionCantidad2.getText()) * notaUtils.toFloatSafe(txtSuspensionUnitario2.getText());

            txtSuspensionTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtSuspensionUnitario2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtSuspensionCantidad2.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtSuspensionTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //Mecanica

        txtMecanicaCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtMecanicaCantidad.getText()) * notaUtils.toFloatSafe(txtMecanicaUnitario.getText());

            txtMecanicaTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtMecanicaUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtMecanicaCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtMecanicaTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //mecanica2

        txtMecanicaCantidad2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtMecanicaCantidad2.getText()) * notaUtils.toFloatSafe(txtMecanicaUnitario2.getText());

            txtMecanicaTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtMecanicaUnitario2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtMecanicaCantidad2.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtMecanicaTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        //frenos

        txtFrenosCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtFrenosCantidad.getText()) * notaUtils.toFloatSafe(txtFrenosUnitario.getText());

            txtFrenosTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtFrenosUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtFrenosCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtFrenosTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtFrenosCantidad2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtFrenosCantidad2.getText()) * notaUtils.toFloatSafe(txtFrenosUnitario2.getText());

            txtFrenosTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtFrenosUnitario2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtFrenosCantidad2.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtFrenosTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });
        //otros
        txtOtrosCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtOtrosCantidad.getText()) * notaUtils.toFloatSafe(txtOtrosUnitario.getText());

            txtOtrosTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtOtrosUnitario.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtOtrosCantidad.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtOtrosTotal.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });
        //otros2
        txtOtrosCantidad2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtOtrosCantidad2.getText()) * notaUtils.toFloatSafe(txtOtrosUnitario2.getText());

            txtOtrosTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });

        txtOtrosUnitario2.textProperty().addListener((observable, oldValue, newValue) -> {
            float num = notaUtils.toFloatSafe(txtOtrosCantidad2.getText()) * notaUtils.toFloatSafe(newValue.toString());

            txtOtrosTotal2.setText("$" + num);
            txtTotal.setText("$" + sumaTotal());

        });


        //al escribir directamente el el total de cada uno
        txtAlineacionTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtBalanceoTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtLlantasTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtAmorDelTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtAmorTrasTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtSuspensionTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtSuspensionTotal2.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtMecanicaTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtMecanicaTotal2.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtFrenosTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtFrenosTotal2.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtOtrosTotal.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtOtrosTotal2.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtSubTotalMecanica.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtSubTotalFrenos.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });
        txtSubTotalOtros.textProperty().addListener((observable, oldValue, newValue) -> {

            txtTotal.setText("$" + sumaTotal());

        });


    }//operacionesCampos

    protected float sumaTotal() {
        float suma = 0;

        suma = notaUtils.toFloatSafe(txtAlineacionTotal.getText()) +
                notaUtils.toFloatSafe(txtBalanceoTotal.getText()) +
                notaUtils.toFloatSafe(txtLlantasTotal.getText()) +
                notaUtils.toFloatSafe(txtAmorDelTotal.getText()) +
                notaUtils.toFloatSafe(txtAmorTrasTotal.getText()) +
                notaUtils.toFloatSafe(txtSuspensionTotal.getText()) +
                notaUtils.toFloatSafe(txtSuspensionTotal2.getText()) +
                notaUtils.toFloatSafe(txtMecanicaTotal.getText()) +
                notaUtils.toFloatSafe(txtMecanicaTotal2.getText()) +
                notaUtils.toFloatSafe(txtFrenosTotal.getText()) +
                notaUtils.toFloatSafe(txtFrenosTotal2.getText()) +
                notaUtils.toFloatSafe(txtOtrosTotal.getText()) +
                notaUtils.toFloatSafe(txtOtrosTotal2.getText()) +
                notaUtils.toFloatSafe(txtSubTotalMecanica.getText()) +
                notaUtils.toFloatSafe(txtSubTotalFrenos.getText()) +
                notaUtils.toFloatSafe(txtSubTotalOtros.getText()); //16


        return suma;
    }//sumaTotal

    protected void mostrarSlider(Window owner) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(owner);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(15));
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

    protected void dibujarGasolina(double porcentaje) {
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

        setPorcentajeGasNota((int) porcentaje);

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

    protected CheckBox fillCheckBox(CheckBox checkBoxSi, CheckBox checkBoxNo, String status) {

        if (status == null) {
            status = "";
        }


        if (status.equals(StatatusSiNo.SI.toString())) {
            checkBoxSi.setSelected(true);
            return checkBoxSi;
        } else if (status.equals(StatatusSiNo.NO.toString())) {
            checkBoxNo.setSelected(true);
            return checkBoxNo;
        } else {
            checkBoxSi.setSelected(false);
            checkBoxNo.setSelected(false);
            return null;
        }

    }//fillCheckBox
    // Este metodo revisa cuales estan seleccionados mientras que el checkCheckBoxes seleccionada los que ya vienen de base de datos

    protected void checkCheckBoxes() {

        //rayones
        if (cbRayonesSi.isSelected() && !cbRayonesNo.isSelected())
            setRayones(StatatusSiNo.SI.toString());
        else if (!cbRayonesSi.isSelected() && !cbRayonesNo.isSelected())
            setRayones(StatatusSiNo.DESELECCIONADO.toString());
        else
            setRayones(StatatusSiNo.NO.toString());

        //golpes
        if (cbGolpesSi.isSelected() && !cbGolpesNo.isSelected())
            setGolpes(StatatusSiNo.SI.toString());
        else if (!cbGolpesSi.isSelected() && !cbGolpesNo.isSelected())
            setGolpes(StatatusSiNo.DESELECCIONADO.toString());
        else
            setGolpes(StatatusSiNo.NO.toString());

        //tapones
        if (cbTaponesSi.isSelected() && !cbTaponesNo.isSelected())
            setTapones(StatatusSiNo.SI.toString());
        else if (!cbTaponesSi.isSelected() && !cbTaponesNo.isSelected())
            setTapones(StatatusSiNo.DESELECCIONADO.toString());
        else
            setTapones(StatatusSiNo.NO.toString());


        //tapetes
        if (cbTapetesSi.isSelected() && !cbTapetesNo.isSelected())
            setTapetes(StatatusSiNo.SI.toString());
        else if (!cbTapetesSi.isSelected() && !cbTapetesNo.isSelected())
            setTapetes(StatatusSiNo.DESELECCIONADO.toString());
        else
            setTapetes(StatatusSiNo.NO.toString());

        //radio
        if (cbRadioSi.isSelected() && !cbRadioNo.isSelected())
            setRadio(StatatusSiNo.SI.toString());
        else if (!cbRadioSi.isSelected() && !cbRadioNo.isSelected())
            setRadio(StatatusSiNo.DESELECCIONADO.toString());
        else
            setRadio(StatatusSiNo.NO.toString());


        //gato
        if (cbGatoSi.isSelected() && !cbGatoNo.isSelected())
            setGato(StatatusSiNo.SI.toString());
        else if (!cbGatoSi.isSelected() && !cbGatoNo.isSelected())
            setGato(StatatusSiNo.DESELECCIONADO.toString());
        else
            setGato(StatatusSiNo.NO.toString());

        //llave
        if (cbLlaveSi.isSelected() && !cbLlaveNo.isSelected())
            setLlave(StatatusSiNo.SI.toString());
        else if (!cbLlaveSi.isSelected() && !cbLlaveNo.isSelected())
            setLlave(StatatusSiNo.DESELECCIONADO.toString());
        else
            setLlave(StatatusSiNo.NO.toString());

        //llanta
        if (cbLlantaSi.isSelected() && !cbLlantaNo.isSelected())
            setLlanta(StatatusSiNo.SI.toString());
        else if (!cbLlantaSi.isSelected() && !cbLlantaNo.isSelected())
            setLlanta(StatatusSiNo.DESELECCIONADO.toString());
        else
            setLlanta(StatatusSiNo.NO.toString());


    }//checkCheckBoxes

    protected void setPorcentajeGasNota(final int porcentajeGasNota) {
        this.porcentajeGasNota = porcentajeGasNota;
    }

    protected void revisarCheckBoxes(NotaDTO nota) {
        String status[] = new String[8];

        status[0] = nota.getRayones();
        status[1] = nota.getGolpes();
        status[2] = nota.getTapones();
        status[3] = nota.getTapetes();
        status[4] = nota.getRadio();
        status[5] = nota.getGato();
        status[6] = nota.getLlave();
        status[7] = nota.getLlanta();

        fillCheckBox(cbRayonesSi, cbRayonesNo, status[0]);
        fillCheckBox(cbGolpesSi, cbGolpesNo, status[1]);
        fillCheckBox(cbTaponesSi, cbTaponesNo, status[2]);
        fillCheckBox(cbTapetesSi, cbTapetesNo, status[3]);
        fillCheckBox(cbRadioSi, cbRadioNo, status[4]);
        fillCheckBox(cbGatoSi, cbGatoNo, status[5]);
        fillCheckBox(cbLlaveSi, cbLlaveNo, status[6]);
        fillCheckBox(cbLlantaSi, cbLlantaNo, status[7]);


    }//revisarCheckBoxes


}//clase
