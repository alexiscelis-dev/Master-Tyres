package com.mastertyres.nota.repository;

import com.mastertyres.nota.model.Nota;
import com.mastertyres.nota.model.NotaDTO;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaRepository extends JpaRepository<Nota,Integer> {




@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.READ_ONLY, value = "true"))
@Query(" SELECT NEW com.mastertyres.nota.model.NotaDTO( " +
        "n.notaId, n.numNota, n.numFactura, n.fechaYhora, n.fechaVencimiento, n.statusNota, n.createdAt, n.total, " +
        "nd.observaciones, nd.observaciones2, nd.porcentajeGas, nd.rayones, nd.golpes, nd.tapones, nd.tapetes, nd.radio, nd.gato, " +
        "nd.llave, nd.llanta, nd.alineacion, nd.alineacionCantidad, nd.alineacionUnitario, nd.alineacionTotal, nd.balanceo, nd.balanceo2, nd.balanceoCantidad, " +
        "nd.balanceoUnitario, nd.balanceoTotal, nd.amorDelanteros, nd.amorDelCantidad, nd.amorDelUnitario, nd.amorDelTotal, nd.amorTraseros, " +
        "nd.amorTrasCantidad, nd.amorTrasUnitario, nd.amorTrasTotal, nd.suspension, nd.suspension2, nd.suspensionCantidad, nd.suspensionUnitario, " +
        "nd.suspensionTotal, nd.mecanica, nd.mecanica2, nd.mecanicaCantidad, nd.mecanicaUnitario, nd.mecanicaTotal, nd.frenos, nd.frenos2, " +
        "nd.frenosCantidad, nd.frenosUnitario, nd.frenosTotal, nd.otros, nd.otros2, nd.otrosCantidad, nd.otrosUnitario, nd.otrosTotal, " +
        "nd.subTotalMecanica, nd.subTotalFrenos, nd.subTotalOtros, " +
        "c.nombre, c.apellido, c.segundoApellido, c.domicilio, c.rfc, c.correo, " +
        "m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio, n.active )" +
        "FROM Nota n " +
        "JOIN n.detalles nd " +
        "JOIN n.vehiculo v " +
        "JOIN v.cliente c " +
        "JOIN v.marca m " +
        "JOIN v.marca " +
        "JOIN v.modelo mo " +
        "JOIN v.categoria ca " +
        "WHERE n.active = :active")
List<NotaDTO> listarNotas(@Param("active")String active);



}//clase
