package com.mastertyres.fxControllers.ClientesPromocionesController;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.service.ClienteService;
import com.mastertyres.promociones.model.Promocion;
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

import java.util.List;

@Component
public class ClientesPromocionesController {

    @Autowired
    private ClienteService clienteService;

    @FXML private TableView<ClienteSeleccion> tablaClientes;
    @FXML private TableColumn<ClienteSeleccion, String> colNombre;
    @FXML private TableColumn<ClienteSeleccion, Boolean> colCheckBox;
    @FXML private TableColumn<ClienteSeleccion, String> colFechaCumpleanios;
    @FXML private TableColumn<ClienteSeleccion, String> colTelefono;

    private ObservableList<ClienteSeleccion> data = FXCollections.observableArrayList();
    private  Promocion p ;

    @FXML
    public void initialize() {

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
    }

    public void LlenarTabla(Promocion promocionSeleccionada) {
        this.p = promocionSeleccionada;
        List<Cliente> clientes = clienteService.obtenerClientesAplicables(promocionSeleccionada.getPromocionId());
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
        List<Cliente> seleccionados = data.stream()
                .filter(ClienteSeleccion::isSeleccionado)
                .map(ClienteSeleccion::getCliente)
                .toList();

        for (Cliente cliente : seleccionados) {
            String telefono = cliente.getNumTelefono(); // debe incluir lada internacional
            String mensaje = "Master tyres. \n\n" + "¡Hola " + cliente.getNombre() + "! Tenemos una promoción especial para ti 🚗🔥\n\n" + p.getNombre() + "\n\n" + p.getDescripcion() + "\n\n" + "Tienes hasta el: " + p.getFechaFin() + " para reclamar tu promocion";

            String url = "https://api.whatsapp.com/send?phone=" + telefono + "&text=" +
                    java.net.URLEncoder.encode(mensaje, java.nio.charset.StandardCharsets.UTF_8);

            if (hostServices != null) {
                hostServices.showDocument(url); // 👉 abre el navegador por defecto
            }
        }
    }

//    @FXML
//    public void Enviar() {
//        List<Cliente> seleccionados = data.stream()
//                .filter(ClienteSeleccion::isSeleccionado)
//                .map(ClienteSeleccion::getCliente)
//                .toList();
//
//        for (Cliente cliente : seleccionados) {
//            String telefono = "5214432853618"; // en formato internacional, ej: 5214432853618
//            String nombre = "jayro";
//            String titulo = "¡Promoción especial!"; // reemplaza con el título de tu promoción
//            String cumple =  " a" ;
//            String desc = "Tu vehículo aplica para esta promoción 🚗"; // o la descripción de la promoción
//
//            enviarMensajeWhatsAppAPI(telefono, nombre, titulo, cumple, desc);
//        }
//    }
//
//    private void enviarMensajeWhatsAppAPI(String telefono, String nombre, String titulo, String cumple, String desc) {
//        try {
//            String url = "https://graph.facebook.com/v22.0/763011946903635/messages";
//
//            String jsonBody = """
//                {
//                  "messaging_product": "whatsapp",
//                  "to": "%s",
//                  "type": "template",
//                  "template": {
//                    "name": "promocion_oferta",
//                    "language": { "code": "en" },
//                    "components": [
//                      {
//                        "type": "body",
//                        "parameters": [
//                          { "type": "text", "nombre": "%s" },
//                          { "type": "text", "titulo": "%s" },
//                          { "type": "text", "cumple": "%s" },
//                          { "type": "text", "desc": "%s" }
//                        ]
//                      }
//                    ]
//                  }
//                }
//            """.formatted(telefono, nombre, titulo, cumple, desc);
//
//
//            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
//            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
//                    .uri(java.net.URI.create(url))
//                    .header("Authorization", "Bearer EAAKtH1TyxN8BPbHm73FFpduekPttJjqYon1AZCJKgWtmZClYnlAJuXCGTaihBsiacHAjx7lEldRy7fdOFf4ZArit8axZAlTpKgPyhl0MGx2u07oI21KwhvmiJF8ZAOjBfhjQBH4574BSZAxqWRboCH3FlbLrrJZC7EyCgWYfi3kb9TVqw5cASYZBVYEG0EIKsWg4s47vNYqKLJxYTYacou8zZAzRZC0E0RiN3viPU6rWfuFbZBA0QZDZD")
//                    .header("Content-Type", "application/json")
//                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonBody))
//                    .build();
//
//            java.net.http.HttpResponse<String> response =
//                    client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
//
//            System.out.println("Respuesta WhatsApp: " + response.statusCode() + " -> " + response.body());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//



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
}
