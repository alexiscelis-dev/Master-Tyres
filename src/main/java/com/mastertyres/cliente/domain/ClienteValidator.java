package com.mastertyres.cliente.domain;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.cliente.repository.ClienteRepository;
import com.mastertyres.common.exeptions.ClienteException;
import com.mastertyres.vehiculo.model.Vehiculo;
import com.mastertyres.vehiculo.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClienteValidator {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private VehiculoRepository vehiculoRepository;

    public void validarGuardar(Cliente cliente, boolean registrar) {


        String excedio = "Excedio el limite de caracteres permitidos para el campo";


        if (cliente == null) {
            throw new ClienteException("Error interno: No se pudo recuperar la informacion del cliente");
        }

        //Si recibe true significa que va a registrar por lo que hace verificaciones del vehiculo y el rfc

        if (registrar){

            if (existeRFC(cliente.getRfc())) {
                throw new ClienteException("RFC duplicado: Ya existe un cliente registrado con este RFC.");
            }

            if (existeVehiculoPorPlacas(cliente.getVehiculos())){
                throw new ClienteException("Vehiculo duplicado: Ya existe un vehiculo registrado con las mismas placas. " );
            }

            if (existeVehiculoPorNumSerie(cliente.getVehiculos())){
                throw new ClienteException("Vehiculo duplicado: Ya existe un vehiculo registrado con el mismo numero de serie.");
            }

        }


        if (cliente.getNombre() == null || cliente.getNombre().isEmpty()) {
            throw new ClienteException("El campo 'Nombre' es un campo obligatorio");
        }

        if (cliente.getTipoCliente() == null || cliente.getTipoCliente().isEmpty()){
            throw new ClienteException("Debe seleccionar un tipo de cliente");
        }


        if (cliente.getNombre().length() > 50) {
            throw new ClienteException(excedio + " 'Nombre' ");
        }

        if (cliente.getApellido().length() > 30) {
            throw new ClienteException(excedio + " 'Apellido' ");
        }

        if (cliente.getSegundoApellido().length() > 30) {
            throw new ClienteException(excedio + " 'Segundo");
        }

        if (cliente.getHobbie().length() > 30) {
            throw new ClienteException(excedio + " 'Hobbie' ");
        }

        if (cliente.getRfc().length() > 13) {
            throw new ClienteException(excedio + " 'RFC' ");
        }


        if (cliente.getNumTelefono().length() > 13) {
            throw new ClienteException(excedio + " 'Numero de telefono' ");
        }

        if (cliente.getEstado().length() > 30) {
            throw new ClienteException(excedio + " 'Estado' ");
        }

        if (cliente.getCiudad().length() > 60) {
            throw new ClienteException(excedio + " 'Ciudad' ");
        }

        if (cliente.getDomicilio().length() > 200) {
            throw new ClienteException(excedio + " 'Domicilio' ");
        }

        if (cliente.getGenero().length() > 1) {
            throw new ClienteException("Error interno: No se pudo recuperar el genero del cliente");
        }

        if (cliente.getCorreo().length() > 320) {
            throw new ClienteException(excedio + " 'Correo' ");
        }

        if (cliente.getNombreEmpresa().length() > 100) {
            throw new ClienteException(excedio + " 'Nombre de la empresa' ");
        }


    }//validarGuardar

    private boolean existeRFC(String rfc) {
        boolean existe = false;

        if (rfc != null && !rfc.isBlank()) {

            existe = clienteRepository.existeClientePorRFC(rfc);

            if (existe)
                existe = true;
            else
                existe = false;

        }

        return existe;
    }

    private boolean existeVehiculoPorPlacas(List<Vehiculo> listaVehiculos) {
        boolean existe = false;

        // Validar vehículos duplicados contra la base
        for (Vehiculo v : listaVehiculos) {
            String placas = v.getPlacas();

            if (placas != null && !placas.isBlank()) {
                if (vehiculoRepository.existeVehiculoPorPlacas(placas)) {

                    existe = true;
                }
            }

        }
        return existe;

    }

    private boolean existeVehiculoPorNumSerie(List<Vehiculo> listaVehiculos) {
        boolean existe = false;

        // Validar vehículos duplicados contra la base
        for (Vehiculo v : listaVehiculos) {
            String numSerie = v.getNumSerie();


            if (numSerie != null && !numSerie.isBlank()) {
                if (vehiculoRepository.existeVehiculoPorNumeroSerie(numSerie)) {

                    existe = true;
                }
            }
        }
        return existe;
    }



}//class