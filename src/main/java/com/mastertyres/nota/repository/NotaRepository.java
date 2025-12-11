package com.mastertyres.nota.repository;

import com.mastertyres.nota.model.Nota;
import com.mastertyres.nota.model.NotaDTO;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Integer> {

    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT NEW com.mastertyres.nota.model.NotaDTO( " +
            "n.notaId, n.numNota, n.numFactura, n.fechaYhora, n.fechaVencimiento, n.statusNota, n.createdAt, n.active, n.total, " +
            "i.inventarioId," +
            "nd.observaciones, nd.observaciones2, nd.porcentajeGas, nd.rayones, nd.golpes, nd.tapones, nd.tapetes, nd.radio, nd.gato, " +
            "nd.llave, nd.llanta, nd.alineacion, nd.alineacionCantidad, nd.alineacionUnitario, nd.alineacionTotal, " +
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
            "nd.subTotalMecanica, nd.subTotalFrenos, nd.subTotalOtros, nd.llantaCampo, " +
            "nd.llantaCantidad, nd.llantaUnitario, nd.llantaTotal, n.adeudo, n.saldoFavor, " +
            "c.clienteId, c.nombre, c.apellido, c.segundoApellido, c.domicilio, c.rfc, c.correo, c.genero,c.tipoCliente ," +
            "v.vehiculoId, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.kilometros, v.color, v.placas) " +
            "FROM Nota n " +
            "JOIN n.detalles nd " +
            "JOIN n.vehiculo v " +
            "JOIN v.cliente c " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.categoria ca " +
            "JOIN n.inventario i " +
            "WHERE n.active = :active")
    List<NotaDTO> listarNotas(@Param("active") String active);

    @Query("SELECT NEW com.mastertyres.nota.model.NotaDTO( " +
            "n.notaId, n.numNota, n.numFactura, n.fechaYhora, n.fechaVencimiento, n.statusNota, n.createdAt, n.active, n.total, " +
            "i.inventarioId," +
            "nd.observaciones, nd.observaciones2, nd.porcentajeGas, nd.rayones, nd.golpes, nd.tapones, nd.tapetes, nd.radio, nd.gato, " +
            "nd.llave, nd.llanta, nd.alineacion, nd.alineacionCantidad, nd.alineacionUnitario, nd.alineacionTotal, " +
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
            "nd.subTotalMecanica, nd.subTotalFrenos, nd.subTotalOtros, nd.llantaCampo, " +
            "nd.llantaCantidad, nd.llantaUnitario, nd.llantaTotal, n.adeudo, n.saldoFavor, " +
            "c.clienteId, c.nombre, c.apellido, c.segundoApellido, c.domicilio, c.rfc, c.correo,c.genero,c.tipoCliente , " +
            "v.vehiculoId, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.kilometros, v.color, v.placas) " +
            "FROM Nota n " +
            "JOIN n.detalles nd " +
            "JOIN n.vehiculo v " +
            "JOIN v.cliente c " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.categoria ca " +
            "LEFT JOIN n.inventario i " +
            "WHERE n.active = :active")
    Page<NotaDTO> listarNotasPaginado(@Param("active") String active, Pageable pageable);

    @Query("""
SELECT NEW com.mastertyres.nota.model.NotaDTO(
    n.notaId, n.numNota, n.numFactura, n.fechaYhora, n.fechaVencimiento, n.statusNota,
    n.createdAt, n.active, n.total,
    i.inventarioId,
    nd.observaciones, nd.observaciones2, nd.porcentajeGas, nd.rayones, nd.golpes, 
    nd.tapones, nd.tapetes, nd.radio, nd.gato, nd.llave, nd.llanta,
    nd.alineacion, nd.alineacionCantidad, nd.alineacionUnitario, nd.alineacionTotal,
    nd.balanceo, nd.balanceoCantidad, nd.balanceoUnitario, nd.balanceoTotal,
    nd.amorDelanteros, nd.amorDelCantidad, nd.amorDelUnitario, nd.amorDelTotal,
    nd.amorTraseros, nd.amorTrasCantidad, nd.amorTrasUnitario, nd.amorTrasTotal,
    nd.suspension, nd.suspensionCantidad, nd.suspensionUnitario, nd.suspensionTotal,
    nd.suspension2, nd.suspensionCantidad2, nd.suspensionUnitario2, nd.suspensionTotal2,
    nd.mecanica, nd.mecanicaCantidad, nd.mecanicaUnitario, nd.mecanicaTotal,
    nd.mecanica2, nd.mecanicaCantidad2, nd.mecanicaUnitario2, nd.mecanicaTotal2,
    nd.frenos, nd.frenosCantidad, nd.frenosUnitario, nd.frenosTotal,
    nd.frenos2, nd.frenosCantidad2, nd.frenosUnitario2, nd.frenosTotal2,
    nd.otros, nd.otrosCantidad, nd.otrosUnitario, nd.otrosTotal,
    nd.otros2, nd.otrosCantidad2, nd.otrosUnitario2, nd.otrosTotal2,
    nd.subTotalMecanica, nd.subTotalFrenos, nd.subTotalOtros, nd.llantaCampo,
    nd.llantaCantidad, nd.llantaUnitario, nd.llantaTotal,
    n.adeudo, n.saldoFavor,
    c.clienteId, c.nombre, c.apellido, c.segundoApellido, 
    c.domicilio, c.rfc, c.correo,  c.genero,c.tipoCliente ,
    v.vehiculoId, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria,
    v.anio, v.kilometros, v.color, v.placas
)
FROM Nota n
JOIN n.detalles nd
JOIN n.vehiculo v
JOIN v.cliente c
JOIN v.marca m
JOIN v.modelo mo
JOIN v.categoria ca
LEFT JOIN n.inventario i
WHERE n.active = :active
AND (
       LOWER(n.numNota) LIKE LOWER(CONCAT('%', :filtro, '%'))
    OR LOWER(n.numFactura) LIKE LOWER(CONCAT('%', :filtro, '%'))
    OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :filtro, '%'))
    OR LOWER(c.apellido) LIKE LOWER(CONCAT('%', :filtro, '%'))
    OR LOWER(c.segundoApellido) LIKE LOWER(CONCAT('%', :filtro, '%'))
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


    @Query("""
        SELECT DISTINCT n FROM Nota n
        LEFT JOIN FETCH n.detalles nd
        LEFT JOIN FETCH n.cliente c
        LEFT JOIN FETCH n.vehiculo v
        LEFT JOIN FETCH v.marca m
        LEFT JOIN FETCH v.modelo mo
        LEFT JOIN FETCH v.categoria ca
        LEFT JOIN FETCH n.inventario i
        WHERE n.active = 'ACTIVE'
        """)
    List<Nota> findNotasActivasConDetalles();

    @Query("""
        SELECT DISTINCT n FROM Nota n
        LEFT JOIN FETCH n.detalles nd
        LEFT JOIN FETCH n.cliente c
        LEFT JOIN FETCH n.vehiculo v
        LEFT JOIN FETCH v.marca m
        LEFT JOIN FETCH v.modelo mo
        LEFT JOIN FETCH v.categoria ca
        LEFT JOIN FETCH n.inventario i
        WHERE n.active = 'ACTIVE'
        """)
    Page<Nota> findNotasActivasConDetallesPaginado(Pageable pageable);


    @QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
    @Query("SELECT NEW com.mastertyres.nota.model.NotaDTO( " +
            "n.notaId, n.numNota, n.numFactura, n.fechaYhora, n.fechaVencimiento, n.statusNota, n.createdAt, n.active, n.total, " +
            "i.inventarioId," +
            "nd.observaciones, nd.observaciones2, nd.porcentajeGas, nd.rayones, nd.golpes, nd.tapones, nd.tapetes, nd.radio, nd.gato, " +
            "nd.llave, nd.llanta, nd.alineacion, nd.alineacionCantidad, nd.alineacionUnitario, nd.alineacionTotal, " +
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
            "nd.subTotalMecanica, nd.subTotalFrenos, nd.subTotalOtros, nd.llantaCampo, " +
            "nd.llantaCantidad, nd.llantaUnitario, nd.llantaTotal, n.adeudo, n.saldoFavor, " +
            "c.clienteId, c.nombre, c.apellido, c.segundoApellido, c.domicilio, c.rfc, c.correo, c.genero,c.tipoCliente ," +
            "v.vehiculoId, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, v.kilometros, v.color, v.placas) " +
            "FROM Nota n " +
            "JOIN n.detalles nd " +
            "JOIN n.vehiculo v " +
            "JOIN v.cliente c " +
            "JOIN v.marca m " +
            "JOIN v.modelo mo " +
            "JOIN v.categoria ca " +
            "LEFT JOIN n.inventario i " +
            "WHERE n.active = :active AND  n.numNota = :numNota")
    NotaDTO buscarPorNumNota(@Param("active")String active, @Param("numNota")String numNota);

    @Query("SELECT n FROM Nota n WHERE numNota = :numNota")
    Nota findByNumNota(@Param("numNota")String numNota);

    @Modifying
    @Query("UPDATE Nota n SET n.adeudo = :adeudo, n.fechaVencimiento = :fechaVencimiento WHERE n.notaId = :notaId")
    void actualizarAdeudo(@Param("adeudo")float adeudo,
                          @Param("fechaVencimiento")String fechaVencimiento,
                          @Param("notaId")Integer notaId);

    @Modifying
    @Query("UPDATE Nota n SET n.saldoFavor = :saldo WHERE n.notaId = :notaId")
    void actualizarSaldo(@Param("saldo")float saldo, @Param("notaId")Integer notaId);

    @Modifying
    @Query("UPDATE Nota n SET n.updatedAt = :updatedAt WHERE n.notaId = :notaId")
    void actualizarUpdatedAtNota(@Param("notaId")Integer notaId, @Param("updatedAt")String updatedAt);

    @Modifying
    @Query("UPDATE Nota n SET n.numFactura = :numFactura WHERE n.notaId = :notaId ")
    void actualizarNumFactura(@Param("numFactura")String numFactura, @Param("notaId")Integer notaId);

    @Modifying
    @Query("UPDATE Nota n SET n.statusNota = :status WHERE n.notaId = :notaId ")
    void actualizarStatus(@Param("status")String status,@Param("notaId")Integer notaId);




}//clase
