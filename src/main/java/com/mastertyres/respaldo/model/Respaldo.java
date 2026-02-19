package com.mastertyres.respaldo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode
@Table(name = "respaldo")
public class Respaldo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "respaldo_id")
    private Integer respaldoId;

    @Column(name = "nombre_archivo")
    private String nombreArchivo;

    @Column(name = "ruta_archivo")
    private String rutaArchivo;

    @Column(name = "tamanio_bytes")
    private Long tamanioBytes;

    private String estado;

    @Column(name = "codigo_error")
    private String codigoError;

    @Column(name = "tipo_respaldo")
    private String tipoRespaldo;

    @Column(name = "created_at")
    private String createdAt;

}//class
