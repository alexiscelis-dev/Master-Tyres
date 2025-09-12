package com.mastertyres.fxControllers.inventario;

import com.mastertyres.inventario.model.Inventario;
import com.mastertyres.inventario.model.StatusInventario;
import com.mastertyres.inventario.service.InventarioService;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static com.mastertyres.common.MensajesAlert.*;

@Component
public class InventarioController {
    @FXML
    private TableView<Inventario> tablaInventario;
    @FXML
    private TableColumn<Inventario, String> colCodBarras;
    @FXML
    private TableColumn<Inventario, String> colDot;
    @FXML
    private TableColumn<Inventario, String> colMarca;
    @FXML
    private TableColumn<Inventario, String> colModelo;
    @FXML
    private TableColumn<Inventario, String> colMedida;
    @FXML
    private TableColumn<Inventario, String> colIndiceCar;
    @FXML
    private TableColumn<Inventario, String> colIndiceVel;
    @FXML
    private TableColumn<Inventario, Integer> colStock;
    @FXML
    private TableColumn<Inventario, Float> colPrecioCom;
    @FXML
    private TableColumn<Inventario, Float> colPrecioVen;
    @FXML
    private TableColumn<Inventario, String> colObservaciones;
    @FXML
    private TableColumn<Inventario, ImageView> colImg;
    @FXML
    private TableColumn<Inventario, String> colFechaReg;
    @FXML
    private TextField buscarInventarioBuscador;
    @FXML
    private ChoiceBox<String> atributoBusquedaInventario;
    @FXML
    private HBox limpiarChoiceBox;
    @FXML
    private Label statusLabel;

    private PauseTransition delayQuery = new PauseTransition(Duration.millis(300)); //evita que se ejecuta una query cada vez que el usuario
    //presiona una tecla hace un delay

    @Autowired
    private InventarioService inventarioService;


