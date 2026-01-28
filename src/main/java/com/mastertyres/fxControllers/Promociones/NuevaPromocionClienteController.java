package com.mastertyres.fxControllers.Promociones;


import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.model.StatusCliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.utils.FechaUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;

@Component
public class NuevaPromocionClienteController {

    @FXML private AnchorPane rootPane;
    @FXML private Slider porcentajeDescuento;
    @FXML private Label descuentoLabel;
    @FXML private TextField precioSinDescuento;
    @FXML private DatePicker fechaInicio;
    @FXML private DatePicker fechaFin;
    @FXML private Button btnRegistrar;
    @FXML private Button btnRegresar;
    @FXML private ChoiceBox<String> tipoDescuento;
    @FXML private TextField nombrePromocion;
    @FXML private TextField descripcion;
    @FXML private Button btnImagen;
    @FXML private TextField textFieldImg;

    @FXML private TextField txtBuscador;
    @FXML private TableView Tabla_Clientes;
    @FXML private ListView ClientesAgregados;
    @FXML private TableColumn<Cliente, String> colSeleccionCliente;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colRFC;
    @FXML private TableColumn<Cliente, String> colCumpleanos;
    @FXML private TableColumn<Cliente, String> colFechaRegistro;
    @FXML private TableColumn<Cliente, String> colNombreEmpresa;
    @FXML private TableColumn<Cliente, String> colTipoCliente;
    @FXML private TableColumn<Cliente, String> colHobbie;
    @FXML private Button btnBuscar;

    private static final int CLIENTES_POR_PAGINA = 20;
    @FXML private Pagination PaginadorClientes;
    private String terminoBusquedaActual = "";
    private boolean modoBusqueda = false;


    @Autowired
    private ClienteService clienteService;

    @FXML
    private void initialize (){
        ConfigurarTabla();
    }

    private void ConfigurarTabla(){
        colTipoCliente.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getTipoCliente()))
        );

        colNombreEmpresa.setCellValueFactory(data->
                new SimpleStringProperty(valorONull(data.getValue().getNombreEmpresa()))
        );

        colNombre.setCellValueFactory(data ->
                new SimpleStringProperty(nombreCompleto(data.getValue()))
        );

        colHobbie.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getHobbie()))
        );

        colRFC.setCellValueFactory(data ->
                new SimpleStringProperty(valorONull(data.getValue().getRfc()))
        );

        colCumpleanos.setCellValueFactory(data ->
                new SimpleStringProperty(FechaUtils.formatearFecha(data.getValue().getFechaCumple()))
        );

        colFechaRegistro.setCellValueFactory(data ->
                new SimpleStringProperty(FechaUtils.formatearFechaHora(data.getValue().getCreated_at()))
        );

        cargarCliente();
    }

    private VBox ConfigurarPaginador (int indicePagina){

        Page<Cliente> paginaClientes;


        paginaClientes = clienteService.listarClientesConVehiculosPaginado(
                StatusCliente.ACTIVE.toString(),
                indicePagina,
                CLIENTES_POR_PAGINA
        );


        Tabla_Clientes.setItems(FXCollections.observableArrayList(paginaClientes.getContent()));

        VBox contenedor = new VBox(Tabla_Clientes);
        contenedor.setMinHeight(500);
        contenedor.setPrefHeight(500);
        contenedor.setStyle("-fx-background-color: transparent;");
        return contenedor;

    }

    private void Buscador (String busqueda){
        terminoBusquedaActual = busqueda;
        modoBusqueda = !busqueda.trim().isEmpty();

        if (modoBusqueda) {
            //  total de resultados para el buscador general
            long totalResultados = clienteService.contarClientesPorBusquedaGeneral(
                    StatusCliente.ACTIVE.toString(),
                    terminoBusquedaActual
            );

            int totalPaginas = (int) Math.ceil((double) totalResultados / CLIENTES_POR_PAGINA);

            PaginadorClientes.setPageCount(Math.max(totalPaginas, 1));
        } else {
            // Si no hay búsqueda, restaurar paginación normal
            long totalClientes = clienteService.contarClientesActivos(StatusCliente.ACTIVE.toString());
            int totalPaginas = (int) Math.ceil((double) totalClientes / CLIENTES_POR_PAGINA);
            PaginadorClientes.setPageCount(Math.max(totalPaginas, 1));
        }

        //  Asignar PageFactory
        PaginadorClientes.setPageFactory(this::ConfigurarPaginador);

        //  Reiniciar a la primera página
        PaginadorClientes.setCurrentPageIndex(0);
    }

    private void cargarCliente (){

        try {
            long totalClientes = clienteService.contarClientesActivos(StatusCliente.ACTIVE.toString());
            int totalPaginas = (int) Math.ceil((double) totalClientes / CLIENTES_POR_PAGINA);
            PaginadorClientes.setPageCount(Math.max(totalPaginas, 1));
            PaginadorClientes.setPageFactory(this::ConfigurarPaginador);

            // Muestra la primera página
            PaginadorClientes.setCurrentPageIndex(0);

        } catch (Exception e) {
            mostrarError("Error al mostrar datos", "", "No se pudieron cargar los datos. Por favor, inténtalo de nuevo más tarde.");
        }

    }

    private String valorONull(String valor) {
        return (valor == null || valor.isBlank()) ? "N/A" : valor;
    }

    private String nombreCompleto(Cliente c) {
        String n = (c.getNombre() == null ? "" : c.getNombre().trim());
        String a1 = (c.getApellido() == null ? "" : c.getApellido().trim());
        String a2 = (c.getSegundoApellido() == null ? "" : c.getSegundoApellido().trim());

        String full = String.join(" ", n, a1, a2).trim();

        return full.isBlank() ? "N/A" : full;
    }

}
