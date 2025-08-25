package com.mastertyres.fxControllers.vehiculo;


import com.mastertyres.common.ApplicationContextProvider;
import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import com.mastertyres.vehiculo.model.VehiculoStatus;
import com.mastertyres.vehiculo.service.VehiculoService;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mastertyres.common.MensajesAlert.*;


@Component
public class VehiculoController {
    @FXML
    private TableView<VehiculoDTO> tablaVehiculos;
    @FXML
    private TableColumn<VehiculoDTO, String> colCliente; //Columna Cliente es Columna Propietario
    @FXML
    private TableColumn<VehiculoDTO, String> colMarca;
    @FXML
    private TableColumn<VehiculoDTO, String> colModelo;
    @FXML
    private TableColumn<VehiculoDTO, String> colCategoria;
    @FXML
    private TableColumn<VehiculoDTO, String> colColor;
    @FXML
    private TableColumn<VehiculoDTO, Integer> colAnio;
    @FXML
    private TableColumn<VehiculoDTO, String> colPlacas;
    @FXML
    private TableColumn<VehiculoDTO, String> colNumeroSerie;
    @FXML
    private TableColumn<VehiculoDTO, String> colObservaciones;
    @FXML
    private TableColumn<VehiculoDTO, Integer> colKilometraje;
    @FXML
    private TableColumn<VehiculoDTO, String> colUltimoServicio;
    @FXML
    private TableColumn<VehiculoDTO, String> colFechaRegistro;
    @FXML
    private TextField buscarVehiculoBuscador;
    @FXML
    private ChoiceBox<String> atributoBusquedaVehiculos;
    @FXML
    private Label statusLabel;
    @FXML
    private Label choiceBoxLabel;


    private PauseTransition delayQuery = new PauseTransition(Duration.millis(300)); //evita que se ejecuta una query cada vez que el usuario
    //presiona una tecla hace un delay
    private VentanaPrincipalController ventanaPrincipalController;


    @Autowired
    private VehiculoService vehiculoService;


