package com.mastertyres.fxControllers.EditarMarcasModelosCategorias;

import com.mastertyres.common.ApplicationContextProvider;
import com.mastertyres.detalleCategoria.model.DetalleCategoria;
import com.mastertyres.detalleCategoria.service.DetalleCategoriaService;
import com.mastertyres.marca.model.Marca;
import com.mastertyres.marca.services.MarcaService;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class EditarMarcaModeloCategoriaController {

    @FXML private Label lblNombre;
    @FXML private TableView<DetalleCategoria> TablaVehiculoMarca;
    @FXML private TableColumn<DetalleCategoria, String> colMarca;
    @FXML private TableColumn<DetalleCategoria, String> colModelo;
    @FXML private TableColumn<DetalleCategoria, String> colCategoria;
    @FXML private Button btnAgregarMarca;
    @FXML private Button btnAgregarModelo;
    @FXML private Button EliminarMarca;
    @FXML private TilePane contenedorMarcas;
    @FXML private ListView<Marca> listaMarca;

    private final MarcaService marcaService;
    private final DetalleCategoriaService detalleCategoriaService;

    private Marca marcaSeleccionada;

    public EditarMarcaModeloCategoriaController(MarcaService marcaService,
                                                DetalleCategoriaService detalleCategoriaService) {
        this.marcaService = marcaService;
        this.detalleCategoriaService = detalleCategoriaService;
    }

    @FXML
    private void initialize(){

        colMarca.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMarca().getNombreMarca()));

        colModelo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getModelo().getNombreModelo()));

        colCategoria.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategoria().getNombreCategoria()));

        cargarMarcas();

    }

    @FXML
    private void agregarMarca (){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/marca/AgregarMarca.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            AgregarMarcaController controller = loader.getController();


            Stage stage = new Stage();
            stage.setTitle("Agregar Marca");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
            cargarMarcas();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void cargarMarcas() {
        List<Marca> marcas = marcaService.listarMarcas();
        mostrarMarcas(marcas);
    }

    private void mostrarMarcas(List<Marca> marcas) {
        contenedorMarcas.getChildren().clear();
        for (Marca p : marcas) {
            VBox card = crearCardMarca(p);
            contenedorMarcas.getChildren().add(card);
        }
    }

    private VBox crearCardMarca(Marca p) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;");
        card.setPrefSize(500, 100);

        // ========= TEXTO =========
        Label lblNombre = new Label(p.getNombreMarca());
        lblNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: white;");


        VBox textBox = new VBox(5, lblNombre);

        // ========= CONTENEDOR HORIZONTAL =========
        HBox contentBox = new HBox(10, textBox);

        card.getChildren().add(contentBox);

        // ========= ESTILOS INTERACTIVOS =========
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #1A1A1A; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;"));

        // Al hacer clic
        card.setOnMouseClicked(event -> {
            card.setStyle("-fx-background-color: #8EB83D; -fx-padding: 10; -fx-border-color: #8EB83D; -fx-border-radius: 10; -fx-background-radius: 10;");
            mostrarModelos(p);
        });

        return card;
    }

    private void mostrarModelos(Marca m){
        marcaSeleccionada = m;
        btnAgregarModelo.setDisable(false);
        EliminarMarca.setDisable(false);

        lblNombre.setText(m.getNombreMarca());
        llenarTabla(m);
    }

    private void llenarTabla(Marca m){
        int id = m.getMarcaId();
        TablaVehiculoMarca.getItems().clear();
        TablaVehiculoMarca.getItems().addAll(detalleCategoriaService.listarPorMarca(m));
    }

    public void EditarMarca(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlViews/marca/EditarMarca.fxml"));
            loader.setControllerFactory(ApplicationContextProvider.getApplicationContext()::getBean);
            Parent root = loader.load();

            EditarMarcaController controller = loader.getController();
            controller.setMarcaModelos(marcaSeleccionada);

            Stage stage = new Stage();
            stage.setTitle("Editar Marca");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
            cargarMarcas();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