    @FXML
    private void initialize() {
        cargarInventario();

        //Buscar mientras escribes
        buscarInventarioBuscador.setOnKeyReleased(event -> {

            //Buscar mientras escribes


            if (event.getCode() != KeyCode.ENTER)
                delayQuery.setOnFinished(e -> {
                    String seleccion = atributoBusquedaInventario.getValue();
                    String busqueda = buscarInventarioBuscador.getText();

                    if (seleccion == null && busqueda != null && !busqueda.isEmpty())
                        buscarInventario(busqueda);
                    else if (seleccion == null)
                        cargarInventario();
                });
            delayQuery.playFromStart();

        });

        buscarInventarioBuscador.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER) {
                String seleccion = atributoBusquedaInventario.getValue(), busqueda = buscarInventarioBuscador.getText();

                if (seleccion != null && !seleccion.isEmpty() && busqueda != null && !busqueda.isEmpty())
                    buscarInventario(seleccion.toLowerCase(), busqueda);
            }
        });
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
                                    System.out.println("Editar");
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


        colImg.setCellFactory(col -> {
            return new TableCell<Inventario, ImageView>()  {
                private final ImageView imageView = new ImageView();

                {
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);
                    imageView.setPreserveRatio(true);

                    imageView.setOnMouseClicked(event -> {
                        if (getItem() != null)
                            //  expandirImagen(getItem());
                            System.out.println();
                    });

                }//ImageView

                @Override
                protected void updateItem(ImageView item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null)
                        setGraphic(null);
                    else {
                      setGraphic(item);

                        item.setOnMouseClicked(event -> {
                            ImageView grande = new ImageView(item.getImage());
                            grande.setPreserveRatio(true);
                            grande.setFitWidth(600);
                            grande.setFitHeight(600);

                            StackPane root = new StackPane(grande);
                            root.setPadding(new Insets(10));

                            Popup popup = new Popup();
                            popup.getContent().add(root);

                            // Mostrar sobre la ventana principal
                            popup.show(getScene().getWindow());

                            // Para cerrar al hacer clic en cualquier lugar del popup
                            root.setOnMouseClicked(e -> popup.hide());


                        });

                    }

                }//updatedItem


            };
        });

    }//initialize


    private void cargarInventario() {

        colCodBarras.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCodigoBarras())
        );

        colDot.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDot())
        );

        colMarca.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getMarca())
        );

        colModelo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getModelo())
        );

        colMedida.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getMedida())
        );

        colIndiceCar.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getIndiceCarga())
        );

        colIndiceVel.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getIndiceVelocidad())
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
                    DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    String fechaStr = data.getValue().getCreated_at();
                    LocalDate fecha = LocalDate.parse(fechaStr, formatterEntrada);
                    String texto = fecha.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

                    return new SimpleStringProperty(texto);
                }
        );

        colObservaciones.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getObservaciones())
        );
        colImg.setCellValueFactory(data ->
        {
            String ruta = data.getValue().getImagen();
            ImageView imagenView = new ImageView();

            try {
                if (ruta != null && !ruta.isEmpty() && new File(ruta).exists()) {
                    Image image = new Image("file:" + ruta, 100, 100, true, true);
                    imagenView.setImage(image);

                } else {
                    InputStream is = getClass().getResourceAsStream("/icons/imagenPorDefecto.jpg");

                    if (is != null) {
                        Image defaultImage = new Image(is, 100, 100, true, true);
                        imagenView.setImage(defaultImage);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }//try catch
            imagenView.setFitWidth(100);
            imagenView.setFitHeight(100);
            imagenView.setPreserveRatio(true);

            return new SimpleObjectProperty<>(imagenView);
        });

        colImg.setCellFactory(column -> new TableCell<Inventario, ImageView>() {
                    @Override
                    protected void updateItem(final ImageView item, final boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            setGraphic(item);
                        }
                    }
                }

        );

        cargarDatosInventario();

    }//cargarTabla

    private void cargarDatosInventario() {

        try {
            tablaInventario.getItems().setAll(inventarioService.listarInventario(StatusInventario.ACTIVE.toString()));


        } catch (Exception e) {
            mostrarError("Error al mostrar datos", "", "No se pudieron cargar los datos. Por favor, inténtalo de nuevo más tarde.");
        }

    }//cargarDatosInventario

    private void buscarInventario(String busqueda) {

        List<Inventario> inventarios = inventarioService.buscadorInventario(StatusInventario.ACTIVE.toString(), busqueda);
        tablaInventario.setItems(FXCollections.observableList(inventarios));
    }//buscarInventario

    private void buscarInventario(String seleccion, String busqueda) {

        switch (seleccion) {


            case "codigo de barras" -> {
                List<Inventario> inventarios = inventarioService.buscarPorCodBarras(StatusInventario.ACTIVE.toString(), busqueda);
                tablaInventario.setItems(FXCollections.observableList(inventarios));

            }
            case "dot" -> {
                List<Inventario> inventarios = inventarioService.buscarPorDot(StatusInventario.ACTIVE.toString(), busqueda);
                tablaInventario.setItems(FXCollections.observableList(inventarios));
            }
            case "marca" -> {
                List<Inventario> inventarios = inventarioService.buscarPorMarca(StatusInventario.ACTIVE.toString(), busqueda);
                tablaInventario.setItems(FXCollections.observableList(inventarios));
            }
            case "modelo" -> {
                List<Inventario> inventarios = inventarioService.buscarPorModelo(StatusInventario.ACTIVE.toString(), busqueda);
                tablaInventario.setItems(FXCollections.observableList(inventarios));
            }
            case "medida" -> {
                List<Inventario> inventarios = inventarioService.buscarPorMedida(StatusInventario.ACTIVE.toString(), busqueda);
                tablaInventario.setItems(FXCollections.observableList(inventarios));
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
                        List<Inventario> inventarios = inventarioService.buscarPorFecha(StatusInventario.ACTIVE.toString(), fechaConsulta);
                        tablaInventario.setItems(FXCollections.observableList(inventarios));
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
                        List<Inventario> inventarios = inventarioService.buscarPorFecha(StatusInventario.ACTIVE.toString(), LocalDate.parse(consultaInicio), LocalDate.parse(consultaFinal));
                        tablaInventario.setItems(FXCollections.observableList(inventarios));

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

    }//buscarInventario

}//clase
