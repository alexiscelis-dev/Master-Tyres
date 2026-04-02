package com.mastertyres.nota.repository;

import com.mastertyres.nota.entity.Nota;
import com.mastertyres.nota.DTOs.NotaDTO;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Integer> {

    final String SELECT_NOTA_DTO = "SELECT NEW com.mastertyres.nota.model.NotaDTO(" +
            "n.notaId, n.numNota, n.numFactura, n.fechaYhora, n.fechaVencimiento, n.statusNota, n.createdAt, n.active, n.total, " +
            "i.inventarioId, " +
            "nd.observaciones, nd.observaciones2, nd.porcentajeGas, nd.rayones, nd.golpes, nd.tapones, nd.tapetes, nd.radio, nd.gato, nd.llave, nd.llanta, " +
            "nd.alineacion, nd.alineacionCantidad, nd.alineacionUnitario, nd.alineacionTotal, " +
            "nd.balanceo, nd.balanceoCantidad, nd.balanceoUnitario, nd.balanceoTotal, " +
            "nd.amorDelanteros, nd.amorDelCantidad, nd.amorDelUnitario, nd.amorDelTotal, " +
            "nd.amorTraseros, nd.amorTrasCantidad, nd.amorTrasUnitario, nd.amorTrasTotal, " +
            "nd.suspension, nd.suspensionCantidad, nd.suspensionUnitario, nd.suspensionTotal, " +
            "nd.suspension2, nd.suspensionCantidad2, nd.suspensionUnitario2, nd.suspensionTotal2, " +
            "nd.mecanica, nd.mecanicaCantidad, nd.mecanicaUnitario, nd.mecanicaTotal, " +
            "nd.mecanica2, nd.mecanicaCantidad2, nd.mecanicaUnitario2, nd.mecanicaTotal2, " +
            "nd.frenos, nd.frenosCantidad, nd.frenosUnitario, nd.frenosTotal, " +
            "nd.frenos2, nd.frenosCantidad2, nd.frenosUnitario2, nd.frenosTotal2, " +
            "nd.otros, nd.otrosCantidad, nd.otrosUnitario, nd.otrosTotal, " +
            "nd.otros2, nd.otrosCantidad2, nd.otrosUnitario2, nd.otrosTotal2, " +
            "nd.subTotalMecanica, nd.subTotalFrenos, nd.subTotalOtros, nd.llantaCampo, nd.llantaCantidad, nd.llantaUnitario, nd.llantaTotal, " +
            "n.adeudo, n.saldoFavor, " +
            "ndc.nombreClienteDetalleId, ndc.nombreClienteNota, ndc.direccion1Nota, ndc.direccion2Nota, ndc.rfcNota, ndc.correoNota, " +
            "ndc.marcaNota, ndc.modeloNota, ndc.categoriaNota,ndc.anioNota, ndc.kilometrosNota, ndc.placasNota,c.clienteId,v.vehiculoId) " +
            "FROM Nota n " +
            "JOIN n.detalles nd " +
            "JOIN n.notaClienteDetalles ndc " +
            "JOIN n.vehiculo v " +
            "JOIN v.cliente c " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.categoria ca " +
            "LEFT JOIN n.inventario i ";

    final String SELECT_NOTA = """
            SELECT DISTINCT n FROM Nota n
            LEFT JOIN FETCH n.detalles nd
            LEFT JOIN FETCH n.notaClienteDetalles ndc
            LEFT JOIN FETCH n.cliente c 
            LEFT JOIN FETCH n.vehiculo v
            LEFT JOIN FETCH v.marca m
            LEFT JOIN FETCH v.modelo mo
            LEFT JOIN FETCH v.categoria ca 
            LEFT JOIN FETCH n.inventario i
            """;
    final String UPDATE_NOTA = "UPDATE Nota n SET";
    final String SELECT_FROM_NOTA = "SELECT n FROM Nota n";


    @Query(SELECT_NOTA_DTO + " WHERE n.active = :active")
    List<NotaDTO> listarNotas(@Param("active") String active);


    @Query(SELECT_NOTA_DTO + " WHERE n.active = :active")
    Page<NotaDTO> listarNotasPaginado(@Param("active") String active, Pageable pageable);

    @Query(
            SELECT_NOTA_DTO + " " +
                    """
                                 WHERE n.active = :active
                                AND (
                                       LOWER(n.numNota) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR LOWER(n.numFactura) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR LOWER(n.fechaYhora) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR LOWER(n.fechaVencimiento) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR LOWER(n.statusNota) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR LOWER(c.apellido) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR LOWER(c.segundoApellido) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR LOWER(c.domicilio) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR LOWER(c.ciudad) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR LOWER(c.estado) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR LOWER(c.rfc) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR LOWER(m.nombreMarca) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR LOWER(mo.nombreModelo) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR LOWER(ca.nombreCategoria) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR LOWER(v.placas) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR LOWER(v.color) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                    OR STR(n.total) LIKE CONCAT('%', :filtro, '%')
                                    OR STR(n.adeudo) LIKE CONCAT('%', :filtro, '%')
                                    OR STR(n.saldoFavor) LIKE CONCAT('%', :filtro, '%')
                                    OR (i.inventarioId IS NOT NULL AND STR(i.inventarioId) LIKE CONCAT('%', :filtro, '%'))
                                    OR LOWER(nd.observaciones) LIKE LOWER(CONCAT('%', :filtro, '%'))
                                )
                            """)
    Page<NotaDTO> buscarNotas(
            @Param("active") String active,
            @Param("filtro") String filtro,
            Pageable pageable);

    @Query(SELECT_NOTA_DTO + " " + "WHERE n.active = :active AND n.numFactura LIKE %:numFactura%")
    Page<NotaDTO> buscarPorNumeroFactura(@Param("numFactura") String numFactura,
                                         @Param("active") String active,
                                         Pageable pageable);

    @Query(SELECT_NOTA_DTO + " " + "WHERE n.active = :active AND n.numNota LIKE %:numNota%")
    Page<NotaDTO> buscarPorNumeroNota(@Param("numNota") String numNota,
                                      @Param("active") String active,
                                      Pageable pageable);

    @Query(SELECT_NOTA_DTO + " " + "WHERE n.active = :active AND LOWER(ndc.nombreClienteNota) LIKE LOWER(CONCAT('%', :nombreCliente, '%')) ")
    Page<NotaDTO> buscarPorNombreCliente(@Param("nombreCliente") String nombreCliente,
                                         @Param("active") String active,
                                         Pageable pageable);


    @Query(SELECT_NOTA_DTO + " " + "WHERE n.active = :active AND DATE(n.fechaYhora) = :fecha")
    Page<NotaDTO> buscarPorFechaNota(@Param("fecha") LocalDate fecha,
                                     @Param("active") String active,
                                     Pageable pageable);

    @Query(SELECT_NOTA_DTO + " " + "WHERE n.active = :active AND n.fechaYhora BETWEEN :fecha AND :fecha2")
    Page<NotaDTO> buscarPorFechaNotaRango(@Param("fecha") String fecha,
                                          @Param("fecha2") String fecha2,
                                          @Param("active") String active,
                                          Pageable pageable);

    @Query(SELECT_NOTA_DTO + " " + "WHERE n.active = :active " +
            "AND (LOWER(ndc.marcaNota) LIKE LOWER(CONCAT('%', :vehiculo, '%')) " +
            "OR LOWER(ndc.modeloNota) LIKE LOWER(CONCAT('%', :vehiculo, '%')))")
    Page<NotaDTO> buscarPorVehiculo(@Param("vehiculo") String vehiculo, @Param("active") String active, Pageable pageable);


    @Query(SELECT_NOTA_DTO + " " + "WHERE n.active = :active AND n.fechaVencimiento = :fechaVencimiento")
    Page<NotaDTO> buscarPorFechaVencimiento(@Param("fechaVencimiento") String fechaVencimiento,
                                            @Param("active") String active,
                                            Pageable pageable);

    @Query(SELECT_NOTA_DTO + " " + "WHERE n.active = :active AND n.fechaVencimiento BETWEEN :fechaVencimiento AND :fechaVencimiento2")
    Page<NotaDTO> buscarPorFechaVencimientoRango(@Param("fechaVencimiento") String fechaVencimiento,
                                                 @Param("fechaVencimiento2") String fechaVencimiento2,
                                                 @Param("active") String active, Pageable pageable);

    @Query(SELECT_NOTA_DTO + " " + "WHERE n.active = :active " +
            "AND (LOWER(ndc.direccion1Nota) LIKE LOWER(CONCAT('%', :direccion, '%')) " +
            "OR LOWER(ndc.direccion2Nota) LIKE LOWER(CONCAT('%', :direccion, '%')) " +
            "OR LOWER(c.domicilio) LIKE LOWER(CONCAT('%', :direccion, '%')) " +
            "OR LOWER(c.ciudad) LIKE LOWER(CONCAT('%', :direccion, '%')) " +
            "OR LOWER(c.estado) LIKE LOWER(CONCAT('%', :direccion, '%')))")
    Page<NotaDTO> buscarPorDireccion(@Param("direccion") String direccion,
                                     @Param("active") String active,
                                     Pageable pageable);

    @Query(SELECT_NOTA_DTO + " " + "WHERE n.active = :active AND LOWER(ndc.placasNota) LIKE LOWER(CONCAT('%', :placas, '%'))")
    Page<NotaDTO> buscarPorPlacas(@Param("placas") String placas,
                                  @Param("active") String active,
                                  Pageable pageable);

    @Query(SELECT_NOTA_DTO + " " + "WHERE n.active = :active AND LOWER(ndc.rfcNota) LIKE LOWER(CONCAT('%', :rfc, '%'))")
    Page<NotaDTO> buscarPorRfc(@Param("rfc") String rfc,
                               @Param("active") String active,
                               Pageable pageable);

    @Query(SELECT_NOTA_DTO + " " + "WHERE n.active = :active  AND n.adeudo > 0 ORDER BY n.createdAt DESC")
    Page<NotaDTO> buscarPorAdeudo(@Param("active") String active, Pageable pageable);

    @Query(SELECT_NOTA_DTO + " " + "WHERE n.active = :active AND n.total BETWEEN :total1 AND :total2 ORDER BY n.createdAt DESC")
    Page<NotaDTO> buscarPorTotal(@Param("total1") Double total1,@Param("total2")Double total2, @Param("active") String active, Pageable pageable);

    @Query(SELECT_NOTA_DTO + " " + "WHERE n.active = :active AND n.saldoFavor > 0 ORDER BY n.createdAt DESC")
    Page<NotaDTO> buscarPorSaldoFavor(@Param("active") String active, Pageable pageable);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_NOTA + " " + "WHERE n.active = 'ACTIVE'")
    List<Nota> findNotasActivasConDetalles();

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_NOTA + " " + "WHERE n.active = 'ACTIVE' ")
    Page<Nota> findNotasActivasConDetallesPaginado(Pageable pageable);


    @Query(SELECT_NOTA_DTO + " " + "WHERE n.active = :active AND n.numNota = :numNota")
    NotaDTO buscarPorNumNota(@Param("active") String active, @Param("numNota") String numNota);

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query(SELECT_FROM_NOTA + " " + "WHERE numNota = :numNota")
    Nota findByNumNota(@Param("numNota") String numNota);

    @Modifying
    @Query(UPDATE_NOTA + " " + "n.adeudo = :adeudo, n.fechaVencimiento = :fechaVencimiento WHERE n.notaId = :notaId")
    void actualizarAdeudo(@Param("adeudo") float adeudo,
                          @Param("fechaVencimiento") String fechaVencimiento,
                          @Param("notaId") Integer notaId);

    @Modifying
    @Query(UPDATE_NOTA + " " + "n.saldoFavor = :saldo WHERE n.notaId = :notaId")
    void actualizarSaldo(@Param("saldo") float saldo, @Param("notaId") Integer notaId);

    @Modifying
    @Query(UPDATE_NOTA + " " + "n.updatedAt = :updatedAt WHERE n.notaId = :notaId")
    void actualizarUpdatedAtNota(@Param("notaId") Integer notaId, @Param("updatedAt") String updatedAt);

    @Modifying
    @Query(UPDATE_NOTA + " " + "n.numFactura = :numFactura WHERE n.notaId = :notaId ")
    void actualizarNumFactura(@Param("numFactura") String numFactura, @Param("notaId") Integer notaId);

    @Modifying
    @Query(UPDATE_NOTA + " " + "n.statusNota = :status WHERE n.notaId = :notaId ")
    void actualizarStatus(@Param("status") String status, @Param("notaId") Integer notaId);

    @Modifying
    @Query(UPDATE_NOTA + " " + "n.active = :active WHERE n.notaId = :notaId")
    void eliminarNota(@Param("active") String active, @Param("notaId") Integer notaId);

    @Modifying
    @Query(UPDATE_NOTA + " " + "n.fechaVencimiento = :fechaVencimiento WHERE  n.notaId = :notaId AND n.active = :active")
    void actualizarFechaVencimiento(@Param("fechaVencimiento") String fechaVencimiento, @Param("notaId") Integer notaId, @Param("active") String active);


    //consultas de historial

    @Query(SELECT_NOTA_DTO + " " + "WHERE n.active = :active " +
            " AND LOWER(ndc.nombreClienteNota) LIKE LOWER(CONCAT('%', :nombreCliente, '%')) ORDER BY n.createdAt DESC")
    List<NotaDTO> buscarHistorial(@Param("active") String active, @Param("nombreCliente") String nombreCliente, Pageable pageable);


}//clase
