package com.mastertyres.fxControllers.nota;

import com.mastertyres.categoria.model.Categoria;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.common.RegexTools;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.inventario.service.InventarioService;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.nota.model.Nota;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.model.StatusNota;
import com.mastertyres.nota.service.NotaService;
import com.mastertyres.notaDetalle.model.NotaDetalle;
import com.mastertyres.vehiculo.model.Vehiculo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.mastertyres.common.MensajesAlert.mostrarError;
import static com.mastertyres.common.MensajesAlert.mostrarInformacion;


@Component
public class VentanaRegistro {
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


    private NotaDTO nota; // Se recibe la instancia con la informacion en el constructor para poder registrar
    private float aFavorNota;
    private float adeudoNota;
    private String fechaVencimiento;
    private String numFactura;


    public void setNota(NotaDTO nota) {
        this.nota = nota;
    }



    @FXML
    private void initialize() {

        grupoOpciones();
        RegexTools.aplicarNumerosDecimal(txtAdeudo);
        RegexTools.aplicarNumerosDecimal(txtSaldoAfavor);

        btnRegistrar.setOnAction(event -> registrar());

    }//initialize

    private void grupoOpciones() {
        rbPagado.setSelected(true);

        ToggleGroup grupoOpciones = new ToggleGroup();
        rbPagado.setToggleGroup(grupoOpciones);
        rbPorPagar.setToggleGroup(grupoOpciones);
        rbAfavor.setToggleGroup(grupoOpciones);

        grupoOpciones.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                RadioButton seleccionado = (RadioButton) newToggle;
                System.out.println("seleccionado:" + seleccionado.getText().toLowerCase());

                switch (seleccionado.getText().toLowerCase()) {
                    case "pagado" -> {
                        txtAdeudo.setVisible(false);
                        txtSaldoAfavor.setVisible(false);
                        hbAdeudo.setVisible(false);
                        txtAdeudo.setText("");
                        txtSaldoAfavor.setText("");
                        dpFecha.setValue(null);
                        setAdeudoNota(0);
                        setaFavorNota(0);
                        setNumFactura(txtNumFactura.getText());

                    }
                    case "por pagar" -> {
                        txtSaldoAfavor.setVisible(false);
                        txtAdeudo.setVisible(true);
                        hbAdeudo.setVisible(true);
                        txtSaldoAfavor.setText("");
                        dpFecha.setValue(null);
                        setAdeudoNota(toIntSafe(txtAdeudo.getText()));
                        setaFavorNota(0);

                        if (dpFecha.getValue() != null){
                            setFechaVencimiento(dpFecha.getValue().toString());
                        }

                        setNumFactura(txtNumFactura.getText());

                    }

                    case "a favor" -> {
                        txtAdeudo.setVisible(false);
                        txtSaldoAfavor.setVisible(true);
                        hbAdeudo.setVisible(false);
                        txtAdeudo.setText("");
                        setaFavorNota(toIntSafe(txtSaldoAfavor.getText()));
                        setNumFactura(txtNumFactura.getText());

                    }

                }//switch
            }
        });
    }//grupoOpciones

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtNumFactura.getScene().getWindow();
        stage.close();

    }

    //convierte un espacio vacio en 0 si no se selecciona nada
    private int toIntSafe(String texto) {
        try {
            if (texto == null || texto.trim().isEmpty()) {
                return 0;
            }
            return (int) Double.parseDouble(texto.trim());

        } catch (NumberFormatException e) {
            return 0;

        }
    }

    private void registrar() {

        String strNumFactura = "";

        if ( getNumFactura() == null || getNumFactura().trim().isEmpty())
            strNumFactura = null;
        else
            strNumFactura = getNumFactura();

        System.out.println("Despues de la pestaña");
        System.out.println("llanta"+nota.getLlanta());
        System.out.println("radio"+nota.getRadio());
        System.out.println("rayones"+nota.getRayones());

        nota.setStatusNota(estadoNota());
        nota.setAdeudo(getAdeudoNota());
        nota.setSaldoFavor(getaFavorNota());
        nota.setFechaVencimiento(getFechaVencimiento() != null ? getFechaVencimiento() : "");
        nota.setNumFactura(strNumFactura);

        Cliente clienteNota = Cliente.builder()
                .clienteId(nota.getClienteId())
                .nombre(nota.getNombreCliente())
                .apellido(nota.getApellido())
                .segundoApellido(nota.getSegundoApellido())
                .domicilio(nota.getDomicilio())
                .rfc(nota.getRfc())
                .correo(nota.getCorreo())
                .build();

        Marca marca = Marca.builder()
                .nombreMarca(nota.getMarca())
                .build();

        Modelo modelo = Modelo.builder()
                .nombreModelo(nota.getModelo())
                .build();
        Categoria categoria = Categoria.builder()
                .nombreCategoria(nota.getCategoria())
                .build();

        Vehiculo vehiculo = Vehiculo.builder()
                .vehiculoId(nota.getVehiculoId())
                .cliente(clienteNota)
                .marca(marca)
                .modelo(modelo)
                .categoria(categoria)
                .anio(nota.getAnio())
                .kilometros(nota.getKilometros())
                .color(nota.getColor())
                .placas(nota.getPlacas())
                .build();

        Inventario llantaRegistrar = null;

        if (nota.getInventarioId() != null){
            try {
                llantaRegistrar = inventarioService.buscarLlantaPorId(nota.getInventarioId());
            }catch (Exception e){
                mostrarError("Error de consulta","","Ocurrio un error" );
                e.printStackTrace();
            }

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
                .llanta(nota.getLlanta())
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
                .adeudo(getAdeudoNota())
                .saldoFavor(getaFavorNota())
                .build();

        try {

            notaService.guardarNota(nuevaNota,notaDetalle);

            mostrarInformacion("Nota registrada","","La nota se registro correctamente");


        }catch (Exception e){
            mostrarError("Error","","Se producjo un error" );
            e.printStackTrace();
        }




    }//registrar

    public RadioButton getRbPagado() {
        return this.rbPagado;
    }

    public void setRbPagado(final RadioButton rbPagado) {
        this.rbPagado = rbPagado;
    }


    public float getaFavorNota() {
        return this.aFavorNota;
    }

    public void setaFavorNota(final float aFavorNota) {
        this.aFavorNota = aFavorNota;
    }

    public float getAdeudoNota() {
        return this.adeudoNota;
    }

    public void setAdeudoNota(final float adeudoNota) {
        this.adeudoNota = adeudoNota;
    }

    public String getFechaVencimiento() {
        return this.fechaVencimiento;
    }

    public void setFechaVencimiento(final String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getNumFactura() {
        return this.numFactura;
    }

    public void setNumFactura(final String numFactura) {
        this.numFactura = numFactura;
    }

    private String estadoNota(){
     String estado = ""   ;
     
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
