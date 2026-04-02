package com.mastertyres.inventario.service;

import com.mastertyres.inventario.entity.Inventario;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IInventarioService {

    List<Inventario>listarInventario(String active);

    List<Inventario>buscadorInventario(String active, String busqueda);

    List<Inventario>buscarPorCodBarras(String active, String busqueda);

    List<Inventario>buscarPorDot(String active, String busqueda);

    List<Inventario>buscarPorMarca(String active, String busqueda);

    List<Inventario>buscarPorModelo(String active, String busqueda);

    List<Inventario>buscarPorMedida(String active, String busqueda);

    List<Inventario>buscarPorFecha(String active, LocalDate fecha);

    List<Inventario>buscarPorFecha(String active, LocalDate fechaInicio, LocalDate fechaFin);

    int eliminarInventario(String inactive, Integer idInventario);

    void guardarInventario(Inventario inventario);

    void actualizarInventario(Inventario inventario);

    void actualizarUptatedAt(String now, Integer idInventario);

    Optional<Inventario> findByCodigoBarras(String codBarras);

    Optional<Inventario>findByDot(String dot);


    void actualizarCreatedAt(String now, Integer id);

    Inventario buscarLlantaPorId(Integer idLlanta);

    void actualizarStock(Integer inventarioId, Integer stock, String active);

    List<Inventario>  first100Inventario(String active);

    long contarInventariosActivos(String active);

    long contarInventarioPorBusquedaGeneral(String status, String termino);

    Page<Inventario> listarInventarioPaginado(String active, int pagina, int tamanoPagina);

    Page<Inventario> buscadorInventarioPaginado(String active, String busqueda, int pagina, int tamanoPagina);

    Page<Inventario> buscarPorCodBarrasPaginado(String active, String busqueda, int pagina, int tamanoPagina);

    Page<Inventario> buscarPorDotPaginado(String active, String busqueda, int pagina, int tamanoPagina);

    Page<Inventario> buscarPorMarcaPaginado(String active, String busqueda, int pagina, int tamanoPagina);

    Page<Inventario> buscarPorModeloPaginado(String active, String busqueda, int pagina, int tamanoPagina);

    Page<Inventario> buscarPorMedidaPaginado(String active, String busqueda, int pagina, int tamanoPagina);

    Page<Inventario> buscarPorFechaPaginado(String active, LocalDate fecha, int pagina, int tamanoPagina);

    Page<Inventario> buscarPorFechaPaginadoRangos(String active,
                                                  LocalDate fechaInicio,
                                                  LocalDate fechaFin,
                                                  int pagina,
                                                  int tamanoPagina);







}//interface