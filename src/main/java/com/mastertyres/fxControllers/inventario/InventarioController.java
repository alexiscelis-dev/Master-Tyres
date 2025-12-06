package com.mastertyres.fxControllers.inventario;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.model.StatusCliente;
import com.mastertyres.common.ApplicationContextProvider;
import com.mastertyres.fxControllers.EditarControllers.EditarInventarioController;
import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.inventario.model.StatusInventario;
import com.mastertyres.inventario.service.InventarioService;
import com.mastertyres.vehiculo.model.VehiculoDTO;
import com.mastertyres.vehiculo.model.StatusVehiculo;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static com.mastertyres.common.MensajesAlert.*;

@NoArgsConstructor
@Component
public class InventarioController {


    @FXML private TableView<Inventario> tablaInventario;
    @FXML private TableColumn<Inventario, String> colCodBarras;
    @FXML private TableColumn<Inventario, String> colDot;
    @FXML private TableColumn<Inventario, String> colMarca;
    @FXML private TableColumn<Inventario, String> colModelo;
    @FXML private TableColumn<Inventario, String> colMedida;
    @FXML private TableColumn<Inventario, String> colIndiceCar;
    @FXML private TableColumn<Inventario, String> colIndiceVel;
    @FXML private TableColumn<Inventario, Integer> colStock;
    @FXML private TableColumn<Inventario, Float> colPrecioCom;
    @FXML private TableColumn<Inventario, Float> colPrecioVen;
    @FXML private TableColumn<Inventario, String> colObservaciones;
    @FXML private TableColumn<Inventario, String> colFechaReg;
    @FXML private TextField buscarInventarioBuscador;
    @FXML private ChoiceBox<String> atributoBusquedaInventario;
    @FXML private HBox limpiarChoiceBox;
    @FXML private Label statusLabel;
    @FXML private Button btnAgregarInventario;

    private VentanaPrincipalController ventanaPrincipalController;


    private PauseTransition delayQuery = new PauseTransition(Duration.millis(300)); //evita que se ejecuta una query cada vez que el usuario
    //presiona una tecla hace un delay

    @Autowired
    private InventarioService inventarioService;

    @FXML
    private Pagination paginadorInventarios;
    private static final int INVENTARIOS_POR_PAGINA = 20;
    private List<Inventario> todosLosInventarios;
    private String terminoBusquedaActual = "";
    private boolean modoBusqueda = false;


    @FXML
    private void initialize() {

        cargarInventario();

        atributoBusquedaInventario.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {

            if (newValue != null) {

                if (newValue.toLowerCase().equals("sin stock"))
                    buscarInventarioBuscador.setEditable(false);

                else
                    buscarInventarioBuscador.setEditable(true);

                switch (newValue.toLowerCase()) {
                    case "sin stock" -> {
                        List<Inventario> inventarios = inventarioService.listarInventario(StatusInventario.SIN_STOCK.toString());
                        tablaInventario.setItems(FXCollections.observableList(inventarios));
                    }

                }
            }
        }));

