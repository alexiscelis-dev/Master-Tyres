package com.mastertyres.fxControllers.nota;

import com.mastertyres.fxControllers.ventanaPrincipal.VentanaPrincipalController;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.model.StatusNota;
import com.mastertyres.nota.service.NotaService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class NotaController {
    @FXML
    private TilePane contenedorNotas;
    @FXML
    private Label lblStatus;
    @FXML
    private Label lblNumNota;
    @FXML
    private Label lblNumFactura;
    @FXML
    private Label lblCliente;
    @FXML
    private Label lblVehiculo;
    @FXML
    private Label lblFechaEmicion;
    @FXML
    private Label lblFechaLimite;
    @FXML
    private Label lblSaldoFavor;
    @FXML
    private Label lblAdeudo;
    @FXML
    private Label lblTotal;
    @FXML
    private Button btnNuevaNota;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnImprimir;
    @FXML
    private Button btnDarPlazo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnHistorial;
    @FXML
    private TextField txtBuscar;

    private VentanaPrincipalController ventanaPrincipalController;


    private NotaDTO notaSeleccionada;


    @Autowired
    NotaService notaService;

    public void setVentanaPrincipalController(VentanaPrincipalController controller) {
        this.ventanaPrincipalController = controller;
    }

    @FXML
    private void initialize() {
        cargarNota();

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty())
                cargarNota();
            else
                cargarNotasFiltradas();
        });

    }//initialize

    private void cargarNota() {
        List<NotaDTO> notas = notaService.listarNotas(StatusNota.ACTIVE.toString());
        mostrarNotas(notas);


    }//cargarNota

    private void mostrarNotas(List<NotaDTO> notas) {
        contenedorNotas.getChildren().clear();

        for (NotaDTO nota : notas) {
            VBox card = crearCardNota(nota);
            contenedorNotas.getChildren().add(card);
        }

    }//mostrarNotas

    private VBox crearCardNota(NotaDTO nota) {

        VBox card = new VBox();
        card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;");
        card.setPrefSize(500, 100);


        Label numeroNota = new Label(nota.getNumNota());
        numeroNota.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");
        Label cliente = new Label(
                nota.getNombreCliente() + " " + (nota.getApellido() != null ? nota.getApellido() : "") + " " +
                        (nota.getSegundoApellido() != null ? nota.getSegundoApellido() : "")
        );
        cliente.setStyle("-fx-text-fill: white;");
        Label vehiculo = new Label(
                nota.getMarca() + " " + nota.getModelo() + " " + nota.getAnio()
        );
        vehiculo.setStyle("-fx-text-fill: white;");
        Label total = new Label("Total: $" + nota.getTotal());
        total.setStyle("-fx-text-fill: white;");

        VBox textBox = new VBox(5, numeroNota, cliente, vehiculo, total);
        HBox contenBox = new HBox(10, textBox);
        card.getChildren().add(contenBox);


        // ========= ESTILOS INTERACTIVOS =========
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"));

        // Al hacer clic
        card.setOnMouseClicked(event -> {
            card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;");
            mostrarDetalleNota(nota);
        });

        return card;
    }//crearCardNota

    private void mostrarDetalleNota(NotaDTO nota) {
        notaSeleccionada = nota;

        btnEditar.setDisable(false);
        btnImprimir.setDisable(false);
        btnDarPlazo.setDisable(false);
        btnEliminar.setDisable(false);
        btnHistorial.setDisable(false);

        switch (nota.getStatusNota()) {

            case "PAGADO" -> lblStatus.setText("PAGADA");
            case "POR_PAGAR" -> lblStatus.setText("POR PAGAR");
            case "VENCIDO" -> lblStatus.setText("VENCIDO");
            case "A_FAVOR" -> lblStatus.setText("A FAVOR");

        }

        DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fechaStr = nota.getCreatedAt();
        LocalDateTime fecha = LocalDateTime.parse(fechaStr, formatterEntrada);
        String fechaFormateada = fecha.format(formatter2);


        lblNumNota.setText(nota.getNumNota());


        lblNumFactura.setText(nota.getNumFactura() != null ? nota.getNumFactura() : "Sin facturar");


        lblCliente.setText(nota.getNombreCliente() + " " + (nota.getApellido() != null ? nota.getApellido() : "") + " " +
                (nota.getSegundoApellido() != null ? nota.getSegundoApellido() : ""));
        lblVehiculo.setText(nota.getMarca() + " " + nota.getModelo() + " " + nota.getAnio());


        lblFechaEmicion.setText(fechaFormateada);

        lblFechaLimite.setText(nota.getFechaVencimiento());

        lblAdeudo.setText("$" + nota.getAdeudo());
        lblTotal.setText("$" + nota.getTotal());


        lblSaldoFavor.setText("$" + nota.getSaldoFavor());


    }//mostrarDetalleNota

    private void cargarNotasFiltradas() {

    }//cargarNotasFiltradas

    @FXML
    private void agregarNotas(ActionEvent actionEvent) {

        ventanaPrincipalController.viewContent(
                null,
                "/fxmlViews/nota/NotaFormulario.fxml",
                "Agregar Nota");

    }//agregarNotas


}//class
