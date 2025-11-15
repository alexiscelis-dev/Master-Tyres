package com.mastertyres.fxControllers.nuevaPromocion;

import com.mastertyres.common.MenuContextSetting;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.modelo.model.Modelo;
import com.mastertyres.promociones.model.Promocion;
import com.mastertyres.promociones.model.StatusPromocion;
import com.mastertyres.promociones.model.TipoDescuento;
import com.mastertyres.promociones.service.PromocionService;
import com.mastertyres.vehiculoPromocion.model.VehiculoPromocion;
import com.mastertyres.vehiculoPromocion.service.VehiculoPromocionService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mastertyres.common.MensajesAlert.*;
import static javafx.collections.FXCollections.observableList;

@Component
public class NuevaPromocionController {
    @FXML private AnchorPane rootPane;
    @FXML private Slider porcentajeDescuento;
    @FXML private Label descuentoLabel;
    @FXML private TextField precioSinDescuento;
    @FXML private DatePicker fechaInicio;
    @FXML private DatePicker fechaFin;
    @FXML private Button btnRegistrar;
    @FXML private Button btnLimpiar;
    @FXML private ChoiceBox<String> tipoDescuento;
    @FXML private TextField nombrePromocion;
    @FXML private TextField descripcion;
    @FXML private ChoiceBox<Marca> choiceMarca;
    @FXML private ChoiceBox<Modelo> choiceModelo;
    @FXML private ChoiceBox<Integer> choiceAnio;
    @FXML private TableView tableVehiculosParticipantes;
    @FXML private TableColumn<VehiculoPromocion, String> colMarca;
    @FXML private TableColumn<VehiculoPromocion, String> colModelo;
    @FXML private TableColumn<VehiculoPromocion, Integer> colAnio;
    @FXML private Button btnAgregarVehiculo;
    @FXML private Button btnImagen;
    @FXML private TextField textFieldImg;
    @FXML private TableColumn<VehiculoPromocion, Void> colEliminar;

    private ObservableList<VehiculoPromocion> vehiculos = FXCollections.observableList(FXCollections.observableArrayList());

    @Autowired
    private PromocionService promocionService;
    @Autowired
    private VehiculoPromocionService vehiculoPromocionService;


