package com.mastertyres.fxControllers.Promociones;

import com.mastertyres.ClientesPromocion.service.ClientePromocionService;
import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.common.interfaces.IFxController;
import com.mastertyres.common.interfaces.ILoading;
import com.mastertyres.common.service.TaskService;
import com.mastertyres.fxComponents.LoadingComponentController;
import com.mastertyres.promociones.model.Promocion;
import com.mastertyres.promociones.model.TipoPromocion;
import javafx.application.HostServices;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;
import static com.mastertyres.common.utils.MensajesAlert.mostrarInformacion;

@Component
public class ClientesAplicablesController implements IFxController, ILoading {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ClientePromocionService clientePromocionService;
    @Autowired
    private TaskService taskService;

    @FXML
    private TableView<ClienteSeleccion> tablaClientes;
    @FXML
    private TableColumn<ClienteSeleccion, String> colNombre;
    @FXML
    private TableColumn<ClienteSeleccion, Boolean> colCheckBox;
    @FXML
    private TableColumn<ClienteSeleccion, String> colFechaCumpleanios;
    @FXML
    private TableColumn<ClienteSeleccion, String> colTelefono;

    private ObservableList<ClienteSeleccion> data = FXCollections.observableArrayList();
    private Promocion promocion;
    private LoadingComponentController loadingOverlayController;

    @FXML
    public void initialize() {

        configuraciones();
        listeners();

    }//initialize


    @Override
    public void configuraciones() {

        tablaClientes.setEditable(true);


        colNombre.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getCliente().getNombre() + " " +
                                (cellData.getValue().getCliente().getApellido() == null ? "" : cellData.getValue().getCliente().getApellido()) + " " +
                                (cellData.getValue().getCliente().getSegundoApellido() == null ? "" : cellData.getValue().getCliente().getSegundoApellido())
                )
        );
        colFechaCumpleanios.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCliente().getFechaCumple() == null ? "" : cellData.getValue().getCliente().getFechaCumple())
        );
        colTelefono.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCliente().getNumTelefono() == null ? "" : cellData.getValue().getCliente().getNumTelefono())
        );


        colCheckBox.setEditable(true);
        colCheckBox.setCellValueFactory(cellData -> cellData.getValue().seleccionadoProperty());
        colCheckBox.setCellFactory(CheckBoxTableCell.forTableColumn(colCheckBox));


        tablaClientes.setItems(data);

    }//configuraciones

    @Override
    public void listeners() {

    }

    @Override
    public void setInitializeLoading(LoadingComponentController loading) {
        this.loadingOverlayController = loading;
    }

//    public void LlenarTabla(Promocion promocionSeleccionada) {
//        this.p = promocionSeleccionada;
//        List<Cliente> clientes = clienteService.obtenerClientesAplicables(promocionSeleccionada.getPromocionId());
//        data.clear();
//        for (Cliente c : clientes) {
//            data.add(new ClienteSeleccion(c));
//        }
//        tablaClientes.setItems(data);
//    }

    public void LlenarTabla(Promocion promocionSeleccionada) {
        this.promocion = promocionSeleccionada;

        List<Cliente> clientes;

        if (TipoPromocion.VEHICULO.toString().equals(promocionSeleccionada.getTipoPromocion())) {
            clientes = clienteService.obtenerClientesAplicables(promocionSeleccionada.getPromocionId());
        } else if (TipoPromocion.CLIENTE.toString().equals(promocionSeleccionada.getTipoPromocion())) {
            clientes = clientePromocionService.obtenerClientesAplicables(promocionSeleccionada.getPromocionId());
        } else {
            clientes = new ArrayList<>();
        }

        data.clear();
        for (Cliente c : clientes) {
            data.add(new ClienteSeleccion(c));
        }
        tablaClientes.setItems(data);
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) tablaClientes.getScene().getWindow();
        stage.close();
    }

    private HostServices hostServices;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    public void Enviar() {

        taskService.runTask(
                loadingOverlayController,
                () -> {

                    List<Cliente> seleccionados = data.stream()
                            .filter(ClienteSeleccion::isSeleccionado)
                            .map(ClienteSeleccion::getCliente)
                            .toList();

                    ArrayList<String> whatsAppUrls = new ArrayList<>();
                    String telefono = "", mensaje = "", url = "";


                    for (Cliente cliente : seleccionados) {
                        telefono = cliente.getNumTelefono(); // debe incluir lada internacional

                        mensaje = "Master tires. \n\n" + "¡Hola " + cliente.getNombre() + "! Tenemos una promoción especial para ti 🚗🔥\n\n" +
                                promocion.getNombre() + "\n\n" + promocion.getDescripcion() + "\n\n" + "Tienes hasta el: " +
                                promocion.getFechaFin() + " para reclamar tu promocion";

                        url = "https://api.whatsapp.com/send?phone=" + telefono + "&text=" +
                                java.net.URLEncoder.encode(mensaje, java.nio.charset.StandardCharsets.UTF_8);

                        whatsAppUrls.add(url);
                    }
                    return whatsAppUrls;

                }, (whatsAppUrls) -> {


                    if ( whatsAppUrls == null || whatsAppUrls.isEmpty()){
                        return;
                    }

                    for(String urlNavegador: whatsAppUrls){
                        if(hostServices != null){
                            hostServices.showDocument(urlNavegador);
                        }
                    }

                    mostrarInformacion(
                            "¡Chats listos!",
                            "Se han abierto las ventanas de WhatsApp en su navegador.",
                            "Por favor, revise las pestañas abiertas y presione el botón de enviar en cada chat para finalizar."
                    );

                }, (ex) ->{
                    ex.printStackTrace();
                    mostrarError("Error al enviar","","No se pudieron generar los enlaces de WhatsApp.");

                },null


        );

    }//envar


    @FXML
    public void SeleccionarTodos() {
        data.forEach(cs -> cs.setSeleccionado(true));

    }

    @FXML
    public void DeseleccionarTodos() {
        data.forEach(cs -> cs.setSeleccionado(false));

    }


    public static class ClienteSeleccion {
        private final Cliente cliente;
        private final BooleanProperty seleccionado = new SimpleBooleanProperty(false);

        public ClienteSeleccion(Cliente cliente) {
            this.cliente = cliente;
        }

        public Cliente getCliente() {
            return cliente;
        }

        public BooleanProperty seleccionadoProperty() {
            return seleccionado;
        }

        public boolean isSeleccionado() {
            return seleccionado.get();
        }

        public void setSeleccionado(boolean seleccionado) {
            this.seleccionado.set(seleccionado);
        }
    }
}//class
