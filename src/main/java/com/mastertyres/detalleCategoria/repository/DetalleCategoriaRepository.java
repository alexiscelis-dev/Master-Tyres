package com.mastertyres.detalleCategoria.repository;

import com.mastertyres.categoria.entity.Categoria;
import com.mastertyres.detalleCategoria.entity.DetalleCategoria;
import com.mastertyres.marca.entity.Marca;
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
import java.util.Optional;

@Repository
public interface DetalleCategoriaRepository extends JpaRepository<DetalleCategoria, Integer> {

   final String SELECT_FROM_DC = "SELECT dc FROM DetalleCategoria dc";
   final String UPDATE_DC = "UPDATE DetalleCategoria dc SET";
   final String DELETE_DC = "DELETE FROM DetalleCategoria dc";

    // Obtener todos los modelos + categorías por marca, excluyendo los genéricos
    @QueryHints({@QueryHint(name = "org.hibernate.readOnly", value = "true")})
    @Query("""
        SELECT dc FROM DetalleCategoria dc
        JOIN FETCH dc.modelo
        JOIN FETCH dc.categoria
        WHERE dc.marca.marcaId = :marcaId
        AND dc.marca.marcaId <> 1
        AND dc.modelo.modeloId <> 1
        AND dc.categoria.categoriaId <> 1
    """)
    List<DetalleCategoria> findByMarcaId(@Param("marcaId") Integer marcaId);

    // Obtener detalle por objeto Marca, excluyendo los genéricos
    @QueryHints({@QueryHint(name = "org.hibernate.readOnly", value = "true")})
    @Query("""
        SELECT d FROM DetalleCategoria d
        JOIN FETCH d.marca
        JOIN FETCH d.modelo
        JOIN FETCH d.categoria
        WHERE d.marca = :marca
        AND d.marca.marcaId <> 1
        AND d.modelo.modeloId <> 1
        AND d.categoria.categoriaId <> 1
    """)
    List<DetalleCategoria> findByMarcaWithRelations(@Param("marca") Marca marca);

    @QueryHints({@QueryHint(name = "org.hibernate.readOnly", value = "true")})
    @Query("""
        SELECT d FROM DetalleCategoria d
        JOIN FETCH d.marca m
        JOIN FETCH d.modelo mo
        JOIN FETCH d.categoria c
        WHERE LOWER(m.nombreMarca) LIKE LOWER(CONCAT('%', :busqueda, '%'))
           OR LOWER(mo.nombreModelo) LIKE LOWER(CONCAT('%', :busqueda, '%'))
           OR LOWER(c.nombreCategoria) LIKE LOWER(CONCAT('%', :busqueda, '%'))
        ORDER BY m.nombreMarca, mo.nombreModelo
    """)
    List<DetalleCategoria> buscarPorMarcaModeloOCategoria(@Param("busqueda") String busqueda);

    @QueryHints({@QueryHint(name = "org.hibernate.readOnly", value = "true")})
    @Query(SELECT_FROM_DC + " " +
    """
    WHERE LOWER(dc.marca.nombreMarca) LIKE LOWER(CONCAT('%', :busqueda, '%'))
       OR LOWER(dc.modelo.nombreModelo) LIKE LOWER(CONCAT('%', :busqueda, '%'))
       OR LOWER(dc.categoria.nombreCategoria) LIKE LOWER(CONCAT('%', :busqueda, '%'))
    ORDER BY dc.marca.nombreMarca, dc.modelo.nombreModelo
""")
    Page<DetalleCategoria> buscarPorMarcaModeloOCategoriaPaginado(@Param("busqueda") String busqueda, Pageable pageable);

    @QueryHints(@QueryHint(name = "org.hibernate.readOnly", value = "true"))
    List<DetalleCategoria> findByCategoria(Categoria categoria);

    // Obtener todos los registros con el modelo específico
    @QueryHints({@QueryHint(name = "org.hibernate.readOnly", value = "true")})
    @Query(SELECT_FROM_DC + " " + "WHERE dc.modelo.modeloId = :modeloId")
    List<DetalleCategoria> findByModeloId(@Param("modeloId") Integer modeloId);

    // Buscar un registro con una combinación específica de marca, modelo y categoría
    @QueryHints({@QueryHint(name = "org.hibernate.readOnly", value = "true")})
    @Query(SELECT_FROM_DC + " " +"WHERE dc.marca.marcaId = :marcaId AND dc.modelo.modeloId = :modeloId AND " +
            "dc.categoria.categoriaId = :categoriaId")
    Optional<DetalleCategoria> findByMarcaModeloCategoria(
            @Param("marcaId") Integer marcaId,
            @Param("modeloId") Integer modeloId,
            @Param("categoriaId") Integer categoriaId
    );

    // Reasignar un registro a modelo y categoría genéricos (1,1)
    @Modifying
    @Query(UPDATE_DC + " " + "dc.modelo.modeloId = 1, dc.categoria.categoriaId = 1 WHERE dc.detalle_categoria_id = :id ")
    void reasignarAGenerico(@Param("id") Integer id);

    // Eliminar un registro específico
    @Modifying
    @Query(DELETE_DC + " " + "WHERE dc.detalle_categoria_id = :id")
    void eliminarPorId(@Param("id") Integer id);

    @Modifying
    @Query(DELETE_DC + " " +"WHERE dc.marca.marcaId = :marcaId")
    void eliminarPorMarcaId(@Param("marcaId") Integer marcaId);

    @QueryHints({@QueryHint(name = "org.hibernate.readOnly", value = "true")})
    @Query("SELECT dc.categoria FROM DetalleCategoria dc " +
            "WHERE dc.marca.marcaId = :marcaId AND dc.modelo.modeloId = :modeloId ORDER BY dc.categoria.nombreCategoria")
    List<Categoria> findCategoriasByMarcaAndModelo(@Param("marcaId") Integer marcaId,
                                                   @Param("modeloId") Integer modeloId);


@QueryHints({@QueryHint(name = "org.hibernate.readOnly", value = "true")})
@Query(SELECT_FROM_DC + " " + """
      WHERE dc.marca.marcaId = :#{#detalles.marca.marcaId}
      AND dc.modelo.modeloId = :#{#detalles.modelo.modeloId}
      AND dc.categoria.categoriaId = :#{#detalles.categoria.categoriaId}
""")
Optional<DetalleCategoria>existeModelo(@Param("detalles") DetalleCategoria detalles);

}//class
