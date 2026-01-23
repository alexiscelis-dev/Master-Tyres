package com.mastertyres.fxControllers.historial;

import com.mastertyres.fxComponents.LoadingComponentController;
import com.mastertyres.fxComponents.interfaces.ILoading;
import com.mastertyres.nota.model.BaseNota;
import com.mastertyres.nota.model.NotaDTO;
import com.mastertyres.nota.service.NotaService;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HistorialController extends BaseNota implements ILoading {
    @FXML
    private TilePane contenedorHistorial;
    @FXML
    private VBox detallePromocion;
    @FXML
    private ImageView ivZoom;
    @FXML
    private ChoiceBox<Integer> choiceLimite;

    @Autowired
    private NotaService notaService;

    private LoadingComponentController loadingOverlayController;
    private VBox cardSeleccionada = null;

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;

    }

    @FXML
    private void initialize() {

        configuraciones();
        cargarNota();

    }//initialize

    private void configuraciones() {
        zoom();

    }//configuraciones

    private void zoom() {

        detallePromocion.setOnMouseEntered(event -> {
            ivZoom.setVisible(true);
        });

        detallePromocion.setOnMouseExited(event -> {
            ivZoom.setVisible(false);
        });
    }//zoom


    private void cargarNota(){
        contenedorHistorial.setVgap(20);
        configurarChoiceBox();
    }//cargarNota

    private void configurarChoiceBox(){
        String limite = choiceLimite.getValue() != null ? choiceLimite.getValue().toString() : "5";

        List<NotaDTO> historial = notaService.buscarHistorial(Integer.parseInt(limite));

        mostrarNotas(historial);


    }//configurarChoiceBox

    private void mostrarNotas(List<NotaDTO> historial){
        contenedorHistorial.getChildren().clear();
        cardSeleccionada = null;

        for (NotaDTO nota : historial){
            VBox card = crearCardNota(nota);
            contenedorHistorial.getChildren().add(card);
        }

    }//mostrarNotas

    private VBox crearCardNota(NotaDTO cardHistorial){

        VBox card = new VBox();
        String estiloVerde = "-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10";
        String estiloSeleccionado = "-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10; -fx-text-fill: #1A1A1A;";
        String estiloPorDefecto = "";
        card.setStyle(estiloVerde);
        card.setUserData(estiloPorDefecto);

        card.setPrefSize(500, 100);

        Label numeroNota = new Label(cardHistorial.getNumNota());
        numeroNota.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");


        String nombreCliente = cardHistorial.getNombreClienteNota() != null
                ? notaUtils.eliminarPuntos(cardHistorial.getNombreClienteNota())
                : "SIN CLIENTE";

        Label lblCliente = new Label(nombreCliente);

        lblCliente.setStyle("-fx-text-fill: white;");

        String marca = cardHistorial.getMarcaNota() != null
                ? notaUtils.eliminarPuntos(cardHistorial.getMarcaNota())
                : "";

        String modelo = cardHistorial.getModeloNota() != null
                ? notaUtils.eliminarPuntos(cardHistorial.getModeloNota())
                : "";

        String anio = cardHistorial.getAnioNota() != null
                ? cardHistorial.getAnioNota().toString()
                : "";

        Label lblVehiculo = new Label((marca + " " + modelo + " " + anio).trim());

        lblVehiculo.setStyle("-fx-text-fill: white;");

        Label total = new Label("Total: $" + cardHistorial.getTotal());

        total.setStyle("-fx-text-fill: white;");

        VBox textBox = new VBox(5, numeroNota, lblCliente, lblVehiculo, total);
        HBox contenBox = new HBox(10, textBox);

        card.getChildren().add(contenBox);

        card.setOnMouseEntered(e -> {
            if (card != cardSeleccionada) {
                card.setStyle(estiloSeleccionado);
            }
        });

        card.setOnMouseExited(e -> {
            if (card != cardSeleccionada) {
                card.setStyle((String) card.getUserData());
            }
        });

        card.setOnMouseClicked(e -> {
            if (cardSeleccionada != null && cardSeleccionada != card) {
                cardSeleccionada.setStyle((String) cardSeleccionada.getUserData());
            }
            cardSeleccionada = card;
            card.setStyle(estiloSeleccionado);
        });

        return  card;
    }//crearCardNota


}//class
