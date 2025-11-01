package com.mastertyres.nota.repository;

import com.mastertyres.nota.model.Nota;
import com.mastertyres.nota.model.NotaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaRepository extends JpaRepository<Nota,Integer> {




    @Query("(SELECT NEW com.mastertyres.nota.model.NotaDTO(n.notaId, n.numNota, n.numFactura, n.total, n.fechayHora, n.observaciones, n.porcentajeGas, " +
            "n.rayones, n.golpes, n.tapones, n.tapetes, n.radio, n.gato, n.llave, n.llanta, n.fechaVencimiento,n.statusNota,n.createdAt, nd.precio, " +
            " nd.descripcionServicio, nd.cantidad, nd.precioUnitario, nd.montoPagado, c.nombre, c.apellido, c.segundoApellido, c.domicilio, c.rfc, " +
            "c.correo, m.nombreMarca, mo.nombreModelo, ca.nombreCategoria, v.anio )" +
            "FROM Nota n " +
            "JOIN NotaDetalle nd ON n.notaId = nd.notaDetalleId " +
            "JOIN Vehiculo v ON n.vehiculo.vehiculoId = v.vehiculoId " +
            "JOIN Cliente c ON n.cliente.clienteId = c.clienteId " +
            "JOIN Marca m ON v.marca.marcaId = m.marcaId " +
            "JOIN Modelo mo ON v.modelo.modeloId = mo.modeloId " +
            "JOIN Categoria ca ON v.categoria.categoriaId = ca.categoriaId " +
            "WHERE n.active = :active ) ")
    List<NotaDTO>listarNotas(@Param("active")String active);
}//clase
