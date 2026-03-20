package com.mastertyres.promociones.service;

import com.mastertyres.cliente.model.Cliente;
import com.mastertyres.promociones.model.Promocion;
import com.mastertyres.vehiculoPromocion.model.VehiculoPromocion;

import java.util.List;


public interface IPromocionService{

    void guardarPromocion(Promocion promocion);

    Promocion buscarPromocionId(Integer id);

    List<Promocion> obtenerPromocionesActivas();

    List<Promocion> obtenerPromocionesPendientes();

    List<Promocion> buscarPromociones(String texto);

    void desactivarPromocion(Integer id);

    void crearPromocionConVehiculos(Promocion promocion, List<VehiculoPromocion> vehiculos);

    void crearPromocionConClientes(Promocion promocion, List<Integer> clientesIds);

    void actualizarPromocionConVehiculos(Promocion promocion, List<VehiculoPromocion> nuevosVehiculos);

    void actualizarPromocionConClientes(Promocion promocion, List<Cliente> nuevosClientes);

    void actualizarPromocionesVencidas();




}//IPromocionService