//        //Buscar mientras escribes
       /* buscarInventarioBuscador.setOnKeyReleased(event -> {

            if (event.getCode() != KeyCode.ENTER) {
                delayQuery.setOnFinished(e -> {
                    String seleccion = atributoBusquedaInventario.getValue();
                    String busqueda = buscarInventarioBuscador.getText();

                    if (seleccion == null && busqueda != null && !busqueda.isEmpty()) {
                        buscarInventario(busqueda);
                    } else {
                        // Si no hay búsqueda, salir del modo búsqueda y recargar tabla
                        modoBusqueda = false;
                        terminoBusquedaActual = "";
                        paginadorInventarios.setPageFactory(this::crearPaginaInventario);
                        paginadorInventarios.setCurrentPageIndex(0);
                    }

                });
                delayQuery.playFromStart();
            }
        });*/
        buscarInventarioBuscador.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {

                String seleccion = atributoBusquedaInventario.getValue();
                String busqueda = buscarInventarioBuscador.getText();

                // SOLO funciona si hay un filtro seleccionado
                if (seleccion != null && !seleccion.isEmpty()) {

                    // Si el texto está vacío, resetea y detén
                    if (busqueda == null || busqueda.isEmpty() && seleccion == null) {
                        resetBusqueda();
                        return;
                    }

                    // Ejecutar búsqueda específica
                    buscarInventario(seleccion.toLowerCase(), busqueda);
                }
            }
        });

       /* buscarInventarioBuscador.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER) {
                String seleccion = atributoBusquedaInventario.getValue(), busqueda = buscarInventarioBuscador.getText();

                if (seleccion != null && !seleccion.isEmpty() && busqueda != null && !busqueda.isEmpty())
                    buscarInventario(seleccion.toLowerCase(), busqueda);
            }
        });*/
        buscarInventarioBuscador.setOnKeyReleased(event -> {

            if (event.getCode() == KeyCode.ENTER) return; // ignorar enter aquí

            delayQuery.setOnFinished(e -> {

                String seleccion = atributoBusquedaInventario.getValue();
                String busqueda = buscarInventarioBuscador.getText();

                // 🔥 SOLO ejecutar búsqueda general si NO hay filtro
                if (seleccion == null || seleccion.isEmpty()) {

                    if (busqueda == null || busqueda.isEmpty()) {
                        resetBusqueda();
                        return;
                    }

                    // Búsqueda general mientras escribe
                    buscarInventario(busqueda);
                }
                // Si sí hay filtro → no hace nada, porque solo se buscan al presionar ENTER
            });

            delayQuery.playFromStart();
        });

        //Clic derecho
        limpiarChoiceBox.setOnMouseClicked(event -> {
            if ((event.getButton() == MouseButton.MIDDLE || event.getButton() == MouseButton.PRIMARY) && event.getClickCount() == 2)
                atributoBusquedaInventario.setValue(null);
        });

        tablaInventario.setRowFactory(tabla -> {
            TableRow<Inventario> fila = new TableRow<>();

            fila.setOnContextMenuRequested(event -> {
                if (!fila.isEmpty()) {
                    Inventario seleccionado = fila.getItem();
                    Integer inventarioId = seleccionado.getInventarioId();

                    ListView<String> listaOpciones = new ListView<>();
                    listaOpciones.getItems().addAll("Ver informacion", "Copiar", "Copiar fila completa", "Editar", "Eliminar");
                    listaOpciones.setPrefSize(200, 150);
                    listaOpciones.getStyleClass().add("popup-table");
                    listaOpciones.getStylesheets().add(
                            getClass().getResource("/styles_css/Lista.css").toExternalForm()
                    );

                    Popup listViewPopup = new Popup();
                    listViewPopup.getContent().add(listaOpciones);
                    listViewPopup.setAutoHide(true);


                    listaOpciones.setOnMouseClicked(e -> {
                        String seleccion = listaOpciones.getSelectionModel().getSelectedItem();

                        if (seleccion != null) {
                            switch (seleccion) {


                                case "Ver informacion" -> {
                                    try {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/inventario/DetalleInventario.fxml"));
                                        loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);

                                        Parent root = loader.load();
                                        DetalleInventarioController detalleInventario = loader.getController();
                                        detalleInventario.InformacionInventario(seleccionado);


                                        Stage stage = new Stage(StageStyle.UTILITY);
                                        stage.setTitle("Informacion del inventario");
                                        stage.setScene(new Scene(root));
                                        stage.showAndWait();


                                    } catch (IOException ex) {
                                        mostrarError("Error", "", "Ocurrio un error");
                                    }

                                }

                                case "Eliminar" -> {

                                    String elementoEliminar = seleccionado.getMarca() + " " + seleccionado.getModelo() + " " + seleccionado.getMedida();

                                    String mensaje = "¿Estas seguro que quieres eliminar el siguiente elemento? \n\n " + elementoEliminar;

                                    boolean eliminar = mostrarConfirmacion("Eliminar llanta", mensaje, "Esta accion no se podra deshacer", "Eliminar", "Cancelar");

                                    if (eliminar) {

                                        inventarioService.eliminarInventario(StatusInventario.INACTIVE.toString(), inventarioId);
                                        cargarInventario();

                                    } else
                                        mostrarInformacion("Accion cancelada", "", "Accion cancelada");

                                }
                                case "Editar" -> {
                                    try {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/inventario/EditarInventario.fxml"));
                                        loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);

                                        Parent root = loader.load();
                                        EditarInventarioController controller = loader.getController();
                                        controller.editarInventario(seleccionado);
                                        Stage stage = new Stage(StageStyle.UTILITY);
                                        stage.setTitle("Editar inventario");
                                        stage.setResizable(false);
                                        stage.setScene(new Scene(root));
                                        stage.showAndWait();

                                    } catch (IOException ex) {
                                        mostrarError("Ocurrio un error", "", "Ocurrio un error al mostrar la ventana");
                                        ex.printStackTrace();
                                    }
                                }
                                case "Copiar" -> {
                                    var selectedCell = tablaInventario.getSelectionModel().getSelectedCells();

                                    if (!selectedCell.isEmpty()) {
                                        TablePosition<?, ?> position = selectedCell.get(0);
                                        int row = position.getRow();
                                        TableColumn<?, ?> columna = position.getTableColumn();

                                        Object value = columna.getCellData(row);

                                        int indexColumna = tablaInventario.getColumns().indexOf(columna);

                                        if (indexColumna == 11) {
                                            listViewPopup.hide();
                                            return;
                                        }


                                        if (value != null) {
                                            ClipboardContent content = new ClipboardContent();
                                            content.putString(value.toString());
                                            Clipboard.getSystemClipboard().setContent(content);

                                            statusLabel.setVisible(true);
                                            statusLabel.setText("Texto copiado");

                                            new Thread(() -> {

                                                try {
                                                    Thread.sleep(2500);
                                                } catch (Exception exception) {
                                                    exception.printStackTrace();
                                                }
                                                Platform.runLater(() -> statusLabel.setText(""));
                                            }).start();


                                        }


                                    }
                                }
                                case "Copiar fila completa" -> {
                                    Inventario item = tablaInventario.getSelectionModel().getSelectedItem();
                                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                    DateTimeFormatter outPutFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                                    String fecha = item.getCreated_at().toString();

                                    LocalDateTime fechaHora = LocalDateTime.parse(fecha, inputFormatter);
                                    String fechaFormat = fechaHora.format(outPutFormatter);


                                    String filaCopiada = item.getCodigoBarras() + " " + item.getDot() + " " + item.getMarca() + " " + item.getModelo() + " " + item.getMedida() + " " +
                                            item.getIndiceCarga() + " " + item.getIndiceVelocidad() + " " + item.getStock() + " " + item.getPrecioCompra() + " " + item.getPrecioVenta() + " " +
                                            item.getObservaciones() + " " + fechaFormat;

                                    ClipboardContent content = new ClipboardContent();
                                    content.putString(filaCopiada);
                                    Clipboard.getSystemClipboard().setContent(content);

                                    statusLabel.setVisible(true);
                                    statusLabel.setText("Fila copiada");

                                    new Thread(() -> {
                                        try {
                                            Thread.sleep(2500);
                                        } catch (Exception exception) {
                                            exception.printStackTrace();
                                        }
                                        Platform.runLater(() -> statusLabel.setText(""));
                                    }).start();


                                }

                            }//switch
                            listViewPopup.hide();
                        }
                    });
                    Point2D coordenadasPantalla = fila.localToScreen(event.getX(), event.getY());
                    listViewPopup.show(fila.getScene().getWindow(), coordenadasPantalla.getX(), coordenadasPantalla.getY());


                }//if if empty
            });

            return fila;
        });

        colObservaciones.setPrefWidth(400);
        colObservaciones.setMinWidth(100);


    }//initialize

    private void resetBusqueda() {
        modoBusqueda = false;
        terminoBusquedaActual = "";
        atributoBusquedaInventario.setValue(null);
        buscarInventarioBuscador.clear();

        paginadorInventarios.setPageFactory(this::crearPaginaInventario);
        paginadorInventarios.setCurrentPageIndex(0);

        cargarDatosInventario();  // vuelve a cargar conteo y items normales
    }


    private VBox crearPaginaInventario(int indicePagina) {
        Page<Inventario> paginaInventario;

        if (modoBusqueda) {
            paginaInventario = inventarioService.buscadorInventarioPaginado(
                    StatusInventario.ACTIVE.toString(),
                    terminoBusquedaActual,
                    indicePagina,
                    INVENTARIOS_POR_PAGINA
            );
        } else {
            paginaInventario = inventarioService.listarInventarioPaginado(
                    StatusInventario.ACTIVE.toString(),
                    indicePagina,
                    INVENTARIOS_POR_PAGINA
            );
        }

        tablaInventario.setItems(FXCollections.observableArrayList(paginaInventario.getContent()));

        VBox contenedor = new VBox(tablaInventario);
        contenedor.setMinHeight(500);
        contenedor.setPrefHeight(500);
        contenedor.setStyle("-fx-background-color: transparent;");
        return contenedor;
    }

    private VBox crearPaginaInventarioFiltro(int indicePagina) {
        Page<Inventario> paginaInventario = Page.empty();


        if (modoBusqueda) {
            String busqueda = terminoBusquedaActual.trim();
            String activo = StatusInventario.ACTIVE.toString();
            String key = atributoBusquedaInventario.getValue();
            switch (key.toLowerCase()) {

                case "codigo de barras" -> {
                    paginaInventario = inventarioService.buscarPorCodBarrasPaginado(activo, busqueda, indicePagina, INVENTARIOS_POR_PAGINA);
                }
                case "dot" -> {
                    paginaInventario = inventarioService.buscarPorDotPaginado(activo, busqueda, indicePagina, INVENTARIOS_POR_PAGINA);
                }
                case "marca" -> {
                    paginaInventario = inventarioService.buscarPorMarcaPaginado(activo, busqueda, indicePagina, INVENTARIOS_POR_PAGINA);
                }
                case "modelo" -> {
                    paginaInventario = inventarioService.buscarPorModeloPaginado(activo, busqueda, indicePagina, INVENTARIOS_POR_PAGINA);
                }
                case "medida" -> {
                    paginaInventario = inventarioService.buscarPorMedidaPaginado(activo, busqueda, indicePagina, INVENTARIOS_POR_PAGINA);
                }
                case "fecha de registro" -> {
                        boolean consultar = false;

                        if (busqueda.matches("\\d{2}-\\d{2}-\\d{4}")) {
                            String fecha = busqueda;
                            LocalDate fechaConsulta = null;

                            try {
                                DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                DateTimeFormatter formatterConsulta = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                                LocalDate fechaLD = LocalDate.parse(fecha, formatterEntrada);
                                fechaConsulta = LocalDate.parse(fechaLD.format(formatterConsulta));

                                consultar = true;

                            } catch (DateTimeParseException e) {
                                mostrarWarning("Fecha no valida", "", "La fecha ingresada no es valida vuelva a intentarlo");
                                consultar = false;

                            }

                            if (consultar) {
                                paginaInventario = inventarioService.buscarPorFechaPaginado(activo, fechaConsulta, indicePagina, INVENTARIOS_POR_PAGINA);
                            }

                        } else if (busqueda.matches("\\d{2}-\\d{2}-\\d{4},\\d{2}-\\d{2}-\\d{4}")) {  //rango de fechas

                            String fecha[] = busqueda.split(",");
                            String consultaInicio = "", consultaFinal = "";

                            try {
                                DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                DateTimeFormatter formatterConsulta = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                                LocalDate fecha1 = LocalDate.parse(fecha[0], formatterEntrada);
                                LocalDate fecha2 = LocalDate.parse(fecha[1], formatterEntrada);

                                //Ordenar la fecha mayor
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
                                paginaInventario = inventarioService.buscarPorFechaPaginadoRangos(activo, LocalDate.parse(consultaInicio), LocalDate.parse(consultaFinal), indicePagina, INVENTARIOS_POR_PAGINA);
                            }


                        } else {
                            List<Inventario> inventarioVacio = new ArrayList<>();
                            tablaInventario.setItems(FXCollections.observableList(inventarioVacio));
                            mostrarWarning("Formato incorrecto", "Favor de ingresar un formato correcto", "Por ejemplo dd-mm-yyyy o bien" +
                                    " dd-mm-yyyy,dd-mm-yyyy si desea buscar por un rango de fechas");
                        }

                    }
                default -> {
                        mostrarWarning("Informacion no valida", "", "Asegurese de buscar por el campo correspondiente.");
                    }
                }//switch
            } else {
                paginaInventario = inventarioService.listarInventarioPaginado(
                        StatusVehiculo.ACTIVE.toString(), indicePagina, INVENTARIOS_POR_PAGINA);
            }



        tablaInventario.setItems(FXCollections.observableArrayList(paginaInventario.getContent()));

        VBox contenedor = new VBox(tablaInventario);
        contenedor.setMinHeight(500);
        contenedor.setPrefHeight(500);
        contenedor.setStyle("-fx-background-color: transparent;");
        return contenedor;
    }

    private void cargarInventario() {

        // Helper para valores String
        final Function<String, String> safe = s ->
                (s == null || s.trim().isEmpty()) ? "N/A" : s;


        colCodBarras.setCellValueFactory(data ->
                new SimpleStringProperty(safe.apply(data.getValue().getCodigoBarras()))
        );

        colDot.setCellValueFactory(data ->
                new SimpleStringProperty(safe.apply(data.getValue().getDot()))
        );

        colMarca.setCellValueFactory(data ->
                new SimpleStringProperty(safe.apply(data.getValue().getMarca()))
        );

        colModelo.setCellValueFactory(data ->
                new SimpleStringProperty(safe.apply(data.getValue().getModelo()))
        );

        colMedida.setCellValueFactory(data ->
                new SimpleStringProperty(safe.apply(data.getValue().getMedida()))
        );

        colIndiceCar.setCellValueFactory(data ->
                new SimpleStringProperty(safe.apply(data.getValue().getIndiceCarga()))
        );

        colIndiceVel.setCellValueFactory(data ->
                new SimpleStringProperty(safe.apply(data.getValue().getIndiceVelocidad()))
        );

        colStock.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getStock()).asObject()
        );

        colPrecioCom.setCellValueFactory(data ->
                new SimpleFloatProperty(data.getValue().getPrecioCompra()).asObject()
        );

        colPrecioVen.setCellValueFactory(data ->
                new SimpleFloatProperty(data.getValue().getPrecioVenta()).asObject()
        );

        colFechaReg.setCellValueFactory(data -> {

            String fechaStr = data.getValue().getCreated_at();

            if (fechaStr == null || fechaStr.trim().isEmpty()) {
                return new SimpleStringProperty("N/A");
            }

            try {
                DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime fecha = LocalDateTime.parse(fechaStr, formatterEntrada);

                String salida = fecha.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

                return new SimpleStringProperty(salida);
            } catch (Exception e) {
                return new SimpleStringProperty("N/A");
            }
        });


        colObservaciones.setCellValueFactory(data ->
                new SimpleStringProperty(safe.apply(data.getValue().getObservaciones()))
        );

        cargarDatosInventario();
    }//cargarTabla

    private String valorONull(String valor) {
        return (valor == null || valor.isBlank()) ? "N/A" : valor;
    }

    private void cargarDatosInventario() {

        try {
            long totalvehiculos = inventarioService.contarInventariosActivos(StatusInventario.ACTIVE.toString());
            int totalPaginas = (int) Math.ceil((double) totalvehiculos / INVENTARIOS_POR_PAGINA);
            paginadorInventarios.setPageCount(Math.max(totalPaginas, 1));
            paginadorInventarios.setPageFactory(this::crearPaginaInventario);

            // Muestra la primera página
            paginadorInventarios.setCurrentPageIndex(0);

        } catch (Exception e) {
            mostrarError("Error al mostrar datos", "", "No se pudieron cargar los datos. Por favor, inténtalo de nuevo más tarde.");
        }


    }//cargarDatosInventario

    private void buscarInventario(String busqueda) {
        terminoBusquedaActual = busqueda;
        modoBusqueda = !busqueda.trim().isEmpty();

        if (modoBusqueda) {
            // 🔥 total de resultados para el buscador general
            long totalResultados = inventarioService.contarInventarioPorBusquedaGeneral(
                    StatusInventario.ACTIVE.toString(),
                    terminoBusquedaActual
            );

            int totalPaginas = (int) Math.ceil((double) totalResultados / INVENTARIOS_POR_PAGINA);

            paginadorInventarios.setPageCount(Math.max(totalPaginas, 1));
        } else {
            // Si no hay búsqueda, restaurar paginación normal
            long totalClientes = inventarioService.contarInventariosActivos(StatusInventario.ACTIVE.toString());
            int totalPaginas = (int) Math.ceil((double) totalClientes / INVENTARIOS_POR_PAGINA);
            paginadorInventarios.setPageCount(Math.max(totalPaginas, 1));
        }

        paginadorInventarios.setCurrentPageIndex(0); // Reinicia la paginación
        paginadorInventarios.setPageFactory(this::crearPaginaInventario);

    }

    private void buscarInventario(String seleccion, String busqueda) { // porque se va a buscar, vehiculo a buscar

        terminoBusquedaActual = busqueda;
        modoBusqueda = !busqueda.trim().isEmpty();
        String activo = StatusInventario.ACTIVE.toString();
        //atributoBusquedaInventario.setValue(seleccion);

        Page<Inventario> paginaFiltrada = null;

        switch (seleccion) {
            case "codigo de barras" -> {
                paginaFiltrada = inventarioService.buscarPorCodBarrasPaginado(activo, busqueda, 0, INVENTARIOS_POR_PAGINA);
            }
            case "dot" -> {
                paginaFiltrada = inventarioService.buscarPorDotPaginado(activo, busqueda, 0, INVENTARIOS_POR_PAGINA);
            }
            case "marca" -> {
                paginaFiltrada = inventarioService.buscarPorMarcaPaginado(activo, busqueda, 0, INVENTARIOS_POR_PAGINA);
            }
            case "modelo" -> {
                paginaFiltrada = inventarioService.buscarPorModeloPaginado(activo, busqueda, 0, INVENTARIOS_POR_PAGINA);
            }
            case "medida" -> {
                paginaFiltrada = inventarioService.buscarPorMedidaPaginado(activo, busqueda, 0, INVENTARIOS_POR_PAGINA);
            }
            case "fecha de registro" -> {
                boolean consultar = false;

                if (busqueda.matches("\\d{2}-\\d{2}-\\d{4}")) {
                    String fecha = busqueda;
                    LocalDate fechaConsulta = null;

                    try {
                        DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        DateTimeFormatter formatterConsulta = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                        LocalDate fechaLD = LocalDate.parse(fecha, formatterEntrada);
                        fechaConsulta = LocalDate.parse(fechaLD.format(formatterConsulta));

                        consultar = true;

                    } catch (DateTimeParseException e) {
                        mostrarWarning("Fecha no valida", "", "La fecha ingresada no es valida vuelva a intentarlo");
                        consultar = false;

                    }

                    if (consultar) {
                        paginaFiltrada = inventarioService.buscarPorFechaPaginado(activo, fechaConsulta, 0, INVENTARIOS_POR_PAGINA);
                    }

                } else if (busqueda.matches("\\d{2}-\\d{2}-\\d{4},\\d{2}-\\d{2}-\\d{4}")) {  //rango de fechas

                    String fecha[] = busqueda.split(",");
                    String consultaInicio = "", consultaFinal = "";

                    try {
                        DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        DateTimeFormatter formatterConsulta = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                        LocalDate fecha1 = LocalDate.parse(fecha[0], formatterEntrada);
                        LocalDate fecha2 = LocalDate.parse(fecha[1], formatterEntrada);

                        //Ordenar la fecha mayor
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
                        paginaFiltrada = inventarioService.buscarPorFechaPaginadoRangos(activo, LocalDate.parse(consultaInicio), LocalDate.parse(consultaFinal), 0, INVENTARIOS_POR_PAGINA);
                    }


                } else {
                    List<Inventario> inventarioVacio = new ArrayList<>();
                    tablaInventario.setItems(FXCollections.observableList(inventarioVacio));
                    mostrarWarning("Formato incorrecto", "Favor de ingresar un formato correcto", "Por ejemplo dd-mm-yyyy o bien" +
                            " dd-mm-yyyy,dd-mm-yyyy si desea buscar por un rango de fechas");
                }

            }
            default -> {
                mostrarWarning("Informacion no valida", "", "Asegurese de buscar por el campo correspondiente.");
            }
        }

        // ACTUALIZA EL PAGECOUNT CON EL TOTAL DE COINCIDENCIAS
        int totalPaginas = paginaFiltrada.getTotalPages();
        paginadorInventarios.setPageCount(Math.max(totalPaginas, 1));


        // Reinicia el paginador para la búsqueda
        paginadorInventarios.setPageFactory(this::crearPaginaInventario);
        paginadorInventarios.setCurrentPageIndex(0);

    }//buscarVehiculo

    @FXML
    private void agregarInventario(ActionEvent event) {

        ventanaPrincipalController.viewContent(
                null,
                "/fxmlViews/inventario/AgregarInventario.fxml",
                "Agregar llanta"
        );
        ventanaPrincipalController.cambiarPaginaEtiqueta.setText("Agregar llanta");



    }//agregarInventario

    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;
    }

    //actualiza
    @FXML
    private void actualizarTabla(ActionEvent actionEvent){
        atributoBusquedaInventario.setValue(null);
        buscarInventarioBuscador.setText("");

        modoBusqueda = false;
        terminoBusquedaActual = "";

        paginadorInventarios.setPageFactory(this::crearPaginaInventario);
        paginadorInventarios.setCurrentPageIndex(0);

        //cargarInventario();
    }

}//clase