    @FXML
    public void initialize() {
        MenuContextSetting.disableMenu(rootPane);
        MenuContextSetting.disableMenuDatePicker(rootPane);

        cargarPorcentaje();
        tableVehiculosParticipantes.setItems(vehiculos);


        cargartabla();

        //Abre y cierra el popup del choiceBox para que aparezca en la posicion correcta y no debajo de la tabla
        choiceAnio.setOnMousePressed(event -> {
            if (!choiceAnio.isShowing()) {
                choiceAnio.show();
                choiceAnio.hide();
            }
        });

        //Inicializar marca, modelo, año
        vehiculosParticipantesInitialize();

        precioSinDescuento.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));

        porcentajeDescuento.valueProperty().addListener((observable, valAnterior, valNuevo) -> {

            obtenerPorcentaje(valNuevo.doubleValue());

        });

        tipoDescuento.getSelectionModel().selectedItemProperty().addListener((observable, valorAnterior, nuevoValor) -> {


            if (nuevoValor != null && !nuevoValor.isEmpty())
                tipoDescuento(nuevoValor.toLowerCase());

        });

        btnLimpiar.setOnAction(event -> clean());

        btnRegistrar.setOnAction(event -> registrarPromocion());

        btnAgregarVehiculo.setOnAction(event -> agregarTabla());

        colEliminar.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button();

            {
                // Quitar texto y agregar imagen
                Image img = new Image(getClass().getResourceAsStream("/icons/delete.png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(18);   // tamaño icono
                iv.setFitHeight(18);
                btn.setGraphic(iv);

                btn.setOnAction(e -> {
                    VehiculoPromocion v = getTableView().getItems().get(getIndex());
                    vehiculos.remove(v);
                });

                // (opcional) estilo para que sea redondo o plano
                btn.setStyle("-fx-background-color: red;");

                btn.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
                    if (isNowHovered) {
                        btn.setStyle("-fx-scale-x: 1.1;\n" +
                                "    -fx-scale-y: 1.1;");
                    } else {
                        btn.setStyle("-fx-background-color: red;");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        btnImagen.setOnAction(event -> seleccionarImg());


    }//initialize


    private void cargarPorcentaje() {
        List<String> tiposDescuentos = new ArrayList<>();
        tiposDescuentos.add(TipoDescuento.PORCENTAJE.toString());
        tiposDescuentos.add(TipoDescuento.OTRO.toString());
        tipoDescuento.setItems(FXCollections.observableList(tiposDescuentos));
    }

    private void clean() {
        nombrePromocion.setText("");
        descripcion.setText("");
        precioSinDescuento.setText("");
        porcentajeDescuento.setValue(0);
        descuentoLabel.setText("Descuento 0%");
        fechaInicio.setValue(null);
        fechaFin.setValue(null);
        tipoDescuento.setValue(null);

        choiceAnio.setValue(null);
        choiceMarca.setValue(null);
        choiceModelo.setValue(null);
        tableVehiculosParticipantes.getItems().clear();
        textFieldImg.setText("");
    }

    private void obtenerPorcentaje(Double valNuevo) {

        double porcentaje = (valNuevo.doubleValue() - porcentajeDescuento.getMin()) /
                (porcentajeDescuento.getMax() - porcentajeDescuento.getMin());


        String styleTrack = String.format(
                "-fx-background-color: linear-gradient(to right, #8EB83D %.0f%%, #cccccc %.0f%%);",
                porcentaje * 100, porcentaje * 100

        );
        int porcentajeInt = (int) (porcentaje * 100);



        if (porcentajeDescuento.lookup(".track") != null) {
            porcentajeDescuento.lookup(".track").setStyle(styleTrack);
            descuentoLabel.setText("Descuento " + porcentajeInt + "%");

        }
    }//obtenerPorcentaje

    private void tipoDescuento(String tipo) {

        switch (tipo) {

            case "porcentaje" -> {
                precioSinDescuento.setDisable(false);
                porcentajeDescuento.setDisable(false);
                LocalDate fecha = LocalDate.now();
                fechaInicio.setValue(fecha);
                fechaFin.setValue(fecha);
                fechaFin.setDisable(false);
                fechaInicio.setDisable(false);
                btnImagen.setDisable(false);
                textFieldImg.setDisable(false);


            }
            case "otro" -> {
                precioSinDescuento.setDisable(false);
                porcentajeDescuento.setDisable(false);
                LocalDate fecha = LocalDate.now();
                fechaInicio.setValue(fecha);
                fechaFin.setValue(fecha);
                fechaFin.setDisable(false);
                fechaInicio.setDisable(false);
                btnImagen.setDisable(false);
                textFieldImg.setDisable(false);

            }
        }//switch

    }

    private void vehiculosParticipantesInitialize() {

        choiceMarca.setItems(observableList(promocionService.listarMarcas()));
        choiceModelo.setItems(observableList(promocionService.listarModelos()));

        List<Integer> anios = new ArrayList<>();

        int anioActual = LocalDate.now().getYear();

        for (int i = anioActual; i >= 1950; i--) {
            anios.add(i);
        }
        choiceAnio.getItems().setAll(anios);


    }//vehiculosParticipantesInitialize

    private boolean empty() {
        boolean empty = false;
        if (nombrePromocion.getText() == null || descripcion.getText() == null || tipoDescuento.getValue() == null || precioSinDescuento.getText() == null ||
                fechaInicio.getValue() == null || fechaFin.getValue() == null || choiceMarca.getValue() == null || choiceModelo.getValue() == null ||
                nombrePromocion.getText().isEmpty() || descripcion.getText().isEmpty() || precioSinDescuento.getText().isEmpty()
                || tableVehiculosParticipantes.getItems() == null )

            empty = true;

        else {
            empty = false;
        }

        return empty;
    }

    private boolean registrarPromocion() {

        if (!empty() && validarFecha(fechaInicio.getValue(), fechaFin.getValue())) {

            String nombre, descripcion, tipoDescuento, precio, porcentaje, inicioPromo, finPromo;
            insertarPromocion();

        } else if (empty())
            mostrarWarning("Campos vacios", "", "Favor de completar cada uno de los campos solicitados.");

        return true;
    }

    private boolean validarFecha(LocalDate fechaInicioLD, LocalDate fechaFinLD) {
        boolean fechaValida = false;

        if (fechaFinLD.isAfter(fechaInicioLD)) {
            fechaValida = true;

        } else if (fechaFinLD.isBefore(fechaInicioLD)) {
            mostrarWarning("Fecha incorrecta", "", "Le fecha de fin no puede ser antes que la fecha de inicio, vuelva a intentarlo");

            fechaValida = false;

        } else if (fechaFinLD.equals(fechaInicioLD)) {

          fechaValida =  mostrarConfirmacion("Fechas iguales", "Ha ingresado la misma fecha de inicio y de fin para la promocion.", "¿Desea continuar?", "Continuar", "Cancelar");

          if (fechaValida)
              fechaValida = true;
          else
              fechaValida = false;

        }
        return fechaValida;
    }//validarFecha

    private void cargartabla() {

        colMarca.setCellValueFactory(data -> {
            return new SimpleObjectProperty<>(data.getValue().getMarca().getNombreMarca());
        });
        colModelo.setCellValueFactory(data -> {
            return new SimpleObjectProperty<>(data.getValue().getModelo().getNombreModelo());
        });

        colAnio.setCellValueFactory(data -> {
            return new SimpleObjectProperty<>(data.getValue().getAnnio());
        });
    }//cargartabla

    private void agregarTabla() {
        Marca marca = choiceMarca.getValue();
        Modelo modelo = choiceModelo.getValue();
        Integer anio = choiceAnio.getValue();

        if (marca != null && modelo != null && anio != null) {
            VehiculoPromocion vehiculo = new VehiculoPromocion();
            vehiculo.setMarca(marca);
            vehiculo.setModelo(modelo);
            vehiculo.setAnnio(anio);

            if (choiceMarca != null && choiceModelo != null && choiceAnio != null && !vehiculos.contains(vehiculo))
                vehiculos.add(vehiculo);
        }

    }//agregarVehiculo

    //metodo se manda llamar en registrar promocion

    private void insertarPromocion() {

        Promocion promocion = Promocion.builder()
                .nombre(nombrePromocion.getText())
                .descripcion(descripcion.getText())
                .tipoDescuento(tipoDescuento.getValue())
                .precio(Float.parseFloat(precioSinDescuento.getText()))
                .porcentaje((int) porcentajeDescuento.getValue())
                .fechaInicio(String.valueOf(fechaInicio.getValue()))
                .fechaFin(String.valueOf(fechaFin.getValue()))
                .active(StatusPromocion.ACTIVE.toString())
                .img(textFieldImg.getText() != null ? textFieldImg.getText() : "")
                .build();


        try {
            promocionService.guardarPromocion(promocion);

            if (promocion.getPromocionId() == null) {
                mostrarError("Error", "", "Error al insertar promocion");
                return;
            }
            ObservableList<VehiculoPromocion> vehiculos = tableVehiculosParticipantes.getItems();

            for (VehiculoPromocion vehiculoPromocionSeleccionado : vehiculos) {

                Integer marcaId = vehiculoPromocionSeleccionado.getMarca().getMarcaId();
                Integer modeloId = vehiculoPromocionSeleccionado.getModelo().getModeloId();
                Integer anio = vehiculoPromocionSeleccionado.getAnnio();

                Marca marca = new Marca();
                marca.setMarcaId(marcaId);

                Modelo modelo = new Modelo();
                modelo.setModeloId(modeloId);

                VehiculoPromocion vehiculosCompatibles = VehiculoPromocion.builder()
                        .marca(marca)
                        .modelo(modelo)
                        .promocion(promocion)
                        .annio(anio)
                        .build();
                vehiculoPromocionService.guardarVehiculosAplicables(vehiculosCompatibles);

            }//for

            mostrarInformacion("Promocion registrada", "", "La promocion se registro exitosamente");
            clean();


        } catch (Exception e) {
            mostrarError("Error al crear promocion", "","Ha ocurrido un error al crear la promocion vuelva a iintentarlo mas tarde");
            clean();
            System.out.println(e.getMessage());
        }


    }//insertarPromocion

    private  void  seleccionarImg(){
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de imagen","*.png","*.jpg","*.jpeg")
        );

        Stage stage = (Stage) btnImagen.getScene().getWindow();
        File archivo = fileChooser.showOpenDialog(stage);

        if (archivo != null){
            String url = archivo.getAbsolutePath();
            textFieldImg.setText(url);
        }

    }//seleccionarImg


}//clase