    @FXML
    public void initialize() {


        cargarVehiculos();


        //Click derecho borrar

        tablaVehiculos.setRowFactory(tabla -> {
            TableRow<VehiculoDTO> fila = new TableRow<>();

            fila.setOnContextMenuRequested(event -> {
                if (!fila.isEmpty()) {
                    VehiculoDTO vehiculoSeleccionado = fila.getItem();
                    Integer vehiculoId = vehiculoSeleccionado.getId();

                    ListView<String> listaOpciones = new ListView<>();
                    listaOpciones.getItems().addAll("Copiar", "Copiar fila completa", "Editar", "Eliminar");
                    listaOpciones.setPrefSize(200, 150);
                    listaOpciones.getStyleClass().add("popup-table");

                    Popup listViewPopup = new Popup();
                    listViewPopup.getContent().add(listaOpciones);
                    listViewPopup.setAutoHide(true);


                    listaOpciones.setOnMouseClicked(e -> {
                        String seleccion = listaOpciones.getSelectionModel().getSelectedItem();


                        if (seleccion != null) {
                            switch (seleccion) {

                                case "Eliminar" -> {


                                    Alert ventanaEliminar = new Alert(Alert.AlertType.WARNING);
                                    ventanaEliminar.setTitle("Eliminar vehiculo");

                                    String propietario, vehiculo;

                                    //verificar que no contenga null  mostrar mensaje
                                    propietario = (vehiculoSeleccionado.getNombreCliente() != null ? vehiculoSeleccionado.getNombreCliente() : "") + " " +
                                            (vehiculoSeleccionado.getApellido() != null ? vehiculoSeleccionado.getApellido() : "") + " " +
                                            (vehiculoSeleccionado.getSegundoApellido() != null ? vehiculoSeleccionado.getSegundoApellido() : "");


                                    String vehiculoEliminar = propietario + " " + vehiculoSeleccionado.getNombreMarca() + " " + vehiculoSeleccionado.getNombreModelo() + " " + vehiculoSeleccionado.getAnio();

                                    ventanaEliminar.setHeaderText("¿Estas seguro que quieres eliminar el siguiente vehiculo? \n\n " + vehiculoEliminar);

                                    ventanaEliminar.setContentText("Esta accion no se podra deshacer");


                                    ButtonType buttonEliminar = new ButtonType("Elimnar", ButtonBar.ButtonData.YES);
                                    ButtonType buttonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                                    ventanaEliminar.getButtonTypes().setAll(buttonEliminar, buttonCancelar);

                                    Optional<ButtonType> resultado = ventanaEliminar.showAndWait();

                                    if (resultado.isPresent() && resultado.get() == buttonEliminar) {

                                        System.out.println("vehiculoId = " + vehiculoId);

                                        vehiculoService.eliminarVehiculo(VehiculoStatus.INACTIVE.toString(), vehiculoId);

                                        cargarVehiculos(); //metodo que cargar los datos en la tabla

                                    } else {
                                        mostrarInformacion("Accion cancelada", "", "Accion cancelada");

                                    }//else


                                }

                                case "Editar" -> {
                                    System.out.println("Editado");

                                }

                                case "Copiar" -> {
                                    var selectedCell = tablaVehiculos.getSelectionModel().getSelectedCells();

                                    if (!selectedCell.isEmpty()) {
                                        TablePosition<?, ?> position = selectedCell.get(0);
                                        int row = position.getRow();
                                        TableColumn<?, ?> columna = position.getTableColumn();

                                        Object value = columna.getCellData(row);

                                        if (value != null) {
                                            ClipboardContent content = new ClipboardContent();
                                            content.putString(value.toString());
                                            Clipboard.getSystemClipboard().setContent(content);

                                            statusLabel.setVisible(true);
                                            statusLabel.setText("Texto copiado ");

                                            new Thread(() -> {
                                                try {
                                                    Thread.sleep(2500);

                                                } catch (InterruptedException exception) {
                                                    exception.printStackTrace();
                                                }
                                                javafx.application.Platform.runLater(() -> statusLabel.setText(""));

                                            }).start();


                                        } else {
                                            System.out.println("No hay celda seleccionada");
                                        }

                                    }

                                }
                                case "Copiar fila completa" -> {

                                    VehiculoDTO item = tablaVehiculos.getSelectionModel().getSelectedItem();

                                    if (item != null) {


                                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                        DateTimeFormatter registroFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                        String fechaRegistro = "", ultimoServicio = "";
                                        LocalDate fecha;

                                        if (item.getUltimoServicio() != null && !item.getUltimoServicio().isEmpty()) {

                                            fecha = LocalDate.parse(item.getUltimoServicio(), inputFormatter);
                                            ultimoServicio = fecha.format(outputFormatter);

                                            fecha = LocalDate.parse(item.getFechaRegistro(), registroFormatter);
                                            fechaRegistro = fecha.format(outputFormatter);

                                        } else {

                                            fecha = LocalDate.parse(item.getFechaRegistro(), registroFormatter);
                                            fechaRegistro = fecha.format(outputFormatter);
                                            ultimoServicio = "";

                                        }


                                        String filaCopiada = item.getNombreCliente() + " " + (item.getApellido() != null ? item.getApellido() : "") + " " +
                                                (item.getSegundoApellido() != null ? item.getSegundoApellido() : "") + " " +
                                                (item.getNombreMarca() != null ? item.getNombreMarca() : "") + " " +
                                                (item.getNombreModelo() != null ? item.getNombreModelo() : "") + " " +
                                                (item.getNombreCategoria() != null ? item.getNombreCategoria() : "") + " " +
                                                (item.getColor() != null ? item.getColor() : "") + " " +
                                                (item.getAnio() != null ? item.getAnio() : "") + " " +
                                                (item.getPlacas() != null ? item.getPlacas() : "") + " " +
                                                (item.getNumSerie() != null ? item.getNumSerie() : "") + " " +
                                                (item.getKilometros() != null ? item.getKilometros() : "") + " " +
                                                (item.getObservaciones() != null ? item.getObservaciones() : "") + " " +
                                                ultimoServicio + " " + fechaRegistro;


                                        ClipboardContent content = new ClipboardContent();
                                        content.putString(filaCopiada);
                                        Clipboard.getSystemClipboard().setContent(content);

                                        statusLabel.setVisible(true);
                                        statusLabel.setText("Fila copiada");

                                        new Thread(() -> {
                                            try {
                                                Thread.sleep(2500);

                                            } catch (InterruptedException exception) {
                                                exception.printStackTrace();
                                            }
                                            javafx.application.Platform.runLater(() -> {
                                                        statusLabel.setText("");
                                                        statusLabel.setVisible(false);
                                                    }
                                            );


                                        }).start();


                                    }

                                }


                            }//switch
                            listViewPopup.hide();
                        }


                    });
                    Point2D coordenadasPantalla = fila.localToScreen(event.getX(), event.getY()); //convertir las coordenadas relativas a las obsolutas de la pantalla para que la lista aparezca justo donde click


                    listViewPopup.show(fila.getScene().getWindow(), coordenadasPantalla.getX(), coordenadasPantalla.getY());

                }
            });

            return fila;
        });


        //Enter buscar
        buscarVehiculoBuscador.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER){
                String seleccion = atributoBusquedaVehiculos.getValue(), busqueda = buscarVehiculoBuscador.getText();

                if (seleccion != null && !seleccion.isEmpty() && busqueda != null && !busqueda.isEmpty()) {
                    buscarVehiculo(seleccion.toLowerCase(),busqueda);
                }
            }
        });


        //Buscar mientras escribes
        buscarVehiculoBuscador.setOnKeyReleased(event -> {

           if (event.getCode() != KeyCode.ENTER)
               delayQuery.setOnFinished(e -> {
                   String seleccion = atributoBusquedaVehiculos.getValue();
                   String busqueda = buscarVehiculoBuscador.getText();

                   if (seleccion == null && busqueda != null && !busqueda.isEmpty())
                       buscarVehiculo(busqueda);
                   else if (seleccion == null)
                       cargarVehiculos();
               });
           delayQuery.playFromStart();
        });

        //pone en null la lista de ChoiceBox
        choiceBoxLabel.setOnMouseClicked(event -> {

            if ((event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.MIDDLE) && event.getClickCount() == 2 )
                atributoBusquedaVehiculos.setValue(null); // pone el valor en null para que vuelva a buscar dinamicamente



        });


    }//initialize

    @FXML
    private void agregarVehiculo(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml_views/AgregarVehiculo.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            Pane panel = ventanaPrincipalController.getPanelMenu();
            panel.getChildren().setAll(root);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);

        } catch (IOException e) {
            e.printStackTrace();

        }

    }


    private void cargarVehiculos() {

        colCliente.setCellValueFactory(data -> {

            String nombre = data.getValue().getNombreCliente() != null ? data.getValue().getNombreCliente() : "";
            String apellido = data.getValue().getApellido() != null ? data.getValue().getApellido() : "";
            String segundoApellido = data.getValue().getSegundoApellido() != null ? data.getValue().getSegundoApellido() : "";
            String propietario = nombre + " "  + apellido + " " + segundoApellido;

            return new SimpleStringProperty(propietario);
        });

        colMarca.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombreMarca())
        );

        colModelo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombreModelo())

        );
        colColor.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getColor())
        );
        colCategoria.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombreCategoria())
        );
        colAnio.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getAnio()).asObject()

        );
        colPlacas.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPlacas())

        );
        colNumeroSerie.setCellValueFactory(
                new PropertyValueFactory<>("numSerie")
        );

        colObservaciones.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getObservaciones())
        );

        colKilometraje.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getKilometros()).asObject()
        );

        DateTimeFormatter input = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter output = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        colUltimoServicio.setCellValueFactory(data -> {
                    String fechaStr = data.getValue().getUltimoServicio();

                    if (fechaStr == null || fechaStr.isEmpty()) {
                        return new SimpleStringProperty("N/A");
                    } else {
                        LocalDate fecha = LocalDate.parse(fechaStr, input);
                        String texto = fecha.format(output);
                        return new SimpleStringProperty(texto);
                    }
                }
        );


        colFechaRegistro.setCellValueFactory(data -> {

                    DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String fechaStr = data.getValue().getFechaRegistro();
                    LocalDate fecha = LocalDate.parse(fechaStr, formatterEntrada);
                    String texto = fecha.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));  //mostrar solo fecha sin la hora del registro
                    return new SimpleStringProperty(texto);
                }

                //      new SimpleStringProperty(data.getValue().getFechaRegistro())
        );

        cargarDatosVehiculo();


    }//cargarVehiculos


    private void cargarDatosVehiculo() {

        //Llenar tabla con registros
        try {
            tablaVehiculos.getItems().setAll(vehiculoService.listarVehiculos(VehiculoStatus.ACTIVE.toString()));

        } catch (Exception e) {
            mostrarError("Error al mostrar datos", "", "No se pudieron cargar los datos. Por favor, inténtalo de nuevo más tarde.");

        }


    }//cargarDatosVehiculo

    private void buscarVehiculo(String seleccion, String busqueda) { // porque se va a buscar, vehiculo a buscar

            switch (seleccion) {

                case "propietario" -> {
                    String nombre = busqueda;

                    List<VehiculoDTO> vehiculoPorNombre = vehiculoService.buscarVehiculoPorPropietario(VehiculoStatus.ACTIVE.toString(), nombre);
                    tablaVehiculos.setItems(FXCollections.observableList(vehiculoPorNombre));

                }
                case "marca" -> {

                    List<VehiculoDTO> vehiculosPorMarca = vehiculoService.buscarVehiculoPorMarca(VehiculoStatus.ACTIVE.toString(), busqueda);
                    tablaVehiculos.setItems(FXCollections.observableList(vehiculosPorMarca));

                }
                case "modelo" -> {

                    List<VehiculoDTO> vehiculoPorModelo = vehiculoService.buscarVehiculoPorModelo(VehiculoStatus.ACTIVE.toString(), busqueda);
                    tablaVehiculos.setItems(FXCollections.observableList(vehiculoPorModelo));

                }
                case "categoria" -> {

                    List<VehiculoDTO> vehiculoPorCategoria = vehiculoService.buscarVehiculoPorCategoria(VehiculoStatus.ACTIVE.toString(), busqueda);
                    tablaVehiculos.setItems(FXCollections.observableList(vehiculoPorCategoria));

                }
                case "color" -> {

                    List<VehiculoDTO> vehiculoPorColor = vehiculoService.buscarVehiculoPorColor(VehiculoStatus.ACTIVE.toString(), busqueda);
                    tablaVehiculos.setItems(FXCollections.observableList(vehiculoPorColor));

                }
                case "año" -> {

                    String[] anios;
                    Integer anio;


                    if (busqueda.matches("\\d{4}")) { // yyyy
                        anio = Integer.parseInt(busqueda);

                        List<VehiculoDTO> vehiculoPorAnio = vehiculoService.buscarVehiculoPorAnio(VehiculoStatus.ACTIVE.toString(), anio);
                        tablaVehiculos.setItems(FXCollections.observableList(vehiculoPorAnio));


                    } else if (busqueda.matches("\\d{4},\\d{4}")) { //yyyy,yyyy
                        anios = busqueda.split(",");

                        Arrays.stream(anios).sorted();
                        //ordenar años para realizar la consulta siempre con el menor primero
                        String[] aniosOrdenados = Arrays.stream(anios).sorted().toArray(String[]::new);

                        List<VehiculoDTO> vehiculosPorAnio = vehiculoService.buscarVehiculoPorAnio(VehiculoStatus.ACTIVE.toString(), Integer.parseInt(aniosOrdenados[0]), Integer.parseInt(aniosOrdenados[1]));
                        tablaVehiculos.setItems(FXCollections.observableList(vehiculosPorAnio));

                    } else {
                        List<VehiculoDTO> vehiculoVacio = new ArrayList<>();
                        tablaVehiculos.setItems(FXCollections.observableList(vehiculoVacio));
                        mostrarWarning("Formato incorrecto", "Favor de ingresar un formato de texto correcto", "Por ejemplo yyyy, o bien yyyy,yyyy para buscar por un rango de fechas.");

                    }
                }
                case "placas" -> {

                    String placas = busqueda;
                    placas.toUpperCase();
                    List<VehiculoDTO> vehicululoPorPlacas = vehiculoService.buscarVehiculoPorPlacas(VehiculoStatus.ACTIVE.toString(), placas);
                    tablaVehiculos.setItems(FXCollections.observableList(vehicululoPorPlacas));

                }
                case "numero serie" -> {
                    busqueda.toUpperCase();

                    List<VehiculoDTO> vehiculoPorNumSerie = vehiculoService.buscarVehiculoPorNumSerie(VehiculoStatus.ACTIVE.toString(), busqueda);
                    tablaVehiculos.setItems(FXCollections.observableList(vehiculoPorNumSerie));


                }
                case "kilometraje" -> {

                    String[] kilometraje;

                    if (busqueda.matches("\\d+")) {
                        Integer vehiculoBuscadoInt = Integer.parseInt(busqueda);
                        List<VehiculoDTO> vehiculoPorkilometros = vehiculoService.buscarVehiculoPorKilometros(VehiculoStatus.ACTIVE.toString(), vehiculoBuscadoInt);
                        tablaVehiculos.setItems(FXCollections.observableList(vehiculoPorkilometros));

                    } else if (busqueda.matches("\\d+,\\d+")) {
                        kilometraje = busqueda.split(",");

                        //ordenar por el kilometro menor para realizar la consulta
                        Arrays.stream(kilometraje).sorted();

                        String[] kilometrajeOrdenado = Arrays.stream(kilometraje).sorted().toArray(String[]::new);

                        List<VehiculoDTO> vehiculoPorKilometros = vehiculoService.buscarVehiculoPorKilometros(VehiculoStatus.ACTIVE.toString(), Integer.parseInt(kilometrajeOrdenado[0]), Integer.parseInt(kilometrajeOrdenado[1]));
                        tablaVehiculos.setItems(FXCollections.observableList(vehiculoPorKilometros));
                    } else {

                        List<VehiculoDTO> vehiculoVacio = new ArrayList<>();
                        tablaVehiculos.setItems(FXCollections.observableList(vehiculoVacio));
                        mostrarWarning("Formato incorrecto", "Favor de ingresar un formato correcto", "Por ejemplo 1000 o bien 0,1000 " +
                                " si desea buscar por un rango de kilometros");

                    }

                }
                case "ultimo servicio" -> {

                    boolean consultar = false;


                    //forma dd-mm-yyyy
                    if (busqueda.matches("\\d{2}-\\d{2}-\\d{4}")) {
                        String fecha = busqueda;
                        String fechaConsulta = "";


                        try {
                            DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            DateTimeFormatter formatterConsulta = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                            LocalDate fechaLD = LocalDate.parse(fecha, formatterEntrada);

                            fechaConsulta = fechaLD.format(formatterConsulta);

                            consultar = true;

                        } catch (DateTimeParseException e) {
                            mostrarWarning("Fecha no valida", "", "La fecha ingresada no es valida vuelva a intentarlo");
                            consultar = false;

                        }
                        if (consultar) {
                            List<VehiculoDTO> vehiculoPorUltimoServicio = vehiculoService.buscarVehiculoPorUltimoServicio(VehiculoStatus.ACTIVE.toString(), fechaConsulta);
                            tablaVehiculos.setItems(FXCollections.observableList(vehiculoPorUltimoServicio));


                        }

                        // forma dd-mm-yyyy,dd-mm-yyyy
                    } else if (busqueda.matches("\\d{2}-\\d{2}-\\d{4},\\d{2}-\\d{2}-\\d{4}")) {
                        String[] fecha = busqueda.split(",");
                        String consultaInicio = "", consultaFinal = "";

                        try {
                            DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            DateTimeFormatter formatterConsulta = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                            LocalDate fecha1 = LocalDate.parse(fecha[0], formatterEntrada);
                            LocalDate fecha2 = LocalDate.parse(fecha[1], formatterEntrada);

                            //ordenar fecha mayor al inicio para hacer la consulta
                            if (fecha1.isAfter(fecha2)) {
                                LocalDate aux = fecha1;
                                fecha1 = fecha2;
                                fecha2 = aux;

                            }
                            consultaInicio = fecha1.format(formatterConsulta);
                            consultaFinal = fecha2.format(formatterConsulta);

                            consultar = true;

                        } catch (DateTimeParseException e) {
                            mostrarWarning("Fecha no valida", "", "La fecha ingresada no es valida vuelva a intentarlo");
                            consultar = false;
                        }

                        if (consultar) {

                            List<VehiculoDTO> vehiculoPorUltimoServicio = vehiculoService.buscarVehiculoPorUltimoServicio(VehiculoStatus.ACTIVE.toString(), consultaInicio, consultaFinal);
                            tablaVehiculos.setItems(FXCollections.observableList(vehiculoPorUltimoServicio));

                        }

                    } else {
                        List<VehiculoDTO> vehiculoVacio = new ArrayList<>();
                        tablaVehiculos.setItems(FXCollections.observableList(vehiculoVacio));
                        mostrarWarning("Formato incorrecto", "Favor de ingresar un formato correcto", "Por ejemplo dd-mm-yyyy o bien" +
                                " dd-mm-yyyy,dd-mm-yyyy si desea buscar por un rango de fechas");

                    }
                }

                case "fecha registro" -> {

                    boolean consultar = false;

                    if (busqueda.matches("\\d{2}-\\d{2}-\\d{4}")) {
                        String fecha = busqueda;
                        LocalDate fechaConsulta;


                        try {
                            DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            DateTimeFormatter formatterConsulta = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                            LocalDate fechaLD = LocalDate.parse(fecha, formatterEntrada);
                            fechaConsulta = LocalDate.parse(fechaLD.format(formatterConsulta));

                            consultar = true;

                            if (consultar) {

                                List<VehiculoDTO> vehiculosPorFechaRegistro = vehiculoService.buscarVehiculoPorRegistro(VehiculoStatus.ACTIVE.toString(), fechaConsulta);
                                tablaVehiculos.setItems(FXCollections.observableList(vehiculosPorFechaRegistro));

                            }


                        } catch (DateTimeParseException e) {
                            mostrarWarning("Fecha no valida", "", "La fecha ingresada no es valida vuelva a intentarlo");
                            consultar = false;

                        }

                    } else if (busqueda.matches("\\d{2}-\\d{2}-\\d{4},\\d{2}-\\d{2}-\\d{4}")) {

                        String[] fecha = busqueda.split(",");
                        String consultaInicio = "", consultaFinal = "";


                        try {
                            DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            DateTimeFormatter formatterConsulta = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                            LocalDate fecha1 = LocalDate.parse(fecha[0], formatterEntrada);
                            LocalDate fecha2 = LocalDate.parse(fecha[1], formatterEntrada);

                            //ordenar fecha mayor al inicio para hacer la consulta
                            if (fecha1.isAfter(fecha2)) {
                                LocalDate aux = fecha1;
                                fecha1 = fecha2;
                                fecha2 = aux;
                            }

                            consultaInicio = fecha1.format(formatterConsulta);
                            consultaFinal = fecha2.format(formatterConsulta);

                            consultar = true;

                        } catch (DateTimeParseException e) {
                            mostrarWarning("Fecha no valida", "", "La fecha ingresada no es valida vuelva a intentarlo");
                            consultar = false;
                        }

                        if (consultar) {
                            List<VehiculoDTO> vehiculoPorFechaRegistro = vehiculoService.buscarVehiculoPorRegistro(VehiculoStatus.ACTIVE.toString(), LocalDate.parse(consultaInicio), LocalDate.parse(consultaFinal));
                            tablaVehiculos.setItems(FXCollections.observableList(vehiculoPorFechaRegistro));
                        }


                    } else {
                        List<VehiculoDTO> vehiculoVacio = new ArrayList<>();
                        tablaVehiculos.setItems(FXCollections.observableList(vehiculoVacio));
                        mostrarWarning("Formato incorrecto", "Favor de ingresar un formato correcto", "Por ejemplo dd-mm-yyyy o bien" +
                                " dd-mm-yyyy,dd-mm-yyyy si desea buscar por un rango de fechas");

                    }

                }
                default -> {
                    mostrarWarning("Informacion no valida", "", "Asegurese de buscar por el campo correspondiente.");

                }
            }//switch

    }//buscarVehiculo


    public void buscarVehiculo(String busqueda) {
        List<VehiculoDTO> vehiculos = vehiculoService.buscadorVehiculo(VehiculoStatus.ACTIVE.toString(),busqueda);
        tablaVehiculos.setItems(FXCollections.observableList(vehiculos));

    }


    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;
    }//setVentanaPrincipalController

 }//clase
